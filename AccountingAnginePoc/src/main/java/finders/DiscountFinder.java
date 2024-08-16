package finders;

import java.io.Serializable;

import All.Block;
import key.Key;
import key.KeyFinder;
import key.KeyValue;
import key.KeyType;
import learning.LearningEngine;
import text.TextBlock;

public class DiscountFinder extends KeyFinder<Double> implements Serializable {
	
			/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="learningEngine">L'instance du moteur d'apprentissage.</param>
			/// <param name="fournisseur">Chaine de caractères qui représente le fournisseur. Il peut être null.</param>
			public DiscountFinder (LearningEngine learningEngine, String fournisseur) throws Exception {
				
				super(new Key (KeyType.DISCOUNT), learningEngine, fournisseur);
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
				
				return FindValueInTextBlock (keyBlock, allBlocks,  isValueChecked,checkResult);
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
				return false;
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

}