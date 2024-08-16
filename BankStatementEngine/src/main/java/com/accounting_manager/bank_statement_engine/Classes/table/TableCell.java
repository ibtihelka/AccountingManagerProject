package com.accounting_manager.bank_statement_engine.Classes.table;


import com.accounting_manager.bank_statement_engine.Classes.fontcolor.ColorPalette;
import com.accounting_manager.bank_statement_engine.Classes.geometry.Region;
import com.accounting_manager.bank_statement_engine.Classes.block.Block;
import com.accounting_manager.bank_statement_engine.Classes.text.TextBlock;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

public class TableCell extends Block implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6780573870100153621L;
    /// <summary>
    /// Indice de la ligne de la cellule.
    /// </summary>
    private int						rowIndex;
    /// <summary>
    /// Indice de la colonne de la cellule.
    /// </summary>
    private int						columnIndex;

    /// <summary>
    /// Obtient
    /// l'instance table de la cellule en cours.
    /// </summary>
    public TableBlock ParentBlock()
    {
        return (TableBlock)super.getParentBlock();
    }
    /// <summary>
    /// Obtient
    /// l'indice de la ligne de la cellule.
    /// </summary>
    public int						getRowIndex()
    {
        return this.rowIndex;
    }
    /// <summary>
    /// Obtient
    /// l'indice de la colonne de la cellule.
    /// </summary>
    public int						getColumnIndex()
    {
        return this.columnIndex;


    }
    /// <summary>
    /// Indique que la cellule en cours est
    /// la première de la ligne de son tableau.
    /// </summary>
    public boolean					isFirstInRow()
    {
        return this.rowIndex == 0;
    }
    /// <summary>
    /// Indique que la cellule en cours est
    /// la dernière de la ligne de son tableau.
    /// </summary>
    public boolean						isLastInRow()
    {
        return this.rowIndex == this.ParentBlock().getRowsCount() - 1;
    }
    /// <summary>
    /// Indique que la cellule en cours est
    /// la première de la colonne de son tableau.
    /// </summary>
    public boolean						isFirstInColumn()
    {
        return this.columnIndex == 0;}
    /// <summary>
    /// Indique que la cellule en cours est
    /// la dernière de la colonne de son tableau.
    /// </summary>
    public boolean	isLastInColumn()
    {
        return (this.columnIndex == this.ParentBlock().getRowsCount() - 1);
    }
    /// <summary>
    /// Initialise une nouvelle instance de la classe
    /// avec des paramétres spécifiés.
    /// </summary>
    /// <param name="rowIndex">L'indice de la ligne de la cellule.</param>
    /// <param name="columnIndex">l'indice de la colonne de la cellule.</param>
    /// <param name="textBlocks">Liste des blocs textes qui composent le document.</param>
    public TableCell (int rowIndex, int columnIndex, List<TextBlock> textBlocks)
    {
        super(null, (List<Block>)(List<?>)textBlocks);
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }


    /// <summary>
    /// Initialise une nouvelle instance de la classe
    /// avec des paramétres spécifiés.
    /// </summary>
    /// <param name="rowIndex">L'indice de la ligne de la cellule.</param>
    /// <param name="columnIndex">l'indice de la colonne de la cellule.</param>
    /// <param name="textBlocks">Liste des blocs textes qui composent le document.</param>
    public TableCell (int rowIndex, int columnIndex, List<TextBlock> textBlocks , Region region)
    {
        super(region, (List<Block>)(List<?>)textBlocks);
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }
    /// <summary>

    /// Retourne la chaine de caractéres représentant

    /// le contenu du bloc cellule en cours.
    /// </summary>

    /// <returns>Chaine de caractéres de type <see cref="string" />.</returns>
//
//    @Override
//    public  String toString()
//    {
//        return
//                "TableCell{" +
//                        "row:" + this.rowIndex + ",col:" + this.columnIndex +
//                        ",text:'" + TextBlock.GetSequenceString (GetInnerBlocks ()) + "'" +
//                        "}";
//    }
    /// <summary>
    /// Retourne l'instance du bloc texte qui se trouve
    /// à la position <paramref name="index" /> de la cellule.
    /// </summary>
    /// <param name="index">Entier qui représente l'indice du bloc texte.</param>
    /// <returns>Une instance de type <see cref="FacTextBlock" />.</returns>
    public  TextBlock GetInnerBlock (int index)
    {return (TextBlock)super.GetInnerBlock(index);}
    /// <summary>
    /// Retourne une copie de la liste des blocs textes
    /// de la cellule en cours.
    /// </summary>
    /// <returns>Un tableau de type <see cref="FacTextBlock" />.</returns>
    public List<Block> GetInnerBlocks ()
    {
//        List<TextBlock> t = (List<TextBlock>)(List<?>)  super.GetInnerBlocks();
        return  super.GetInnerBlocks();

    }
    /// <summary>
    /// Retourne la cellule précedente de la ligne.
    /// </summary>
    /// <returns>
    /// Une instance non nulle si il la cellule précedente existe.
    /// Nulle sinon.
    /// </returns>
    public TableCell GetPreviousInRow ()
    {
        if (this.isFirstInRow())
            return null;
        else
            return this.ParentBlock().GetCell (this.rowIndex - 1, this.columnIndex);
    }
    /// <summary>
    /// Retourne la cellule suivante de la ligne.
    /// </summary>
    /// <returns>
    /// Une instance non nulle si il la cellule suivante existe.
    /// Nulle sinon.
    /// </returns>
    public TableCell GetNextInRow ()
    {
        if (this.isLastInRow())
            return null;
        else
            return this.ParentBlock().GetCell (this.rowIndex + 1, this.columnIndex);
    }
    /// <summary>
    /// Retourne la cellule précedente de la colonne.
    /// </summary>
    /// <returns>
    /// Une instance non nulle si il la cellule précedente existe.
    /// Nulle sinon.
    /// </returns>
    public TableCell GetPreviousInColumn ()
    {
        if (this.isFirstInColumn())
            return null;
        else
            return this.ParentBlock().GetCell (this.rowIndex, this.columnIndex - 1);
    }
    /// <summary>
    /// Retourne la cellule suivante de la colonne.
    /// </summary>
    /// <returns>
    /// Une instance non nulle si il la cellule suivante existe.
    /// Nulle sinon.
    /// </returns>
    public TableCell GetNextInColumn ()
    {
        if (this.isLastInColumn())
            return null;
        else
            return this.ParentBlock().GetCell (this.rowIndex, this.columnIndex + 1);
    }
    /// <summary>
    /// Dessine le bloc en cours sur une instance d'un graphics.
    /// </summary>
    /// <param name="g">L'instance sur laquelle on fait le dessin.</param>
    /// <param name="colorPalette">Palette des couleurs.</param>

    @Override
    public void Draw(Graphics2D g, ColorPalette colorPalette)
    {
        g.setColor(colorPalette.GetCellBlockColor ());
        g.fillRect(this.getRegion().getLeft(), this.getRegion().getTop(), this.getRegion().getWidth(), this.getRegion().getHeight());
        DrawInnerBlocks (g, colorPalette);

    }



}
