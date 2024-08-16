package learning;

import java.io.Serializable;

import geometry.Region;

public class LearningRegion implements Serializable {
	
			/**
	 * 
	 */
	private static final long serialVersionUID = -4292743525255865320L;
			/// <summary>
			/// Region de la valeur.
			/// </summary>
			private Region				region;
			/// <summary>
			/// Poids de la région.
			/// </summary>
			private int						weight;
			/// <summary>
			/// Larguer du document utilisé lors
			/// de l'apprentissage.
			/// </summary>
			public static int				DOCUMENT_WIDTH = 1024;
			/// <summary>
			/// Hauteur du document utilisé lors
			/// de l'apprentissage.
			/// </summary>
			public static int				DOCUMENT_HEIGHT = 768;
			/// <summary>
			/// Ceofficient de tolérance utilisé lors
			/// de la comparsaion par la méthode <see cref="IsSame" />.
			/// </summary>
			public static float				COMPARE_CEOFF = 0.05f;
			public Region getRegion() {
				return region;
			}
			public int getWeight() {
				return weight;
			}
			public void setRegion(Region region) {
				this.region = region;
			}
			public void setWeight(int weight) {
				this.weight = weight;
			}
			
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="region">Region de la valeur.</param>
			/// <param name="documentRegion">Dimesions du document de la région.</param>
			public LearningRegion (Region region, Region documentRegion)
			{this.region = LearningRegion.adjustRegion (region, documentRegion);}
			
			
			/// <summary>
			/// Ajuste la région passée en paramètre en la mettant
			/// à l'échelle utilisée lors de l'apprentissage.
			/// </summary>
			/// <param name="region">L'instance de la région à ajuster.</param>
			/// <param name="documentRegion">Dimesions du document de la région.</param>
			/// <returns>Nouvelle région <see cref="FacRegion" /> ajustée.</returns>
			public static Region adjustRegion (Region region, Region documentRegion)
			{
				float		widthCoeff = (float)LearningRegion.DOCUMENT_WIDTH / documentRegion.getWidth();
				float		heightCeoff = (float)LearningRegion.DOCUMENT_HEIGHT / documentRegion.getHeight();


				return new Region (
								(int)(region.getLeft() * widthCoeff),
								(int)(region.getTop() * heightCeoff),
								(int)(region.getRight() * widthCoeff), 
								(int)(region.getBottom() * heightCeoff));
			}
			
			/// <summary>
			/// Teste si la région en cours et la region passée
			/// en paramètre sont égales.
			/// </summary>
			/// <param name="region">Region objet du test.</param>
			/// <param name="documentRegion">Dimesions du document de la région.</param>
			/// <returns>True si les deux régions sont les mêmes, False sinon.</returns>
			public boolean isSame (Region region, Region documentRegion)
			{
				Region	adjustedRegion = LearningRegion.adjustRegion (region, documentRegion);
				// Calaculer la tolérance des dimensions.
				int			minTop = (int)(this.region.getTop() * (1 - LearningRegion.COMPARE_CEOFF / 2));
				int			maxTop = (int)(this.region.getTop() * (1 + LearningRegion.COMPARE_CEOFF / 2));
				int			minRight = (int)(this.region.getRight() * (1 - LearningRegion.COMPARE_CEOFF / 2));
				int			maxRight = (int)(this.region.getRight() * (1 + LearningRegion.COMPARE_CEOFF / 2));
				int			minBottom = (int)(this.region.getBottom() * (1 - LearningRegion.COMPARE_CEOFF / 2));
				int			maxBottom = (int)(this.region.getBottom() * (1 + LearningRegion.COMPARE_CEOFF / 2));
				int			minLeft = (int)(this.region.getLeft() * (1 - LearningRegion.COMPARE_CEOFF / 2));
				int			maxLeft = (int)(this.region.getLeft() * (1 + LearningRegion.COMPARE_CEOFF / 2));

				return
					adjustedRegion.getTop() >= minTop &&	adjustedRegion.getTop() <= maxTop &&
					adjustedRegion.getRight() >= minRight &&	adjustedRegion.getRight() <= maxRight &&
					adjustedRegion.getBottom() >= minBottom &&	adjustedRegion.getBottom() <= maxBottom &&
					adjustedRegion.getLeft() >= minLeft &&	adjustedRegion.getLeft() <= maxLeft;
			}	
			
			/// <summary>
			/// Clacule en pixel la distance entre la région
			/// de l'instance en cours <see cref="Region" /> et
			/// la région passée en paramètre <paramref name="region" />.
			/// </summary>
			/// <param name="region">Région à calculer la distance.</param>
			/// <param name="documentRegion">Dimesions du document de la région.</param>
			public int getDistance (Region region, Region documentRegion)
			{
				// Ajustement des dimensions.
				Region	adjustedRegion = LearningRegion.adjustRegion (region, documentRegion);
				// Calculer la distance.
				int			x = (this.region.getLeft() + this.region.getRight()) / 2 - (adjustedRegion.getLeft() + adjustedRegion.getRight()) / 2;
				int			y = (this.region.getTop() + this.region.getBottom()) / 2 - (adjustedRegion.getTop() + adjustedRegion.getBottom()) / 2;

				return (int)Math.sqrt (x * x + y * y);
			}

}