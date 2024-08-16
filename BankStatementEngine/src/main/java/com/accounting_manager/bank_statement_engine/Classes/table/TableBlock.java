package com.accounting_manager.bank_statement_engine.Classes.table;

import com.accounting_manager.bank_statement_engine.Classes.fontcolor.ColorPalette;
import com.accounting_manager.bank_statement_engine.Classes.geometry.Region;
import com.accounting_manager.bank_statement_engine.Classes.block.Block;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class TableBlock extends Block implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5241080675762509484L;
    /// <summary>
    /// Nombre des lignes.
    /// </summary>
    private int						rowsCount;
    /// <summary>
    /// Nombre des colonnes.
    /// </summary>
    private int						columnsCount;

    /// <summary>
    /// Obtient
    /// le nombre de lignes du tableau.
    /// </summary>
    public int getRowsCount() {
        return rowsCount;
    }

    /// <summary>
    /// Obtient
    /// le nombre des colonnes.
    /// </summary>
    public int getColumnsCount() {
        return columnsCount;
    }
    public void setRowsCount(int rowsCount) {
        this.rowsCount = rowsCount;
    }
    public void setColumnsCount(int columnsCount) {
        this.columnsCount = columnsCount;
    }

    /// <summary>
    /// Initialise une nouvelle instance de la classe
    /// avec des paramètres spécifiés.
    /// </summary>
    /// <param name="rowsCount">Nombre de lignes.</param>
    /// <param name="columnsCount">Nombre de colonnes.</param>
    /// <param name="cells">Liste des cellules du tableau.</param>
    public TableBlock (int rowsCount, int columnsCount, List<TableCell> cells)
    {
        super(null, (List<Block>)(List<?>)TableBlock.adjustedCells (rowsCount, columnsCount, cells));
        this.rowsCount = rowsCount;
        this.columnsCount = columnsCount;
    }


    /// <summary>
    /// Initialise une nouvelle instance de la classe
    /// avec des paramètres spécifiés.
    /// </summary>
    /// <param name="rowsCount">Nombre de lignes.</param>
    /// <param name="columnsCount">Nombre de colonnes.</param>
    /// <param name="cells">Liste des cellules du tableau.</param>
    public TableBlock (int rowsCount, int columnsCount, List<TableCell> cells , Region region)
    {
        super(region, (List<Block>)(List<?>)TableBlock.adjustedCells (rowsCount, columnsCount, cells));
        this.rowsCount = rowsCount;
        this.columnsCount = columnsCount;
    }
    /// <summary>
    /// Ajuste la liste des cellules pour qu'elle soit
    /// compatible avec le nombre de lignes et le nombre
    /// de colonnes de la table.
    /// </summary>
    /// <param name="rowsCount">Nombre de lignes.</param>
    /// <param name="columnsCount">Nombre de colonnes.</param>
    /// <param name="cells">Tableau original des cellules.</param>
    /// <returns>Tableau ajusté des cellules.</returns>
    private static List<TableCell> adjustedCells (int rowsCount, int columnsCount, List<TableCell> cells)
    {
        List<TableCell>	newCells = new ArrayList<>();

        for(TableCell cell :  cells)
            newCells.add(cell);

        for (int i = 0; i < newCells.size(); i ++)
        {
            if (newCells.get(i) == null) {
//                    newCells[i] = new TableCell(i / columnsCount, i % columnsCount, new TextBlock[0]);
            }            }

        return newCells;
    }
    /// <summary>
    /// Retourne l'instance de la cellule qui se trouve
    /// à la ligne de position <paramref name="rowIndex" /> et
    /// la colonne de position <paramref name="columnIndex" /> de la table.
    /// </summary>
    /// <param name="rowIndex">Entier qui représente l'indice de la ligne de la cellule.</param>
    /// <param name="columnIndex">Entier qui représente l'indice de la colonne de la cellule.</param>
    /// <returns>Une instance de type <see cref="FacTableCell" />.</returns>
    public TableCell GetCell (int rowIndex, int columnIndex)
    {
        return (TableCell)GetInnerBlock (rowIndex * this.columnsCount + columnIndex);
    }
    /// <summary>
    /// Dessine le bloc en cours sur une instance d'un graphics.
    /// </summary>
    /// <param name="g">L'instance sur laquelle on fait le dessin.</param>
    /// <param name="colorPalette">Palette des couleurs.</param>


    @Override
    public void Draw(Graphics2D g, ColorPalette colorPalette) {

        g.setColor(colorPalette.GetCellBlockColor ());
        g.fillRect(this.getRegion().getLeft(), this.getRegion().getTop(), this.getRegion().getWidth(), this.getRegion().getHeight());

        //Pen			p = new Pen (Color.Red, 5);
        //Largeur de Pen
        g.setColor(Color.BLUE);
        g.setStroke(new BasicStroke( 5));
        g.drawRect(this.getRegion().getLeft(), this.getRegion().getTop(), this.getRegion().getWidth(), this.getRegion().getHeight());

        DrawInnerBlocks (g, colorPalette);

    }

}
