package com.accounting_manager.accounting_engine.Classes.finders;

import com.accounting_manager.accounting_engine.Classes.All.NumberHelper;
import com.accounting_manager.accounting_engine.Classes._Out._Out;
import com.accounting_manager.accounting_engine.Classes.block.Block;
import com.accounting_manager.accounting_engine.Classes.geometry.PositionType;
import com.accounting_manager.accounting_engine.Classes.key.Key;
import com.accounting_manager.accounting_engine.Classes.key.KeyFinder;
import com.accounting_manager.accounting_engine.Classes.key.KeyType;
import com.accounting_manager.accounting_engine.Classes.key.KeyValue;
import com.accounting_manager.accounting_engine.Classes.learning.LearningEngine;
import com.accounting_manager.accounting_engine.Classes.text.TextBlock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NumberPageFinder extends KeyFinder<Integer> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /// <summary>
    /// Initialise une nouvelle instance de la classe
    /// avec des paramètres par défaut.
    /// </summary>
    /// <param name="learningEngine">L'instance du moteur d'apprentissage.</param>
    /// <param name="fournisseur">Chaine de caractères qui représente le fournisseur. Il peut être null.</param>
    public NumberPageFinder() throws Exception {

        super(new Key(KeyType.NUMERO_PAGE));

    }

    public NumberPageFinder(LearningEngine learningEngine, String fournisseur) throws Exception {

        super(new Key(KeyType.NUMERO_PAGE), learningEngine, fournisseur);

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
    @SuppressWarnings("unused")
    @Override
    public List<TextBlock> FindValue(KeyValue keyBlock, List<Block> allBlocks, _Out<Boolean> isValueChecked, _Out<Boolean> checkResult) {
        _Out<Float>				nextBlockWeight = new _Out<>(0f);
        _Out<List<TextBlock>>		nextBlocks = new _Out<>(new ArrayList<>());

        keyBlock.setPosition(PositionType.RIGHT);
        FindValueInTextBlocUsingRight (keyBlock, allBlocks, false,  nextBlockWeight,  nextBlocks);
        keyBlock.setWeight(nextBlockWeight.get());

        TextBlock		nextBlock = nextBlocks == null ? null : nextBlocks.get().get(0);

        if (keyBlock.getValueBlocks() != null)
            checkResult.set(CheckValue (keyBlock, allBlocks));

        if (checkResult.get())
        {
            isValueChecked.set(true);
            return keyBlock.getValueBlocks();
        }

        return keyBlock.getValueBlocks();
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
        return NumberHelper.IsDigit(NumberHelper.FindFirstChar(TextBlock.GetSequenceString(keyValue.getValueBlocks())));
    }

    /// <summary>
    /// Retourne la valeur trouvée de type <see cref="string" />.
    /// </summary>
    /// <param name="validKeyValue">La valeur valide de l'instance en cours.</param>
    /// <returns>Valeur trouvée.</returns>
    @Override
    public Integer GetValue(KeyValue validKeyValue) {
        return Integer.parseInt(String.valueOf(NumberHelper.FindFirstChar(TextBlock.GetSequenceString(validKeyValue.getValueBlocks()))));
    }

}