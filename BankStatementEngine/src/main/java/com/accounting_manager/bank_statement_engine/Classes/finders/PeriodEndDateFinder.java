package com.accounting_manager.bank_statement_engine.Classes.finders;


import com.accounting_manager.bank_statement_engine.Classes.All.NumberHelper;
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
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Log4j2
public class PeriodEndDateFinder extends KeyFinder<Date> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /// <summary>
    /// Tableau qui contient les valeurs qui correspondent
    /// à la clé null.
    /// </summary>
    private List<KeyValue> _NullKeyValues;

    private Date bankStatementDate;

    /// <summary>
    /// Initialise une nouvelle instance de la classe
    /// avec des paramètres par défaut.
    /// </summary>
    /// <param name="learningEngine">L'instance du moteur d'apprentissage.</param>
    /// <param name="fournisseur">Chaine de caractères qui représente le fournisseur. Il peut être null.</param>
    public PeriodEndDateFinder() throws Exception {
        super(new Key(KeyType.PERIOD_END_DATE));
    }

    public PeriodEndDateFinder(LearningEngine learningEngine, String fournisseur, Date bankStatementDate) throws Exception {
        super(new Key(KeyType.PERIOD_END_DATE), learningEngine, fournisseur);
        this.bankStatementDate=bankStatementDate;
    }

    /// <summary>
    /// Teste si une date d'une facture est invalide.
    /// </summary>
    /// <param name="keyInvalidator">Clé qui permet de valider une date d'une facture.</param>
    /// <param name="prevSeq">Chaine de caractères qui représente la séquence d'avant de la valeur.</param>
    /// <param name="currSeq">Chaine de caractères qui représente la séquence en cours de la valeur.</param>
    /// <param name="nextSeq">Chaine de caractères qui représente la séquence d'après de la valeur.</param>
    /// <returns>True si c'est une fausse date, False sinon.</returns>
    private static boolean _IsWrongPeriodEndDate(String keyInvalidator, String prevSeq, String currSeq, String nextSeq) {
        return (prevSeq != null && prevSeq.toLowerCase().contains(keyInvalidator.toLowerCase()))
                || currSeq.toLowerCase().contains(keyInvalidator.toLowerCase())
                || (nextSeq != null && nextSeq.toLowerCase().contains(keyInvalidator.toLowerCase()));
    }

    public boolean _CheckValue(KeyValue keyValue, _Out<Date> periodEndDate) {
        // Cas particuliers : présence d'une date d'une loi + exercice.
        TextBlock prevBlock = keyValue.getValueBlocks().get(0).getPreviousTextBlock();
        TextBlock nextBlock = keyValue.getValueBlocks().get(keyValue.getValueBlocks().size() - 1).getNextTextBlock();

        String prevSeq;
        if (prevBlock != null)
            prevSeq = TextBlock.GetSequenceString(prevBlock.getLeftSequence());
        else
            prevSeq = null;

        String nextSeq = null;

        if (nextBlock != null) {
            nextSeq = TextBlock.GetSequenceString(nextBlock.getRightSequence());
            // Cas particulier : présence de la chaine "au" dans une mot.
            int position = nextSeq.toLowerCase().indexOf("au");

            if (position > 0 && (nextSeq.length() - position > 2)) {
                if (NumberHelper.IsAlphabetic(nextSeq.charAt(position - 1)) && NumberHelper.IsAlphabetic(nextSeq.charAt(position + 2)))
                    nextSeq = null;
            }
        }

        String currSeq = TextBlock.GetSequenceString(keyValue.getValueBlocks());

        if (!PeriodEndDateFinder._IsWrongPeriodEndDate("Loi", prevSeq, currSeq, null)
                && !PeriodEndDateFinder._IsWrongPeriodEndDate("Exercice", prevSeq, currSeq, null)
                && !PeriodEndDateFinder._IsWrongPeriodEndDate("clos", prevSeq, currSeq, null)
                && !PeriodEndDateFinder._IsWrongPeriodEndDate("echeance", prevSeq, currSeq, null)
                && !PeriodEndDateFinder._IsWrongPeriodEndDate("echtance", prevSeq, currSeq, null)
                && !PeriodEndDateFinder._IsWrongPeriodEndDate("ech6ance", prevSeq, currSeq, null)
                && !PeriodEndDateFinder._IsWrongPeriodEndDate("6ch6ance", prevSeq, currSeq, null)
                && !PeriodEndDateFinder._IsWrongPeriodEndDate("edition", prevSeq, currSeq, null)
                && !PeriodEndDateFinder._IsWrongPeriodEndDate("cave", prevSeq, currSeq, null)
                && !PeriodEndDateFinder._IsWrongPeriodEndDate("commande", prevSeq, currSeq, null)
                && !PeriodEndDateFinder._IsWrongPeriodEndDate("mail", prevSeq, currSeq, null)
                && !PeriodEndDateFinder._IsWrongPeriodEndDate("signe", prevSeq, currSeq, null)
                && !PeriodEndDateFinder._IsWrongPeriodEndDate("cpter", prevSeq, currSeq, null)
                && !PeriodEndDateFinder._IsWrongPeriodEndDate("compter", prevSeq, currSeq, null)
                && !PeriodEndDateFinder._IsWrongPeriodEndDate("prelevee", prevSeq, currSeq, null)
                && !PeriodEndDateFinder._IsWrongPeriodEndDate("Liv", prevSeq, currSeq, null)
                && !PeriodEndDateFinder._IsWrongPeriodEndDate("vers", prevSeq, currSeq, null)
                && !PeriodEndDateFinder._IsWrongPeriodEndDate("soit", prevSeq, currSeq, null)
                && !PeriodEndDateFinder._IsWrongPeriodEndDate("arret", prevSeq, currSeq, null))
        {
            try {
                boolean b = NumberHelper.ConvertDate(currSeq, periodEndDate);
                return b && periodEndDate.get().before(bankStatementDate);
            } catch (ParseException e) {
                log.error("An error occurred while converting the period end date: " + e);
                return false;
            }

        } else {
            periodEndDate.set(new Date());
            return false;
        }
    }

    /// <summary>
    /// Obtient
    /// le tableau qui contient les valeurs
    /// qui correspondent à la clé null.
    /// </summary>
    public List<KeyValue> getNullKeyValues() {
        return this._NullKeyValues;
    }

    /// <summary>
    /// Vérifie les clés trouvées. Il est possible de suivre
    /// la recherche des valeurs même si aucune clé n'a été trouvée.
    /// </summary>
    /// <param name="keyValues">Liste des blocs/valeurs des clés trouvées.</param>
    /// <returns>Une valeur indiquant s'il faut ajouter la recherche sans clé.</returns>
    public boolean ChekAllKeys(List<KeyValue> keyValues) {
        super.ChekAllKeys(keyValues);
        return true;
    }

    /// <summary>
    /// Teste si une date d'une facture est invalide.
    /// </summary>
    /// <param name="keyInvalidator">Clé qui permet de valider une date d'une facture.</param>
    /// <param name="prevSeq">Chaine de caractères qui représente la séquence d'avant de la valeur.</param>
    /// <param name="currSeq">Chaine de caractères qui représente la séquence en cours de la valeur.</param>
    /// <param name="nextSeq">Chaine de caractères qui représente la séquence d'après de la valeur.</param>
    /// <returns>True si c'est une fausse date, False sinon.</returns>

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
        // Test s'il existe des mot clé.
        if (keyBlock.getKeyBlocks() != null)
            return FindValueInTextBlock (keyBlock, allBlocks,  isValueChecked,  checkResult);
        else
        {
            this._NullKeyValues = FindValuesNullKey (keyBlock, allBlocks);

            isValueChecked.set(true);
            checkResult.set(false);
            return null;
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
    public boolean CheckValue(KeyValue keyValue, List<Block> allBlocks) {
        _Out<Date> periodEndDate = new _Out<>(null);
        return this._CheckValue(keyValue, periodEndDate);
    }

    /// <summary>
    /// Retourne la valeur trouvée de type <see cref="DateTime" />.
    /// </summary>
    /// <param name="validKeyValue">La valeur valide de l'instance en cours.</param>
    /// <returns>Valeur trouvée.</returns>
    @Override
    public Date GetValue(KeyValue validKeyValue) {
        _Out<Date> periodEndDate = new _Out<>(null);
        this._CheckValue(validKeyValue, periodEndDate);

        return periodEndDate.get();
    }

    /// <summary>
    /// Retourne tous les instances clés/valeurs de l'instance
    /// en cours.
    /// </summary>
    /// <returns>Tableau de type <see cref="FacKeyValue" />.</returns>
    public List<KeyValue> GetAllKeyValues() {
        List<KeyValue> allKeyValues = super.GetAllKeyValues();

        if (this._NullKeyValues != null)
            allKeyValues.addAll(this._NullKeyValues);

        return allKeyValues;
    }


}
