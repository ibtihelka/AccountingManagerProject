package fontcolor;

import java.awt.Color;
import java.io.Serializable;
import java.util.Random;

public final class ColorPalette implements Serializable {

			/**
	 * 
	 */
	private static final long serialVersionUID = 6355531929853135648L;
			/// <summary>
			/// Générateur de nombres aléatoires.
			/// </summary>
			@SuppressWarnings("unused")
			private Random			rndGenerator;
			/// <summary>
			/// Couleur des blocs textes.
			/// </summary>
			private Color []		textBlockColors;
			/// <summary>
			/// Couleur des cellules.
			/// </summary>
			private Color []		 cellBlockColors;
			/// <summary>
			/// Couleur des tables.
			/// </summary>
			private Color []		tableBlockColors;
			
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres par défaut.
			/// </summary>
			public ColorPalette (){
				
				this.rndGenerator = new Random ();

				@SuppressWarnings("unused")
				int			alpha = 127 /* 50% */;
				// Blocs textes.			
				this.textBlockColors = new Color [] {new Color(212, 212, 212)};
				// Blocs cellules.
				this.cellBlockColors = new Color [] {new Color(212, 88, 59)};
				// Blocs tables.
				this.tableBlockColors = new Color [] {new Color(40, 196, 212)};
			}
			
			/// <summary>
			/// Retourne d'une façon aléatoire une couleur pour
			/// un bloc text.
			/// </summary>
			/// <returns>Une couleur.</returns>
			public Color GetTextBlockColor (){
				
				return this.textBlockColors [0];
			}
			
			/// <summary>
			/// Retourne d'une façon aléatoire une couleur pour
			/// un bloc cellule.
			/// </summary>
			/// <returns>Une couleur.</returns>
			
			public Color GetCellBlockColor (){
				
				return this.cellBlockColors [0];
			}
			/// <summary>
			/// Retourne d'une façon aléatoire une couleur pour
			/// un bloc tableau.
			/// </summary>
			/// <returns>Une couleur.</returns>
			public Color GetTableBlockColor (){
				
				return this.tableBlockColors [0];
			}

}