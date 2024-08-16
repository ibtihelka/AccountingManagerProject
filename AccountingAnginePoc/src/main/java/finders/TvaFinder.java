package finders;

import java.io.Serializable;

import All.Block;
import key.Key;
import key.KeyFinder;
import key.KeyType;
import key.KeyValue;
import learning.LearningEngine;
import text.TextBlock;

public class TvaFinder extends KeyFinder<Double> implements Serializable {
	
			/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
			/// <summary>
			/// Indique que le second
			/// traitement est activé.
			/// </summary>
			private boolean				isSecondProcessing;
			
			
			/// <summary>
			/// Obtient
	        /// une valeur indiquant que le second
			/// traitement est activé.
			/// </summary>
			public boolean isSecondProcessing() {
				return isSecondProcessing;
			}

			public void setSecondProcessing(boolean isSecondProcessing) {
				this.isSecondProcessing = isSecondProcessing;
			}
			@Override
			public float		HAlignRate(){
				
				return this.isSecondProcessing ? 1.5f * super.HAlignRate() : super.HAlignRate();
			}
			/// <summary>
			/// Obtient
	        /// le taux  de la largeur de l'intervalle
			/// utilisée pour faire les recherches des blocs
			/// dans le cas d'un vertical.
			/// </summary>
			@Override
			public float		VAlignRate(){
				
				return this.isSecondProcessing ? 1.5f * super.VAlignRate() : super.VAlignRate();
				
			}
			
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="isSecondProcessing">Indique que le second traitement est activé.</param>
			/// <param name="learningEngine">L'instance du moteur d'apprentissage.</param>
			/// <param name="fournisseur">Chaine de caractères qui représente le fournisseur. Il peut être null.</param>
			public TvaFinder (boolean isSecondProcessing, LearningEngine learningEngine, String fournisseur) throws Exception {
				 super (new Key (KeyType.TVA), learningEngine, fournisseur);
				this.isSecondProcessing = isSecondProcessing;
				}
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="learningEngine">L'instance du moteur d'apprentissage.</param>
			/// <param name="fournisseur">Chaine de caractères qui représente le fournisseur. Il peut être null.</param>
			public TvaFinder (LearningEngine learningEngine, String fournisseur) throws Exception{
				  this (false, learningEngine, fournisseur);
			}
			
			/// <summary>
			/// Retourne la sous-séquence d'un bloc texte.
			/// </summary>
			/// <param name="keyValue">Instance clé/valeur en cours.</param>
			/// <param name="textBlock">Bloc texte.</param>
			/// <returns>Tableau de <see cref="FacTextBlock" /> qui représente la sous-séquence.</returns>
			/// <remarks>Par défaut cette méthode utilise la séquence retournée par la propriété <see cref="FacTextBlock.Sequence" />.</remarks>
			@Override
			public TextBlock [] GetSubSequence (KeyValue keyValue, TextBlock textBlock){
				
				return TextBlock.GetSubSequence (textBlock.getSequence(), textBlock, TextBlock.SEQ_SPACING_RATE * 1.5f);
			
			}
			/// <summary>
			/// Recherche la valeur de la clé à partir du bloc
			/// de la clé <paramref name="keyBlock" />.
			/// </summary>
			/// <param name="keyBlock">Bloc texte de la clé.</param>
			/// <param name="allBlocks">Liste de tous les blocs objects de la recherche.</param>
			/// <param name="isValueChecked">Indique que la valeur a été vérifiée.</param>
			/// <param name="checkResult">Dans le cas où la valeur a été vérifiée ce paramètre contient le résultat.</param>
			/// <returns>Un bloc texte non null si la valeur vient d'être trouvée. Nulle sinon.</returns>
			@Override
			public TextBlock[] FindValue(KeyValue keyBlock, Block[] allBlocks, boolean isValueChecked, boolean checkResult) {
				
				return FindValueInTextBlock (keyBlock, allBlocks,  isValueChecked,  checkResult);
			}
		
			/// <summary>
			/// Vérifie si la valeur trouvée d'une clé est une bonne
			/// valeur ou non.
			/// </summary>
			/// <param name="keyValue">Bloc texte qui représente la valeur de la clé.</param>
			/// <param name="allBlocks">Liste de tous les blocs objects de la recherche.</param>
			/// <returns>True si la valeur est bonne. False sinon.</returns>
			@Override
			public boolean CheckValue(KeyValue keyValue, Block[] allBlocks) {
				try {
					return CheckAmountValue (keyValue, allBlocks);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return isSecondProcessing;
			}
		
			/// <summary>
			/// Retourne la valeur trouvée de type <see cref="double" />.
			/// </summary>
			/// <param name="validKeyValue">La valeur valide de l'instance en cours.</param>
			/// <returns>Valeur trouvée.</returns>
			@Override
			public Double GetValue(KeyValue validKeyValue) {
				return GetAmountValue (validKeyValue);
			}
			
			/// <summary>
			/// Vérifie les valeurs des clés trouvées. Ceci permet de vérifier l'intégrité
			/// des valeurs et d'annuler certaines clés/valeurs.
			/// </summary>
			/// <param name="keyValues">Tableau des valeurs des clés.</param>
			/// <param name="allBlocks">Liste de tous les blocs objects de la recherche.</param>
			@Override
			public void CheckAllValues (KeyValue [] keyValues, Block [] allBlocks){
				
				CheckSuppAllValues (keyValues, this.isSecondProcessing, allBlocks);
				
				}
			
			

}