package finders;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

import All.Block;
import key.Key;
import key.KeyFinder;
import key.KeyValue;
import learning.LearningEngine;
import key.KeyType;
import text.TextBlock;
import All.NumberHelper;

public class DateFinder extends KeyFinder<Date> implements Serializable {
	
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
			public KeyValue []		getNullKeyValues(){
				return this._NullKeyValues;
			}
			
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres par défaut.
			/// </summary>
			/// <param name="learningEngine">L'instance du moteur d'apprentissage.</param>
			/// <param name="fournisseur">Chaine de caractères qui représente le fournisseur. Il peut être null.</param>
			public DateFinder (LearningEngine learningEngine, String fournisseur) throws Exception 
			{
				super(new Key (KeyType.DATE), learningEngine, fournisseur);
			}
			/// <summary>
			/// Vérifie les clés trouvées. Il est possible de suivre
			/// la recherche des valeurs même si aucune clé n'a été trouvée.
			/// </summary>
			/// <param name="keyValues">Liste des blocs/valeurs des clés trouvées.</param>
			/// <returns>Une valeur indiquant s'il faut ajouter la recherche sans clé.</returns>
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
				// Test s'il existe des mot clé.
				if (keyBlock.getKeyBlocks() != null)
					return FindValueInTextBlock (keyBlock, allBlocks,  isValueChecked,  checkResult);				
				else
				{
					this._NullKeyValues = FindValuesNullKey (keyBlock, allBlocks);

					isValueChecked = true;
					checkResult = false;
					return null;
				}
			}
			
			/// <summary>
			/// Teste si une date d'une facture est invalide.
			/// </summary>
			/// <param name="keyInvalidator">Clé qui permet de valider une date d'une facture.</param>
			/// <param name="prevSeq">Chaine de caractères qui représente la séquence d'avant de la valeur.</param>
			/// <param name="currSeq">Chaine de caractères qui représente la séquence en cours de la valeur.</param>
			/// <param name="nextSeq">Chaine de caractères qui représente la séquence d'après de la valeur.</param>
			/// <returns>True si c'est une fausse date, False sinon.</returns>
			private static boolean _IsWrongDate (String keyInvalidator, String prevSeq, String currSeq, String nextSeq)
			{
				return
					prevSeq != null && prevSeq.toUpperCase().indexOf (keyInvalidator.toUpperCase()) > -1 ||
					currSeq.toUpperCase().indexOf (keyInvalidator.toUpperCase()) > -1 ||
					nextSeq != null && nextSeq.toUpperCase().indexOf (keyInvalidator.toUpperCase()) > -1;
			}
			
			/// <summary>
			/// Teste si une date d'une facture est invalide.
			/// </summary>
			/// <param name="keyInvalidator">Clé qui permet de valider une date d'une facture.</param>
			/// <param name="prevSeq">Chaine de caractères qui représente la séquence d'avant de la valeur.</param>
			/// <param name="currSeq">Chaine de caractères qui représente la séquence en cours de la valeur.</param>
			/// <param name="nextSeq">Chaine de caractères qui représente la séquence d'après de la valeur.</param>
			/// <returns>True si c'est une fausse date, False sinon.</returns>
			
			public static boolean _CheckValue (KeyValue keyValue,  Date date) {
				// Cas particuliers : présence d'une date d'une loi + exercice.
				TextBlock	prevBlock = keyValue.getValueBlocks() [0].getPreviousTextBlock();
				TextBlock	nextBlock = keyValue.getValueBlocks() [keyValue.getValueBlocks().length - 1].getNextTextBlock();

				String			prevSeq;			
				if (prevBlock != null)
					prevSeq = TextBlock.GetSequenceString (prevBlock.getLeftSequence());
				else
					prevSeq = null;

				String			nextSeq = null;

				if (nextBlock != null)
				{
					nextSeq = TextBlock.GetSequenceString (nextBlock.getRightSequence());
					// Cas particulier : présence de la chaine "au" dans une mot.
					int position = nextSeq.toLowerCase().indexOf("au");

					if (position > 0 && (nextSeq.length() - position > 2))
					{
						if (NumberHelper.IsAlphabetic (nextSeq.charAt(position - 1)) && NumberHelper.IsAlphabetic (nextSeq.charAt(position + 2)))
							nextSeq = null;
					}
				}	

				String			currSeq = TextBlock.GetSequenceString (keyValue.getValueBlocks());

				if (!DateFinder._IsWrongDate ("Loi", prevSeq, currSeq, null) &&
					!DateFinder._IsWrongDate ("Exercice", prevSeq, currSeq, null) &&
					!DateFinder._IsWrongDate ("clos", prevSeq, currSeq, null) &&
					!DateFinder._IsWrongDate ("au", prevSeq, currSeq, nextSeq) &&
					!DateFinder._IsWrongDate ("echeance", prevSeq, currSeq, null) &&
					!DateFinder._IsWrongDate ("echtance", prevSeq, currSeq, null) &&
					!DateFinder._IsWrongDate ("ech6ance", prevSeq, currSeq, null) &&
					!DateFinder._IsWrongDate ("6ch6ance", prevSeq, currSeq, null) &&
					!DateFinder._IsWrongDate ("edition", prevSeq, currSeq, null) &&
					!DateFinder._IsWrongDate ("cave", prevSeq, currSeq, null) &&
					!DateFinder._IsWrongDate ("commande", prevSeq, currSeq, null) &&
					!DateFinder._IsWrongDate ("mail", prevSeq, currSeq, null) &&
					!DateFinder._IsWrongDate ("signe", prevSeq, currSeq, null) &&
					!DateFinder._IsWrongDate ("cpter", prevSeq, currSeq, null) &&
					!DateFinder._IsWrongDate ("compter", prevSeq, currSeq, null) &&
					!DateFinder._IsWrongDate ("prelevee", prevSeq, currSeq, null) &&
					!DateFinder._IsWrongDate ("Liv", prevSeq, currSeq, null) &&
					!DateFinder._IsWrongDate ("vers", prevSeq, currSeq, null) &&
					!DateFinder._IsWrongDate ("soit", prevSeq, currSeq, null) &&
					!DateFinder._IsWrongDate ("arret", prevSeq, currSeq, null))
				{
					try {
						return NumberHelper.ConvertDate (currSeq,  date);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}
				}
				else
				{
					date = new Date ();
					return false;
				}
			}
			/// <summary>
			/// Vérifie si la valeur trouvée d'une clé est une bonne
			/// valeur ou non.
			/// </summary>
			/// <param name="keyValue">Bloc texte qui représente la valeur de la clé.</param>
			/// <param name="allBlocks">Liste de tous les blocs objects de la recherche.</param>
			/// <returns>True si la valeur est bonne. False sinon.</returns>
			@Override
			public boolean CheckValue (KeyValue keyValue, Block [] allBlocks)
			{
				Date		date = null;
				return DateFinder._CheckValue (keyValue, date);
			}
			/// <summary>
			/// Retourne la valeur trouvée de type <see cref="DateTime" />.
			/// </summary>
			/// <param name="validKeyValue">La valeur valide de l'instance en cours.</param>
			/// <returns>Valeur trouvée.</returns>
			@Override
			public Date GetValue (KeyValue validKeyValue)
			{
				Date		date = null;
				DateFinder._CheckValue (validKeyValue, date);

				return date;
			}
			/// <summary>
			/// Retourne tous les instances clés/valeurs de l'instance
			/// en cours.
			/// </summary>
			/// <returns>Tableau de type <see cref="FacKeyValue" />.</returns>
			public  KeyValue [] GetAllKeyValues ()
			{
				KeyValue []		allKeyValues = super.GetAllKeyValues ();

				if (this._NullKeyValues != null)
				{
					KeyValue []		_allKeyValues = new KeyValue [allKeyValues.length + this._NullKeyValues.length];

					
					_allKeyValues=Arrays.copyOfRange(allKeyValues, 0, allKeyValues.length);
					_allKeyValues=Arrays.copyOfRange(this._NullKeyValues, allKeyValues.length, this._NullKeyValues.length);

					
					return _allKeyValues;
				}
				else
					return allKeyValues;
			}
		
			

}
