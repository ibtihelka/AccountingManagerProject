package com.accounting_manager.bank_statement_engine.Classes.finders;

import com.accounting_manager.bank_statement_engine.Classes.All.NumberHelper;
import com.accounting_manager.bank_statement_engine.Classes._Out._Out;
import com.accounting_manager.bank_statement_engine.Classes.block.Block;
import com.accounting_manager.bank_statement_engine.Classes.geometry.PositionType;
import com.accounting_manager.bank_statement_engine.Classes.geometry.Region;
import com.accounting_manager.bank_statement_engine.Classes.key.*;
import com.accounting_manager.bank_statement_engine.Classes.learning.LearningEngine;
import com.accounting_manager.bank_statement_engine.Classes.text.TextBlock;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class NumberFactureFinder extends KeyFinder<_Out<String>> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /// <summary>
    /// Pourcentage de chiffres dans une chaine.
    /// </summary>
    public static float DIGITS_RATE_MIN = 0.5f;
    /// <summary>
    /// Le taux  de la largeur de l'intervalle
    /// utilisée pour faire les recherches des blocs
    /// dans le cas d'un horizontal.
    /// </summary>
    public static float HALIGN_RATE = 0.95f;

    /// <summary>
    /// Initialise une nouvelle instance de la classe
    /// avec des paramètres par défaut.
    /// </summary>
    /// <param name="learningEngine">L'instance du moteur d'apprentissage.</param>
    /// <param name="fournisseur">Chaine de caractères qui représente le fournisseur. Il peut être null.</param>
    public NumberFactureFinder() throws Exception {
        super(new Key(KeyType.NUMERO_FACTURE));
    }

    public NumberFactureFinder(LearningEngine learningEngine, String fournisseur) throws Exception {

        super(new Key(KeyType.NUMERO_FACTURE), learningEngine, fournisseur);
    }

    /// <summary>
    /// Teste si une partie du numéro de la facture
    /// ne doit pas être accepté.
    /// </summary>
    /// <param name="text">Texte objet du test.</param>
    /// <returns>True si le texte doit être érroné, False sinon.</returns>
    private static boolean isWrongNumFacturePart(_Out<String> text) {
        try {
            return
                    NumberHelper.IsSiret(text) ||
                            NumberHelper.IsTva(text) ||
                            NumberHelper.IsBicNumber(NumberHelper.CleanFloatStr(text.get())) ||
                            NumberHelper.IsIbanNumber(NumberHelper.CleanFloatStr(text.get())) ||
                            NumberHelper.ConvertDate(text.get()) ||
                            text.get().toLowerCase().indexOf("tva") > -1 ||
                            text.get().toLowerCase().indexOf("siret") > -1 ||
                            text.get().toLowerCase().indexOf("loi") > -1 ||
                            text.get().indexOf("FR") > -1 || text.get().indexOf("R.I.B") > -1 || text.get().indexOf("RIB") > -1 ||
                            text.get().indexOf("B.L.") > -1 || text.get().indexOf("BL") > -1 ||
                            text.get().indexOf("T6I") > -1 || text.get().indexOf("T6l") > -1 ||
                            text.get().toLowerCase().indexOf("client") > -1;
        } catch (ParseException e) {
            log.error("An error occurred while checking the value: " + e);
            return false;
        }
    }

    /// <summary>
    /// Retourne la liste de chaines de caractères
    /// de type <see cref="KeyString" /> pour le type
    /// de clé <see cref="FacKeyType.NUMERO_FACTURE" /> pour les cas non valides.
    /// </summary>
    /// <returns>Tableau de chaines de caractères.</returns>
    public static List<KeyString> GetExceptedStrings() {
        List<KeyString> keyStrings = new ArrayList<>();

        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("d'", true), new KeyStringChar("ordre")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("no"), new KeyStringChar("d'", true), new KeyStringChar("ordre")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("tel"), new KeyStringChar(".", true), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("tel"), new KeyStringChar(".", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("fax"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("fax")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("compte"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("de", true), new KeyStringChar("compte")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("rcs"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("rcs")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("t.v.a."), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("t.v.a.")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("siret"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("de", true), new KeyStringChar("siret")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("bon"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("bon")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("bons"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("bons")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("TVA")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("client"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("client")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("decret"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("decret")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("quai"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("quai")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("loi"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("loi")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("bp"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("bp")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("b.p."), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("prestation")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("prestation"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("b.p.")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("rib"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("rib")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("R.I.B."), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("R.I.B.")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("page"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("page")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("livraison"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("livraison")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("agrees"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("agrees")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("contrat"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("de", true), new KeyStringChar("contrat")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("be"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("be")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("commande"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("commande")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("agrement"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("d'", true), new KeyStringChar("agrement")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("numero"), new KeyStringChar("de", true), new KeyStringChar("compte")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("numero"), new KeyStringChar("d'", true), new KeyStringChar("adherent")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("adherent"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("d'", true), new KeyStringChar("adherent")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("appel"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("d'", true), new KeyStringChar("appel")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("accise"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("d'", true), new KeyStringChar("accise")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("identification"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("d'", true), new KeyStringChar("identification")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("poste"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("poste")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("compteur"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("compteur")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("intra"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("intracommunautaire")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("numero"), new KeyStringChar("intracommunautaire")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("intra")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("intracommunautaire")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("ut"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("ut")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("d'or"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("d'or")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar(".art"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar(".art")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("postale")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("postale"), new KeyStringChar("n"), new KeyStringChar("°", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("facture"), new KeyStringChar("vers"), new KeyStringChar("le")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("facture"), new KeyStringChar("du")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("totalite"), new KeyStringChar("de"), new KeyStringChar("la"), new KeyStringChar("facture")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("lot"), new KeyStringChar("n°")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("lot"), new KeyStringChar("numero")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("numero"), new KeyStringChar("client")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("numero"), new KeyStringChar("article")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("numero"), new KeyStringChar("moteur")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("reception"), new KeyStringChar("facture")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("facture"), new KeyStringChar("a")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("factures")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("facture"), new KeyStringChar(".", true), new KeyStringChar("(", true), new KeyStringChar("loi"), new KeyStringChar("n"), new KeyStringChar("°")}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("totaux"), new KeyStringChar("de"), new KeyStringChar("la"), new KeyStringChar("facture"), new KeyStringChar(":", true, true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("total"), new KeyStringChar("de", true), new KeyStringChar("la", true), new KeyStringChar("facture"), new KeyStringChar(":", true, true)}), -2));
        return keyStrings;
    }

    //Convert KeyStringChar Table to string
    public static List<KeyStringChar> KeyStringCharToList(KeyStringChar[] keyStringChars) {
        List<KeyStringChar> stringChars = new ArrayList<>();

        for (KeyStringChar k : keyStringChars)
            stringChars.add(k);

        return stringChars;
    }

    /// <summary>
    /// Obtient
    /// le taux  de la largeur de l'intervalle
    /// utilisée pour faire les recherches des blocs
    /// dans le cas d'un horizontal.
    /// </summary>
    @Override
    public float HAlignRate() {

        return NumberFactureFinder.HALIGN_RATE;

    }

    /// <summary>
    /// Obtient
    /// le ceofficient du poids des zones
    /// lors de l'évaluation totale du poids.
    /// </summary>
    @Override
    public float WeightZoneCoeff() {

        return WEIGHT_ZONE_COEFF / 10;

    }

    /// <summary>
    /// Retourne la sous-séquence d'un bloc texte.
    /// </summary>
    /// <param name="keyValue">Instance clé/valeur en cours.</param>
    /// <param name="textBlock">Bloc texte.</param>
    /// <returns>Tableau de <see cref="FacTextBlock" /> qui représente la sous-séquence.</returns>
    @Override
    public List<TextBlock> GetSubSequence(KeyValue keyValue, TextBlock textBlock) {
        float seqSpacingRate = TextBlock.SEQ_SPACING_RATE * 1.4f;

        if (keyValue.getPosition() == PositionType.RIGHT)
            return TextBlock.GetSubSequence(textBlock.getRightSequence(), textBlock, seqSpacingRate);
        else
            return TextBlock.GetSubSequence(textBlock.getSequence(), textBlock, seqSpacingRate);
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
    public List<TextBlock> FindValue(KeyValue keyBlock, List<Block> allBlocks, _Out<Boolean> isValueChecked, _Out<Boolean> checkResult) {
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
    public boolean CheckValue(KeyValue keyValue, List<Block> allBlocks) {
        List<TextBlock> valueBlocks = keyValue.getValueBlocks();
        // Fixer le position du bloc de base.
        int basicBlockIndex;

        if (keyValue.getPosition() == PositionType.DOWN ||
                keyValue.getPosition() == PositionType.LEFT) {
            basicBlockIndex = Region.getIndex(keyValue.getValueBaseBlock(), (List<Block>) (List<?>) valueBlocks);
        } else if (keyValue.getPosition() == PositionType.RIGHT)
            basicBlockIndex = 0;
        else
            basicBlockIndex = valueBlocks.size() - 1;
        // Translation vers la droite.
        _Out<String> valueStr = new _Out<>(null);
        int subSeqStartIndex = 0, subSeqEndIndex = valueBlocks.size() - 1;

        for (int i = 0; i < valueBlocks.size(); i++) {
            valueStr.set(valueBlocks.get(i).getText());

            for (int j = i; j < valueBlocks.size(); j++) {
                if (j > i)
                    valueStr.set(valueStr.get() + valueBlocks.get(j).getText());

                if (NumberFactureFinder.isWrongNumFacturePart(NumberHelper.CleanNumberFac(valueStr))) {
                    // Not OK.
                    if (basicBlockIndex >= i && basicBlockIndex <= j)
                        return false;
                        // OK.
                    else {
                        if (basicBlockIndex < i && i - 1 < subSeqEndIndex)
                            subSeqEndIndex = i - 1;

                        if (basicBlockIndex > j && j + 1 > subSeqStartIndex)
                            subSeqStartIndex = j + 1;
                    }
                }
            }
        }
        // Nouvelle séquence.
        List<TextBlock> newValueBlocks = new ArrayList<>();

        newValueBlocks.addAll(valueBlocks);

        if (newValueBlocks.size() == 0 ||
                NumberHelper.IsPhoneNumber(newValueBlocks) ||
                NumberHelper.DigitsRate(TextBlock.GetSequenceString(newValueBlocks)) < NumberFactureFinder.DIGITS_RATE_MIN) {
            return false;
        } else
            return true;
    }

    /// <summary>
    /// Retourne la valeur trouvée de type <see cref="string" />.
    /// </summary>
    /// <param name="validKeyValue">La valeur valide de l'instance en cours.</param>
    /// <returns>Valeur trouvée.</returns>
    @Override
    public _Out<String> GetValue(KeyValue validKeyValue) {
        _Out<String> out = new _Out<>(TextBlock.GetSequenceString(validKeyValue.getValueBlocks()));
        return NumberHelper.CleanNumberFac(out);
    }
}