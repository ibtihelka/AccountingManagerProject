package com.accounting_manager.accounting_engine.Classes.geometry;

import com.accounting_manager.accounting_engine.Classes.block.Block;

import java.io.Serializable;
import java.util.List;

public class Region implements Serializable {
			/**
	 * 
	 */
	private static final long serialVersionUID = 2688430649804552181L;
			/// <summary>
			/// Position gauche de la région.
			/// </summary>
			private int						left;
			/// <summary>
			/// Position du haut de la région.
			/// </summary>
			private int						top;
			/// <summary>
			/// Position de droite de la région.
			/// </summary>
			private int						right;
			/// <summary>
			/// Position du bas de la région.
			/// </summary>
			private int						bottom;
			
			
			
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="left">Position du gauche.</param>
			/// <param name="top">Position du haut.</param>
			/// <param name="right">Position de droite.</param>
			/// <param name="bottom">Position du bas.</param>
			public Region(int left, int top, int right, int bottom) {
				super();
				this.left = left;
				this.top = top;
				this.right = right;
				this.bottom = bottom;
			}

			public int getLeft() {
				return left;
			}

			public int getTop() {
				return top;
			}

			public int getRight() {
				return right;
			}

			public int getBottom() {
				return bottom;
			}
			
			/// <summary>
			/// Obtient
			/// la largeur de la région.
			/// </summary>

			public int getWidth() {
				return this.right-this.left;
			}
			/// <summary>
			/// Obtient
			/// l'hauteur de la région.
			/// </summary>
			public int getHeight() {
				return this.bottom - this.top;
			}
			/// <summary>
			/// Teste si deux regions sont les mêmes.
			/// </summary>
			/// <param name="region1">Region 1.</param>
			/// <param name="region2">Region 2.</param>
			/// <returns>True si les deux régions sont les mêmes, False sinon.</returns>
			
			public static boolean isSame (Region region1, Region region2)
			{
				
				return
					region1.getTop() == region2.getTop() &&
					region1.getRight() == region2.getRight() &&
					region1.getBottom() == region2.getBottom() &&
					region1.getLeft() == region2.getLeft();
			}
			
			/// <summary>
			/// Retourne l'indice d'un bloc dans la liste des blocs
			/// passée en paramètre <paramref name="blocks" />. La méthode
			/// de recherche se base sur la comparaison des régions.
			/// </summary>
			/// <param name="block">Bloc objet de la recherche.</param>
			/// <param name="blocks">Liste des blocs.</param>
			/// <returns>Indice du bloc, -1 si il n'a pas été trouvé.</returns>
			public static int getIndex (Block block, List<Block> blocks)
			{
				Block		_block;

				for (int i = 0; i < blocks.size(); i ++)
				{
					_block = blocks.get(i);

					if (block != null && _block != null && Region.isSame (block.getRegion(), _block.getRegion()))
						return i;
				}

				return -1;
			}
			
			
			/// <summary>
			/// Teste si un bloc fait partie de la liste des blocs
			/// passée en paramètre <paramref name="blocks" />.
			/// </summary>
			/// <param name="block">Bloc objet du </param>
			/// <param name="blocks">Liste des blocs.</param>
			/// <returns>True si le bloc existe, False sinon.</returns>
			public static boolean isExists (Block block, List<Block> blocks){
				
				return Region.getIndex (block, blocks) > -1;
			}
			
			/// <summary>
			/// Indique que la région en cours inclus une autre région.
			/// L'inclusion est complète.
			/// </summary>
			/// <param name="region">Région objet du test.</param>
			/// <returns>True si la région <paramref name="region" /> est inclue dans la région en cours, False sinon.</returns>
			public boolean includes (Region region){
				return
					region.left >= this.left &&
					region.right <= this.right &&
					region.top >= this.top &&
					region.bottom <= this.bottom;
			}
			
			/// <summary>
			/// Teste si le point donné par ses coordonnées
			/// <paramref name="x" /> et <paramref name="y" />
			/// est inclu dans la région en cours.
			/// </summary>
			/// <param name="x">X du point.</param>
			/// <param name="y">Y du point.</param>
			/// <returns>True si le poids est dans la région, false sinon.</returns>
			public boolean pointInside (int x, int y)
			{
				return
					x >= this.left && x <= this.right &&
					y >= this.top && y <= this.bottom;
			}
			/// <summary>
			/// Indique si la région en cours a une
			/// partie commune avec la région passée en paramètre.
			/// </summary>
			/// <param name="region">Région objet du test.</param>
			/// <returns>
			/// True si la région en cours a une partie
			/// commune avec la région passée en paramètre,
			/// False sinon.
			/// </returns>
			public boolean Share (Region region)
			{
				return
					pointInside (region.getLeft(), region.getTop()) || 
					pointInside (region.getRight(), region.getTop()) ||
					pointInside (region.getLeft(), region.getBottom()) ||
					pointInside (region.getRight(), region.getBottom());
			}

	@Override
	public String toString() {
		return "Region{" +
				"left=" + left +
				", top=" + top +
				", right=" + right +
				", bottom=" + bottom +
				'}';
	}
}