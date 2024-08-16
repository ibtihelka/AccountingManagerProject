package com.accounting_manager.bank_statement_engine.Classes.key;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeyString implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6484971322346044822L;
    /// <summary>
    /// Tableau de caractères de type <see cref="FacKeyStringChar" />
    /// qui représente la chaines de caractères de la clé.
    /// </summary>
    private List<KeyStringChar> stringChars;
    /// <summary>
    /// Indique qu'il faut exclure la clé.
    /// </summary>
    private boolean isExcepted;
    /// <summary>
    /// Indique que la chaine de caractères
    /// de la clé est sensible à la casse.
    /// </summary>
    private boolean isCaseSensitive;
    /// <summary>
    /// Poids de la clé. Le poids le plus faible
    /// représente la clé la plus importante.
    /// </summary>
    private int weight;
    /// <summary>
    /// Indique qu'il ne faut pas vérifier les chaines
    /// de caractères de mauvaises qualités.
    /// </summary>
    private Boolean[] skipCheckBQ;
    /// <summary>
    /// Chaine de caractères qui représente
    /// l'instance en cours.
    /// </summary>
    private String string;

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
    private KeyString(List<KeyStringChar> stringChars, Boolean[] skipCheckBQ, boolean isExcepted, boolean isCaseSensitive, int weight) {
        this.stringChars = stringChars;
        this.skipCheckBQ = skipCheckBQ;

        this.isExcepted = isExcepted || weight < -1;
        this.isCaseSensitive = isCaseSensitive;
        this.weight = weight;
        if (areAllElementsNull(skipCheckBQ)) {
            for (int i = 0; i < skipCheckBQ.length; i++) {
                skipCheckBQ[i] = false;
            }
        }
        this.string = ToString("", true);
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
    public KeyString(List<KeyStringChar> stringChars, boolean isExcepted, boolean isCaseSensitive, int weight) {

        this(stringChars, new Boolean[stringChars.size()], isExcepted, isCaseSensitive, weight);
    }

    /// <summary>
    /// Initialise une nouvelle instance de la classe
    /// avec des paramètres spécifiés. La clé sera insensible à la casse.
    /// </summary>
    /// <param name="stringChars">Chaines de caractères de la clé.</param>
    /// <param name="weight">Poids de la clé.</param>
    public KeyString(List<KeyStringChar> stringChars, int weight) {

        this(stringChars, false, false, weight);
    }

    /// <summary>
    /// Initialise une nouvelle instance de la classe
    /// avec des paramètres spécifiés.
    /// </summary>
    /// <param name="stringChars">Chaines de caractères de la clé.</param>
    /// <param name="isCaseSensitive">Indique que la chaine de caractères
    /// de la clé est sensible à la casse.</param>
    public KeyString(List<KeyStringChar> stringChars, boolean isCaseSensitive) {

        this(stringChars, false, isCaseSensitive, -1);
    }

    /// <summary>
    /// Initialise une nouvelle instance de la classe
    /// avec des paramètres spécifiés.
    /// </summary>
    /// <param name="stringChars">Chaines de caractères de la clé.</param>
    public KeyString(List<KeyStringChar> stringChars) {

        this(stringChars, false, false, -1);
    }

    public static boolean areAllElementsNull(Object[] array) {
        for (Object element : array) {
            if (element != null) {
                return false;
            }
        }
        return true;
    }

    public static String[] concatenateArrays(String[] array1, String[] array2) {
        String[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    public List<KeyStringChar> getStringChars() {
        return this.stringChars;
    }

    public void setStringChars(List<KeyStringChar> stringChars) {
        this.stringChars = stringChars;
    }

    public boolean isExcepted() {
        return isExcepted;
    }

    public void setExcepted(boolean isExcepted) {
        this.isExcepted = isExcepted;
    }

    public boolean isCaseSensitive() {
        return isCaseSensitive;
    }

    public void setCaseSensitive(boolean isCaseSensitive) {
        this.isCaseSensitive = isCaseSensitive;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Boolean[] getSkipCheckBQ() {
        return skipCheckBQ;
    }

    public void setSkipCheckBQ(Boolean[] skipCheckBQ) {
        this.skipCheckBQ = skipCheckBQ;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getStringCharsCount() {
        return this.stringChars.size();
    }

    /// <summary>
    /// Retourne le caractères qui se trouve
    /// à la position <paramref name="index" />
    /// de la chaine de caractères de l'instance en cours.
    /// </summary>
    /// <param name="index">Entier qui représente l'indice du caractère.</param>
    /// <returns>Une instance de type <see cref="string" />.</returns>
    public KeyStringChar GetStringChar(int index) {

        return this.stringChars.get(index);
    }

    /// <summary>
    /// Retourne la liste complète de chaines
    /// de cracatères que l'instance en cours peut représenter.
    /// Cette méthode se base sur les caractères optionnels
    /// <see cref="FacKeyStringChar.IsOptional" /> et les mauvaises
    /// détections.
    /// </summary>
    /// <returns>Tableau de même type que la classe en cours.</returns>
    public List<KeyString> GetAllKeyStrings() {
        List<KeyString> allKeyStrings = new ArrayList<>();

        KeyStringChar[] keyStringChars;
        KeyStringChar keyStringChar;
        String[] badQualityChars, _badQualityChars;
        Boolean[] skipCheckBQ;

        for (int i = 0; i < this.getStringChars().size(); i++) {
            keyStringChar = this.getStringChars().get(i);
            // Optionnel.
            if (keyStringChar.isOptional()) {
                // Avec le caract�re optionnel.
                keyStringChars = this.getStringChars().toArray(new KeyStringChar[this.getStringChars().size()]);
                keyStringChars[i] = new KeyStringChar(keyStringChar.getString(), keyStringChar.isSingle(), false, keyStringChar.isShared(), keyStringChar.isSupportBadQuality(), keyStringChar.getMinSeperatorWidth(), keyStringChar.getMaxSeperatorWidth());

                skipCheckBQ = this.skipCheckBQ.clone();

                allKeyStrings.addAll(new KeyString(Arrays.asList(keyStringChars), skipCheckBQ, this.isExcepted, this.isCaseSensitive, this.weight).GetAllKeyStrings());
                // Sans le caract�re optionnel.
                keyStringChars = new KeyStringChar[this.getStringChars().size() - 1];

                for (int j = 0; j < i; j++) {
                    keyStringChars[j] = this.getStringChars().get(j);
                }

                for (int j = i + 1; j < this.getStringChars().size(); j++) {
                    keyStringChars[j - 1] = this.getStringChars().get(j);
                }

                skipCheckBQ = this.skipCheckBQ.clone();

                allKeyStrings.addAll(new KeyString(Arrays.asList(keyStringChars), skipCheckBQ, this.isExcepted, this.isCaseSensitive, this.weight).GetAllKeyStrings());
                break;
            }
            // Mauvaise d�tection.
            else if (keyStringChar.isSupportBadQuality() && !this.skipCheckBQ[i]) {
                badQualityChars = KeyStringChar.GetBadQualityChars(keyStringChar.getString()).toArray(new String[KeyStringChar.GetBadQualityChars(keyStringChar.getString()).size()]);

                if (badQualityChars.length > 0) {
                    _badQualityChars = concatenateArrays(new String[]{keyStringChar.getString()}, badQualityChars);
                    // Ajout r�cusif.
                    for (String badQualityChar : _badQualityChars) {
                        keyStringChars = this.getStringChars().toArray(new KeyStringChar[this.getStringChars().size()]);
                        keyStringChars[i] = new KeyStringChar(badQualityChar, keyStringChar.isSingle(), false, keyStringChar.isShared(), keyStringChar.isSupportBadQuality(), keyStringChar.getMinSeperatorWidth(), keyStringChar.getMaxSeperatorWidth());

                        skipCheckBQ = this.skipCheckBQ.clone();
                        skipCheckBQ[i] = true;

                        allKeyStrings.addAll(new KeyString(Arrays.asList(keyStringChars), skipCheckBQ, this.isExcepted, this.isCaseSensitive, this.weight).GetAllKeyStrings());
                    }

                }
                this.skipCheckBQ[i] = true;
            }
        }

        if (allKeyStrings.size() == 0)
            return Arrays.asList(new KeyString[]{this});

        return allKeyStrings;

    }

    //	public List<KeyString> GetAllKeyStrings ()
//	{
//		List<KeyString>		allKeyStrings = new ArrayList<> ();
//
//		List<KeyStringChar>		keyStringChars;
//		KeyStringChar		keyStringChar;
//		List<String>				badQualityChars, _badQualityChars;
//		List<Boolean> skipCheckBQ;
//
//		for (int i = 0; i < this.getStringChars().size(); i ++)
//		{
//			keyStringChar = this.getStringChars().get(i);
//			// Optionnel.
//			if (keyStringChar.isOptional())
//			{
//				// Avec le caract re optionnel.
//				keyStringChars =  this.getStringChars().stream().collect(Collectors.toList());
//				keyStringChars.set(i, new KeyStringChar(keyStringChar.getString(), keyStringChar.isSingle(), false, keyStringChar.isShared(), keyStringChar.isSupportBadQuality(), keyStringChar.getMinSeperatorWidth(), keyStringChar.getMaxSeperatorWidth()));
//
//				skipCheckBQ = this.skipCheckBQ.stream().collect(Collectors.toList());
//
//				allKeyStrings.addAll(new KeyString (keyStringChars, skipCheckBQ, this.isExcepted, this.isCaseSensitive, this.weight).GetAllKeyStrings ());
//				// Sans le caract re optionnel.
//				keyStringChars = new ArrayList<>(this.getStringChars().size() - 1);
//
//				for (int j = 0; j < i; j ++)
//				{
//					keyStringChars.set(j, this.getStringChars().get(j));}
//
//				for (int j = i + 1; j < this.getStringChars().size(); j ++)
//				{
//					keyStringChars.set(j - 1, this.getStringChars().get(j));}
//
//				skipCheckBQ = this.skipCheckBQ.stream().collect(Collectors.toList());
//
//				allKeyStrings.addAll(new KeyString (keyStringChars, skipCheckBQ, this.isExcepted, this.isCaseSensitive, this.weight).GetAllKeyStrings ());
//				break;
//			}
//			// Mauvaise d tection.
//			else if (keyStringChar.isSupportBadQuality() && !this.skipCheckBQ.get(i))
//			{
//				badQualityChars = KeyStringChar.GetBadQualityChars(keyStringChar.getString()).stream().collect(Collectors.toList());
//
//				if (badQualityChars.size() > 0)
//				{
//					_badQualityChars = new ArrayList<>();
//					// Ajouter la chaine de caract res en cours.
//					_badQualityChars.add(keyStringChar.getString());
//					_badQualityChars.addAll(badQualityChars);
//					// Ajout r cusif.
//					for (String badQualityChar : _badQualityChars)
//					{
//						keyStringChars = this.getStringChars().stream().collect(Collectors.toList());
//						keyStringChars.set(i, new KeyStringChar(badQualityChar, keyStringChar.isSingle(), false, keyStringChar.isShared(), keyStringChar.isSupportBadQuality(), keyStringChar.getMinSeperatorWidth(), keyStringChar.getMaxSeperatorWidth()));
//
//						skipCheckBQ = this.skipCheckBQ.stream().collect(Collectors.toList());
//						skipCheckBQ.set(i, true);
//
//						allKeyStrings.addAll (new KeyString (keyStringChars, skipCheckBQ, this.isExcepted, this.isCaseSensitive, this.weight).GetAllKeyStrings ());
//					}
//				}
//
//				this.skipCheckBQ.set(i, true);
//			}
//		}
//
//		if (allKeyStrings.size() == 0)
//			return Arrays.asList(new KeyString [] {this});
//		else
//		{
//
//			int []				lengths = new int [allKeyStrings.size()];
//
//			for (int i = 0; i < allKeyStrings.size(); i ++)
//			{lengths [i] = allKeyStrings.get(i).ToString (" ").length();}
//
//			ArrayList<KeyString> sortedkeyStringsList = new ArrayList(allKeyStrings);
//			Collections.sort(sortedkeyStringsList, Comparator.comparing(s -> lengths[allKeyStrings.indexOf(s)]).reversed());
//
//
//			return sortedkeyStringsList;
//		}
//	}
    /// <summary>
    /// Retourne la chaine de caractères résultat
    /// de la concaténation des chaines de caractères
    /// de l'instance en cours.
    /// </summary>
    /// <param name="sep">Spérateur entre les caractères.</param>
    /// <param name="useCaseSensitivity">Indique qu'il faut utiliser la propriété de la sensibilité à la casse.</param>
    /// <returns>Chaine de caractères de type <see cref="string" />.</returns>
    public String ToString(String sep, boolean useCaseSensitivity) {
        sep = useCaseSensitivity && !this.isCaseSensitive ? sep.toUpperCase() : sep;
        String str = "";
        KeyStringChar strChar;
        String _strChar;

        for (int i = 0; i < this.stringChars.size(); i++) {
            strChar = this.stringChars.get(i);
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
    public String ToString(String sep) {
        return ToString(sep, true);
    }

    /// <summary>
    /// Retourne la chaine de caractères résultat
    /// de la concaténation des chaines de caractères
    /// de l'instance en cours.
    /// </summary>
    /// <returns>Chaine de caractères de type <see cref="string" />.</returns>
    @Override
    public String toString() {
        return ToString(" ", false);
    }
}
