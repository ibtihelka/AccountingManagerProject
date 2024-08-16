package finders;

import java.io.Serializable;
import java.util.Arrays;

import All.Block;
import key.Key;
import key.KeyFinder;
import key.KeyType;
import key.KeyValue;
import learning.LearningEngine;
import text.TextBlock;

public class AmountFinder extends KeyFinder<Double> implements Serializable {
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
			/// <summary>
			/// Tableau qui contient les valeurs qui correspondent
			/// à la clé null.
			/// </summary>
			private KeyValue []		_NullKeyValues;
			

			/// <summary>
			/// Obtient
	        /// le tableau qui contient les valeurs
			/// qui correspondent à la clé null.
			/// </summary>
			public KeyValue[] get_NullKeyValues() {
						return _NullKeyValues;
					}

			public void set_NullKeyValues(KeyValue[] _NullKeyValues) {
				this._NullKeyValues = _NullKeyValues;
			}
			
			
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres par défaut.
			/// </summary>
			/// <param name="learningEngine">L'instance du moteur d'apprentissage.</param>
			/// <param name="fournisseur">Chaine de caractères qui représente le fournisseur. Il peut être null.</param>
			public AmountFinder (LearningEngine learningEngine, String fournisseur) throws Exception  
			{
				super (new Key (KeyType.AMOUNT), learningEngine, fournisseur);
			}
			/// <summary>
			/// Vérifie les clés trouvées. Il est possible de suivre
			/// la recherche des valeurs même si aucune clé n'a été trouvée.
			/// </summary>
			/// <param name="keyValues">Liste des blocs/valeurs des clés trouvées.</param>
			/// <returns>Une valeur indiquant s'il faut ajouter la recherche sans clé.</returns>
			@Override
			public  boolean ChekAllKeys (KeyValue [] keyValues)
			{
				super.ChekAllKeys (keyValues);
				return true;
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
				this._NullKeyValues = FindValuesNullKey (keyBlock, allBlocks);

				isValueChecked = true;
				checkResult = false;
				return null;
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
			/// <summary>
			/// Retourne tous les instances clés/valeurs de l'instance
			/// en cours.
			/// </summary>
			/// <returns>Tableau de type <see cref="FacKeyValue" />.</returns>
			@Override
			public KeyValue [] GetAllKeyValues ()
			{
				KeyValue []		allKeyValues = super.GetAllKeyValues ();
				KeyValue []		_allKeyValues = new KeyValue [allKeyValues.length + this._NullKeyValues.length];

				_allKeyValues=Arrays.copyOfRange(allKeyValues, 0, allKeyValues.length);
				_allKeyValues=Arrays.copyOfRange(this._NullKeyValues, allKeyValues.length, this._NullKeyValues.length);
				

				return _allKeyValues;
			}

}