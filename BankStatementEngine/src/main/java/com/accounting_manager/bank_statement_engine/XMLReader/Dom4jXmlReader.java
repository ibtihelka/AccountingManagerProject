package com.accounting_manager.bank_statement_engine.XMLReader;

import com.accounting_manager.bank_statement_engine.Classes.fontcolor.FacFont;
import com.accounting_manager.bank_statement_engine.Classes.fontcolor.FontStyle;
import com.accounting_manager.bank_statement_engine.Classes.block.Block;
import com.accounting_manager.bank_statement_engine.Classes.document._Document;
import com.accounting_manager.bank_statement_engine.Classes.geometry.Region;
import com.accounting_manager.bank_statement_engine.Classes.table.TableBlock;
import com.accounting_manager.bank_statement_engine.Classes.table.TableCell;
import com.accounting_manager.bank_statement_engine.Classes.text.TextBlock;
import lombok.extern.log4j.Log4j2;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

@Log4j2
public class Dom4jXmlReader {

    private static String getMergedText(List<TextBlock> textBlocks) {
        String text = textBlocks.get(0).getText();

        for (int i = 1; i < textBlocks.size(); i++) {
            if (textBlocks.get(i) instanceof TextBlock)
                text += textBlocks.get(i).getText();
        }
        return text;
    }

    public static Region GetRegion(List<Block> innerBlocks) {
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;
        boolean firstValidRegion = true;

        Region region;

        for (Block innerBlock : innerBlocks) {
            region = innerBlock.getRegion();
            // Cas d'une région point.
            if (region.getWidth() == 0 && region.getHeight() == 0)
                continue;
            // Les autres cas.
            if (firstValidRegion || region.getLeft() < left)
                left = region.getLeft();

            if (firstValidRegion || region.getTop() < top)
                top = region.getTop();

            if (firstValidRegion || region.getRight() > right)
                right = region.getRight();

            if (firstValidRegion || region.getBottom() > bottom)
                bottom = region.getBottom();

            firstValidRegion = false;
        }

        return new Region(left, top, right, bottom);
    }

    // lire le fichier XML et extraire les blocs de type Table et Text
    public List<_Document> readXml(String path) throws DocumentException {
        SAXReader reader = new SAXReader();
        int top = 0;
        Document document = reader.read(path);
        List<_Document> documents = new ArrayList<>();
        List<Node> pages = document.selectNodes("//*[local-name()='page']");
        try {
            for (Node p : pages) {
                documents.add(getDocument(p, top));
                top = Integer.parseInt(p.valueOf("@height"));
            }
        } catch (Exception e){
            log.error("No pages available "+ e);
        }
        return documents;
    }

    public List<_Document> readXmlFromUrl(URL url) throws DocumentException {
        SAXReader reader = new SAXReader();
        int top = 0;
        Document document = reader.read(url);
        List<_Document> documents = new ArrayList<>();
        List<Node> pages = document.selectNodes("//*[local-name()='page']");
        try {
            for (Node p : pages) {
                documents.add(getDocument(p, top));
                top = Integer.parseInt(p.valueOf("@height"));
            }
        } catch (Exception e){
            log.error("No pages available "+ e);
        }
        return documents;
    }

    private _Document getDocument(Node page, int top) {
        _Document outDocument;
        List<Node> blocks = page.selectNodes(".//*[local-name()='block']");
        List<Block> allBlocks = new ArrayList<>();

        try {
            for (Node n : blocks) {

                if (n.valueOf("@blockType").equals("Text")) {
                    allBlocks.addAll(parseTextBlocks(n.selectNodes(".//*[local-name()='line']")));
                } else if ((n.valueOf("@blockType").equals("Table"))) {
                    int rowsCount = n.selectNodes(".//*[local-name()='row']").size();
                    int columnsCount = 0;
                    List<Node> numCells = n.selectSingleNode(".//*[local-name()='row']").selectNodes(".//*[local-name()='cell']");

                    for (Node c : numCells) {
                        if (c.valueOf("@colSpan").equals(""))
                            columnsCount++;
                        else
                            columnsCount += Integer.parseInt(c.valueOf("@colSpan"));
                    }
                    List<Node> rows = n.selectNodes(".//*[local-name()='row']");
                    List<TableCell> tableCells = new ArrayList<>();

                    for (int i = 0; i < rows.size(); i++) {
                        List<Node> cells = rows.get(i).selectNodes(".//*[local-name()='cell']");
                        for (int j = 0; j < cells.size(); j++) {
                            List<Node> lines = cells.get(j).selectNodes(".//*[local-name()='line']");
                            if (parseTextBlocks(lines).size() > 0)
                                tableCells.add(new TableCell(i, j, parseTextBlocks(lines)));
                        }
                    }
                    allBlocks.add(new TableBlock(rowsCount, columnsCount, tableCells));
                }
            }
        } catch (Exception e) {
            log.error("Pas des Blocks dans la page" + e);
        }

        int width = Integer.parseInt(page.valueOf("@width"));
        int height = Integer.parseInt(page.valueOf("@height"));
        outDocument = new _Document(new Region(0, top, width, height), allBlocks);
        outDocument.GetAllBlocks().forEach(b -> b.setDocument(outDocument));
        List<TableBlock> tableBlocks = (List<TableBlock>) (List<?>) outDocument.GetAllBlocks().stream().filter(s -> s instanceof TableBlock).collect(Collectors.toList());
        List<TableCell> tableCells = (List<TableCell>) (List<?>) outDocument.GetAllBlocks().stream().filter(s -> s instanceof TableCell).collect(Collectors.toList());
        for (TableBlock t : tableBlocks) {
            List<TextBlock> textBlocks = new ArrayList<>();
            for (TableCell cell : tableCells) {
                if (cell.getParentBlock().equals(t))
                    textBlocks.addAll((List<TextBlock>) (List<?>) cell.GetInnerBlocks());
            }
            prevNextTextBlock(textBlocks);
        }

        return outDocument;
    }

    private void prevNextTextBlock(List<TextBlock> textBlocks) {
        ListIterator<TextBlock> listIterator = textBlocks.listIterator();
        while (listIterator.hasNext() && listIterator.nextIndex() + 1 < textBlocks.size()) {
            textBlocks.get(listIterator.nextIndex()).setNextTextBlock(textBlocks.get(listIterator.nextIndex() + 1));
            listIterator.next();
            textBlocks.get(listIterator.nextIndex()).setPreviousTextBlock(textBlocks.get(listIterator.previousIndex()));
        }
    }

    private List<TextBlock> parseTextBlocks(List<Node> lines) {
        List<TextBlock> linesBlocks = new ArrayList<>();
        try {
            for (int i = 0; i < lines.size(); i++) {
                List<TextBlock> innerBlocks = new ArrayList<>();
                List<Node> formatting = lines.get(i).selectNodes(".//*[local-name()='formatting']");

                for (Node f : formatting) {

                    FacFont font = getFont(f);
                    List<Node> charParams = f.selectNodes(".//*[local-name()='charParams']");
                    try {
                        for (int j = 0; j < charParams.size(); j++) {
                            if (!charParams.get(j).getText().equals(" "))
                                innerBlocks.add(new TextBlock(getRegion(charParams.get(j)), charParams.get(j).getText(), font));
                            else {
                                linesBlocks.add(new TextBlock(GetRegion((List<Block>) (List<?>) innerBlocks), getMergedText(innerBlocks), font));
                                innerBlocks = new ArrayList<>();
                            }
                        }
                        if (innerBlocks.size() > 0)
                            linesBlocks.add(new TextBlock(GetRegion((List<Block>) (List<?>) innerBlocks), getMergedText(innerBlocks), font));
                    } catch (Exception e) {
                        log.error("charParams extraction error" + e);
                    }

                }
            }
        } catch (Exception e) {
            log.error("TextBlock Parsing Exception" + e);
        }

        prevNextTextBlock(linesBlocks);
        return linesBlocks;
    }

    private FacFont getFont(Node node) {
        String fontName = node.valueOf("@ff");
        float fontSize = Float.parseFloat(node.valueOf("@fs"));
        FontStyle fontStyle;
        if (node.valueOf("@bold").equals("1"))
            fontStyle = FontStyle.BOLD;
        else if (node.valueOf("@italic").equals("1"))
            fontStyle = FontStyle.ITALIC;
        else if (node.valueOf("@underline").equals("1"))
            fontStyle = FontStyle.UNDERLINE;
        else if (node.valueOf("@strikeout").equals("1"))
            fontStyle = FontStyle.STRIKEOUT;
        else
            fontStyle = FontStyle.REGULAR;

        return new FacFont(fontName, fontSize, fontStyle);
    }

    private Region getRegion(Node node) {
        int l = Integer.parseInt(node.valueOf("@l"));
        int t = Integer.parseInt(node.valueOf("@t"));
        int r = Integer.parseInt(node.valueOf("@r"));
        int b = Integer.parseInt(node.valueOf("@b"));
        return new Region(l, t, r, b);
    }

}