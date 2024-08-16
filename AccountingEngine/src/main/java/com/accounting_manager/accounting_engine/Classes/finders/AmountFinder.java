package com.accounting_manager.accounting_engine.Classes.finders;

import com.accounting_manager.accounting_engine.Classes._Out._Out;
import com.accounting_manager.accounting_engine.Classes.block.Block;
import com.accounting_manager.accounting_engine.Classes.key.Key;
import com.accounting_manager.accounting_engine.Classes.key.KeyFinder;
import com.accounting_manager.accounting_engine.Classes.key.KeyType;
import com.accounting_manager.accounting_engine.Classes.key.KeyValue;
import com.accounting_manager.accounting_engine.Classes.learning.LearningEngine;
import com.accounting_manager.accounting_engine.Classes.text.TextBlock;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.util.List;

@Log4j2
public class AmountFinder extends KeyFinder<Double> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /// <summary>
    /// Tableau qui contient les valeurs qui correspondent
    /// à la clé null.
    /// </summary>
    private List<KeyValue> _NullKeyValues;


    /// <summary>
    /// Initialise une nouvelle instance de la classe
    /// avec des paramètres par défaut.
    /// </summary>
    /// <param name="learningEngine">L'instance du moteur d'apprentissage.</param>
    /// <param name="fournisseur">Chaine de caractères qui représente le fournisseur. Il peut être null.</param>
    public AmountFinder() throws Exception {
        super(new Key(KeyType.AMOUNT));
    }

    public AmountFinder(LearningEngine learningEngine, String fournisseur) throws Exception {
        super(new Key(KeyType.AMOUNT), learningEngine, fournisseur);
    }

    /// <summary>
    /// Obtient
    /// le tableau qui contient les valeurs
    /// qui correspondent à la clé null.
    /// </summary>
    public List<KeyValue> get_NullKeyValues() {
        return _NullKeyValues;
    }

    public void set_NullKeyValues(List<KeyValue> _NullKeyValues) {
        this._NullKeyValues = _NullKeyValues;
    }

    /// <summary>
    /// Vérifie les clés trouvées. Il est possible de suivre
    /// la recherche des valeurs même si aucune clé n'a été trouvée.
    /// </summary>
    /// <param name="keyValues">Liste des blocs/valeurs des clés trouvées.</param>
    /// <returns>Une valeur indiquant s'il faut ajouter la recherche sans clé.</returns>
    @Override
    public boolean ChekAllKeys(List<KeyValue> keyValues) {
        super.ChekAllKeys(keyValues);
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
    public List<TextBlock> FindValue(KeyValue keyBlock, List<Block> allBlocks, _Out<Boolean> isValueChecked, _Out<Boolean> checkResult) {
        this._NullKeyValues = FindValuesNullKey (keyBlock, allBlocks);

        isValueChecked.set(true);
        checkResult.set(false);
        return null;
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
            return CheckAmountValue(keyValue, allBlocks);
        } catch (Exception e) {
            log.error("An error occurred while checking the value: " + e);
        }
        return false;
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
    /// Retourne tous les instances clés/valeurs de l'instance
    /// en cours.
    /// </summary>
    /// <returns>Tableau de type <see cref="FacKeyValue" />.</returns>
    @Override
    public List<KeyValue> GetAllKeyValues() {
        List<KeyValue> allKeyValues = super.GetAllKeyValues();
        allKeyValues.addAll(this._NullKeyValues);

        return allKeyValues;
    }

}