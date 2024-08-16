package com.accounting_manager.bank_statement_engine.Classes.block;


import com.accounting_manager.bank_statement_engine.Classes.document.DocumentZone;
import com.accounting_manager.bank_statement_engine.Classes.document._Document;
import com.accounting_manager.bank_statement_engine.Classes.fontcolor.ColorPalette;
import com.accounting_manager.bank_statement_engine.Classes.geometry.Region;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Block implements Serializable {
	/**
	 *
	 **/
	@Serial
	private static final long serialVersionUID = -1303098598253847598L;
	private Region region;
	/// <summary>
	/// Bloc parent du bloc en cours. En cas
	/// où le bloc est racine alors cet attribut
	/// sera null.
	/// </summary>
	private Block				parentBlock;
	/// <summary>
	/// Blocs internes du bloc en cours.
	/// </summary>
	private List<Block>				innerBlocks;
	/// <summary>
	/// L'instance document du bloc en cours.
	/// </summary>
	private _Document document;
	/// <summary>
	/// Zone du bloc dans le document.
	/// </summary>
	private DocumentZone documentZone;


	public Region getRegion() {
		return region;
	}
	public Block getParentBlock() {
		return parentBlock;
	}
	public List<Block> getInnerBlocks() {
		return innerBlocks;
	}
	public _Document getDocument() {
		return document;
	}
	public DocumentZone getDocumentZone() {
		return documentZone;
	}

	public int	innerBlocksCount(){
		return this.innerBlocks.size();
	}
	public void setDocument(_Document document){
		{
			this.document = document;
			this.documentZone = document.GetZone (this.region);

			for (int i = 0; i < this.innerBlocks.size(); i ++)
			{this.innerBlocks.get(i).document = document;}
		}
	}

	/// <summary>
	/// Initialise une nouvelle instance de la classe
	/// avec des paramètres spécifiés.
	/// </summary>
	/// <param name="region">Region du bloc.</param>
	/// <param name="innerBlocks">Blocs internes du bloc en cours.</param>
	public Block(Region region, List<Block> innerBlocks)
	{
		this.region = region != null ? region : Block.GetRegion (innerBlocks);
		this.innerBlocks = innerBlocks;

		for(Block innerBlock : innerBlocks)
		{innerBlock.parentBlock = this;}
	}


	/// <summary>
	/// Retourne la région qui englobe les différentes blocs internes.
	/// </summary>
	/// <param name="innerBlocks">Liste de blocs internes.</param>
	/// <returns>Une instance d'une région.</returns>
	/// <remarks>Les régions points ne seront pas inclus.</remarks>
	public static Region GetRegion (List<Block> innerBlocks)
	{
		int				left = 0;
		int				top = 0;
		int				right = 0;
		int				bottom = 0;
		boolean			firstValidRegion = true;

		Region		region;

		for(Block innerBlock : innerBlocks)
		{
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

		return new Region (left, top, right, bottom);
	}

	/// <summary>
	/// Retourne l'instance du bloc interne qui se trouve
	/// à la position <paramref name="index" /> des blocs internes
	/// du bloc en cours.
	/// </summary>
	/// <param name="index">Entier qui représente l'indice du bloc interne.</param>
	/// <returns>Une instance de type <see cref="FacBlock" />.</returns>
	public Block  GetInnerBlock (int index)
	{return this.innerBlocks.get(index);}

	/// <summary>
	/// Retourne une copie de la liste de blocs internes
	/// du blocs en cours.
	/// </summary>
	/// <returns>Un tableau copie des blocs internes du bloc en cours.</returns>
	public List<Block> GetInnerBlocks ()
	{return new ArrayList<>(this.innerBlocks);}

	/// <summary>
	/// Ajoute tous les blocs enfants dans la liste passe en paramètre.
	/// </summary>
	/// <param name="documentZone">Zone du filtrage.</param>
	/// <param name="allInnerBlocks">Tableau qui va contenir le résultat.</param>
	private void addAllInnerBlocks (DocumentZone documentZone, List<Block> allInnerBlocks)
	{
		for(Block innerBlock : this.innerBlocks)
		{
			if (documentZone.getNumDocumentZone() == DocumentZone.UNDEFINED.getNumDocumentZone() ||
					(innerBlock.getDocumentZone().getNumDocumentZone() & documentZone.getNumDocumentZone()) == documentZone.getNumDocumentZone())
			{
				allInnerBlocks.add (innerBlock);
			}

			innerBlock.addAllInnerBlocks (documentZone, allInnerBlocks);
		}
	}

	/// <summary>
	/// Retourne le bloc en cours et tous les blocs enfants du bloc en cours.
	/// </summary>
	/// <param name="documentZone">Zone du filtrage. <see cref="FacDocumentZone.UNDEFINED" /> indique qu'aucun filtre de zone ne doit être appliqué.</param>
	/// <param name="addCurrentBlock">Indique qu'il faut ajouter le bloc en cours.</param>
	/// <returns>Tableau de type <see cref="FacBlock" />.</returns>
	public List<Block> GetAllBlocks (DocumentZone documentZone, boolean addCurrentBlock)
	{
		List<Block>	allInnerBlocks = new ArrayList<Block> ();
		// Ajouter le bloc en cours.
		if (addCurrentBlock && (
				documentZone.getNumDocumentZone() == DocumentZone.UNDEFINED.getNumDocumentZone() ||
						(this.documentZone.getNumDocumentZone() & documentZone.getNumDocumentZone()) == documentZone.getNumDocumentZone()))
		{
			allInnerBlocks.add (this);
		}
		// Ajouter les blocs enfants.
		addAllInnerBlocks (documentZone, allInnerBlocks);

		return allInnerBlocks;
	}

	/// <summary>
	/// Retourne le bloc en cours et tous les blocs enfants du bloc en cours.
	/// </summary>
	/// <param name="documentZone">Zone du filtrage. <see cref="FacDocumentZone.UNDEFINED" /> indique qu'aucun filtre de zone ne doit être appliqué.</param>
	/// <returns>Tableau de type <see cref="FacBlock" />.</returns>
	public List<Block> getAllBlocks (DocumentZone documentZone)
	{return GetAllBlocks (documentZone, true);}

	/// <summary>
	/// Dessine le bloc en cours sur une instance d'un graphics.
	/// </summary>
	/// <param name="g">L'instance sur laquelle on fait le dessin.</param>
	/// <param name="colorPalette">Palette des couleurs.</param>
	public abstract void Draw (Graphics2D g, ColorPalette colorPalette);

	/// <summary>
	/// Dessine les blocs internes du bloc en cours.
	/// </summary>
	/// <param name="g">L'instance sur laquelle on fait le dessin.</param>
	/// <param name="colorPalette">Palette des couleurs.</param>
	public void DrawInnerBlocks (Graphics2D g, ColorPalette colorPalette)
	{
		for(Block innerBlock : this.innerBlocks)
		{innerBlock.Draw (g, colorPalette);}
	}

	/// <summary>
	/// Filtre une liste de blocs en ne sélectionnant
	/// que les blocs de type {T}.
	/// </summary>
	/// <typeparam name="T">Type des blocs à filter.</typeparam>
	/// <param name="blocks">Liste de blocs objet du filtre.</param>
	/// <returns>Tableau de blocs <see cref="FacBlock" /> qui représente le résultat du filtrage.</returns>
	public static <T> List<Block> FilterBlocks  (List<Block> blocks, Class<T> type)
	{
		return  new ArrayList<Block> ().stream().filter(type::isInstance).collect(Collectors.toList());
	}


}