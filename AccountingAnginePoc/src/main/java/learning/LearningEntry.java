package learning;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import geometry.Region;

public class LearningEntry implements Serializable {
			/**
	 * 
	 */
	private static final long serialVersionUID = 4565124812013733743L;
			/// <summary>
			/// Fournisseur de l'entrée utilisé comme clé
			/// lors de la classement des entrées par le moteur
			/// d'apprentissage.
			/// </summary>
			private String					fournisseur;
			/// <summary>
			/// Liste des régions de l'apprentissage.
			/// </summary>
			private List<LearningRegion>	regions;
			/// <summary>
			/// Coefficient 'A' utilisé lors de l'évaluation
			/// d'une region par la méthode <see cref="GetWeight" />.
			/// </summary>
			public static float				A_CEOFF = 1;
			/// <summary>
			/// Coefficient 'B' utilisé lors de l'évaluation
			/// d'une region par la méthode <see cref="GetWeight" />.
			/// </summary>
			public static float				B_CEOFF = 1;
			/// <summary>
			/// Valeur maximale à partir de laquelle
			/// un bloc sera éléminé.
			/// </summary>
			public static float				MAX_WEIGHT = 1000;
			
			
			public String getFournisseur() {
				return fournisseur;
			}
			public void setFournisseur(String fournisseur) {
				this.fournisseur = fournisseur;
			}
			
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="fournisseur">Fournisseur de l'entrée utilisé comme clé
			/// lors de la classement des entrées par le moteur d'apprentissage.</param>
			public LearningEntry (String fournisseur)
			{
				this.fournisseur = fournisseur;
				this.regions = new ArrayList<LearningRegion> ();
			}
			
			/// <summary>
			/// Met à jour la liste des régions pour la nouvelle region.
			/// </summary>
			/// <param name="region">Region apprise.</param>
			/// <param name="documentRegion">Dimensions du document de la région <paramref name="region" />.</param>
			public void Update (Region region, Region documentRegion)
			{
				// Comparer.
				boolean		isNewRegion = true;

				for(LearningRegion learningRegion : this.regions)
				{
					// Renforcement.
					if (learningRegion.isSame (region, documentRegion))
					{
						learningRegion.setWeight(learningRegion.getWeight()+1);
						isNewRegion = false;
					}
				}
				// Nouvelle région.
				if (isNewRegion)
					this.regions.add (new LearningRegion (region, documentRegion));
			}
			/// <summary>
			/// Retourne le poids d'une region selon les données de l'apprentissage.
			/// </summary>
			/// <param name="region">Région à évaluer.</param>
			/// <param name="documentRegion">Dimensions du document de la région <paramref name="region" />.</param>
			/// <returns>
			/// <see cref="float" /> qui représente le poids de la région
			/// <paramref name="region" /> en utilisant les données
			/// de l'apprentissage de l'instance en cours.
			/// </returns>
			public float GetWeight (Region region, Region documentRegion)
			{
				// Calculer le poids.
				double					weight = 0;

				for(LearningRegion learningRegion : this.regions)
				{weight += 1 / (LearningEntry.A_CEOFF + LearningEntry.B_CEOFF * learningRegion.getWeight()) * learningRegion.getDistance (region, documentRegion);}

				return (float)weight;
			}

}