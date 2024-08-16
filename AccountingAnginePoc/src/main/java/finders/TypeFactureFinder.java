package finders;

import java.io.Serializable;

import All.Block;
import key.Key;
import key.KeyFinder;
import key.KeyType;
import key.KeyValue;
import learning.LearningEngine;
import text.TextBlock;

public class TypeFactureFinder extends KeyFinder<Boolean> implements Serializable {

			/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres par défaut.
			/// </summary>
			/// <param name="learningEngine">L'instance du moteur d'apprentissage.</param>
			/// <param name="fournisseur">Chaine de caractères qui représente le fournisseur. Il peut être null.</param>
			public TypeFactureFinder (LearningEngine learningEngine, String fournisseur) throws Exception 
			{
				super (new Key (KeyType.TYPE_FACTURE), learningEngine, fournisseur);
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
				
				return new TextBlock [] {keyBlock.getKeyBlocks() [0]};
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

				return true;
			}
		
			/// <summary>
			/// Retourne la valeur trouvée de type <see cref="bool" />.
			/// </summary>
			/// <param name="validKeyValue">La valeur valide de l'instance en cours.</param>
			/// <returns>Valeur trouvée.</returns>
			@Override
			public Boolean GetValue(KeyValue validKeyValue) {
				
				String				keyValue = TextBlock.GetSequenceString (validKeyValue.getValueBlocks());
				keyValue = keyValue.toUpperCase ();

				if (keyValue == "AVOIR")
					return true;
				else
					return false;
			}
			

}