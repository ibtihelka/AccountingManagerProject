package com.accounting_manager.bank_statement_engine.Classes.table;

import com.accounting_manager.bank_statement_engine.Classes.block.Block;
import com.accounting_manager.bank_statement_engine.Classes.fontcolor.ColorPalette;
import com.accounting_manager.bank_statement_engine.Classes.geometry.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TableBlockTest {

    private Region regionMock;

    private Block blockMock;
    @Spy
    private List<TableCell> cellsMock = new ArrayList<>();
    private TableBlock tableBlock;

    private BufferedImage image;

    private Graphics2D gMock;

    private ColorPalette colorPaletteMock;

    @BeforeEach
    public void setUp() {
        regionMock = mock(Region.class);
        blockMock = mock(Block.class);
        colorPaletteMock = mock(ColorPalette.class);
        gMock = Mockito.mock(Graphics2D.class);
        image = new BufferedImage(768, 1024, BufferedImage.TYPE_INT_ARGB);


    }

    @Test
    public void testDraw() {

        tableBlock = new TableBlock(4, 3, cellsMock, regionMock );

        when(colorPaletteMock.GetCellBlockColor()).thenReturn(new Color(212, 88, 59));


        when(regionMock.getLeft()).thenReturn(0);
        when(regionMock.getTop()).thenReturn(0);
        when(regionMock.getWidth()).thenReturn(100);
        when(regionMock.getHeight()).thenReturn(100);

        // callinggg draw function
        tableBlock.Draw(gMock, colorPaletteMock);
        // Verifications
        Mockito.verify(gMock).setColor(new Color(212, 88, 59));
        Mockito.verify(gMock).fillRect(0, 0, 100, 100);
        Mockito.verify(gMock).setColor(Color.BLUE);
        Mockito.verify(gMock).setStroke(new BasicStroke(5));
        Mockito.verify(gMock).drawRect(0, 0, 100, 100);
    }
}