package com.accounting_manager.bank_statement_engine.Classes.finders;

import com.accounting_manager.bank_statement_engine.Classes.All.NumberHelper;
import com.accounting_manager.bank_statement_engine.Classes.All.StringHelper;
import com.accounting_manager.bank_statement_engine.Classes._Out._Out;
import com.accounting_manager.bank_statement_engine.Classes.block.Block;
import com.accounting_manager.bank_statement_engine.Classes.geometry.PositionType;
import com.accounting_manager.bank_statement_engine.Classes.key.Key;
import com.accounting_manager.bank_statement_engine.Classes.key.KeyFinder;
import com.accounting_manager.bank_statement_engine.Classes.key.KeyType;
import com.accounting_manager.bank_statement_engine.Classes.key.KeyValue;
import com.accounting_manager.bank_statement_engine.Classes.learning.LearningEngine;
import com.accounting_manager.bank_statement_engine.Classes.learning.LearningEntry;
import com.accounting_manager.bank_statement_engine.Classes.text.TextBlock;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class IbanNumberFinder extends KeyFinder<String> implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /// <summary>
    /// Initialise une nouvelle instance de la classe
    /// avec des paramètres par défaut.
    /// </summary>
    /// <param name="learningEngine">L'instance du moteur d'apprentissage.</param>
    public IbanNumberFinder() throws Exception {

        super(new Key(KeyType.IBAN_NUMBER));
    }

    public IbanNumberFinder(LearningEngine learningEngine) throws Exception {

        super(new Key(KeyType.IBAN_NUMBER), learningEngine, null);
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
    public List<TextBlock> FindValue(KeyValue keyValue, List<Block> allBlocks, _Out<Boolean> isValueChecked, _Out<Boolean> checkResult) {

        _Out<Float> prevBlockWeight = new _Out<>(0f);
        _Out<List<TextBlock>> previousBlocks = new _Out<>(new ArrayList<>());

        keyValue.setPosition(PositionType.RIGHT);
        FindValueInTextBlocUsingRight(keyValue, allBlocks, false, prevBlockWeight, previousBlocks);
        keyValue.setWeight(prevBlockWeight.get());

        TextBlock previousBlock = previousBlocks.get() == null ? null : previousBlocks.get().get(0);
        keyValue.setValueBlocks((previousBlock == null ? null : previousBlock.getLeftSequence()));


        if (keyValue.getValueBlocks() != null)
            checkResult.set(CheckValue(keyValue, allBlocks));

        List<TextBlock> valueBlocks;

        if (checkResult.get()) {
            isValueChecked.set(true);
            valueBlocks = new ArrayList<>(keyValue.getValueBlocks());
        } else {
            _Out<Float> nextBlockWeight = new _Out<>(0f);
            _Out<List<TextBlock>> nextBlocks = new _Out<>(new ArrayList<>());

            keyValue.setPosition(PositionType.LEFT);
            FindValueInTextBlocUsingRight(keyValue, allBlocks, false, nextBlockWeight, nextBlocks);
            keyValue.setWeight(nextBlockWeight.get());

            TextBlock nextBlock = nextBlocks.get() == null ? null : nextBlocks.get().get(0);

            valueBlocks = nextBlock == null ? null : nextBlock.getRightSequence();
        }
        // Mettre à jour le poids.
        keyValue.setWeight(GetLearningBlockWeight(GetLearningEntry(keyValue, checkResult), keyValue.getVirtualValueBlock(), keyValue.getWeight()));
        return valueBlocks;
    }

    /// <summary>
    /// Vérifie si la valeur trouvée d'une clé est une bonne
    /// valeur ou non.
    /// </summary>
    /// <param name="keyValue">Bloc texte qui représente la valeur de la clé.</param>
    /// <param name="value">Valeur trouvée.</param>
    /// <returns>True si la valeur est bonne. False sinon.</returns>
    private boolean _CheckValue(KeyValue keyValue, _Out<String> value) {
        String ibanNumberStr = TextBlock.GetSequenceString(keyValue.getValueBlocks());

        for (int i = 0; i < ibanNumberStr.length(); i++) {
            if (i + 27 < ibanNumberStr.length())
                value.set(StringHelper.substring(ibanNumberStr, i, 27));
            else
                value.set(StringHelper.substring(ibanNumberStr, i, ibanNumberStr.length() - i));

            if (NumberHelper.IsIbanNumber(value.get()))
                return true;
        }

        value.set(null);
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
    public boolean CheckValue(KeyValue keyValue, List<Block> allBlocks) {
        _Out<String> stringValue = new _Out<>(null);
        return _CheckValue(keyValue, stringValue);
    }

    /// <summary>
    /// Retourne la valeur trouvée de type <see cref="string" />.
    /// </summary>
    /// <param name="validKeyValue">La valeur valide de l'instance en cours.</param>
    /// <returns>Valeur trouvée.</returns>
    @Override
    public String GetValue(KeyValue validKeyValue) {
        _Out<String> stringValue = new _Out<>(null);
        _CheckValue(validKeyValue, stringValue);
        return stringValue.get();
    }

    /// <summary>
    /// Retourne l'entrée d'apprentissage à utilisé pour calauler
    /// le poids d'apprentiisage
    /// </summary>
    /// <param name="keyValue">Une instance de clé/valeur <see cref="FacKeyValue" />.</param>
    /// <param name="isValidValue">Indique si la valeur est valide.</param>
    /// <returns>Une instance de type <see cref="FacLearningEntry" /> qui représente l'entrée de l'apprentissage.</returns>
    @Override
    public LearningEntry GetLearningEntry(KeyValue keyValue, _Out<Boolean> isValidValue) {
        if (isValidValue.get()) {
            String ibanNumber = GetValue(keyValue);
            return this.getLearningEngine().GetEntry(KeyType.IBAN_NUMBER, ibanNumber);
        } else
            return null;
    }

}