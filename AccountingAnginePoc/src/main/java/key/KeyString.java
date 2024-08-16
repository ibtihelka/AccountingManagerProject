package key;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class KeyString  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6484971322346044822L;
			/// <summary>
			/// Tableau de caractères de type <see cref="FacKeyStringChar" />
			/// qui représente la chaines de caractères de la clé.
			/// </summary>
			private KeyStringChar []		stringChars;
			/// <summary>
			/// Indique qu'il faut exclure la clé.
			/// </summary>
			private boolean					isExcepted;
			/// <summary>
			/// Indique que la chaine de caractères
			/// de la clé est sensible à la casse.
			/// </summary>
			private boolean					isCaseSensitive;
			/// <summary>
			/// Poids de la clé. Le poids le plus faible
			/// représente la clé la plus importante.
			/// </summary>
			private int						weight;
			/// <summary>
			/// Indique qu'il ne faut pas vérifier les chaines
			/// de caractères de mauvaises qualités.
			/// </summary>
			private boolean []					skipCheckBQ;
			/// <summary>
			/// Chaine de caractères qui représente
			/// l'instance en cours.
			/// </summary>
			private String					string;
			public KeyStringChar[] getStringChars() {
				return stringChars;
			}
			public boolean isExcepted() {
				return isExcepted;
			}
			public boolean isCaseSensitive() {
				return isCaseSensitive;
			}
			public int getWeight() {
				return weight;
			}
			public boolean[] getSkipCheckBQ() {
				return skipCheckBQ;
			}
			public String getString() {
				return string;
			}
			public void setStringChars(KeyStringChar[] stringChars) {
				this.stringChars = stringChars;
			}
			public int getStringCharsCount()
			{
				return this.stringChars.length;
			}
			public void setExcepted(boolean isExcepted) {
				this.isExcepted = isExcepted;
			}
			public void setCaseSensitive(boolean isCaseSensitive) {
				this.isCaseSensitive = isCaseSensitive;
			}
			public void setWeight(int weight) {
				this.weight = weight;
			}
			public void setSkipCheckBQ(boolean[] skipCheckBQ) {
				this.skipCheckBQ = skipCheckBQ;
			}
			public void setString(String string) {
				this.string = string;
			}
			
			
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="stringChars">Chaines de caractères de la clé.</param>
			/// <param name="skipCheckBQ">Indique qu'il ne faut pas vérifier les chaines
			/// de caractères de mauvaises qualités.</param>
			/// <param name="isExcepted">Indique qu'il faut exclure la clé.</param>
			/// <param name="isCaseSensitive">Indique que la chaine de caractères
			/// de la clé est sensible à la casse.</param>
			/// <param name="weight">Poids de la clé.</param>
			private KeyString (KeyStringChar [] stringChars, boolean [] skipCheckBQ, boolean isExcepted, boolean isCaseSensitive, int weight){
				this.stringChars = stringChars;
				this.skipCheckBQ = skipCheckBQ;

				this.isExcepted = isExcepted || weight < -1;
				this.isCaseSensitive = isCaseSensitive;
				this.weight = weight;

				this.string = ToString ("", true);
			}
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="stringChars">Chaines de caractères de la clé.</param>
			/// <param name="isExcepted">Indique qu'il faut exclure la clé.</param>
			/// <param name="isCaseSensitive">Indique que la chaine de caractères
			/// de la clé est sensible à la casse.</param>
			/// <param name="weight">Poids de la clé.</param>
			public KeyString (KeyStringChar [] stringChars, boolean isExcepted, boolean isCaseSensitive, int weight){
				
				 this (stringChars, new boolean [stringChars.length], isExcepted, isCaseSensitive, weight);
			}
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés. La clé sera insensible à la casse.
			/// </summary>
			/// <param name="stringChars">Chaines de caractères de la clé.</param>
			/// <param name="weight">Poids de la clé.</param>
			public KeyString (KeyStringChar [] stringChars, int weight){
				
				  this (stringChars, false, false, weight);
			}
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="stringChars">Chaines de caractères de la clé.</param>
			/// <param name="isCaseSensitive">Indique que la chaine de caractères
			/// de la clé est sensible à la casse.</param>
			public KeyString (KeyStringChar [] stringChars, boolean isCaseSensitive){
				
				 this (stringChars, false, isCaseSensitive, -1);
			}
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="stringChars">Chaines de caractères de la clé.</param>
			public KeyString (KeyStringChar [] stringChars) {
				
				 this (stringChars, false, false, -1);
			}
			
			/// <summary>
			/// Retourne le caractères qui se trouve
			/// à la position <paramref name="index" />
			/// de la chaine de caractères de l'instance en cours.
			/// </summary>
			/// <param name="index">Entier qui représente l'indice du caractère.</param>
			/// <returns>Une instance de type <see cref="string" />.</returns>
			public KeyStringChar GetStringChar (int index){
				
				return this.stringChars [index];
			}
			
			/// <summary>
			/// Retourne la liste complète de chaines
			/// de cracatères que l'instance en cours peut représenter.
			/// Cette méthode se base sur les caractères optionnels
			/// <see cref="FacKeyStringChar.IsOptional" /> et les mauvaises
			/// détections.
			/// </summary>
			/// <returns>Tableau de même type que la classe en cours.</returns>
			public KeyString [] GetAllKeyStrings ()
			{
				List<KeyString>	    allKeyStrings = new ArrayList<KeyString> ();

				KeyStringChar []	keyStringChars;
				KeyStringChar		keyStringChar;
				String []			badQualityChars, _badQualityChars;
				boolean []			skipCheckBQ;

				for (int i = 0; i < this.getStringChars().length; i ++)
				{
					keyStringChar = this.getStringChars() [i];
					// Optionnel.
					if (keyStringChar.isOptional())
					{
						// Avec le caractère optionnel.
						keyStringChars = (KeyStringChar [])this.getStringChars().clone ();
						keyStringChars [i] = new KeyStringChar (keyStringChar.getString(), keyStringChar.isSingle(),  false, keyStringChar.isShared(), keyStringChar.isSupportBadQuality(), keyStringChar.getMinSeperatorWidth(), keyStringChar.getMaxSeperatorWidth());

						skipCheckBQ = (boolean [])this.getSkipCheckBQ().clone ();

						Collections.addAll (allKeyStrings,new KeyString (keyStringChars, skipCheckBQ, this.isExcepted, this.isCaseSensitive, this.weight).GetAllKeyStrings ());
						// Sans le caractère optionnel.
						keyStringChars = new KeyStringChar [this.stringChars.length - 1];

						for (int j = 0; j < i; j ++)
						{
							keyStringChars [j] = this.stringChars [j];
						}

						for (int j = i + 1; j < this.stringChars.length; j ++)
						{
							keyStringChars [j - 1] = this.stringChars [j];
						}

						skipCheckBQ = (boolean [])this.skipCheckBQ.clone ();

						Collections.addAll (allKeyStrings,new KeyString (keyStringChars, skipCheckBQ, this.isExcepted, this.isCaseSensitive, this.weight).GetAllKeyStrings ());
						break;
					}
					// Mauvaise détection.
					else if (keyStringChar.isSupportBadQuality() && !this.skipCheckBQ [i])
					{
						badQualityChars = KeyStringChar.GetBadQualityChars (keyStringChar.getString());

						if (badQualityChars.length > 0)
						{
							_badQualityChars = new String [badQualityChars.length + 1];
							// Ajouter la chaine de caractères en cours.
							_badQualityChars [0] = keyStringChar.getString();
							_badQualityChars=Arrays.copyOfRange (badQualityChars, 1,badQualityChars.length);
							// Ajout récusif.
							for(String badQualityChar : _badQualityChars)
							{
								keyStringChars = (KeyStringChar [])this.stringChars.clone ();
								keyStringChars [i] = new KeyStringChar (badQualityChar, keyStringChar.isSingle(), false, keyStringChar.isShared(), keyStringChar.isSupportBadQuality(), keyStringChar.getMinSeperatorWidth(), keyStringChar.getMaxSeperatorWidth());

								skipCheckBQ = (boolean [])this.skipCheckBQ.clone ();
								skipCheckBQ [i] = true;

								Collections.addAll (allKeyStrings,new KeyString (keyStringChars, skipCheckBQ, this.isExcepted, this.isCaseSensitive, this.weight).GetAllKeyStrings ());
							}
						}

						this.skipCheckBQ [i] = true;
					}
				}

				if (allKeyStrings.size() == 0)
					return new KeyString [] {this};
				else
				{
					KeyString []		keyStrings = (KeyString[]) allKeyStrings.toArray ();
					int []				lengths = new int [keyStrings.length];

					for (int i = 0; i < keyStrings.length; i ++)
					{
						lengths [i] = keyStrings [i].ToString (" ").length();
					}

					//Arrays.Sort<int,KeyString> (lengths, keyStrings);
					//Arrays.Reverse (keyStrings);
					
					
					List<KeyString> keyStringsList = Arrays.asList(keyStrings);
					ArrayList<KeyString> sortedkeyStringsList = new ArrayList(keyStringsList);
					Collections.sort(sortedkeyStringsList, Comparator.comparing(s -> lengths[keyStringsList.indexOf(s)]).reversed());
				     

					return (KeyString[])sortedkeyStringsList.toArray();
				}
			}
			
			
			/// <summary>
			/// Retourne la chaine de caractères résultat
			/// de la concaténation des chaines de caractères
			/// de l'instance en cours.
			/// </summary>
			/// <param name="sep">Spérateur entre les caractères.</param>
			/// <param name="useCaseSensitivity">Indique qu'il faut utiliser la propriété de la sensibilité à la casse.</param>
			/// <returns>Chaine de caractères de type <see cref="string" />.</returns>
			public String ToString (String sep, boolean useCaseSensitivity)
			{
				sep = useCaseSensitivity && !this.isCaseSensitive ? sep.toUpperCase () : sep;
				String				str = "";

				KeyStringChar	strChar;
				String				_strChar;

				for (int i = 0; i < this.stringChars.length; i ++)
				{
					strChar = this.stringChars [i];
					_strChar = useCaseSensitivity && !this.isCaseSensitive ? strChar.getUpperString() : strChar.getString();

					if (i == 0)
						str = _strChar;
					else
						str += sep + _strChar;
				}

				return str;
			}
			
			/// <summary>
			/// Retourne la chaine de caractères résultat
			/// de la concaténation des chaines de caractères
			/// de l'instance en cours.
			/// </summary>
			/// <param name="sep">Spérateur entre les caractères.</param>
			/// <returns>Chaine de caractères de type <see cref="string" />.</returns>
			public String ToString (String sep){
				return ToString (sep, true);
				}
			/// <summary>
			/// Retourne la chaine de caractères résultat
			/// de la concaténation des chaines de caractères
			/// de l'instance en cours.
			/// </summary>
			/// <returns>Chaine de caractères de type <see cref="string" />.</returns>
			@Override
			public String toString ()
			{
				return ToString (" ", false);
				}
}
