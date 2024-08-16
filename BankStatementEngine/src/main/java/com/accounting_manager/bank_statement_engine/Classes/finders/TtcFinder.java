package com.accounting_manager.bank_statement_engine.Classes.finders;

import com.accounting_manager.bank_statement_engine.Classes._Out._Out;
import com.accounting_manager.bank_statement_engine.Classes.block.Block;
import com.accounting_manager.bank_statement_engine.Classes.key.Key;
import com.accounting_manager.bank_statement_engine.Classes.key.KeyFinder;
import com.accounting_manager.bank_statement_engine.Classes.key.KeyType;
import com.accounting_manager.bank_statement_engine.Classes.key.KeyValue;
import com.accounting_manager.bank_statement_engine.Classes.learning.LearningEngine;
import com.accounting_manager.bank_statement_engine.Classes.text.TextBlock;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.util.List;

@Log4j2
public class TtcFinder extends KeyFinder<Double> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /// <summary>
    /// Poids maximal qu'il ne faut pas dépasser
    /// pour inclure une valeur.
    /// </summary>
    public static float VALUE_MAX_WEIGHT = 5000;
    /// <summary>
    /// Intervalle de l'arrondissement. Ceci permet
    /// de calculer les incertitudes.
    /// </summary>
    public static double ROUND = 0.01;
    /// <summary>
    /// Indique que le second
    /// traitement est activé.
    /// </summary>
    private boolean isSecondProcessing;


    /// <summary>
    /// Initialise une nouvelle instance de la classe
    /// avec des paramètres spécifiés.
    /// </summary>
    /// <param name="isSecondProcessing">Indique que le second traitement est activé.</param>
    /// <param name="learningEngine">L'instance du moteur d'apprentissage.</param>
    /// <param name="fournisseur">Chaine de caractères qui représente le fournisseur. Il peut être null.</param>
    public TtcFinder(boolean isSecondProcessing) throws Exception {

        super(new Key(KeyType.TTC));
        this.isSecondProcessing = isSecondProcessing;

    }

    public TtcFinder(boolean isSecondProcessing, LearningEngine learningEngine, String fournisseur) throws Exception {

        super(new Key(KeyType.TTC), learningEngine, fournisseur);
        this.isSecondProcessing = isSecondProcessing;

    }

    /// <summary>
    /// Initialise une nouvelle instance de la classe
    /// avec des paramètres spécifiés.
    /// </summary>
    /// <param name="learningEngine">L'instance du moteur d'apprentissage.</param>
    /// <param name="fournisseur">Chaine de caractères qui représente le fournisseur. Il peut être null.</param>
    public TtcFinder (LearningEngine learningEngine, String fournisseur) throws Exception{

        this (false, learningEngine, fournisseur);

    }

    /// <summary>
    /// Initialise une nouvelle instance de la classe
    /// avec des paramètres spécifiés.
    /// </summary>
    /// <param name="learningEngine">L'instance du moteur d'apprentissage.</param>
    /// <param name="fournisseur">Chaine de caractères qui représente le fournisseur. Il peut être null.</param>
    public TtcFinder(

    ) throws Exception {

        this(false);

    }

    /// <summary>
    /// Teste si la totale TTC est égale à la somme des deux
    /// autres montants.
    /// </summary>
    /// <param name="ht">Montant HT.</param>
    /// <param name="tva">Montant TVA</param>
    /// <param name="ttc">Montant TTC.</param>
    /// <returns>True si TTC = HT + TVA, False sinon.</returns>
    public static boolean IsValidSum(double ht, double tva, double ttc) {

        return Math.abs(ttc - (ht + tva)) <= 2 * TtcFinder.ROUND;

    }

    /// <summary>
    /// Teste si le montant TTC est lié au montant HT
    /// par un taux connu (2.1, 5.5 et 19.6).
    /// </summary>
    /// <param name="ttc">Montant TTC.</param>
    /// <param name="ht">Montant HT</param>
    /// <returns>True si les deux montants sont liés par un taux connu, False sinon.</returns>
    public static boolean IsValidRateUsingTtcHt(double ttc, double ht) {
        if (ht == 0)
            return false;

        double rate = 100 * (ttc - ht) / ht;
        double dRate = 100 * (1 / ht * TtcFinder.ROUND + ttc / (ht * ht) * TtcFinder.ROUND);

        return Math.abs(rate - 2.1) < dRate || Math.abs(rate - 5.5) < dRate || Math.abs(rate - 19.6) < dRate;
    }

    /// <summary>
    /// Teste si le montant TTC est lié au montant TVA
    /// par un taux connu (2.1, 5.5 et 19.6).
    /// </summary>
    /// <param name="ttc">Montant TTC.</param>
    /// <param name="tva">Montant TVA</param>
    /// <returns>True si les deux montants sont liés par un taux connu, False sinon.</returns>
    public static boolean IsValidRateUsingTtcTva(double ttc, double tva) {
        double ttcTva = ttc - tva;
        if (ttcTva == 0)
            return false;

        double rate = 100 * tva / ttcTva;
        double dRate = 100 * (tva / (ttcTva * ttcTva) * TtcFinder.ROUND + ttc / (ttcTva * ttcTva) * TtcFinder.ROUND);

        return Math.abs(rate - 2.1) < dRate || Math.abs(rate - 5.5) < dRate || Math.abs(rate - 19.6) < dRate;
    }

    /// <summary>
    /// Obtient
    /// une valeur indiquant que le second
    /// traitement est activé.
    /// </summary>
    public boolean isSecondProcessing() {
        return isSecondProcessing;
    }

    public void setSecondProcessing(boolean isSecondProcessing) {
        this.isSecondProcessing = isSecondProcessing;
    }

    /// <summary>
    /// Obtient
    /// le taux  de la largeur de l'intervalle
    /// utilisée pour faire les recherches des blocs
    /// dans le cas d'un horizontal.
    /// </summary>
    @Override
    public float HAlignRate() {

        return this.isSecondProcessing ? 1.5f * super.HAlignRate() : super.HAlignRate();

    }

    /// <summary>
    /// Obtient
    /// le taux  de la largeur de l'intervalle
    /// utilisée pour faire les recherches des blocs
    /// dans le cas d'un vertical.
    /// </summary>
    @Override
    public float VAlignRate() {
        return this.isSecondProcessing ? 1.5f * super.VAlignRate() : super.VAlignRate();
    }

    /// <summary>
    /// Obtient
    /// le poids maximal qu'il ne faut pas dépasser
    /// pour inclure une valeur.
    /// </summary>
    @Override
    public float ValueMaxWeight() {

        return TtcFinder.VALUE_MAX_WEIGHT;
    }

    /// <summary>
    /// Retourne la sous-séquence d'un bloc texte.
    /// </summary>
    /// <param name="keyValue">Instance clé/valeur en cours.</param>
    /// <param name="textBlock">Bloc texte.</param>
    /// <returns>Tableau de <see cref="FacTextBlock" /> qui représente la sous-séquence.</returns>
    /// <remarks>Par défaut cette méthode utilise la séquence retournée par la propriété <see cref="FacTextBlock.Sequence" />.</remarks>
    @Override
    public List<TextBlock> GetSubSequence(KeyValue keyValue, TextBlock textBlock) {

        return TextBlock.GetSubSequence(textBlock.getSequence(), textBlock, TextBlock.SEQ_SPACING_RATE * 1.5f);

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

        try {
            return CheckBicValue(keyValue, allBlocks);
        } catch (Exception e) {
            log.error("An error occurred while checking the value: " + e);
        }
        return isSecondProcessing;

    }

    /// <summary>
    /// Retourne la valeur trouvée de type <see cref="double" />.
    /// </summary>
    /// <param name="validKeyValue">La valeur valide de l'instance en cours.</param>
    /// <returns>Valeur trouvée.</returns>
    @Override
    public Double GetValue(KeyValue validKeyValue) {

        return GetAmountValue(validKeyValue);

    }

    /// <summary>
    /// Vérifie les valeurs des clés trouvées. Ceci permet de vérifier l'intégrité
    /// des valeurs et d'annuler certaines clés/valeurs.
    /// </summary>
    /// <param name="keyValues">Tableau des valeurs des clés.</param>
    /// <param name="allBlocks">Liste de tous les blocs objects de la recherche.</param>
    @Override
    public void CheckAllValues(List<KeyValue> keyValues, List<Block> allBlocks) {

        CheckSuppAllValues(keyValues, this.isSecondProcessing, allBlocks);

    }


}