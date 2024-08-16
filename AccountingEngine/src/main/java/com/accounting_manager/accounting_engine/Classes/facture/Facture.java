package com.accounting_manager.accounting_engine.Classes.facture;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.accounting_manager.accounting_engine.Classes.All.NumberHelper;
import com.accounting_manager.accounting_engine.Classes._Out._Out;
import com.accounting_manager.accounting_engine.Classes.document.DocumentProcessing;
import com.accounting_manager.accounting_engine.Classes.document._Document;
import com.accounting_manager.accounting_engine.Classes.finders.*;
import com.accounting_manager.accounting_engine.Classes.key.KeyFinder;
import com.accounting_manager.accounting_engine.Classes.key.KeyType;
import com.accounting_manager.accounting_engine.Classes.key.KeyValue;
import com.accounting_manager.accounting_engine.Classes.learning.LearningEngine;
import com.accounting_manager.accounting_engine.Classes.text.TextBlock;
import com.accounting_manager.accounting_engine.XMLReader.Dom4jXmlReader;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Log4j2
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "fullFileName")
public class Facture implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 7980598540359269499L;
    /// <summary>
    /// Largeur petite du label de la boite.
    /// </summary>
    public static int BOX_LABEL_WIDTH = 100;
    /// <summary>
    /// Largeur grande du label de la boite
    /// dans le cas du mode débogage.
    /// </summary>
    public static int DEBUG_BOX_LABEL_WIDTH_BIG = 75;
    /// <summary>
    /// Largeur petite du label de la boite
    /// dans le cas du mode débogage.
    /// </summary>
    public static int DEBUG_BOX_LABEL_WIDTH_SMALL = 40;
    /// <summary>
    /// Hauteur du label de la boite.
    /// </summary>
    public static int BOX_LABEL_HEIGHT = 30;
    /// <summary>
    /// Largeur de la ligne de la rectangle
    /// de la boîte.
    /// </summary>
    public static int BOX_LINE_WIDTH = 2;
    /// <summary>
    /// Police de caractères à utiliser pour
    /// dessiner le label de la boîte.
    /// </summary>
    public static Font BOX_FONT = new Font("Verdana", Font.BOLD, 14);
    /// <summary>
    /// Couleur de la boite des clés.
    /// </summary>
    public static Color KEY_BOX_COLOR = Color.RED;
    /// <summary>
    /// Couleur du label la boite des clés.
    /// </summary>
    public static Color KEY_BOX_LABEL_COLOR = Color.WHITE;
    /// <summary>
    /// Couleur de la boite des valeurs.
    /// </summary>
    public static Color VALUE_BOX_COLOR = new Color(39, 120, 180);
    /// <summary>
    /// Couleur du label de la boite des valeurs.
    /// </summary>
    public static Color VALUE_BOX_LABEL_COLOR = Color.WHITE;
    /// <summary>
    /// Couleur de la boite des valeurs
    /// destinées au débogage.
    /// </summary>
    public static Color DEBUG_VALUE_BOX_COLOR = Color.BLUE;
    /// <summary>
    /// Couleur du label de la boite
    /// des valeurs destinées au débogage.
    /// </summary>
    public static Color DEBUG_VALUE_BOX_LABEL_COLOR = Color.WHITE;
    /// <summary>
    /// Nombre fichiers aléatoires sevis.
    /// </summary>
    private static int rndFileNamesCount = 0;
    /// <summary>
    /// Générateur de nombres aléatoires
    /// utilisé lors de la génération des
    /// noms des fichiers aléatoires.
    /// </summary>
    private static Random rndFileNamesGenerator = new Random();
    /// <summary>
    /// Objet qui permet la synchronisation
    /// lors du choix d'un nom de fihcier aléotoire.
    /// </summary>
    private static Object sync_ImageRndNames = new Object();
    /// <summary>
    /// L'instance multi-facture de la facture en cours.
    /// </summary>
    private MultiFacture multiFacture;
    /// <summary>
    /// Indice de la facture en cours dans son document
    /// multi-facture <see cref="FacMultiFacture" /> parent.
    /// </summary>
    private int index;
    /// <summary>
    /// Nom du fichier de la facture. Le chemin
    /// doit être complet.
    /// </summary>
    @JsonProperty
    private String fullFileName;

    public Facture(MultiFacture multiFacture, int index, String fullFileName) {

        this.multiFacture = multiFacture;
        this.index = index;
        this.fullFileName = fullFileName;
    }

    public static String getUniqueFileName(String filePath, String separator) {
        // Extraire l'extension.
        int indexOf = filePath.lastIndexOf('.');

        String ext;
        String fileWithoutExt;

        if (indexOf > -1) {
            ext = filePath.substring(indexOf + 1);
            System.out.println(ext);
            fileWithoutExt = filePath.substring(0, indexOf);
        } else {
            ext = null;
            fileWithoutExt = filePath;
        }
        // Créer la partie aléatoire.
        String file_name;
        File f;

        //lock in c#
        synchronized (Facture.sync_ImageRndNames) {
            do {
                file_name =
                        fileWithoutExt +
                                separator + Facture.rndFileNamesCount + separator + Facture.rndFileNamesGenerator.nextInt(10000) +
                                (ext != null ? "." : "") + ext;

                f = new File(file_name);

            }
            while (f.exists());

            Facture.rndFileNamesCount++;
        }

        return file_name;
    }

    public static String getUniqueFileName(String filePath) {
        return Facture.getUniqueFileName(filePath, "-");
    }

    /// <summary>
    /// Obtient le nom du flux de données de l'image
    /// de la facture pour un type donné.
    /// </summary>
    /// <param name="imageType">Type de l'image.</param>
    /// <returns>Chaine de caractères qui représente le nom du flux.</returns>
    private static String getImageStreamName(ImageType imageType) {

        return imageType.toString();
    }


    /// <summary>
    /// Retourne un nom aléatoire unique d'un fichier.
    /// </summary>
    /// <param name="filePath">Nom complet du fichier.</param>
    /// <param name="separator">Chaine de caractères qui représente le séparateur.</param>
    /// <returns>Nom complet unique du fihcier.</returns>

    /// <summary>
    /// Obtient le nom du fichier de l'image pour un type donné.
    /// </summary>
    /// <param name="fileName">Nom du fichier orginale de l'image.</param>
    /// <param name="imageType">Type de l'image.</param>
    /// <returns>Chaine de caractères qui représente le nom du fichier de l'image.</returns>
    public static String getImageFileName(String fileName, ImageType imageType) {

        return fileName + "-" + Facture.getImageStreamName(imageType).toLowerCase() + ".mark.jpg";
    }
    /// <summary>
    /// Retourne un nom aléatoire unique d'un fichier.
    /// </summary>
    /// <param name="filePath">Nom complet du fichier.</param>
    /// <returns>Nom complet unique du fihcier.</returns>

    /// <summary>
    /// Obtient le nom du flux de données de la facture
    /// pour certaines configurations.
    /// </summary>
    /// <param name="processingType">Type du traitement.</param>
    /// <param name="rotationType">Type de la rotation.</param>
    /// <returns>Chaine de caractères qui représente le nom du flux.</returns>
    private static String getDocStreamName(DocumentProcessing processingType) {

        return String.valueOf(processingType);
    }

    /// <summary>
    /// Retourne le nom du fichier qui va contenir
    /// les données de sortie du processus.
    /// </summary>
    /// <param name="fileName">Nom du fichier.</param>
    /// <returns>Nom du fichier qui contient le résultat.</returns>
    public static String GetOutputProcessFileName(String fileName) {

        return fileName + ".ocr";
    }

    /// <summary>
    /// Construire une chaine de caractères compatible
    /// avec un passage d'arguments à un programme.
    /// </summary>
    /// <param name="args">Liste des chaines de caractères qui représente les arguments.</param>
    /// <returns>Chaine de caractères formatée.</returns>
    public static String FormatProcessArgs(String[] args) {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < args.length; i++) {
            if (i > 0)
                str.append(' ');
            char[] chars = args[i].toCharArray();
            for (char c : chars) {
                if (c == '|')
                    str.append("|0");
                else if (c == ' ')
                    str.append("|1");
                else
                    str.append(c);
            }
        }

        return str.toString();
    }

    /// <summary>
    /// Construire la chaine de caractères originale
    /// d'un argument passé en paramètre à un programme.
    /// </summary>
    /// <param name="argument">Chaines de caractères qui représente l'argument.</param>
    /// <returns>Chaine de caractères de l'argument original.</returns>
    public static String UnFormatProcessArgument(String argument) {
        StringBuilder str = new StringBuilder();
        char c;

        for (int i = 0; i < argument.length(); i++) {

            char[] chars = argument.toCharArray();
            c = chars[i];
            if (c == '|') {
                if (chars[i + 1] == '0')
                    str.append('|');
                else
                    str.append(' ');

                i++;
            } else
                str.append(c);
        }

        return str.toString();
    }

    /// <summary>
    /// Fusionne deux informations sur les factures.
    /// </summary>
    /// <param name="basicInfos">Informations de base.</param>
    /// <param name="newInfos">Nouvelles informations.</param>
    private static void mergeFactureInfos(FactureInfo basicInfos, FactureInfo newInfos) {
        if (basicInfos != null) {
            if (newInfos.isHasNumeroFacture()) {
                basicInfos.setNumeroFactureKeyValue(newInfos.getNumeroFactureKeyValue());
                basicInfos.setNumeroFactureUsedKeyValues(KeyValue.Merge(basicInfos.getNumeroFactureUsedKeyValues(), newInfos.getNumeroFactureUsedKeyValues()));
            }

            if (newInfos.isHasNumeroTva()) {
                basicInfos.setNumeroTvaKeyValue(newInfos.getNumeroTvaKeyValue());
                basicInfos.setNumeroTvaUsedKeyValues(KeyValue.Merge(basicInfos.getNumeroTvaUsedKeyValues(), newInfos.getNumeroTvaUsedKeyValues()));
            }

            if (newInfos.isHasNumeroSiret()) {
                basicInfos.setNumeroSiretKeyValue(newInfos.getNumeroSiretKeyValue());
                basicInfos.setNumeroSiretKeyValues(KeyValue.Merge(basicInfos.getNumeroSiretKeyValues(), newInfos.getNumeroSiretKeyValues()));
            }

            if (newInfos.isHasNumeroRcs()) {
                basicInfos.setNumeroRcsKeyValue(newInfos.getNumeroRcsKeyValue());
                basicInfos.setNumeroRcsKeyValues(KeyValue.Merge(basicInfos.getNumeroRcsKeyValues(), newInfos.getNumeroRcsKeyValues()));
            }

            if (newInfos.isHasNumeroPage()) {
                basicInfos.setNumeroPageKeyValue(newInfos.getNumeroPageKeyValue());
                basicInfos.setNumeroPageKeyValues(KeyValue.Merge(basicInfos.getNumeroPageKeyValues(), newInfos.getNumeroPageKeyValues()));
            }

            if (newInfos.isHasTtc()) {
                basicInfos.setTtcKeyValue(newInfos.getTtcKeyValue());
                basicInfos.setTtcUsedKeyValues(KeyValue.Merge(basicInfos.getTtcUsedKeyValues(), newInfos.getTtcUsedKeyValues()));
            }

            if (newInfos.isHasHt()) {
                if (newInfos.getHtKeyValue() != null)
                    basicInfos.setHtKeyValue(newInfos.getHtKeyValue());
                else
                    basicInfos.setHt(newInfos.getHt());

                basicInfos.setHtUsedKeyValues(KeyValue.Merge(basicInfos.getHtUsedKeyValues(), newInfos.getHtUsedKeyValues()));
            }

            if (newInfos.isHasTva()) {
                if (newInfos.getTvaKeyValue() != null)
                    basicInfos.setTvaKeyValue(newInfos.getTvaKeyValue());
                else
                    basicInfos.setTva(newInfos.getTva());

                basicInfos.setTvaUsedKeyValues(KeyValue.Merge(basicInfos.getTvaUsedKeyValues(), newInfos.getTvaUsedKeyValues()));
            }

            if (newInfos.isHasTvaSupp()) {
                basicInfos.setTvaSuppKeyValue(newInfos.getTvaSuppKeyValue());
                basicInfos.setTvaSuppUsedKeyValues(KeyValue.Merge(basicInfos.getTvaSuppUsedKeyValues(), newInfos.getTvaSuppUsedKeyValues()));
            }

            if (newInfos.isHasDiscount()) {
                basicInfos.setDiscountKeyValue(newInfos.getDiscountKeyValue());
                basicInfos.setDiscountUsedKeyValues(KeyValue.Merge(basicInfos.getDiscountUsedKeyValues(), newInfos.getDiscountUsedKeyValues()));
            }

            if (newInfos.isHasDate()) {
                basicInfos.setDateKeyValue(newInfos.getDateKeyValue());
                basicInfos.setDateUsedKeyValues(KeyValue.Merge(basicInfos.getDateUsedKeyValues(), newInfos.getDateUsedKeyValues()));
            }

            if (newInfos.isHasTypeFacture()) {
                basicInfos.setTypeFactureKeyValue(newInfos.getTypeFactureKeyValue());
                basicInfos.setTypeFactureUsedKeyValues(KeyValue.Merge(basicInfos.getTypeFactureUsedKeyValues(), newInfos.getTypeFactureUsedKeyValues()));
            }
        }
    }

    /// <summary>
    /// Permet l'extraction des montants HT, TVA et TTC.
    /// </summary>
    /// <param name="htFinder">L'instance <see cref="FacHtFinder" /> en cours.</param>
    /// <param name="tvaFinder">L'instance <see cref="FacTvaFinder" /> en cours.</param>
    /// <param name="ttcFinder">L'instance <see cref="FacTtcFinder" /> en cours.</param>
    /// <param name="tvaSuppFinder">L'instance <see cref="FacTvaSuppFinder" /> en cours.</param>
    /// <param name="amountFinder">L'instance <see cref="FacAmountFinder" /> en cours. Elle peut être nulle.</param>
    /// <param name="isSecondProcessing">Indique que le second traitement est activé.</param>
    /// <param name="documents">Instances des documents à inclure dans le traitement.</param>
    /// <param name="factureInfo">L'instance de <see cref="FactureInfo" /> en cours.</param>
    /// <returns>True si les montants on été trouvées, False sinon.</returns>
    private static boolean extractHtTvaTtc(
            HtFinder htFinder, TvaFinder tvaFinder, TtcFinder ttcFinder, TvaSuppFinder tvaSuppFinder, AmountFinder amountFinder,
            boolean isSecondProcessing,
            List<_Document> documents, FactureInfo factureInfo) {
        if (amountFinder == null) {
            htFinder.Find(documents);
            tvaFinder.Find(documents);
            ttcFinder.Find(documents);

            if (!isSecondProcessing)
                tvaSuppFinder.Find(documents);
        } else
            amountFinder.Find(documents);
        // Extraire toutes les valeurs.
        List<KeyValue> htKeyValues = htFinder.GetAllKeyValues();
        List<KeyValue> tvaKeyValues = tvaFinder.GetAllKeyValues();
        List<KeyValue> ttcKeyValues = ttcFinder.GetAllKeyValues();
        List<KeyValue> tvaSuppKeyValues = amountFinder == null ? tvaSuppFinder.GetAllKeyValues() : null;
        List<KeyValue> amountKeyValues = amountFinder != null ? amountFinder.GetAllKeyValues() : null;
        // Mettre à jour la liste complète des clés/valeurs en utilisant "AmountFinder".
        if (amountKeyValues != null) {
            htKeyValues = Facture.mergeKeyValues(htKeyValues, amountKeyValues);
            tvaKeyValues = Facture.mergeKeyValues(tvaKeyValues, amountKeyValues);
        }
        // Mise en cache des valeurs utilisées.
        factureInfo.setHtUsedKeyValues(htKeyValues);
        factureInfo.setTvaUsedKeyValues(tvaKeyValues);
        factureInfo.setTtcUsedKeyValues(ttcKeyValues);

        if (tvaSuppKeyValues != null)
            factureInfo.setTvaSuppUsedKeyValues(tvaSuppKeyValues);
        // Extraire les valeurs.
        Facture.fixHtTvaTtcValues(htFinder, tvaFinder, ttcFinder, tvaSuppFinder, htKeyValues, tvaKeyValues, ttcKeyValues, tvaSuppKeyValues);
        // Lancer l'extraction.
        boolean isFound;

        _Out<Double> validHt = new _Out<Double>(0.0);
        _Out<Double> validTva = new _Out<Double>(0.0);
        _Out<Double> validTtc = new _Out<Double>(0.0);

        _Out<KeyValue> validHtKeyValue = new _Out<KeyValue>(null);
        _Out<KeyValue> validTvaKeyValue = new _Out<KeyValue>(null);
        _Out<KeyValue> validTtcValue = new _Out<KeyValue>(null);
        _Out<KeyValue> validTvaSuppValue = new _Out<KeyValue>(null);

        if (amountKeyValues == null) {
            isFound = Facture.extractHtTvaTtc(
                    htKeyValues, tvaKeyValues, ttcKeyValues, tvaSuppKeyValues,
                    validHt, validTva, validTtc,
                    validHtKeyValue, validTvaKeyValue, validTtcValue, validTvaSuppValue);
        } else {
            isFound = Facture.extractHtTvaTtcWithTVARates(
                    htKeyValues, tvaKeyValues, ttcKeyValues,
                    validHt, validTva, validTtc,
                    validHtKeyValue, validTvaKeyValue, validTtcValue);
            validTvaSuppValue = null;
        }
        // Valeur trouvée.
        if (isFound) {
            if (validHtKeyValue.get() != null)
                factureInfo.setHtKeyValue(validHtKeyValue.get());
            else
                factureInfo.setHt(validHt.get());

            if (validTvaKeyValue.get() != null)
                factureInfo.setTvaKeyValue(validTvaKeyValue.get());
            else
                factureInfo.setTva(validTva.get());

            factureInfo.setTtcKeyValue(validTtcValue.get());
        }

        return isFound;
    }

    /// <summary>
    /// Fusionne deux tableaux de clé/valeur <see cref="KeyValue" />.
    /// </summary>
    /// <param name="keyValues1">Tableau 1.</param>
    /// <param name="keyValues2">Tableau 2.</param>
    /// <returns>Tableau résultat de la fusion des deux tableaux passés en paramètre.</returns>
    private static List<KeyValue> mergeKeyValues(List<KeyValue> keyValues1, List<KeyValue> keyValues2) {
        List<KeyValue> keyValues = new ArrayList<>(keyValues1.size() + keyValues2.size());


        keyValues.addAll(keyValues1);

        keyValues.addAll(keyValues2);

        return keyValues;
    }

    /// <summary>
    /// Permet l'extraction des montants HT, TVA et TTC.
    /// </summary>
    /// <param name="htFinder">L'instance <see cref="FacHtFinder" /> en cours.</param>
    /// <param name="tvaFinder">L'instance <see cref="FacTvaFinder" /> en cours.</param>
    /// <param name="ttcFinder">L'instance <see cref="FacHtFinder" /> en cours.</param>
    /// <param name="tvaSuppFinder">L'instance <see cref="FacTvaSuppFinder" /> en cours.</param>
    /// <param name="htKeyValues">Liste de toutes les valeurs possible des montants HT.</param>
    /// <param name="tvaKeyValues">Liste de toutes les valeurs possible des montants TVA.</param>
    /// <param name="ttcKeyValues">Liste de toutes les valeurs possible des montants TTC.</param>
    /// <param name="tvaSuppKeyValues">Liste de toutes les valeurs possible des TVA-Supplémantaires.</param>
    private static void fixHtTvaTtcValues(
            HtFinder htFinder, TvaFinder tvaFinder, TtcFinder ttcFinder, TvaSuppFinder tvaSuppFinder,
            List<KeyValue> htKeyValues, List<KeyValue> tvaKeyValues, List<KeyValue> ttcKeyValues, List<KeyValue> tvaSuppKeyValues) {
        // HT.
        for (KeyValue keyValue : htKeyValues) {
            if (keyValue.isValidValue())
                keyValue.setValue(htFinder.GetValue(keyValue));
        }
        // TVA.
        for (KeyValue keyValue : tvaKeyValues) {
            if (keyValue.isValidValue())
                keyValue.setValue(tvaFinder.GetValue(keyValue));
        }
        // TTC.
        for (KeyValue keyValue : ttcKeyValues) {
            if (keyValue.isValidValue())
                keyValue.setValue(ttcFinder.GetValue(keyValue));
        }
        // TVA-Supp.
        if (tvaSuppKeyValues != null) {
            for (KeyValue keyValue : tvaSuppKeyValues) {
                if (keyValue.isValidValue())
                    keyValue.setValue(tvaSuppFinder.GetValue(keyValue));
            }
        }
    }

    /// <summary>
    /// Permet l'extraction des montants HT, TVA et TTC.
    /// </summary>
    /// <param name="htKeyValues">Liste de toutes les valeurs possible des montants HT.</param>
    /// <param name="tvaKeyValues">Liste de toutes les valeurs possible des montants TVA.</param>
    /// <param name="ttcKeyValues">Liste de toutes les valeurs possible des montants TTC.</param>
    /// <param name="tvaSuppKeyValues">Liste de toutes les valeurs possible des montants TVA-Supplémentaires.</param>
    /// <param name="validHt">Montant HT valide.</param>
    /// <param name="validTva">Montant TVA valide.</param>
    /// <param name="validTtc">Montant TTC valide.</param>
    /// <param name="validHtKeyValue">clé/valeur du montant HT valide.</param>
    /// <param name="validTvaKeyValue">clé/valeur du montant TVA valide.</param>
    /// <param name="validTtcValue">clé/valeur du montant TTC valide.</param>
    /// <param name="validTvaSuppValue">clé/valeur du montant TVA supplémentaire valide.</param>
    /// <returns>True si les montants on été trouvées, False sinon.</returns>
    public static boolean extractHtTvaTtc(
            List<KeyValue> htKeyValues, List<KeyValue> tvaKeyValues, List<KeyValue> ttcKeyValues, List<KeyValue> tvaSuppKeyValues,
            _Out<Double> validHt, _Out<Double> validTva, _Out<Double> validTtc,
            _Out<KeyValue> validHtKeyValue, _Out<KeyValue> validTvaKeyValue, _Out<KeyValue> validTtcValue, _Out<KeyValue> validTvaSuppValue) {
        validHt.set(0.0);
        validTva.set(0.0);
        validTtc.set(0.0);


        boolean isFound = false;

        Double ht, tva, ttc, tvaSum = 0.0;
        Double maxTTC = 0.0;
        // Valider TTC = HT + TVA + TVA supp.
        for (KeyValue htKeyValue : htKeyValues) {
            if (htKeyValue.isValidValue()) {
                ht = (Double) htKeyValue.getValue();
                for (KeyValue tvaKeyValue : tvaKeyValues) {
                    if (tvaKeyValue.isValidValue()) {
                        tva = (Double) tvaKeyValue.getValue();
                        for (KeyValue tvaSuppKeyValue : tvaSuppKeyValues) {
                            if (tvaSuppKeyValue.isValidValue()) {
                                tvaSum = tva + (Double) tvaSuppKeyValue.getValue();
                                for (KeyValue ttcKeyValue : ttcKeyValues) {
                                    if (ttcKeyValue.isValidValue()) {
                                        ttc = (Double) ttcKeyValue.getValue();

                                        if ((tva == 0 || tva < ht) && ht > 0 && ht < ttc && ttc > maxTTC && TtcFinder.IsValidSum(ht, tvaSum, ttc)) {
                                            isFound = true;

                                            validHt.set(ht);
                                            validTva.set(tva);
                                            validTtc.set(ttc);
                                            ;

                                            validHtKeyValue.set(htKeyValue);
                                            validTvaKeyValue.set(tvaKeyValue);
                                            validTtcValue.set(ttcKeyValue);
                                            validTvaSuppValue.set(tvaSuppKeyValue);

                                            maxTTC = ttc;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // Valider TTC = HT + TVA.
        if (!isFound) {
            for (KeyValue htKeyValue : htKeyValues) {
                if (htKeyValue.isValidValue()) {
                    ht = (double) htKeyValue.getValue();
                    // Chosir les instances clés/valeurs avec clé.
                    if (validHtKeyValue != null && validHt.equals(ht) && validHtKeyValue == null && htKeyValue.getString() != null)
                        validHtKeyValue.set(htKeyValue);

                    for (KeyValue tvaKeyValue : tvaKeyValues) {
                        if (tvaKeyValue.isValidValue()) {
                            tva = (Double) tvaKeyValue.getValue();
                            // Chosir les instances clés/valeurs avec clé.
                            if (validTvaKeyValue != null && validTva.equals(maxTTC) && validTvaKeyValue == null && tvaKeyValue.getString() != null)
                                validTvaKeyValue.set(tvaKeyValue);

                            for (KeyValue ttcKeyValue : ttcKeyValues) {
                                if (ttcKeyValue.isValidValue()) {
                                    ttc = (Double) ttcKeyValue.getValue();
                                    // Chosir les instances clés/valeurs avec clé.
                                    if (validTtcValue != null && validTtc.equals(ttc) && validTtcValue == null && ttcKeyValue.getString() != null)

                                        validTtcValue.set(ttcKeyValue);

                                    if ((tva == 0 || tva < ht) && ht > 0 && ht < ttc && ttc > maxTTC && TtcFinder.IsValidSum(ht, tva, ttc)) {
                                        isFound = true;

                                        validHt.set(ht);
                                        validTva.set(tva);
                                        validTtc.set(ttc);

                                        validHtKeyValue.set(htKeyValue);
                                        validTvaKeyValue.set(tvaKeyValue);
                                        validTtcValue.set(ttcKeyValue);

                                        maxTTC = ttc;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // Utiliser les taux.
        if (!isFound) {
            return Facture.extractHtTvaTtcWithTVARates(
                    htKeyValues, tvaKeyValues, ttcKeyValues,
                    validHt, validTva, validTtc,
                    validHtKeyValue, validTvaKeyValue, validTtcValue);
        }

        return isFound;
    }
    /// <summary>
    /// Crée un fichier contenant les données sérialisé du document
    /// essu du traitement de l'OCR sur le fichier <paramref name="fileName" />.
    /// </summary>
    /// <param name="developerSN">Numéro de série du developpeur.</param>
    /// <param name="engineLibPath">Chemin de la librairie.</param>
    /// <param name="fileName">Nom du fichier à traiter.</param>
    /// <param name="rotationType">Indique le type de rotation.</param>
    /// <remarks>Le nom du fichier de sortie sera automatiquement généré à partir du fichier d'entrée.</remarks>
//			public static void ProcessCreateDocument (String developerSN, String engineLibPath, String fileName, DocumentRotation rotationType) throws IOException
//			{
//				Exception		exception = null;
//				byte []			documentBytes = null;
//
//				try
//				{
//					// Créer l'engine.
//					CppOcrEngine		ocrEngine = new CppOcrEngine (developerSN, engineLibPath);
//					// Créer le document.
//					_Document			document;
//					try
//					{document = ocrEngine.NativeCreateDocument (fileName, rotationType);}
//					finally
//					{ocrEngine.Dispose ();}
//					// Ecrire les données du document.
//					documentBytes = document.Serialize ();
//				}
//				catch (Exception _exception)
//				{
//					exception = _exception;
//				}
//				// Ecrire les données dans le fichier de comunication.
//				FileOutputStream fos=null;
//				try
//				{
//					fos = new FileOutputStream(Facture.GetOutputProcessFileName (fileName));
//					fos.write(documentBytes);
//				}
//				finally
//				{
//					fos.close();
//				}
//			}
//
//	/// <summary>
//	/// Crée le document de la facture.
//	/// </summary>
//	/// <param name="developerSN">Numéro de série du developpeur.</param>
//	/// <param name="engineLibPath">Chemin de la librairie.</param>
//	/// <param name="createProcess">Indique qu'il faut créer un processus de traitement pour l'extraction des données.</param>
//	/// <param name="processFileName">Nom de l'exécutable du processus de traitement si le drapeau <paramref name="createProcess" /> est activé.</param>
//	/// <param name="processingType">Type de traitement à faire sur le document.</param>
//	/// <param name="ocrEngine">L'instance du moteur de réconnaissance de caractères.</param>
//	/// <param name="outputDir">Dossier des sorties. Il sera utilisé par le filtre.</param>
//	/// <returns>Une instance d'un document <see cref="FacDocument" />.</returns>
//	public _Document CreateDocument (
//			String developerSN, String engineLibPath, boolean createProcess, String processFileName,
//			DocumentProcessing processingType, OcrEngine ocrEngine, String outputDir) throws IOException
//	{
//		return CreateDocument (
//				developerSN, engineLibPath, createProcess, processFileName,
//				processingType, ocrEngine, DocumentRotation.NONE, outputDir);
//	}

    /// <summary>
    /// Permet l'extraction des montants HT, TVA et TTC en utilisant
    /// certains montants (2.1, 5.5 et 19.6).
    /// </summary>
    /// <param name="htKeyValues">Liste de toutes les valeurs possible des montants HT.</param>
    /// <param name="tvaKeyValues">Liste de toutes les valeurs possible des montants TVA.</param>
    /// <param name="ttcKeyValues">Liste de toutes les valeurs possible des montants TTC.</param>
    /// <param name="validHt">Montant HT valide.</param>
    /// <param name="validTva">Montant TVA valide.</param>
    /// <param name="validTtc">Montant TTC valide.</param>
    /// <param name="validHtKeyValue">clé/valeur du montant HT valide.</param>
    /// <param name="validTvaKeyValue">clé/valeur du montant TVA valide.</param>
    /// <param name="validTtcValue">clé/valeur du montant TTC valide.</param>
    /// <returns>True si les montants on été trouvées, False sinon.</returns>
    private static boolean extractHtTvaTtcWithTVARates(
            List<KeyValue> htKeyValues, List<KeyValue> tvaKeyValues, List<KeyValue> ttcKeyValues,
            _Out<Double> validHt, _Out<Double> validTva, _Out<Double> validTtc,
            _Out<KeyValue> validHtKeyValue, _Out<KeyValue> validTvaKeyValue, _Out<KeyValue> validTtcValue) {
        validHt.set(0.0);
        validTva.set(0.0);
        validTtc.set(0.0);


        boolean isFound = false;
        // Valider TTC = HT + TVA = HT * (1 + T).
        Double ht, tva, ttc;
        Double maxTTC = 0.0;
        // TTC et HT.
        for (KeyValue htKeyValue : htKeyValues) {
            if (htKeyValue.isValidValue()) {
                ht = (double) htKeyValue.getValue();

                for (KeyValue ttcKeyValue : ttcKeyValues) {
                    if (ttcKeyValue.isValidValue()) {
                        ttc = (double) ttcKeyValue.getValue();
                        tva = ttc - ht;

                        if (ht > 0 && ht < ttc && (ttc == 0 || ttc > maxTTC) && TtcFinder.IsValidRateUsingTtcHt(ttc, ht)) {
                            isFound = true;

                            validHt.set(ht);
                            validTva.set(tva);
                            validTtc.set(ttc);

                            validHtKeyValue.set(htKeyValue);
                            validTvaKeyValue = null;
                            validTtcValue.set(ttcKeyValue);

                            maxTTC = ttc;
                        }
                    }
                }
            }
        }
        // TTC et TVA.
        if (!isFound) {
            for (KeyValue tvaKeyValue : tvaKeyValues) {
                if (tvaKeyValue.isValidValue()) {
                    tva = (Double) tvaKeyValue.getValue();
                    for (KeyValue ttcKeyValue : ttcKeyValues) {
                        if (ttcKeyValue.isValidValue()) {
                            ttc = (double) ttcKeyValue.getValue();
                            ht = ttc - tva;

                            if ((tva == 0 || tva < ht) && ttc > maxTTC && TtcFinder.IsValidRateUsingTtcTva(ttc, tva)) {
                                isFound = true;

                                validHt.set(ht);
                                validTva.set(tva);
                                validTtc.set(ttc);

                                validHtKeyValue = null;
                                validTvaKeyValue.set(tvaKeyValue);
                                validTtcValue.set(ttcKeyValue);

                                maxTTC = ttc;
                            }
                        }
                    }
                }
            }
        }

        return isFound;
    }

    /// <summary>
    /// Démarque les blocs utilisés comme clé.
    /// </summary>
    /// <param name="keyFinder">Une instance d'un <see cref="FacKeyFinder{T}" />.</param>
    /// <typeparam name="T">Type de la valeur du 'finder'.</typeparam>
    private static <T> void unmarkBlocks(KeyFinder<T> keyFinder) {
        List<KeyValue> keyValues = keyFinder.GetAllKeyValues();

        for (KeyValue keyValue : keyValues) {
            for (TextBlock textBlock : keyValue.getKeyBlocks()) {
                textBlock.set_isKeyMarked(false);
            }
        }
    }

    /// <summary>
    /// Retourne la largeur du label de la boîte
    /// utilisée pour dessiner les zones. Chaque
    /// type de clé a une largeur adapté.
    /// </summary>
    /// <param name="keyType">Type de la clé.</param>
    /// <returns>Entier qui représente la largeur du label de la boîte.</returns>
    public static int GetBoxLabelWidth(KeyType keyType) throws Exception {
        switch (keyType) {
            case NUMERO_FACTURE:
                return Facture.BOX_LABEL_WIDTH;
            case DATE:
                return Facture.BOX_LABEL_WIDTH;
            case TTC:
                return Facture.BOX_LABEL_WIDTH;
            case HT:
                return Facture.BOX_LABEL_WIDTH;
            case TVA:
                return Facture.BOX_LABEL_WIDTH;
            case TVA_SUPP:
                return Facture.BOX_LABEL_WIDTH;
            case DISCOUNT:
                return Facture.BOX_LABEL_WIDTH;
            case NUMERO_RCS:
                return Facture.BOX_LABEL_WIDTH;
            case NUMERO_SIRET:
                return Facture.BOX_LABEL_WIDTH;
            case NUMERO_TVA:
                return Facture.BOX_LABEL_WIDTH;
            case TYPE_FACTURE:
                return Facture.BOX_LABEL_WIDTH;
            case NUMERO_PAGE:
                return Facture.BOX_LABEL_WIDTH;
            case AMOUNT:
                return Facture.BOX_LABEL_WIDTH;
            default:
                throw new Exception("Unknown key type '" + keyType + "'");
        }
    }

    /// <summary>
    /// Obtient
    /// l'instance multi-facture de la facture en cours.
    /// </summary>
    public MultiFacture getMultiFacture() {
        return multiFacture;
    }

    /// <summary>
    /// Obtient
    /// l'indice de la facture en cours dans son document
    /// multi-facture <see cref="FacMultiFacture" /> parent.
    /// </summary>
    public int getIndex() {
        return index;
    }

    /// <summary>
    /// Obtient
    /// le nom du fichier de la facture. Le chemin
    /// doit être complet.
    /// </summary>
    public String getFullFileName() {
        return fullFileName;
    }

    /// <summary>
    /// Obtient le nom du fichier de graphe pour certaines configurations.
    /// </summary>
    /// <param name="processingType">Type du traitement.</param>
    /// <param name="rotationType">Type de la rotation.</param>
    /// <returns>Chaine de caractères qui représente le nom du fichier de graphe.</returns>
    public String GetDrawFileName(DocumentProcessing processingType) {

        return this.fullFileName + "-" + Facture.getDocStreamName(processingType).toLowerCase() + ".draw.jpg";
    }

    /// <summary>
    /// Crée le document de la facture.
    /// </summary>
    /// <param name="developerSN">Numéro de série du developpeur.</param>
    /// <param name="engineLibPath">Chemin de la librairie.</param>
    /// <param name="createProcess">Indique qu'il faut créer un processus de traitement pour l'extraction des données.</param>
    /// <param name="processFileName">Nom de l'exécutable du processus de traitement si le drapeau <paramref name="createProcess" /> est activé.</param>
    /// <param name="processingType">Type de traitement à faire sur le document.</param>
    /// <param name="ocrEngine">L'instance du moteur de réconnaissance de caractères.</param>
    /// <param name="rotationType">Indique le type de rotation.</param>
    /// <param name="outputDir">Dossier des sorties. Il sera utilisé par le filtre.</param>
    /// <returns>Une instance d'un document <see cref="FacDocument" />.</returns>
    public _Document CreateDocument(
            String developerSN,
            boolean createProcess,
            String processFileName,
            DocumentProcessing processingType,
            String outputDir
    ) throws IOException {
        // Appliquer les filtres.
        String fileName = this.fullFileName;
        // Aucun filtre.
//				if (processingType == DocumentProcessing.NONE)
//					fileName = ocrEngine.ApplyFilter (outputDir, fileName, DocumentProcessing.NONE, rotationType != DocumentRotation.NONE);
//				// Filtre B.
//				if ((processingType.getNumDocumentProcessing() & DocumentProcessing.FILTER_B.getNumDocumentProcessing()) == DocumentProcessing.FILTER_B.getNumDocumentProcessing())
//					fileName = ocrEngine.ApplyFilter (outputDir, fileName, DocumentProcessing.FILTER_B);
//				// Filtre A.
//				if ((processingType.getNumDocumentProcessing() & DocumentProcessing.FILTER_A.getNumDocumentProcessing()) == DocumentProcessing.FILTER_A.getNumDocumentProcessing())
//					fileName = ocrEngine.ApplyFilter (outputDir, fileName, DocumentProcessing.FILTER_A);
        // Détection.
        _Document document = null;

        if (createProcess) {
            // Lancer le processus.
            String processArgs = Facture.FormatProcessArgs(new String[]{
                            developerSN,
                            fileName,
                    }
            );

            ProcessBuilder p = new ProcessBuilder(processFileName, processArgs);
            Process process = p.start();

            // Attendre le processus.
            try {
                process.waitFor();

                // Lire les données.

                String outputProcessFileName = Facture.GetOutputProcessFileName(fileName);

                ObjectInputStream stream = new ObjectInputStream(new FileInputStream(outputProcessFileName));

                Object[] data = (Object[]) stream.readObject();

                stream.close();

                Files.deleteIfExists(Paths.get(outputProcessFileName));

                // Traiter le résultat
                Exception exception = (Exception) data[0];

                // 1. Exception.
                if (exception != null) {
//						throw new ThreadException (exception);
                }
                // 2. Données correctes.
                else {
                    document = (_Document) data[1];
                }
            } catch (Exception ex) {
                process.destroy();
            }

        } else
//					 document = ocrEngine.NativeCreateDocument (fileName, rotationType);
            document.setProcessing(processingType);
//				document.setRotation(rotationType);

        return document;
    }

    /// <summary>
    /// Commence le traitement de la facture en cours.
    /// </summary>
    /// <param name="options">Options du traitement.</param>
    /// <returns>Instance de type <see cref="FactureInfo" /> qui représente les informations de la facture.</returns>
    public FactureInfo startProcessing(ProcessingOption options) throws Exception {
        // Evénement début traitement.
        if (this.multiFacture.getFactureStartProcessing() != null)
            this.multiFacture.getFactureStartProcessing().FactureStartProcessing(this);
        // Traitement 1.
        String fileName = "Accounting_Engine/src/main/resources/images/" + "output.xml";
        _Out<_Document[]> allDocuments = new _Out<>();
        Dom4jXmlReader dom4jHandler = new Dom4jXmlReader();
        allDocuments.set(dom4jHandler.readXml(fileName).toArray(new _Document[dom4jHandler.readXml(fileName).size()]));
        FactureInfo factureInfo = _StartProcessing(null, DocumentProcessing.NONE, options, new _Document[0], allDocuments);
        // Evénement fin traitement.
        if (this.multiFacture.getFactureEndProcessing() != null)
            this.multiFacture.getFactureEndProcessing().FactureEndProcessing(this, factureInfo);

        return factureInfo;
    }
    public FactureInfo startProcessing(ProcessingOption options, _Out<_Document[]> docs) throws Exception {
        // Evénement début traitement.
        if (this.multiFacture.getFactureStartProcessing() != null)
            this.multiFacture.getFactureStartProcessing().FactureStartProcessing(this);
        FactureInfo factureInfo = _StartProcessing(null, DocumentProcessing.NONE, options, new _Document[0], docs);
        // Evénement fin traitement.
        if (this.multiFacture.getFactureEndProcessing() != null)
            this.multiFacture.getFactureEndProcessing().FactureEndProcessing(this, factureInfo);

        return factureInfo;
    }

    /// <summary>
    /// Commence le traitement de la facture en cours.
    /// </summary>
    /// <param name="factureInfo">L'instance qui contient les précedents informations.</param>
    /// <param name="processingType">Type de traitement à faire sur les factures.</param>
    /// <param name="options">Options du traitement.</param>
    /// <param name="includeDocuments">Tableau qui contient les documents à inclure dans la recherche.</param>
    /// <param name="allDocuments">Tous les documents utilisés de la facture.</param>
    /// <returns>Instance de type <see cref="FactureInfo" /> qui représente les informations de la facture.</returns>
//	private FactureInfo _StartProcessing (
//			FactureInfo factureInfo,
//			DocumentProcessing processingType, ProcessingOption options,
//			_Document [] includeDocuments,
//			_Document [] allDocuments) throws Exception
//	{
//		// Premier traitement : sans rotations.
//		FactureInfo		_factureInfo = _StartProcessing (factureInfo, processingType, options, includeDocuments,  allDocuments);
//		// Appliquer la rotation Pi/2.
//		if (_factureInfo.isType1StronglyRejected() || _factureInfo.isType2StronglyRejected())
//		{
//			// Ajouter le filtre  B.
//			if (!_factureInfo.isType2StronglyRejected())
//				processingType.setNumDocumentProcessing( processingType.getNumDocumentProcessing() | DocumentProcessing.FILTER_B.getNumDocumentProcessing());
//
//			_factureInfo = _StartProcessing (_factureInfo, processingType, options, includeDocuments,  allDocuments);
//			// Appliquer la rotation 3Pi/2.
//			if (_factureInfo.isType1StronglyRejected() || _factureInfo.isType2StronglyRejected())
//			{
//				// Ajouter le filtre  B.
//				if (!_factureInfo.isType2StronglyRejected())
//					processingType.setNumDocumentProcessing( processingType.getNumDocumentProcessing() | DocumentProcessing.FILTER_B.getNumDocumentProcessing());
//
//				_factureInfo = _StartProcessing (_factureInfo, processingType, options, includeDocuments,  allDocuments);
//			}
//		}
//
//		return _factureInfo;
//	}
    /// <summary>
    /// Commence le traitement de la facture en cours.
    /// </summary>
    /// <param name="factureInfo">L'instance qui contient les précedents informations.</param>
    /// <param name="processingType">Type de traitement à faire sur les factures.</param>
    /// <param name="options">Options du traitement.</param>
    /// <param name="rotationType">Indique le type de rotation.</param>
    /// <param name="includeDocuments">Tableau qui contient les documents é inclure dans la recherche.</param>
    /// <param name="allDocuments">Tous les documents utilisés de la facture.</param>
    /// <returns>Instance de type <see cref="FactureInfo" /> qui représente les informations de la facture.</returns>
    private FactureInfo _StartProcessing(
            FactureInfo factureInfo,
            DocumentProcessing processingType, ProcessingOption options,
            _Document[] includeDocuments,
            _Out<_Document[]> allDocuments) throws Exception {


        // Fixer le nom du flux de données.
        String streamName = Facture.getDocStreamName(processingType);
        // Extraire le document à partir de la cache.
        FactureCache documentCache = this.multiFacture.getFacturesCache().get(this.index);
//		_Document			document = _Document.Deserialize (documentCache.GetDocStream (streamName));
        _Document document = allDocuments.get()[0];
        boolean mustDraw = document == null;

        if (document == null) {
            document = CreateDocument(
                    this.multiFacture.getDeveloperSN(), this.multiFacture.isCreateProcess(), this.multiFacture.getProcessFileName(),
                    processingType, this.multiFacture.getOutputDir());
            // Mettre à jour sur le cache.
            documentCache.SetDocStream(streamName, document.Serialize());
            this.multiFacture.setMustFlushCache(true);
        }
        // Dessiner le document.
        if ((options.getProcessingOptioNum() & ProcessingOption.DRAW_DOCUMENT.getProcessingOptioNum()) == ProcessingOption.DRAW_DOCUMENT.getProcessingOptioNum()) {
            String drawFileName = GetDrawFileName(processingType);

            File f = new File(drawFileName);
            if (mustDraw || !f.exists())
                document.saveAsImage(drawFileName);
        }
        // Commencer le traitement.
        allDocuments.set(new _Document[includeDocuments.length + 1]);

        allDocuments.get()[allDocuments.get().length - 1] = document;
//		allDocuments.set(Arrays.copyOfRange(includeDocuments, 0, includeDocuments.length));
        FactureInfo _factureInfo = _StartProcessing(Arrays.asList(allDocuments.get()), factureInfo);
        Facture.mergeFactureInfos(factureInfo, _factureInfo);

        return factureInfo == null ? _factureInfo : factureInfo;
    }

    /// <summary>
    /// Commence le traitement de la facture en cours.
    /// </summary>
    /// <param name="documents">Instances des documents à inclure dans le traitement.</param>
    /// <param name="factureInfo">L'instance d'information précédement trouvée.</param>
    /// <returns>Instance de type <see cref="FactureInfo" /> qui représente les informations de la facture.</returns>
    private FactureInfo _StartProcessing(List<_Document> documents, FactureInfo factureInfo) throws Exception {
        FactureInfo _factureInfo = new FactureInfo(this);
        //
        // NOTE : L'ordre des appels des 'Finders' est important.
        //
        String rcs = null;
        LearningEngine learningEngine = this.multiFacture.getLearningEngine();
        // Numéro Siret.
        NumberSiretFinder numberSiretFinder = null;

        try {
            if (factureInfo == null || !factureInfo.isHasNumeroSiret() || !factureInfo.isHasNumeroTva()) {
                numberSiretFinder = new NumberSiretFinder(learningEngine);
                numberSiretFinder.Find(documents);
                _factureInfo.setNumeroSiretKeyValues(numberSiretFinder.GetAllKeyValues());
                // Dévalider les valeurs non valides.
                if (numberSiretFinder.getValidValue() != null && (factureInfo == null || !factureInfo.isHasNumeroSiret())) {
                    numberSiretFinder.getValidValue().setValue(numberSiretFinder.GetValue());
                    _factureInfo.setNumeroSiretKeyValue(numberSiretFinder.getValidValue());
                    rcs = NumberHelper.GetRcsFromSiret(_factureInfo.getNumeroSiret());
                }
            }
        } catch (Exception e) {
            log.error("Siret not found :" + e);
        }
        // Numéro Rcs.
        NumberRcsFinder numberRcsFinder = null;
        try {
            if (factureInfo == null || !factureInfo.isHasNumeroRcs() || !factureInfo.isHasNumeroTva()) {
                numberRcsFinder = new NumberRcsFinder(learningEngine);
                numberRcsFinder.Find(documents);
                _factureInfo.setNumeroRcsKeyValues(numberRcsFinder.GetAllKeyValues());
                // Dévalider les valeurs non valides.
                if (numberRcsFinder.getValidValue() != null && (factureInfo == null || !factureInfo.isHasNumeroRcs())) {
                    numberRcsFinder.getValidValue().setValue(numberRcsFinder.GetValue());
                    _factureInfo.setNumeroRcsKeyValue(numberRcsFinder.getValidValue());
                    rcs = _factureInfo.getNumeroRcs();
                }
            }
        } catch (Exception e) {
            log.error("RCS not found :" + e);
        }
        // Numéro Tva.
        try {
            if (factureInfo == null || !factureInfo.isHasNumeroTva()) {
                NumberTvaFinder numberTvaFinder = new NumberTvaFinder(numberSiretFinder, numberRcsFinder, learningEngine);
                numberTvaFinder.Find(documents);
                _factureInfo.setNumeroTvaUsedKeyValues(numberTvaFinder.GetAllKeyValues());
                // Dévalider les valeurs non valides.
                if (numberTvaFinder.getValidValue() != null) {
                    numberTvaFinder.getValidValue().setValue(numberTvaFinder.GetValue());
                    _factureInfo.setNumeroTvaKeyValue(numberTvaFinder.getValidValue());
                    rcs = NumberHelper.GetRcsFromTva(_factureInfo.getNumeroTva());
                }
            }
        } catch (Exception e) {
            log.error("Tva Number not found :" + e);
        }
        // Type facture.
        try {
            if (factureInfo == null || !factureInfo.isHasTypeFacture()) {
                TypeFactureFinder typeFactureFinder = new TypeFactureFinder(learningEngine, rcs);
                typeFactureFinder.Find(documents);
                _factureInfo.setTypeFactureUsedKeyValues(typeFactureFinder.GetAllKeyValues());
                // Dévalider les valeurs non valides.
                if (typeFactureFinder.getValidValue() != null) {
                    typeFactureFinder.getValidValue().setValue(typeFactureFinder.GetValue());
                    _factureInfo.setTypeFactureKeyValue(typeFactureFinder.getValidValue());
                }
            }
        } catch (Exception e) {
            log.error("Facture Type not found :" + e);
        }
        // Date.
        try {
            if (factureInfo == null || !factureInfo.isHasDate()) {
                DateFinder dateFinder = new DateFinder(learningEngine, rcs);
                dateFinder.Find(documents);
                _factureInfo.setDateUsedKeyValues(dateFinder.GetAllKeyValues());
                // Dévalider les valeurs non valides.
                if (dateFinder.getValidValue() != null) {
                    dateFinder.getValidValue().setValue(dateFinder.GetValue());
                    _factureInfo.setDateKeyValue(dateFinder.getValidValue());
                }
            }
        } catch (Exception e) {
            log.error("Date not found :" + e);
        }
        // Page de la facture.
        try {
            if (factureInfo == null || !factureInfo.isHasNumeroPage()) {
                NumberPageFinder numberPageFinder = new NumberPageFinder(learningEngine, rcs);
                numberPageFinder.Find(documents);
                _factureInfo.setNumeroPageKeyValues(numberPageFinder.GetAllKeyValues());
                // Dévalider les valeurs non valides.
                if (numberPageFinder.getValidValue() != null) {
                    numberPageFinder.getValidValue().setValue(numberPageFinder.GetValue());
                    _factureInfo.setNumeroPageKeyValue(numberPageFinder.getValidValue());
                }
            }
        } catch (Exception e) {
            log.error("Number pages not found :" + e);
        }
        // Remise.
        try {
            if (factureInfo == null || !factureInfo.isHasDiscount()) {
                DiscountFinder discountFinder = new DiscountFinder(learningEngine, rcs);
                discountFinder.Find(documents);
                _factureInfo.setDiscountUsedKeyValues(discountFinder.GetAllKeyValues());
                // Dévalider les valeurs non valides.
                if (discountFinder.getValidValue() != null) {
                    discountFinder.getValidValue().setValue(discountFinder.GetValue());
                    _factureInfo.setDiscountKeyValue(discountFinder.getValidValue());
                }
            }
        } catch (Exception e) {
            log.error("Remise not found :" + e);
        }

        if (!_factureInfo.isHasDiscount() && (factureInfo == null || !factureInfo.isHasHt() || !factureInfo.isHasTva() || !factureInfo.isHasTtc())) {
            // HT, TVA, TTC.
            // Premier traitement.
            try {
                HtFinder htFinder = new HtFinder(learningEngine, rcs);
                TvaFinder tvaFinder = new TvaFinder(learningEngine, rcs);
                TtcFinder ttcFinder = new TtcFinder(learningEngine, rcs);
                TvaSuppFinder tvaSuppFinder = new TvaSuppFinder(learningEngine, rcs);

                if (!Facture.extractHtTvaTtc(htFinder, tvaFinder, ttcFinder, tvaSuppFinder, null, false, documents, _factureInfo)) {
                    // Démarquer les blocs des clés.
                    Facture.unmarkBlocks(htFinder);
                    Facture.unmarkBlocks(tvaFinder);
                    Facture.unmarkBlocks(ttcFinder);
                    // Lancer le second traitement.
                    htFinder = new HtFinder(true, learningEngine, rcs);
                    tvaFinder = new TvaFinder(true, learningEngine, rcs);
                    ttcFinder = new TtcFinder(true, learningEngine, rcs);

                    if (!Facture.extractHtTvaTtc(htFinder, tvaFinder, ttcFinder, tvaSuppFinder, null, true, documents, _factureInfo)) {
                        // Traitement sans clés.
                        AmountFinder amountFinder = new AmountFinder(learningEngine, rcs);
                        Facture.extractHtTvaTtc(htFinder, tvaFinder, ttcFinder, null, amountFinder, true, documents, _factureInfo);
                    }
                }
            } catch (Exception e) {
                log.error("TVA TTC HT not found :" + e);
            }
        }
        // Numéro Facture.
        try {
            if (factureInfo == null || !factureInfo.isHasNumeroFacture()) {
                NumberFactureFinder numberFactureFinder = new NumberFactureFinder(learningEngine, rcs);
                numberFactureFinder.Find(documents);
                _factureInfo.setNumeroFactureUsedKeyValues(numberFactureFinder.GetAllKeyValues());
                // Dévalider les valeurs non valides.
                if (numberFactureFinder.getValidValue() != null) {
                    numberFactureFinder.getValidValue().setValue(numberFactureFinder.GetValue());
                    _factureInfo.setNumeroFactureKeyValue(numberFactureFinder.getValidValue());
                }
            }
        } catch (Exception e) {
            log.error("Facture Number not found :" + e);
        }

        return _factureInfo;
    }
    /// <summary>
    /// Dessine les zones des clés/valeurs trouvées en
    /// utilisant les options <paramref name="imageType" />.
    /// </summary>
    /// <param name="factureInfo">Valeur de l'instance d'informations <see cref="FactureInfo" />.</param>
    /// <param name="imagePath">Chemin du fichier de l'image objet du dessing.</param>
    /// <param name="imageType">Type de l'image.</param>

//			public static void DrawKeyValues (FactureInfo factureInfo, String imagePath, ImageType imageType) throws Exception
//			{
//				// créer le graphique.
//				BufferedImage image = ImageIO.read(new File(imagePath));
//			    Graphics2D bitmap = image.createGraphics ();
//				// Dessiner les clés/valeurs.
//				List<KeyValue>				allKeyValues = factureInfo.GetAllUsedKeyValues ();
//				HashMap<KeyType,List<KeyValue>> keyValuesByType = new HashMap<KeyType,List<KeyValue>> ();
//				// Classer les clés-valeurs par type.
//				List<KeyValue>	keyValues=null;
//
//				for(KeyValue keyValue : allKeyValues)
//				{
//					if ((imageType != ImageType.VALUES || Key.IsValueDrawingType (keyValue.getKey().getType())) &&
//						(imageType == ImageType.ALL_KEYVALUES || keyValue.isValidValue()))
//					{
//						if (!keyValuesByType.containsKey(keyValue.getKey().getType()))
//						{
//							keyValues = new ArrayList<KeyValue> ();
//							keyValuesByType.put(keyValue.getKey().getType(), keyValues);
//						}
//
//						keyValues.add (keyValue);
//					}
//				}
//				// Dessiner les clés.
//				boolean					isDebug = imageType != ImageType.VALUES;
//
//				String					keyTypeStr;
//				int						boxLabelWidth;
//				Color					valueBoxColor;
//				Color					valueLabelColor;
//				KeyValue				_keyValue;
//				Map<Point,Integer>	boxLabelPositions = new HashMap<Point,Integer> ();
//
//				for(Map.Entry<KeyType,List<KeyValue>> keyValuePair : keyValuesByType.entrySet())
//				{
//
//					KeyType key = keyValuePair.getKey();
//					List<KeyValue>  values = keyValuePair.getValue();
//					// Fixer les paramètres du dessin.
//					if (isDebug)
//					{
//						keyTypeStr = Key.GetDebugTypeString (key);
//						boxLabelWidth = -1;
//						valueBoxColor = Facture.DEBUG_VALUE_BOX_COLOR;
//						valueLabelColor = Facture.DEBUG_VALUE_BOX_LABEL_COLOR;
//					}
//					else
//					{
//						keyTypeStr = Key.GetTypeString (key);
//						boxLabelWidth = Facture.GetBoxLabelWidth ((KeyType)keyValuePair.getKey());
//						valueBoxColor = Facture.VALUE_BOX_COLOR;
//						valueLabelColor = Facture.VALUE_BOX_LABEL_COLOR;
//					}
//
//					for (int i = 0; i < values.size(); i ++)
//					{
//						_keyValue = values.get(i);
//						// Dessiner la clé.
//						if (_keyValue.getBasicKeyBlock() != null && isDebug)
//							_CreateBox (isDebug, _keyValue.getBasicKeyBlock().getDocument(), _keyValue.getBasicKeyBlock().getRegion(), keyTypeStr, values.size() > 1 ? (i + 1) : -1, -1, Facture.KEY_BOX_COLOR, Facture.KEY_BOX_LABEL_COLOR, bitmap, boxLabelPositions);
//						// Dessiner la valeur.
//						if (_keyValue.getVirtualValueBlock() != null)
//							_CreateBox (isDebug, _keyValue.getVirtualValueBlock().getDocument(), _keyValue.getVirtualValueBlock().getRegion(), keyTypeStr, values.size() > 1 ? (i + 1) : -1, boxLabelWidth, valueBoxColor, valueLabelColor, bitmap, boxLabelPositions);
//					}
//				}
//				// Sauvegarder l'image.
//
//				ImageIO.write ( image, "jpg", new File ( Facture.getImageFileName (imagePath, imageType)) );
//				bitmap.dispose();
//				System.gc();
//			}
    /// <summary>
    /// Crée une boite autour de la région <paramref name="region" />.
    /// La boîte sera labellisée par l'indice <paramref name="index" />.
    /// </summary>
    /// <param name="isDebugBox">Indique que la boîte à créer est pour le mode débogage.</param>
    /// <param name="document">Document du bloc associé à la boîte.</param>
    /// <param name="region">région de la boîte.</param>
    /// <param name="keyTypeStr">Chaine de caractères qui représente le type de la clé.</param>
    /// <param name="index">Indice de la boîte. Un indice négatif indique qu'il ne faut pas en tenir compte.</param>
    /// <param name="boxLabelWidth">Dans le cas où le mode debogage est désactivé, ce paramètre indique la largeur du label de la boîte.</param>
    /// <param name="boxColor">Couleur des contours de la boîte.</param>
    /// <param name="labelColor">Couleur du label de la boîte.</param>
    /// <param name="g">L'instance du graphics qui pointe vers l'image.</param>
    /// <param name="boxLabelPositions">Positions des labels des boîtes.</param>
//			private static void _CreateBox (
//					boolean isDebugBox, _Document document, Region region, String keyTypeStr, int index,
//					int boxLabelWidth, Color boxColor, Color labelColor,
//					Graphics2D g, Map<Point,Integer> boxLabelPositions)
//			{
//				// Ajuster la région.
//				int [] tabAdjustRegion = Facture.adjustRegion (region, document);
//				int	left= tabAdjustRegion[0];
//				int width =tabAdjustRegion[1];
//				int top =tabAdjustRegion[2];
//				int height= tabAdjustRegion[3];
//				// Dessiner le rectangle.
//				g.setColor(boxColor);
//				g.setStroke(new BasicStroke( Facture.BOX_LINE_WIDTH ));
//				g.drawRect(left, top, width, height);
//				// Dessiner le label.
//				Point			position = new Point (left, top - Facture.BOX_LABEL_HEIGHT);
//				int	positionsCount=0;
//
//				if (!boxLabelPositions.containsKey (position))
//					positionsCount = 1;
//				else
//					positionsCount ++;
//
//				boxLabelPositions.put(position, positionsCount);
//
//				 g.setColor(boxColor);
//				 g.fillRect(left, top - positionsCount * Facture.BOX_LABEL_HEIGHT, isDebugBox ? (index > -1 ? Facture.DEBUG_BOX_LABEL_WIDTH_BIG : Facture.DEBUG_BOX_LABEL_WIDTH_SMALL) : boxLabelWidth, Facture.BOX_LABEL_HEIGHT);
//					// Dessiner le texte.
//				 g.setColor(labelColor);
//					g.setFont(Facture.BOX_FONT);
//					g.drawString(keyTypeStr + (index > -1 ? ("-" + index) : ""), left, top - positionsCount * Facture.BOX_LABEL_HEIGHT + 2);
//				}
    /// <summary>
    /// Ajuste les données de la région pour le type
    /// de rotation du document.
    /// </summary>
    /// <param name="region">L'instance de la région.</param>
    /// <param name="document">L'instance du doucment.</param>
    /// <param name="left">Nouvelle position Left de la région.</param>
    /// <param name="top">Nouvelle position Top de la région.</param>
    /// <param name="width">Nouvelle largeur.</param>
    /// <param name="height">Nouvelle hauteur.</param>
//			private static void adjustRegion (
//					Region region, _Document document,
//					_Out<Integer> left, _Out<Integer> top, _Out<Integer> width, _Out<Integer> height)
//			{
//				switch (document.getRotation())
//				{
//					// Pas de rotation.
//					case NONE :
//					{
//						left.set(region.getLeft());
//						top.set(region.getTop());
//						width.set(region.getWidth());
//						height.set(region.getHeight());
//						break;
//					}
//					// Rotation Pi/2.
//					case ROTATION_1 :
//					{
//						left.set(region.getTop());
//						top.set(document.getRegion().getWidth() - (region.getLeft() + region.getWidth()));
//						width.set(region.getHeight());
//						height.set(region.getWidth());
//						break;
//					}
//					// Rotation 3Pi/2.
//					default :
//					{
//						left.set(document.getRegion().getHeight() - (region.getTop() + region.getHeight()));
//						top.set(region.getLeft());
//						width.set(region.getWidth());
//						height.set(region.getHeight());
//						break;
//					}
//				}
//			}

    // <summary> Ajuste les données de la région pour le type de rotation du document. </summary>
    // <param name="region">L'instance de la région.</param>
    // <param name="document">L'instance du doucment.</param>
    // <param name="left">Nouvelle position Left de la région.</param>
    // <param name="top">Nouvelle position Top de la région.</param>
    // <param name="width">Nouvelle largeur.</param>
    // <param name="height">Nouvelle hauteur.</param>
//			private static int[] adjustRegion (
//								 Region region, _Document document)
//			{
//
//				 int left,  top,  width,  height;
//
//				switch (document.getRotation())
//				{
//					// Pas de rotation.
//					case NONE :
//					{
//						left = region.getLeft();
//						top = region.getTop();
//						width = region.getWidth();
//						height = region.getHeight();
//
//						break;
//					}
//					// Rotation Pi/2.
//					case ROTATION_1 :
//					{
//						left = region.getTop();
//						top = document.getRegion().getWidth() - (region.getLeft() + region.getWidth());
//						width = region.getHeight();
//						height = region.getWidth();
//						break;
//					}
//					// Rotation 3Pi/2.
//					default :
//					{
//						left = document.getRegion().getHeight() - (region.getTop() + region.getHeight());
//						top = region.getLeft();
//						width = region.getHeight();
//						height = region.getWidth();
//						break;
//					}
//				}
//
//				return new int[]{left, top, width, height};
//			}


    /// <summary>
    /// Dessine les zones des clés/valeurs trouvées
    /// dans le cas où le fichier a été trouvée.
    /// </summary>
    /// <param name="factureInfo">Valeur de l'instance d'informations <see cref="FacFactureInfo" />.</param>
    /// <param name="imagePath">Chemin du fichier de l'image objet du dessing.</param>
    /// <param name="drawValues">Indique qu'il faut dessiner seulement les valeurs.</param>
    /// <param name="drawAllKeyValues">Dans le cas où le drapeau <paramref name="drawValues" /> est désactivé,
    /// indique qu'il faut dessiner toutes les clés/valeurs.</param>
//			public static void DrawKeyValues (FactureInfo factureInfo, String imagePath, boolean drawValues, boolean drawAllKeyValues) throws Exception
//			{
//				File f= new File(imagePath);
//				if (f.exists ())
//				{
//					if (drawValues)
//						Facture.DrawKeyValues (factureInfo, imagePath, ImageType.VALUES);
//					else
//					{
//						Facture.DrawKeyValues (factureInfo, imagePath , ImageType.KEYVALUES);
//
//						if (drawAllKeyValues)
//							Facture.DrawKeyValues (factureInfo, imagePath , ImageType.ALL_KEYVALUES);
//					}
//				}
//			}
}

			
	
