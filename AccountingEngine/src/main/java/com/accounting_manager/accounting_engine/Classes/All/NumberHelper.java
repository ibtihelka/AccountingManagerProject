package com.accounting_manager.accounting_engine.Classes.All;

import com.accounting_manager.accounting_engine.Classes._Out._Out;
import com.accounting_manager.accounting_engine.Classes.block.Block;
import com.accounting_manager.accounting_engine.Classes.geometry.Region;
import com.accounting_manager.accounting_engine.Classes.text.TextBlock;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Log4j2
/// <summary>
/// Classe qui réperensente des fonctionnalitées numerique
/// comme les tests vérification des convertion et autres.
/// </summary>
public class NumberHelper implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /// <summary>
    /// Verifie si un caractère est un numéro ou non.
    /// </summary>
    /// <param name="c">Caractère objet du test.</param>
    /// <returns>Vrai si la valeur est un numéro, False sinon.</returns>
    public static boolean IsDigit(char c) {
        // Convertir le caractère en code ascii.
        int asciiCode = (int) c;
        // Tester s'il est un entier.
        if (asciiCode >= 48 && asciiCode <= 57)
            return true;
        else
            return false;
    }

    /// <summary>
    /// Verifie si un caractère est un cractère alphabétique ou non.
    /// </summary>
    /// <param name="c">Caractère objet du test.</param>
    /// <returns>Vrai si la valeur est un alphabet, False sinon.</returns>
    public static boolean IsAlphabetic(char c) {
        String charStr = String.valueOf(c).toUpperCase();
        // Convertir le caractère en code ascii.
        int asciiCode = (int) charStr.charAt(0);
        // Tester s'il est un entier.
        if (asciiCode >= 65 && asciiCode <= 90)
            return true;
        else
            return false;
    }

    /// <summary>
    /// Verifie si la chaine et un numéro rcs.
    /// </summary>
    /// <param name="rcs">Chaine rcs.</param>
    /// <returns>Vrai si est un numéro si non faut.</returns>
    public static boolean IsRcs(String rcs) {
        int sum = 0;
        int value = 0;
        // Tester sur la longeur de la chaine.
        if (rcs.length() == 9) {
            for (int e = 0; e < rcs.length(); e++) {
                // Tester sur l'existantce des caractère non digital.
                if (!NumberHelper.IsDigit(rcs.charAt(e)))
                    return false;
                    // Algorithme de calcul rcs.
                else {
                    value = rcs.charAt(e) - 48;

                    if (e % 2 != 0) {
                        value *= 2;

                        if (value >= 10)
                            sum += (value % 10) + 1;
                        else
                            sum += value;
                    } else
                        sum += value;
                }
            }
            // Valider les calcules.
            return sum % 10 == 0;
        } else
            return false;

    }

    /// <summary>
    /// Verifie si la chaine et un numéro siret.
    /// </summary>
    /// <param name="siret">Chaine siret.</param>
    /// <returns>Vrai si est un numéro siret si non faut.</returns>
    public static boolean IsSiret(_Out<String> siret) {
        int sum = 0;
        int value = 0;
        // Test sur la longeur de la chaine.
        if (siret.get().length() == 14) {
            for (int e = 0; e < siret.get().length(); e++) {
                //Test sur l'existantce des caractère non digital.
                if (!NumberHelper.IsDigit(siret.get().charAt(e))) {
                    e = siret.get().length();
                    sum = 1;
                }
                // Algorithme de calcul siret.
                else {
                    value = siret.get().charAt(e) - 48;

                    if (e % 2 == 0) {
                        value *= 2;

                        if (value >= 10)
                            sum += (value % 10) + 1;
                        else
                            sum += value;
                    } else
                        sum += value;
                }

            }
            // Valider les calcules.
            return sum % 10 == 0;
        } else
            return false;
    }

    /// <summary>
    /// Verifie si la chaine et un numéro siret.
    /// </summary>
    /// <param name="tva">Chaine siret.</param>
    /// <returns>Vrai si est un numéro siret si non faut.</returns>
    public static boolean IsTva(_Out<String> tva) {
        String rcs;
        _Out<String> siret = new _Out<>(null);
        String countryIdentifier;
        boolean isFound;
        // Test sur la longeur de la chaine.
        if (tva.get().length() == 18) {
            // Extraire le numero siret.
            siret.set(StringHelper.substring(tva.get(), 4, 14));
            countryIdentifier = StringHelper.substring(tva.get(), 0, 2);
            isFound = NumberHelper.IsSiret(siret) && countryIdentifier.equals("FR");
            if (isFound)
                return true;
            else
                tva.set(tva.get().substring(0, 13));
        }
        if (tva.get().length() == 13) {
            // Extraire le numero rcs.
            rcs = StringHelper.substring(tva.get(), 4, 9);
            countryIdentifier = StringHelper.substring(tva.get(), 0, 2);
            // Verification.
            return NumberHelper.IsRcs(rcs) && countryIdentifier.equals("FR");
        } else
            return false;
    }

    /// <summary>
    /// Verifie si une chaine de carcatères est un montant.
    /// </summary>
    /// <param name="amount">Chaine de caractères objet du test.</param>
    /// <param name="checkDecSeparator">Indique qu'il faut vérifier la présence du séparetur de décimal.</param>
    /// <returns>True si c'est un montant, False sinon.</returns>
    public static boolean IsAmount(String amount, boolean checkDecSeparator) {
        // Présence du '%'.
        if (amount.indexOf('%') > -1)
            return false;

        amount = amount.replace(',', '.');
        // Verification de la longueur.
        if (amount.length() < 4 || amount.charAt(amount.length() - 1) == '.' || amount.charAt(0) == '.')
            return false;
        else {
            boolean hasDecSeparator = false;
            boolean dijitDetected = false;
            boolean dijitAfterSeparator = false;
            int NumberDijitAfterSeparator = 0;
            char[] chars = amount.toCharArray();

            // iterate over `char[]` array using enhanced for-loop

            for (char c : chars) {
                if (c == '.') {
                    if (!dijitDetected || hasDecSeparator)
                        return false;

                    hasDecSeparator = true;
                } else if (NumberHelper.IsDigit(c)) {
                    dijitDetected = true;

                    if (hasDecSeparator)
                        NumberDijitAfterSeparator++;
                } else
                    return false;
            }

            if (NumberDijitAfterSeparator < 4)
                dijitAfterSeparator = true;
            // Vérification de la présence d'un décimal.
            return checkDecSeparator && hasDecSeparator && dijitAfterSeparator;
        }
    }

    /// <summary>
    /// Verifie si une chaine de carcatères est un montant.
    /// </summary>
    /// <param name="amount">Chaine de caractères objet du test.</param>
    /// <returns>True si c'est un montant, False sinon.</returns>
    public static boolean IsAmount(String amount) {
        return NumberHelper.IsAmount(amount, false);
    }

    /// <summary>
    /// Convertit la chaine de caractère passée en paramètre
    /// en un montant de type <see cref="double" />. Deux nombres
    /// après la virgule seront prise en compte.
    /// </summary>
    /// <param name="amount">Chaine de caractères qui contient le montant. La chaine
    /// de cracrtères doit être nettoyée.</param>
    /// <returns>Un nombre flottant de type <see cref="double" />.</returns>
    public static double GetAmount(String amount) {
        amount = amount.replace(',', '.');
        double d = 0;
        int indexOf = amount.indexOf('.');
        // Montant avec virgule '.'.
        if (indexOf > -1) {
            int decimalDijitsCount = amount.length() - 1 - indexOf;

            if (decimalDijitsCount > 2)
                amount = StringHelper.substring(amount, 0, indexOf + 3);
        }

        return Double.parseDouble(amount);
    }

    /// <summary>
    /// Nettoie un bloc d'un supposé montant.
    /// </summary>
    /// <param name="floatBlocks">Liste des blocs du montant.</param>
    /// <param name="startBlock">Bloc de départ du montant.</param>
    /// <returns>Blocs textes qui contiennent le montant nettoyé.</returns>
    public static List<TextBlock> CleanFloatBlocks(List<TextBlock> floatBlocks, TextBlock startBlock) throws Exception {
        List<TextBlock> subBlocs = new ArrayList<>();
        TextBlock block;

        String cleanFloat;
        _Out<Integer> floatStartIndex = new _Out<>(0);
        TextBlock center;
        _Out<TextBlock> left = new _Out<>(null);
        _Out<TextBlock> right = new _Out<>(null);
        // Partie centrale.
        cleanFloat = NumberHelper.CleanFloatStr(startBlock.getText(), floatStartIndex);

        if (cleanFloat != "") {
            TextBlock startCenterBlock;
            _Out<TextBlock> startLeftBlock = new _Out<>(null), startRightBlock = new _Out<>(null);
            startCenterBlock = startBlock.Split(floatStartIndex.get(), cleanFloat.length(), startLeftBlock, startRightBlock);
            int startBlockIndex = Region.getIndex(startBlock, (List<Block>) (List<?>) floatBlocks);

            if (startBlockIndex < 0)
                throw new Exception("Start bloc must included in sequance blocks");
            // Extraire l'indice du bloc central.
            if (startLeftBlock == null) {
                for (int i = startBlockIndex - 1; i > -1; i--) {
                    block = floatBlocks.get(i);
                    cleanFloat = NumberHelper.CleanFloatStr(block.getText(), floatStartIndex);
                    // OK.
                    if (cleanFloat.length() == block.getText().length())
                        subBlocs.add(block);
                        // Not OK.
                    else {
                        if (cleanFloat.length() > 0) {
                            center = block.Split(floatStartIndex.get(), cleanFloat.length(), left, right);

                            if (right == null)
                                subBlocs.add(center);
                        }
                        break;
                    }
                }

                Collections.reverse(subBlocs);

            }
            // Partie centrale.
            subBlocs.add(startCenterBlock);
            // Partie droite.
            if (startRightBlock == null) {
                for (int i = startBlockIndex + 1; i < floatBlocks.size(); i++) {
                    block = floatBlocks.get(i);
                    cleanFloat = NumberHelper.CleanFloatStr(block.getText(), floatStartIndex);
                    // OK.
                    if (cleanFloat.length() == block.getText().length())
                        subBlocs.add(block);
                        // Not OK.
                    else {
                        if (cleanFloat.length() > 0) {
                            center = block.Split(floatStartIndex.get(), cleanFloat.length(), left, right);

                            if (left == null)
                                subBlocs.add(center);
                        }
                        break;
                    }
                }
            }
        }

        return subBlocs;
    }

    /// <summary>
    /// Nettoie une chaine montant.
    /// </summary>
    /// <param name="str">Chaine du montant.</param>
    /// <param name="floatStartIndex">Indice de début du montant.</param>
    /// <returns>Chaine nettoyée.</returns>
    public static String CleanFloatStr(String str, _Out<Integer> floatStartIndex)
    {
        floatStartIndex.set(-1);
        char c;
        int endIndex = str.length() - 1;
        boolean isNumberChar;

        for (int i = 0; i < str.length(); i++)
        {
            c = str.charAt (i);
            isNumberChar = NumberHelper.IsDigit(c) || c == ',' || c == '.' || c == '%';
            // Nettoyage de gauche.
            if (floatStartIndex.get() == -1)
            {
                if (isNumberChar)
                {
                    floatStartIndex.set(i);
                }
            }
            // Nettoyage de droite.
            else if (!isNumberChar)
            {
                endIndex = i - 1;
                break;
            }

        }
        // Retourner le r�sultat.
        if (floatStartIndex.get() > -1)
        {
            return str.substring(floatStartIndex.get(), endIndex + 1);
        }
        else
        {
            return "";
        }
    }

    /// <summary>
    /// Nettoie une chaine montant.
    /// </summary>
    /// <param name="str">Chaine du montant.</param>
    /// <returns>Chaine nettoyée.</returns>
//    public static String CleanFloatSubstring(String str) {
//        StringBuilder _str = new StringBuilder(str.length());
//        boolean isNotValidSeparator;
//        int length = str.length(), testLength;
//
//        String dotPattern = "[.]+";
//        String virgulPattern = "[,]+";
//        // Use the matches() method to test if the string matches the dot pattern
//        if (str.matches(dotPattern) || str.matches(virgulPattern) )
//            return str;
//
//        for (int i = 0; i < length; i++) {
//            testLength = length - 1;
//
//            if (i + 3 < testLength)
//                isNotValidSeparator = (str.charAt(i) == '.' || str.charAt(i) == ',') && NumberHelper.IsDigit(str.charAt(i + 1)) && NumberHelper.IsDigit(str.charAt(i + 2)) && NumberHelper.IsDigit(str.charAt(i + 3));
//            else if (i + 1 < testLength)
//                isNotValidSeparator = (str.charAt(i) == '.' || str.charAt(i) == ',') && (str.charAt(i + 1) == '.' || str.charAt(i + 1) == ',');
//            else
//                isNotValidSeparator = false;
//
//            if (isNotValidSeparator) {
//                char[] chars = str.toCharArray();
//
//                // iterate over `char[]` array using enhanced for-loop
//
//                for (char c : chars) {
//
//                    _str.append(c);
//                }
//
//
//                _str.deleteCharAt(i);
//                str = _str.toString();
//                i -= 1;
//                length = str.length();
//            }
//        }
//
//        return str;
//    }
    public static String CleanFloatSubstring(String str) {
        if (str.matches("[.,]+")) {
            return str;
        }

        StringBuilder cleaned = new StringBuilder(str.length());
        int length = str.length();

        for (int i = 0; i < length; i++) {
            char currentChar = str.charAt(i);

            if (currentChar == '.' || currentChar == ',') {
                if (isInvalidSeparator(str, i)) {
                    continue;
                }
            }

            cleaned.append(currentChar);
        }

        return cleaned.toString();
    }

    private static boolean isInvalidSeparator(String str, int index) {
        int length = str.length();
        int testLength = length - 1;

        if (index + 3 < testLength) {
            return (NumberHelper.IsDigit(str.charAt(index + 1)) &&
                    NumberHelper.IsDigit(str.charAt(index + 2)) &&
                    NumberHelper.IsDigit(str.charAt(index + 3)));
        } else if (index + 1 < testLength) {
            return (str.charAt(index + 1) == '.' || str.charAt(index + 1) == ',');
        }

        return false;
    }

    /// <summary>
    /// Nettoie une chaine montant.
    /// </summary>
    /// <param name="str">Chaine du montant.</param>
    /// <returns>Chaine nettoyée.</returns>
    public static String CleanFloatStr(String str) {
        _Out<Integer> floatStartIndex = new _Out<>(0);
        return NumberHelper.CleanFloatStr(str, floatStartIndex);
    }

    /// <summary>
    /// Nettoie une chaine de carcatères qui contient un numéro.
    /// </summary>
    /// <param name="str">Chaine de caractères qui contient un numéro.</param>
    /// <returns>Chaine nettoyé.</returns>
    public static String CleanNumberStr(String str) {
        StringBuilder _str = new StringBuilder(str.length());

        char[] chars = str.toCharArray();

        // iterate over `char[]` array using enhanced for-loop

        for (char c : chars) {
            if (NumberHelper.IsDigit(c))
                _str.append(c);
        }

        return _str.toString();
    }

    /// <summary>
    /// Nettoie une chaine de carcatères d'un identifiant  du founisseur.
    /// </summary>
    /// <param name="str">Chaine de caractères qui contient l'identifiant.</param>
    /// <returns>Chaine nettoyé.</returns>
    public static String CleanIdFournisseur(String str) {
        StringBuilder _str = new StringBuilder(str.length());

        char[] chars = str.toCharArray();

        // iterate over `char[]` array using enhanced for-loop

        for (char c : chars) {
            if (c != '.' && c != ',')
                _str.append(c);
        }


        return _str.toString();
    }

    /// <summary>
    /// Nettoyage de la chaine numéro facture.
    /// </summary>
    /// <param name="str">Chaine du numéro facture</param>
    /// <returns>Chaine nettoyer.</returns>
    public static _Out<String> CleanNumberFac(_Out<String> str) {
        StringBuilder _str = new StringBuilder(str.get().length());

        if (str.get().length() >= 2) {
            char c0 = str.get().charAt(0);
            char c1 = str.get().charAt(1);

            if ((c0 == 'n' || c0 == 'N') && (c1 == '°' || c1 == '0' || c1 == 'o' || c1 == '*' || c1 == '\\' || c1 == '\''))
                str.set(str.get().substring(1));
        }
        // effacer la date.
        int positionKey1 = str.get().toLowerCase().indexOf("du");
        int positionKey2 = str.get().toLowerCase().indexOf("ou");
        int positionKey3 = str.get().toLowerCase().indexOf("|");

        if (positionKey1 > -1) {
            str.set(StringHelper.substring(str.get(), 0, positionKey1));
        } else if (positionKey2 > -1) {
            str.set(StringHelper.substring(str.get(), 0, positionKey2));
        } else if (positionKey3 > -1) {
            str.set(StringHelper.substring(str.get(), 0, positionKey3));
        }
        char[] chars = str.get().toCharArray();
        // nettoyer le numéro de facture.
        for (char c : chars) {
            if (NumberHelper.IsDigit(c) || NumberHelper.IsAlphabetic(c) ||
                    c == '-' || c == '/' || c == '_' || c == '.') {
                _str.append(c);
            }
        }

        return new _Out<>(_str.toString());
    }

    /// <summary>
    /// Verifie la chaine si elle est une date.
    /// </summary>
    /// <param name="date">Chaine de test.</param>
    /// <returns>Vrai si est une date, false sinon.</returns>
    public static Boolean TryParse(String str, _Out<Integer> i) {
        try {
            i.set(Integer.parseInt(str));
            return true;
        } catch (NumberFormatException e) {
            log.error("Error occurred while parsing the string : " + str + "\n" + e);
        }
        return false;
    }


//	public static boolean IsDate(String date) {
//		String stringTest;
//		String stringTest2;
//		int day = 0;
//		int month = 0;
//		int year = 0;
//		int counter = 0;
//		_Out<Integer> result = new _Out<Integer>();
//		// Calcule de nombre de caractere '/' existant dans la chaine.
//		for (int k = 0; k < date.length(); k++) {
//			if (date.charAt(k) == '/')
//				counter++;
//		}
//
//		if (counter == 2) {
//			// Extraire le jour, le Month et l'année
//			stringTest = date.substring(0, date.indexOf('/'));
//
//			if (stringTest.length() != 0 && stringTest.length() < 3 && TryParse(stringTest, result)) {
//				day = result.get();
//				stringTest = date.substring(date.indexOf('/') + 1);
//				stringTest2 = stringTest.substring(0, stringTest.indexOf('/'));
//
//				if (stringTest2.length() != 0 && stringTest2.length() < 3 && TryParse(stringTest2, result))
//					month = result.get();
//
//				stringTest = stringTest.substring(stringTest.indexOf('/') + 1);
//
//				if (stringTest.length() != 0 && (stringTest.length() == 2 || stringTest.length() == 4)) {
//					if (stringTest.charAt(0) == '0' && stringTest.length() == 2 && stringTest != "00")
//
//						stringTest = stringTest.substring(0, 20) + "0" + stringTest.substring(20);
//					else if (stringTest.charAt(0) == '9' && stringTest.length() == 2)
//						stringTest = stringTest.substring(0, 29) + "0" + stringTest.substring(29);
//
//					if (TryParse(stringTest, result))
//						year = result.get();
//					else
//						year = 0;
//				}
//			} else if (stringTest.length() != 0 && stringTest.length() == 4 && TryParse(stringTest, result)) {
//				year = result.get();
//				stringTest = date.substring(date.indexOf('/') + 1);
//				stringTest2 = stringTest.substring(0, stringTest.indexOf('/'));
//
//				if (stringTest2.length() != 0 && stringTest2.length() < 3 && TryParse(stringTest2, result))
//					month = result.get();
//
//				stringTest = stringTest.substring(stringTest.indexOf('/') + 1);
//
//				if (stringTest.length() != 0 && stringTest.length() < 3 && TryParse(stringTest, result))
//					day = result.get();
//			}
//			// Vérification de résultat.
//			if (day <= 31 && month <= 12 && day > 0 && month > 0 && year >= 1990 && year <= 2020)
//				return true;
//			else
//				return false;
//		} else
//			return false;
//	}

    public static boolean IsDate(String date) {
        String stringTest;
        String stringTest2;
        int day = 0;
        int month = 0;
        int year = 0;
        int counter = 0;
        _Out<Integer> result = new _Out<>(0);
        // Calcule de nombre de caractere '/' existant dans la chaine.
        for (int k = 0; k < date.length(); k++) {
            if (date.charAt(k) == '/') {
                counter++;
            }
        }

        if (counter == 2) {
            // Extraire le jour, le Month et l'ann�e
            stringTest = StringHelper.substring(date, 0, date.indexOf('/'));
            if (stringTest.length() != 0 && stringTest.length() < 3 && TryParse(stringTest, result)) {
                day = result.get();
                stringTest = date.substring(date.indexOf('/') + 1);
                stringTest2 = StringHelper.substring(stringTest, 0, stringTest.indexOf('/'));

                if (stringTest2.length() != 0 && stringTest2.length() < 3 && TryParse(stringTest2, result)) {
                    month = result.get();
                }

                stringTest = stringTest.substring(stringTest.indexOf('/') + 1);

                if (stringTest.length() != 0 && (stringTest.length() == 2 || stringTest.length() == 4)) {
                    if (stringTest.charAt(0) == '0' && stringTest.length() == 2 && !stringTest.equals("00"))
                        stringTest = "20" + stringTest;
                    else if (stringTest.charAt(0) == '9' && stringTest.length() == 2)
                        stringTest = "19" + stringTest;

                    if (TryParse(stringTest, result))
                        year = result.get();
                    else
                        year = 0;
                }
            } else if (stringTest.length() != 0 && stringTest.length() == 4 && TryParse(stringTest, result)) {
                year = result.get();
                stringTest = date.substring(date.indexOf('/') + 1);
                stringTest2 = StringHelper.substring(stringTest, 0, stringTest.indexOf('/'));
                if (stringTest2.length() != 0 && stringTest2.length() < 3 && TryParse(stringTest2, result))
                    month = result.get();

                stringTest = stringTest.substring(stringTest.indexOf('/') + 1);

                if (stringTest.length() != 0 && stringTest.length() < 3 && TryParse(stringTest, result))
                    day = result.get();
            }
            // V�rification de resultat.
            if (day <= 31 && month <= 12 && day > 0 && month > 0 && year >= 1990 && year <= 2020)
                return true;
            else
                return false;
        } else
            return false;
    }

    /// <summary>
    /// Trouver une date dans une chaine.
    /// </summary>
    /// <param name="str">Chaine de test.</param>
    /// <param name="date">Resultate de la date trouver.</param>
    /// <returns>Retourne si la date et valide ou non.</returns>
//			public static boolean ConvertDate (String str,  _Out<Date> date) throws ParseException
//			{
//
//			    String []			month = {"Janvier", "Fevrier", "Mars", "Avril", "Mai", "Juin", "Juillet", "Aout", "Septembre", "Octobre", "Novembre", "Decembre"};
//			    String []			monthMin  = {"janvier", "fevrier", "mars", "avril", "mai", "juin", "juillet", "aout", "septembre", "octobre", "novembre", "decembre"};
//			    String []			monthDesig = {"janv", "fev", "mars", "avr", "mai", "juin", "juil", "aout", "sept", "oct", "nov", "dec"};
//				String				dateResult = "";
//			    String				dateResultMins;
//			    String				dateResultDesig;
//			    char				compt;
//				char				comptMin;
//				char				comptDesig;
//			    int					value;
//				int					valueMin;
//				int					valueDesig;
//			    // Recherche les nom des mois dans la chaine.
//				for (int d = 0; d < 12; d ++)
//			    {
//			        value = str.indexOf (month [d]);
//			        valueMin = str.indexOf (monthMin [d]);
//			        valueDesig = str.indexOf (monthDesig [d]);
//
//			        while (value > 0 || valueMin > 0 || valueDesig > 0)
//			        {
//						dateResult = "";
//			            dateResultMins = "";
//			            dateResultDesig = "";
//			            if (value > 0)
//			            {
//							// Convertir le nom du mois vers numéro pour un nom qui commence par majiscule.
//							if (d < 9)
//							{
//								compt = (char)(d + 49);
//			                    dateResult = dateResult + "/0" + compt + "/";
//			                    str = str.replace (month [d], dateResult);
//			                }
//			                else
//			                {
//								compt = (char)((d + 1) % 10 + 48);
//			                    dateResult = dateResult + "/1" + compt + "/";
//			                    str = str.replace (month [d], dateResult);
//			                }
//						}
//			            else if (valueMin > 0)
//			            {
//							// Convertir le nom du mois vers numéro pour un nom miniscule.
//							if (d < 9)
//			                {
//								comptMin = (char)(d+49);
//			                    dateResultMins = dateResultMins + "/0" + comptMin + "/";
//			                    str = str.replace (monthMin [d], dateResultMins);
//			                }
//			                else
//			                {
//								comptMin = (char)((d + 1) % 10 + 48);
//			                    dateResultMins = dateResultMins + "/1" + comptMin + "/";
//			                    str = str.replace (monthMin [d], dateResultMins);
//			                }
//						}
//			            else if (valueDesig > 0)
//			            {
//							// Convertir le nom du mois vers numéro pour une designation.
//							if (d < 9)
//			                {
//								comptDesig = (char)(d + 49);
//			                    dateResultDesig = dateResultDesig + "/0" + comptDesig + "/";
//			                    str = str.replace (monthDesig [d], dateResultDesig);
//							}
//			                else
//			                {
//								comptDesig = (char)((d+1)%10+48);
//			                    dateResultDesig = dateResultDesig + "/1" + comptDesig + "/";
//			                    str = str.replace (monthDesig [d], dateResultDesig);
//							}
//						}
//			            value = str.indexOf (month [d]);
//			            valueMin = str.indexOf (monthMin [d]);
//			            valueDesig = str.indexOf (monthDesig [d]);
//					}
//				}
//				// Nettoyage de la chaine.
//				str = NumberHelper.CleanDateStr (str);
//			    str = str.replace ('-', '/');
//			    str = str.replace ('.', '/');
//			    int				posdoubleslach = str.indexOf ("//");
//			    while (posdoubleslach > 0)
//			    {
//			    	StringBuilder		sb0 = new StringBuilder (str.length());
//                	char[] chars = str.toCharArray();
//
//			        // iterate over `char[]` array using enhanced for-loop
//
//				for(char c : chars)
//				{
//
//						sb0.append (c);
//				}
//
//
//					sb0.deleteCharAt(posdoubleslach);
//
//
//					str = sb0.toString();
//					str=str.substring(0, posdoubleslach) + "/" + str.substring(posdoubleslach);
//			        posdoubleslach = str.indexOf ("//");
//				}
//				// Recherche de la date dans la chaine.
//			    int				pos = str.indexOf ("/");
//			    boolean			state = false;
//
//			    while (pos >= 0)
//			    {
//					pos = str.indexOf ("/");
//			        if (pos >= 2)
//			        {
//						if (str.length() - (pos - 2) >= 8)
//						{
//							if (str.length() - (pos - 2) >= 10)
//							{
//								if (pos >= 4 && NumberHelper.IsDigit (str.charAt(pos-4)))
//									dateResult = str.substring (pos - 4, 10);
//								else
//									dateResult = str.substring (pos - 2, 10);
//							}
//							else if (str.length() - (pos - 2) >= 8)
//									dateResult = str.substring (pos - 2, 8);
//
//			                for(int i = 0; i < dateResult.length(); i++)
//			                {
//			                    if (!IsDigit (dateResult.charAt(i)) && dateResult.charAt(i) != '/')
//			                    {
//			                    	StringBuilder		_str = new StringBuilder (dateResult.length());
//			                    	char[] chars = dateResult.toCharArray();
//
//							        // iterate over `char[]` array using enhanced for-loop
//
//								for(char c : chars)
//								{
//
//										_str.append (c);
//								}
//
//
//									_str.deleteCharAt(i);
//
//
//
//			                       dateResult =  _str.toString ();
//			                        i = i-1;
//			                    }
//			                }
//
//			                if (dateResult.charAt(dateResult.length()-1) == '/' && dateResult.length() > 3)
//			                {
//			                	StringBuilder		sb = new StringBuilder (dateResult.length());
//		                    	char[] chars = dateResult.toCharArray();
//
//						        // iterate over `char[]` array using enhanced for-loop
//
//							for(char c : chars)
//							{
//
//									sb.append (c);
//							}
//
//
//								sb.deleteCharAt(dateResult.length()-1);
//								;
//
//
//		                       dateResult =  sb.toString ();
//
//		                    }
//			                }
//
//
//			                if( IsDate (dateResult))
//			                {
//			                    pos = (-1);
//			                    state = true;
//			                }
//			                else
//			                {
//			                	StringBuilder		sb1 = new StringBuilder (str.length());
//		                    	char[] chars = str.toCharArray();
//
//						        // iterate over `char[]` array using enhanced for-loop
//
//							for(char c : chars)
//							{
//
//									sb1.append (c);
//							}
//
//
//								sb1.deleteCharAt(pos);
//								;
//
//
//		                       str =  sb1.toString ();
//			                }
//
//						}
//						else
//						{
//							StringBuilder		sb1 = new StringBuilder (str.length());
//	                    	char[] chars = str.toCharArray();
//
//					        // iterate over `char[]` array using enhanced for-loop
//
//						for(char c : chars)
//						{
//
//								sb1.append (c);
//						}
//
//
//							sb1.deleteCharAt(pos);
//							;
//
//
//	                       str =  sb1.toString ();
//
//						}
//
//
//			         if (pos >= 0)
//			        {
//			        	StringBuilder		sb1 = new StringBuilder (str.length());
//	                	char[] chars = str.toCharArray();
//
//				        // iterate over `char[]` array using enhanced for-loop
//
//					for(char c : chars)
//					{
//
//							sb1.append (c);
//					}
//
//
//						sb1.deleteCharAt(pos);
//						;
//
//
//	                   str =  sb1.toString ();
//			        }
//
//				}
//				// Retour des résultat trouver.
//		        if (!state)
//				{
//					date.set(new Date ());
//					return false;
//				}
//				else
//				{
//
//					@SuppressWarnings("unused")
//					Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(dateResult);
//
//					return true;
//				}
//			}
    public static boolean ConvertDate(String str, _Out<Date> date) throws ParseException {
        String[] month = {"Janvier", "Fevrier", "Mars", "Avril", "Mai", "Juin", "Juillet", "Aout", "Septembre", "Octobre", "Novembre", "Decembre"};
        String[] monthMin = {"janvier", "fevrier", "mars", "avril", "mai", "juin", "juillet", "aout", "septembre", "octobre", "novembre", "decembre"};
        String[] monthDesig = {"janv", "fev", "mars", "avr", "mai", "juin", "juil", "aout", "sept", "oct", "nov", "dec"};
        String dateResult = "";
        String dateResultMins;
        String dateResultDesig;
        char compt;
        char comptMin;
        char comptDesig;
        int value;
        int valueMin;
        int valueDesig;
        // Recherche les nom des mois dans la chaine.
        for (int d = 0; d < 12; d++) {
            value = str.indexOf(month[d]);
            valueMin = str.indexOf(monthMin[d]);
            valueDesig = str.indexOf(monthDesig[d]);

            while (value > 0 || valueMin > 0 || valueDesig > 0) {
                dateResult = "";
                dateResultMins = "";
                dateResultDesig = "";
                if (value > 0) {
                    // Convertir le nom du mois vers num�ro pour un nom qui commence par majiscule.
                    if (d < 9) {
                        compt = (char) (d + 49);
                        dateResult = dateResult + "/0" + compt + "/";
                        str = str.replace(month[d], dateResult);
                    } else {
                        compt = (char) ((d + 1) % 10 + 48);
                        dateResult = dateResult + "/1" + compt + "/";
                        str = str.replace(month[d], dateResult);
                    }
                } else if (valueMin > 0) {
                    // Convertir le nom du mois vers num�ro pour un nom miniscule.
                    if (d < 9) {
                        comptMin = (char) (d + 49);
                        dateResultMins = dateResultMins + "/0" + comptMin + "/";
                        str = str.replace(monthMin[d], dateResultMins);
                    } else {
                        comptMin = (char) ((d + 1) % 10 + 48);
                        dateResultMins = dateResultMins + "/1" + comptMin + "/";
                        str = str.replace(monthMin[d], dateResultMins);
                    }
                } else if (valueDesig > 0) {
                    // Convertir le nom du mois vers num�ro pour une designation.
                    if (d < 9) {
                        comptDesig = (char) (d + 49);
                        dateResultDesig = dateResultDesig + "/0" + comptDesig + "/";
                        str = str.replace(monthDesig[d], dateResultDesig);
                    } else {
                        comptDesig = (char) ((d + 1) % 10 + 48);
                        dateResultDesig = dateResultDesig + "/1" + comptDesig + "/";
                        str = str.replace(monthDesig[d], dateResultDesig);
                    }
                }
                value = str.indexOf(month[d]);
                valueMin = str.indexOf(monthMin[d]);
                valueDesig = str.indexOf(monthDesig[d]);
            }
        }
        // Nettoyage de la chaine.
        str = NumberHelper.CleanDateStr(str);
        str = str.replace('-', '/');
        str = str.replace('.', '/');
        int posdoubleslach = str.indexOf("//");
        while (posdoubleslach > 0) {
            str = StringHelper.remove(str, posdoubleslach, 2);
            str = str.substring(0, posdoubleslach) + "/" + str.substring(posdoubleslach);
            posdoubleslach = str.indexOf("//");
        }
        // Recherche de la date dans la chaine.
        int pos = str.indexOf("/");
        boolean state = false;

        while (pos >= 0) {
            pos = str.indexOf("/");
            if (pos >= 2) {
                if (str.length() - (pos - 2) >= 8) {
                    if (str.length() - (pos - 2) >= 10) {
                        if (pos >= 4 && NumberHelper.IsDigit(str.charAt(pos - 4))) {
                            dateResult = str.substring(pos - 4, pos - 4 + 10);
                        } else {
                            dateResult = str.substring(pos - 2, pos - 2 + 10);
                        }
                    } else if (str.length() - (pos - 2) >= 8) {
                        dateResult = str.substring(pos - 2, pos - 2 + 8);
                    }

                    for (int i = 0; i < dateResult.length(); i++) {
                        if (!IsDigit(dateResult.charAt(i)) && dateResult.charAt(i) != '/') {
                            dateResult = StringHelper.remove(dateResult, i, 1);
                            i = i - 1;
                        }
                    }

                    if (dateResult.charAt(dateResult.length() - 1) == '/' && dateResult.length() > 3) {
                        dateResult = StringHelper.remove(dateResult, dateResult.length() - 1, 1);
                    }

                    if (IsDate(dateResult)) {
                        pos = (-1);
                        state = true;
                    } else {
                        str = StringHelper.remove(str, pos, 1);
                    }
                } else {
                    str = StringHelper.remove(str, pos, 1);
                }
            } else if (pos >= 0) {
                str = StringHelper.remove(str, pos, 1);
            }
        }
        // Retour des r�sultat trouver.
        if (!state) {
            date.set(new Date());
            return false;
        } else {
            String[] formatPatterns = {"dd/MM/yy","dd/MM/yyyy", "yyyy-MM-dd", "MM/dd/yyyy"}; // Add more patterns as needed

            Date parsedDate = null;
            for (String pattern : formatPatterns) {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                sdf.set2DigitYearStart(new Date(0));
                try {
                    parsedDate = sdf.parse(dateResult);
                    break; // Exit the loop if parsing is successful
                } catch (ParseException ignored) {
                    // Parsing failed for the current pattern, try the next one
                }
            }
            if (parsedDate != null) {
                date.set(parsedDate);
            }

            return true;
        }
    }

    /// <summary>
    /// Trouver une date dans une chaine.
    /// </summary>
    /// <param name="str">Chaine de test.</param>
    /// <returns>Retourne si la date et valide ou non.</returns>
    public static boolean ConvertDate(String str) throws ParseException {
        _Out<Date> date = new _Out<>(null);
        return NumberHelper.ConvertDate(str, date);
    }

    /// <summary>
    /// Nettoyage de la chaine.
    /// </summary>
    /// <param name="str">Chaine de caractères.</param>
    /// <returns>Chaine de caractères nettoyée.</returns>
    public static String CleanDateStr(String str) {
        StringBuilder _str = new StringBuilder(str.length());

        char[] chars = str.toCharArray();


        for (char c : chars) {
            if (c != ' ' && c != '\t' && c != 'e' && c != 'r')
                _str.append(c);
        }

        return _str.toString();
    }

    /// <summary>
    /// Teste sur l'existance d'un numéro.
    /// </summary>
    /// <param name="value">Valeur de test.</param>
    /// <returns>Vrai si un numéro trouver faut si non.</returns>
    public static boolean FindDigit(String value) {
        boolean state = false;

        if (value.length() != 0) {
            for (int i = 0; i < value.length(); i++) {
                if (NumberHelper.IsDigit((char) value.charAt(i))) {
                    state = true;
                    i = value.length();
                }
            }
        } else
            state = false;

        return state;
    }

    /// <summary>
    /// Test si le block est un numéro de téléphone ou fax.
    /// </summary>
    /// <param name="textBlocks">Blocks de test.</param>
    /// <returns>Vrai si le block est un numéro fax ou telephone faux si non.</returns>
    public static boolean IsPhoneNumber(List<TextBlock> textBlocks) {
        // Longueur minimale.
        if (textBlocks.size() < 3)
            return false;
        // Longueur > 0.
        TextBlock firstBlock = textBlocks.get(0);
        TextBlock prevFirstBlock = firstBlock.getPreviousTextBlock();
        String strFirstBlock = firstBlock.getText().toUpperCase();
        String strPrevFirstBlock;

        if (strFirstBlock.indexOf("TEL") > -1 || strFirstBlock.indexOf("FAX") > -1)
            return true;
        else if (prevFirstBlock != null) {
            strPrevFirstBlock = prevFirstBlock.getText().toUpperCase();

            if (strPrevFirstBlock.indexOf("TEL") > -1 || strPrevFirstBlock.indexOf("FAX") > -1)
                return true;
            else
                return false;
        } else {
            for (TextBlock textBlock : textBlocks) {
                if (textBlock.getText().length() != 2 || !FindDigit(textBlock.getText()))
                    return false;
            }

            return true;
        }
    }

    /// <summary>
    /// Pourcentage des chiffres dans une chaine.
    /// </summary>
    /// <param name="str">Chaine d'entrée.</param>
    /// <returns>Le pourcentage des chiffres touver dans la chaine.</returns>
    public static float DigitsRate(String str) {
        int countDigit = 0;
        char[] chars = str.toCharArray();
        // Calculer le nombre des chiffre dans une chaine.
        for (char c : chars) {
            if (NumberHelper.IsDigit(c))
                countDigit++;
        }

        return (float) countDigit / str.length();
    }

    /// <summary>
    /// Calcule le nombre d'occurence d'un caractère dans une cahine.
    /// </summary>
    /// <param name="str">Chaine de caractère</param>
    /// <param name="c">Caractère.</param>
    /// <returns>Le nombre d'occurrence.</returns>
    public static int CharCase(String str, char c) {
        int count = 0;
        char[] chars = str.toCharArray();
        for (char _c : chars) {
            if (_c == c)
                count++;
        }

        return count;
    }

    /// <summary>
    /// Trouve le premier numéro.
    /// </summary>
    /// <param name="str">Chaine d'entrée.</param>
    /// <returns>Premier numéro.</returns>
    public static char FindFirstChar(String str) {
        return str.charAt(0);
    }

    /// <summary>
    /// Retourne le numéro rcs à partir du numéro de tva.
    /// </summary>
    /// <param name="tva">Numéro de tva.</param>
    /// <returns>Numero rcs.</returns>
    public static String GetRcsFromTva(String tva) {
        return StringHelper.substring(tva, 4, 9);
    }

    /// <summary>
    /// Retourne le numéro rcs à partir de numéro de siret.
    /// </summary>
    /// <param name="siret">Numéro de siret.</param>
    /// <returns>Numero rcs.</returns>
    public static String GetRcsFromSiret(String siret) {
        return StringHelper.substring(siret, 0, 9);
    }
}
