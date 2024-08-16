package finders;

import java.io.Serializable;

import All.Block;
import All.NumberHelper;
import geometry.PositionType;
import key.Key;
import key.KeyFinder;
import key.KeyType;
import key.KeyValue;
import learning.LearningEngine;
import learning.LearningEntry;
import text.TextBlock;

public class NumberSiretFinder extends KeyFinder<String> implements Serializable{
	
			/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres par défaut.
			/// </summary>
			/// <param name="learningEngine">L'instance du moteur d'apprentissage.</param>
			public NumberSiretFinder (LearningEngine learningEngine) throws Exception{
				
				 super (new Key (KeyType.NUMERO_SIRET), learningEngine, null);
			}

			
			/// <summary>
			/// Recherche la valeur de la clé à partir du bloc
			/// de la clé <paramref name="keyBlock" />.
			/// </summary>
			/// <param name="keyValue">Instance clé/valeur en cours.</param>
			/// <param name="allBlocks">Liste de tous les blocs objects de la recherche.</param>
			/// <param name="isValueChecked">Indique que la valeur a été vérifiée.</param>
			/// <param name="checkResult">Dans le cas où la valeur a été vérifiée ce paramètre contient le résultat.</param>
			/// <returns>Un bloc texte non null si la valeur vient d'être trouvée. Nulle sinon.</returns>
			@Override
			public TextBlock [] FindValue (KeyValue keyValue, Block [] allBlocks,  boolean isValueChecked, boolean checkResult)
			{ 
				float				nextBlockWeight = 0;
				TextBlock []		nextBlocks = null;

				keyValue.setPosition( PositionType.RIGHT);
				FindValueInTextBlocUsingRight (keyValue, allBlocks, false,  nextBlockWeight, nextBlocks);
				keyValue.setWeight(nextBlockWeight);

				TextBlock		nextBlock = nextBlocks == null ? null : nextBlocks [0];
				return nextBlock == null ? null : nextBlock.getRightSequence();
			}
			
			/// <summary>
			/// Vérifie si la valeur trouvée d'une clé est une bonne
			/// valeur ou non.
			/// </summary>
			/// <param name="keyValue">Bloc texte qui représente la valeur de la clé.</param>
			/// <param name="value">Valeur trouvée.</param>
			/// <returns>True si la valeur est bonne. False sinon.</returns>
			private boolean _CheckValue (KeyValue keyValue, String value)
			{
				String		siretStr = TextBlock.GetSequenceString (keyValue.getValueBlocks());

				siretStr = NumberHelper.CleanIdFournisseur (siretStr);

				for (int i = 0; i < siretStr.length(); i ++)
				{
					if (i + 14 < siretStr.length())
						value = siretStr.substring (i, 14);
					else
						value = siretStr.substring (i, siretStr.length() - i);

					if (NumberHelper.IsSiret (value))
						return true;
				}

				value = null;
				return false;
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
				
				String		stringValue = null;
				return _CheckValue (keyValue,  stringValue);
			}
		
			/// <summary>
			/// Retourne la valeur trouvée de type <see cref="string" />.
			/// </summary>
			/// <param name="validKeyValue">La valeur valide de l'instance en cours.</param>
			/// <returns>Valeur trouvée.</returns>
			@Override
			public String GetValue(KeyValue validKeyValue) {
				String		stringValue = null;
				_CheckValue (validKeyValue,  stringValue);
				return stringValue;
			}

			/// <summary>
			/// Retourne l'entrée d'apprentissage à utilisé pour calauler
			/// le poids d'apprentiisage
			/// </summary>
			/// <param name="keyValue">Une instance de clé/valeur <see cref="FacKeyValue" />.</param>
			/// <param name="isValidValue">Indique si la valeur est valide.</param>
			/// <returns>Une instance de type <see cref="FacLearningEntry" /> qui représente l'entrée de l'apprentissage.</returns>
			@Override
			public LearningEntry GetLearningEntry (KeyValue keyValue, boolean isValidValue)
			{
				if (isValidValue)
				{
					String		siret = GetValue (keyValue);
					return this.getLearningEngine().GetEntry (KeyType.NUMERO_RCS, NumberHelper.GetRcsFromSiret (siret));
				}
				else
					return null;
			}
}