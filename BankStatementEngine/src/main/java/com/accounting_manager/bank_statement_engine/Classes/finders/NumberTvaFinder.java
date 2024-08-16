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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NumberTvaFinder extends KeyFinder<String> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /// <summary>
    /// Instance de la classe de recherche
    /// du numéro de SIRET.
    /// </summary>
    private NumberSiretFinder siretFinder;
    /// <summary>
    /// Instance de la classe de recherche
    /// du numéro de R.C.S.
    /// </summary>
    private AccountNumberFinder rcsFinder;


    /// <summary>
    /// Initialise une nouvelle instance de la classe
    /// avec des paramètres spécifiés.
    /// </summary>
    /// <param name="siretFinder">Instance de la classe de recherche du numéro de SIRET.</param>
    /// <param name="rcsFinder">Instance de la classe de recherche du numéro de R.C.S.</param>
    /// <param name="learningEngine">L'instance du moteur d'apprentissage.</param>
    public NumberTvaFinder(NumberSiretFinder siretFinder, AccountNumberFinder rcsFinder) throws Exception {
        super(new Key(KeyType.NUMERO_TVA));
        this.siretFinder = siretFinder;
        this.rcsFinder = rcsFinder;
    }

    public NumberTvaFinder(NumberSiretFinder siretFinder, AccountNumberFinder rcsFinder, LearningEngine learningEngine) throws Exception {
        super(new Key(KeyType.NUMERO_TVA), learningEngine, null);
        this.siretFinder = siretFinder;
        this.rcsFinder = rcsFinder;
    }

    public NumberSiretFinder getSiretFinder() {
        return siretFinder;
    }

    public void setSiretFinder(NumberSiretFinder siretFinder) {
        this.siretFinder = siretFinder;
    }

    public AccountNumberFinder getRcsFinder() {
        return rcsFinder;
    }

    public void setRcsFinder(AccountNumberFinder rcsFinder) {
        this.rcsFinder = rcsFinder;
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
        _Out<Float>				nextBlockWeight = new _Out<>(0f);
        _Out<List<TextBlock>>		nextBlocks = new _Out<>(new ArrayList<>());

        keyValue.setPosition (PositionType.RIGHT);
        FindValueInTextBlocUsingRight (keyValue, allBlocks, false,  nextBlockWeight, nextBlocks);
        keyValue.setWeight( nextBlockWeight.get());

        TextBlock		nextBlock = nextBlocks.get() == null ? null : nextBlocks.get().get(0);
        return nextBlock == null ? null : nextBlock.getRightSequence();
    }

    /// <summary>
    /// Vérifie si la valeur trouvée d'une clé est une bonne
    /// valeur ou non.
    /// </summary>
    /// <param name="keyValue">Bloc texte qui représente la valeur de la clé.</param>
    /// <param name="value">Valeur trouvée.</param>
    /// <returns>True si la valeur est bonne. False sinon.</returns>
    private boolean _CheckValue(KeyValue keyValue, _Out<String> value) {
        String tavStr = TextBlock.GetSequenceString(keyValue.getValueBlocks());

        tavStr = NumberHelper.CleanIdFournisseur(tavStr);

        for (int i = 0; i < tavStr.length(); i++) {
            if (i + 18 < tavStr.length())
                value.set(StringHelper.substring(tavStr , i , 18));
            else
                value.set(StringHelper.substring(tavStr , i , tavStr.length() - i));

            if (NumberHelper.IsTva(value)) {
                // Comparer par rapport à la SIRET.
                if (this.siretFinder.getValidValue() != null) {
                    String siret = this.siretFinder.GetValue();

                    if ((value.get().length() == 18 || value.get().length() == 13) && siret.length() == 14)
                        return StringHelper.substring(value.get() , 4 , 9).toUpperCase() .equals(StringHelper.substring(siret , 0 , 9).toUpperCase());
                }
                // Comparer par rapport au RCS.
                else if (this.rcsFinder.getValidValue() != null) {
                    String rcs = this.rcsFinder.GetValue();

                    if ((value.get().length() == 18 || value.get().length() == 13) && rcs.length() == 9) {
                        return StringHelper.substring(value.get() , 4 , 9).toUpperCase().equals(StringHelper.substring(rcs , 0 , 9).toUpperCase());
                    }
                }

                return true;
            }
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
    public LearningEntry GetLearningEntry (KeyValue keyValue, _Out<Boolean> isValidValue)
    {
        if (isValidValue.get())
        {
            String		tva = GetValue (keyValue);
            return this.getLearningEngine().GetEntry (KeyType.NUMERO_TVA, NumberHelper.GetRcsFromTva (tva));
        }
        else
            return null;
    }

}