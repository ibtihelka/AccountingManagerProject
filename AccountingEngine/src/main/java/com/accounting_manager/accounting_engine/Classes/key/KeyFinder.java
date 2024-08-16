package com.accounting_manager.accounting_engine.Classes.key;

import com.accounting_manager.accounting_engine.Classes._Out._Out;
import com.accounting_manager.accounting_engine.Classes.block.Block;
import com.accounting_manager.accounting_engine.Classes.document.DocumentZone;
import com.accounting_manager.accounting_engine.Classes.document._Document;
import com.accounting_manager.accounting_engine.Classes.geometry.GeometryHelper;
import com.accounting_manager.accounting_engine.Classes.geometry.PositionType;
import com.accounting_manager.accounting_engine.Classes.geometry.Region;
import com.accounting_manager.accounting_engine.Classes.learning.LearningEngine;
import com.accounting_manager.accounting_engine.Classes.learning.LearningEntry;
import com.accounting_manager.accounting_engine.Classes.table.TableCell;
import com.accounting_manager.accounting_engine.Classes.text.TextBlock;
import com.accounting_manager.accounting_engine.Classes.All.NumberHelper;

import java.util.*;
import java.util.stream.Collectors;

public abstract class KeyFinder<T> {
    /// <summary>
    /// Ceofficient de la recherche gauche.
    /// </summary>
    public static float WEIGHT_LEFT_DIR_COEFF = 100;
    /// <summary>
    /// Ceofficient de la recherche haute.
    /// </summary>
    public static float WEIGHT_UP_DIR_COEFF = 25;
    /// <summary>
    /// Ceofficient de la recherche droite.
    /// </summary>
    public static float WEIGHT_RIGHT_DIR_COEFF = 1;
    /// <summary>
    /// Ceofficient de la recherche basse.
    /// </summary>
    public static float WEIGHT_DOWN_DIR_COEFF = 5;
    /// <summary>
    /// Ceofficient de l'alignement lors
    /// de l'évaluation partielle du poids.
    /// </summary>
    public static float WEIGHT_ALIGN_COEFF = 5;
    /// <summary>
    /// Ceofficient de l'alignement positif lors
    /// de l'évaluation partielle du poids.
    /// </summary>
    public static float WEIGHT_POSITIVE_ALIGN_COEFF = 1;
    /// <summary>
    /// Ceofficient de l'alignement négatif lors
    /// de l'évaluation partielle du poids.
    /// </summary>
    public static float WEIGHT_NEGATIVE_ALIGN_COEFF = 5;
    /// <summary>
    /// Ceofficient de la distance lors
    /// de l'évaluation partielle du poids.
    /// </summary>
    public static float WEIGHT_DISTANCE_COEFF = 1;
    /// <summary>
    /// Ceofficient de l'ombre lors
    /// de l'évaluation partielle du poids.
    /// </summary>
    public static float WEIGHT_SHADOW_COEFF = 75;
    /// <summary>
    /// Ceofficient de l'apprentissage lors
    /// de l'évaluation partielle du poids.
    /// </summary>
    public static float LEARNING_COEFF = 100;
    /// <summary>
    /// Ceofficient A du poids des zones préférées
    /// lors de l'évaluation partielle du poids.
    /// </summary>
    public static float WEIGHT_PREFERRED_ZONE_ACOEFF = 0.0001f;
    /// <summary>
    /// Ceofficient B du poids des zones préférées
    /// lors de l'évaluation partielle du poids.
    /// </summary>
    public static float WEIGHT_PREFERRED_ZONE_BCOEFF = 2;
    /// <summary>
    /// Ceofficient du poids des clés <see cref="FacKeyString.Weight" />
    /// lors de l'évaluation totale du poids.
    /// </summary>
    public static float WEIGHT_WEIGHT_COEFF = 200;
    /// <summary>
    /// Ceofficient du poids des zones
    /// lors de l'évaluation totale du poids.
    /// </summary>
    public static float WEIGHT_ZONE_COEFF = 25;
    /// <summary>
    /// Ceofficient du poids alignement/distance
    /// lors de l'évaluation totale du poids.
    /// </summary>
    public static float WEIGHT_ALIGN_DIST_COEFF = 1;
    /// <summary>
    /// Taux d'évaluation des poids midifiés. Ce paramètre
    /// est utilisé pour discrétiser les poids.
    /// </summary>
    public static int WEIGHT_STEP = 20;
    /// <summary>
    /// Poids maximal qu'il ne faut pas dépasser
    /// pour inclure une valeur.
    /// </summary>
    public static float VALUE_MAX_WEIGHT = -1;
    /// <summary>
    /// Poids maximal sur l'horizontal
    /// qu'il ne faut pas dépasser pour dire qu'une valeur
    /// est supplémentaire.
    /// </summary>
    public static float HSUPP_MAX_WEIGHT = 1250;
    /// <summary>
    /// Poids maximal sur le vertical
    /// qu'il ne faut pas dépasser pour dire qu'une valeur
    /// est supplémentaire.
    /// </summary>
    public static float VSUPP_MAX_WEIGHT = 1100;
    /// <summary>
    /// Clé objet de la recherche.
    /// </summary>
    private Key key;
    /// <summary>
    /// Moteur d'apprentissage qui contient
    /// les données apprises des factures.
    /// </summary>
    private LearningEngine learningEngine;
    /// <summary>
    /// Entrée d'apprentissage du fournisseur
    /// associée à l'instance en cours.
    /// </summary>
    private LearningEntry learningEntry;
    /// <summary>
    /// Liste des zones où la clé a été trouvé.
    /// Les valeurs seront inclues.
    /// </summary>
    private List<KeyValue> keyValues;
    /// <summary>
    /// Indique qu'une valeur valide
    /// a été trouvée. Une valeur nulle
    /// indique qu'aucune valeur valide n'a été
    /// trouvée.
    /// </summary>
    private KeyValue validValue;

    /// <summary>
    /// Initialise une nouvelle instance de la classe
    /// avec des paramètres spécifiés.
    /// </summary>
    /// <param name="key">Clé objet de la recherche.</param>
    /// <param name="learningEngine">L'instance du moteur d'apprentissage.</param>
    /// <param name="fournisseur">Chaine de caractères qui représente le fournisseur. Il peut être null.</param>
    public KeyFinder(Key key, LearningEngine learningEngine, String fournisseur) {
        this.key = key;
        // Créer ou récupérer l'entrée.
        this.learningEngine = learningEngine;

        if (fournisseur != null) {
            LearningEntry learningEntry = learningEngine.GetEntry(key.getType(), fournisseur);

            if (learningEntry == null)
                learningEntry = new LearningEntry(fournisseur);

            this.learningEntry = learningEntry;
        }
    }


    public KeyFinder(Key key) {
        this.key = key;
        // Créer ou récupérer l'entrée.
//				this.learningEngine = learningEngine;
//
//				if (fournisseur != null)
//				{
//					LearningEntry		learningEntry = learningEngine.GetEntry (key.getType(), fournisseur);
//
//					if (learningEntry == null)
//						learningEntry = new LearningEntry (fournisseur);
//
//					this.learningEntry = learningEntry;
//				}
    }

    /// <summary>
    /// Dévalide la liste des valeurs sauf une. La clé/valeur
    /// est celle passée par le paramètre <paramref name="keyValueExcept" />.
    /// </summary>
    /// <param name="keyValues">Tableau des instances <see cref="FacKeyValue" />.</param>
    /// <param name="keyValueExcept">Valeur exception.</param>
    public static void InvalidKeyValuesExcept(List<KeyValue> keyValues, KeyValue keyValueExcept) {
        for (KeyValue keyValue : keyValues) {
            if (keyValue != keyValueExcept)
                keyValue.setValidValue(false);
        }
    }

    static void sortCollections(_Out<List<Float>> selWeights, _Out<List<List<TextBlock>>> selValueBlocks) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < selWeights.get().size(); i++) {
            indices.add(i);
        }

        Collections.sort(indices, Comparator.comparingDouble(i -> selWeights.get().get(i)));

        List<Float> sortedF = new ArrayList<>();
        List<List<TextBlock>> sortedS = new ArrayList<>();
        for (int i : indices) {
            sortedF.add(selWeights.get().get(i));
            sortedS.add(selValueBlocks.get().get(i));
        }

        selValueBlocks.set(sortedS);
        selWeights.set(sortedF);
    }

    static void ArryasSort(_Out<List<Float>> selWeights, _Out<List<List<TextBlock>>> selValueBlocks) {
        Float tempselWeights;
        List<TextBlock> tempselValueBlocks;


        for (int i = 0; i < selValueBlocks.get().size() - 1; i++) {
            for (int j = 0; j < selWeights.get().size() - i - 1; j++) {
                if (selWeights.get().get(j) < selWeights.get().get(j + 1)) {
                    tempselWeights = selWeights.get().get(j);
                    tempselValueBlocks = selValueBlocks.get().get(j);

                    selWeights.get().set(j, selWeights.get().get(j + 1));
                    selValueBlocks.get().set(j, selValueBlocks.get().get(j + 1));

                    selWeights.get().set(j + 1, tempselWeights);
                    selValueBlocks.get().set(j + 1, tempselValueBlocks);
                }
            }
        }
    }

    /// <summary>
    /// Convertit une chaine de caractères en un montant.
    /// La chaine de caractères sera nottoyée.
    /// </summary>
    /// <param name="validKeyValue">Une instance valide d'une clé/valeur.</param>
    /// <returns>Un <see cref="double" /> qui contient la valeur convertie.</returns>
    public static double GetAmountValue(KeyValue validKeyValue) {
        String amountStr = NumberHelper.CleanFloatSubstring(TextBlock.GetSequenceString(validKeyValue.getValueBlocks()));
        return NumberHelper.GetAmount(amountStr);
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public KeyValue getValidValue() {
        return validValue;
    }

    public void setValidValue(KeyValue validValue) {
        this.validValue = validValue;
    }

    public LearningEngine getLearningEngine() {
        return learningEngine;
    }
    /// <summary>
    /// Obtient
    /// le ceofficient du poids des zones
    /// lors de l'évaluation totale du poids.
    /// </summary>

    public void setLearningEngine(LearningEngine learningEngine) {
        this.learningEngine = learningEngine;
    }

    public List<KeyValue> getKeyValues() {
        return keyValues;
    }
    /// <summary>
    /// Obtient
    /// la valeur minimale à partir de laquelle
    /// un bloc sera éléminé.
    /// </summary>

    public void setKeyValues(List<KeyValue> keyValues) {
        this.keyValues = keyValues;
    }

    public int getKeyValuesCount() {
        return keyValues.size();
    }

    public float HAlignRate() {

        return GeometryHelper.HALIGN_RATE;

    }

    /// <summary>
    /// Obtient
    /// le taux  de la largeur de l'intervalle
    /// utilisée pour faire les recherches des blocs
    /// dans le cas d'un vertical.
    /// </summary>
    public float VAlignRate() {
        return GeometryHelper.VALIGN_RATE;
    }

    public float WeightZoneCoeff() {
        return WEIGHT_ZONE_COEFF;
    }

    /// <summary>
    /// Obtient
    /// le poids maximal qu'il ne faut pas dépasser
    /// pour inclure une valeur.
    /// </summary>
    public float ValueMaxWeight() {
        return this.VALUE_MAX_WEIGHT;
    }

    public float LearningMaxWeight() {
        return LearningEntry.MAX_WEIGHT;
    }

    /// <summary>
    /// Retourne l'instance de la clé/valeur qui se trouve
    /// à la position <paramref name="index" />.
    /// </summary>
    /// <param name="index">Entier qui représente l'indice de la zone clé/valeur.</param>
    /// <returns>Une instance de type <see cref="FacKeyValue" />.</returns>
    public KeyValue GetKeyValue(int index) {
        return this.keyValues.get(index);
    }

    private <T> List<T> twoDArrayToList(T[][] twoDArray) {
        List<T> list = new ArrayList<T>();
        for (T[] array : twoDArray) {
            list.addAll(Arrays.asList(array));
        }
        return list;
    }

    /// <summary>
    /// Lance la recherche. Cette méthode
    /// exécute d'abord la méthode <see cref="FindKey" />,
    /// la méthode <see cref="FindValue" /> et enfin la méthode
    /// <see cref="CheckValue" />.
    /// </summary>
    /// <param name="documents">Blocs racines à partir du quels on commence la recherche.
    /// Les blocs eux mêmes seront inclus dans la recherche.</param>
    public void Find(List<_Document> documents) {
        // Récupérer tous les blocs à parcourir.
        List<Block> allBlocksList = new ArrayList<Block>();

        for (_Document document : documents)
            allBlocksList.addAll(document.GetAllBlocks());

        List<Block> allBlocks = allBlocksList.stream().collect(Collectors.toList());//allBlocksList.toArray (new Block[allBlocksList.size()]);
        // Extraire les clés valides.
        List<List<TextBlock>> keyBlocks;
        List<TextBlock> _keyBlocks;
        TextBlock keyBlock = null;

        List<KeyValue> _keyValues = new ArrayList<KeyValue>();
        List<TextBlock> unmarkedSeq;

        _Out<List<KeyString>> keyStrings = new _Out<>(new ArrayList<>());
        List<Block> newBlocks = new ArrayList<Block>();
        _Out<List<TextBlock>> _newBlocks = new _Out<>(new ArrayList<>());

        for (int i = 0; i < allBlocks.size(); i++) {
            if (allBlocks.get(i) instanceof TextBlock)
                keyBlock = (TextBlock) allBlocks.get(i);

            if (keyBlock != null && !keyBlock.is_isSeqMarked() && !keyBlock.is_isKeyMarked()) {
                unmarkedSeq = TextBlock.GetUnmarkedSequence(keyBlock.getSequence(), keyBlock);
                //.toArray(new TextBlock[TextBlock.GetUnmarkedSequence (keyBlock.getSequence(), keyBlock).size()]);
                if (unmarkedSeq.size() > 0 && (
                        keyBlocks = FindKey(unmarkedSeq, allBlocks, keyStrings, _newBlocks)) != null) {
                    // Créer l'instance clé/valeur.
                    for (int j = 0; j < keyBlocks.size(); j++) {
                        _keyBlocks = keyBlocks.get(j);
                        _keyValues.add(new KeyValue(this.key, _keyBlocks, keyStrings.get().get(j)));
                        // Marquer les blocs.
                        for (TextBlock keyTextBlock : _keyBlocks) {
                            keyTextBlock.set_isSeqMarked(true);
                        }
                    }

                    newBlocks.addAll(_newBlocks.get());
                }
                // Marquer la séquence.
                TextBlock.markSequence((List<Block>) (List<?>) unmarkedSeq, true);
            }
        }
        // Démarquer les blocs.
        TextBlock.markSequence(allBlocks, false);
        // Mettre à jour la liste totale des blocs.
        if (newBlocks.size() > 0) {
            List<Block> _allBlocks = allBlocks.stream().collect(Collectors.toList());

            _allBlocks.addAll(newBlocks);

            allBlocks = _allBlocks.stream().collect(Collectors.toList());
        }

        List<KeyValue> keyValues = _keyValues.stream().collect(Collectors.toList());
        // Valider l'ensemble des clés.
        List<Integer> keyIndexes = new ArrayList<Integer>();

        boolean addNullKey = ChekAllKeys(keyValues);
        _keyValues = new ArrayList<KeyValue>();

        for (KeyValue keyValue : keyValues) {
            if (keyValue.isValidKey())
                _keyValues.add(keyValue);
        }

        if (addNullKey)
            _keyValues.add(new KeyValue(this.key, null, null));

        keyValues = _keyValues.stream().collect(Collectors.toList());
        // Rechercher les valeurs et les vérifier.
        _Out<Boolean> isValueChecked = new _Out<>(false);
        _Out<Boolean> checkResult = new _Out<>(false);

        for (KeyValue keyValue : keyValues) {
            // Trouver la valeur et vérifiér.
            isValueChecked.set(false);
            checkResult.set(false);

            keyValue.setValueBlocks(FindValue(keyValue, allBlocks, isValueChecked, checkResult));
            // Vérifier la valeur.
            if (keyValue.getValueBlocks() != null && !isValueChecked.get())
                checkResult.set(CheckValue(keyValue, allBlocks));

            keyValue.setValidValue(checkResult.get());
        }
        // Extraire la liste totale des clés/valeurs.
        this.keyValues = keyValues;
        List<KeyValue> allKeyValues = GetAllKeyValues();
        // Vérifier globalement les valeurs.
        CheckAllValues(GetAllKeyValues(), allBlocks);
        // Extraire la valeur valide.
        this.validValue = GetKeyValue(allKeyValues);

    }

    /// Recherche les occurences d'une clé <see cref="Key" />
    /// dans une séquance passée en paramètre.
    /// </summary>
    /// <param name="seqBlocks">Blocs sur lequel se fait la recherche.</param>
    /// <param name="allBlocks">Liste de tous les blocs objects de la recherche.</param>
    /// <param name="keyStrings">Chaines des caractères utilisées pour trouver la clé.</param>
    /// <param name="newBlocks">Liste des blocs à ajouter à la liste totale des blocs.</param>
    /// <returns>Tableau de tableaux de blocs textes de la clé ou null si la recherche n'a rien trouvé.</returns>
    public List<List<TextBlock>> FindKey(
            List<TextBlock> seqBlocks, List<Block> allBlocks,
            _Out<List<KeyString>> keyStrings, _Out<List<TextBlock>> newBlocks) {
        List<List<TextBlock>> _keyBlocks = new ArrayList<>();
        List<KeyString> _keyStrings = new ArrayList<KeyString>();
        List<TextBlock> _newBlocks = new ArrayList<TextBlock>();

        _FindKey(seqBlocks, allBlocks, _keyBlocks, _keyStrings, _newBlocks);

        newBlocks.set(_newBlocks);
        keyStrings.set(_keyStrings);

        return _keyBlocks.size() > 0 ? _keyBlocks : null;
    }

    /// <summary>
    /// Recherche d'une façon récursive
    /// les occurences d'une clé <see cref="Key" />
    /// dans une séquance passée en paramètre.
    /// </summary>
    /// <param name="seqBlocks">Blocs sur lequel se fait la recherche.</param>
    /// <param name="allBlocks">Liste de tous les blocs objects de la recherche.</param>
    /// <param name="keyBlocks">Liste des blocks des clés trouvées.</param>
    /// <param name="keyStrings">Chaines des caractères utilisées pour trouver la clé.</param>
    /// <param name="newBlocks">Liste des blocs à ajouter à la liste totale des blocs.</param>
    private void _FindKey(
            List<TextBlock> seqBlocks, List<Block> allBlocks,
            List<List<TextBlock>> keyBlocks, List<KeyString> keyStrings, List<TextBlock> newBlocks) {
        // Déclarer les variables.
        String seqBlocksStr = TextBlock.GetSequenceString(seqBlocks);

        _Out<List<TextBlock>> newLeftSeqBlocks = new _Out<>(new ArrayList<TextBlock>());
        _Out<List<TextBlock>> newRightSeqBlocks = new _Out<>(new ArrayList<TextBlock>());

        _Out<List<TextBlock>> _keyBlocks = new _Out<>(new ArrayList<TextBlock>());
        _Out<List<TextBlock>> _newBlocks = new _Out<>(new ArrayList<TextBlock>());
        for (KeyString keyString : this.key.getAllKeyStrings()) {
            if (GetKeyBlocks(
                    keyString, seqBlocks, seqBlocksStr,
                    _keyBlocks, newLeftSeqBlocks, newRightSeqBlocks, _newBlocks)) {
                keyBlocks.add(_keyBlocks.get());
                keyStrings.add(keyString);
                newBlocks.addAll(_newBlocks.get());
                // Gauche.
                if (newLeftSeqBlocks.get().size() > 0)
                    _FindKey(newLeftSeqBlocks.get(), allBlocks, keyBlocks, keyStrings, newBlocks);
                // Droite.
                if (newRightSeqBlocks.get().size() > 0)
                    _FindKey(newRightSeqBlocks.get(), allBlocks, keyBlocks, keyStrings, newBlocks);

                return;
            }
        }
    }

    /// <summary>
    /// Extraire les blocks de la chaine de caractère <paramref name="keyString" />
    /// en ajustant si c'est nécessaire les blocks de la séquence <paramref name="seqBlocks" />.
    /// </summary>
    /// <param name="keyString">Chaine de caractères d'une clé.</param>
    /// <param name="seqBlocks">Séquence objet de la recherche.</param>
    /// <param name="seqBlocksStr">Chaine de caractères formée par la séquence <paramref name="seqBlocks" />.</param>
    /// <param name="keyBlocks">Liste des blocs qui constituent la chaine de la clé.</param>
    /// <param name="newLeftSeqBlocks">Liste des blocs de gauche de la nouvelle séquence aprés détection.</param>
    /// <param name="newRightSeqBlocks">Liste des blocs de droite la nouvelle séquence aprés détection.</param>
    /// <param name="newBlocks">Liste des blocs à ajouter à la liste totale des blocs.</param>
    /// <returns>True si la chaine de cractères a été trouvée. False sinon.</returns>
    private boolean GetKeyBlocks(
            KeyString keyString, List<TextBlock> seqBlocks, String seqBlocksStr,
            _Out<List<TextBlock>> keyBlocks, _Out<List<TextBlock>> newLeftSeqBlocks, _Out<List<TextBlock>> newRightSeqBlocks, _Out<List<TextBlock>> newBlocks) {
        // Recherche de la séquence de la clé.
        int keyStartIndex = -1;

        if (keyString.isCaseSensitive())
            keyStartIndex = seqBlocksStr.indexOf(keyString.getString());
        else
            keyStartIndex = seqBlocksStr.toLowerCase().indexOf(keyString.getString().toLowerCase());


        // Occurence trouvée.
        if (keyStartIndex > -1) {
            List<TextBlock> _keyBlocks = new ArrayList<TextBlock>();
            List<TextBlock> _newLeftSeqBlocks = new ArrayList<TextBlock>();
            List<TextBlock> _newRightSeqBlocks = new ArrayList<TextBlock>();
            List<TextBlock> _newBlocks = new ArrayList<TextBlock>();

            int keyEndIndex = keyStartIndex + keyString.getString().length() - 1;
            int seqStartIndex = 0, seqEndIndex;


            _Out<TextBlock> right = new _Out<TextBlock>(null);
            _Out<TextBlock> left = new _Out<TextBlock>(null);
            TextBlock center;
            TextBlock keyBlock;
            boolean isNewKeyBlock;
            // Extraite les blocks de la clé.
            for (TextBlock seqBlock : seqBlocks) {
                seqEndIndex = seqStartIndex + seqBlock.getText().length() - 1;
                // Partie de gauche.
                if (seqEndIndex < keyStartIndex)
                    _newLeftSeqBlocks.add(seqBlock);
                    // Partie de droite.
                else if (seqStartIndex > keyEndIndex)
                    _newRightSeqBlocks.add(seqBlock);
                    // Partie centrale.
                else {
                    keyBlock = seqBlock;
                    isNewKeyBlock = false;
                    // Pas de découpage.
                    if (seqStartIndex < keyStartIndex || seqEndIndex > keyEndIndex) {
                        // Découpage gauche.
                        if (seqStartIndex < keyStartIndex) {

                            center = keyBlock.Split(0, keyStartIndex - seqStartIndex, left, right);

                            _newBlocks.add(center);
                            _newLeftSeqBlocks.add(center);

                            keyBlock = right.get();
                            isNewKeyBlock = true;
                        }
                        // Découpage droite.
                        if (seqEndIndex > keyEndIndex) {
                            center = keyBlock.Split(0, keyBlock.getText().length() - (seqEndIndex - keyEndIndex), left, right);

                            _newBlocks.add(right.get());
                            _newRightSeqBlocks.add(right.get());

                            keyBlock = center;
                            isNewKeyBlock = true;
                        }
                    }

                    _keyBlocks.add(keyBlock);

                    if (isNewKeyBlock)
                        _newBlocks.add(keyBlock);
                }
                // Mettre à jour les indices.
                seqStartIndex = seqEndIndex + 1;
            }
            // Créer les blocs virtuelles de la clé.
            keyBlocks.set(new ArrayList<>(keyString.getStringCharsCount()));

            int keyCharIndex = 0;
            KeyStringChar keyChar = keyString.GetStringChar(keyCharIndex);
            List<TextBlock> __keyBlocks = new ArrayList<TextBlock>();
            TextBlock virtuelKeyBlock;

            keyStartIndex = 0;
            keyEndIndex = keyStartIndex + keyChar.getString().length() - 1;

            seqStartIndex = 0;

            boolean makeKeyBlocks = false;
            boolean returnFalse = false;
            int width, minWidth, maxWidth;

            for (int i = 0; i < _keyBlocks.size(); i++) {
                keyBlock = _keyBlocks.get(i);
                seqEndIndex = seqStartIndex + keyBlock.getText().length() - 1;

                if (seqEndIndex > keyEndIndex) {
                    center = keyBlock.Split(0, keyBlock.getText().length() - (seqEndIndex - keyEndIndex), left, right);

                    __keyBlocks.add(center);
                    _keyBlocks.add(i + 1, right.get());

                    seqEndIndex = seqEndIndex - keyBlock.getText().length() + center.getText().length();
                    makeKeyBlocks = true;
                } else {
                    __keyBlocks.add(keyBlock);
                    makeKeyBlocks = seqEndIndex == keyEndIndex;
                }
                // Cr�er les blocs virtuels de la cl�.
                if (makeKeyBlocks || i == _keyBlocks.size() - 1) {
                    // Single.
                    if (keyChar.isSingle() && (__keyBlocks.size() != 1 || __keyBlocks.get(0).isVirtual())) {
                        returnFalse = true;
                        break;
                    }
                    // OK.
                    virtuelKeyBlock = new TextBlock(__keyBlocks);
                    keyBlocks.get().add(keyCharIndex, virtuelKeyBlock);
                    // V�rifier l'espacement.
                    if (keyCharIndex > 0) {
                        width = virtuelKeyBlock.getRegion().getLeft() - keyBlocks.get().get(keyCharIndex - 1).getRegion().getRight();
                        minWidth = (int) (keyChar.getMinSeperatorWidth() * virtuelKeyBlock.getDocument().getRegion().getWidth());
                        maxWidth = (int) (keyChar.getMaxSeperatorWidth() * virtuelKeyBlock.getDocument().getRegion().getWidth());

                        if (keyChar.getMinSeperatorWidth() >= 0 && width < minWidth || keyChar.getMaxSeperatorWidth() >= 0 && width > maxWidth) {
                            returnFalse = true;
                            break;
                        }
                    }
                    // Incr�menter la cl�.
                    keyCharIndex++;
                    if (keyCharIndex <= keyBlocks.get().size() && keyCharIndex != keyString.getStringCharsCount())
//					if (keyEndIndex < keyBlocks.get().size())
                    {
                        keyChar = keyString.GetStringChar(keyCharIndex);
                        keyStartIndex = keyEndIndex + 1;
                        keyEndIndex = keyStartIndex + keyChar.getString().length() - 1;
                    }
                    __keyBlocks.clear();
                }

                seqStartIndex = seqEndIndex + 1;
            }
            // TRUE.
            if (!returnFalse) {
                newLeftSeqBlocks.set(_newLeftSeqBlocks);
                newRightSeqBlocks.set(_newRightSeqBlocks);
                newBlocks.set(_newBlocks);

                return true;
            }
        }
        // Occurence non trouvée.
        keyBlocks.set(null);
        newLeftSeqBlocks.set(null);
        newRightSeqBlocks.set(null);
        newBlocks.set(null);

        return false;
    }

    /// <summary>
    /// Marque les blocs d'une clé.
    /// </summary>
    /// <param name="keyBlocks">Liste des blocs textes de la clé.</param>
    /// <param name="keyString">Chaine de cractères de la clé.</param>
    public void MarkKeyBlocks(List<TextBlock> keyBlocks, KeyString keyString) {
        for (int i = 0; i < keyBlocks.size(); i++) {
            if (!keyString.GetStringChar(i).isShared())
                keyBlocks.get(i).set_isKeyMarked(true);
        }
    }

    /// <summary>
    /// Vérifie les clés trouvées. Il est possible de suivre
    /// la recherche des valeurs même si aucune clé n'a été trouvée.
    /// </summary>
    /// <param name="keyValues">Liste des blocs/valeurs des clés trouvées.</param>
    /// <returns>Une valeur indiquant s'il faut ajouter la recherche sans clé.</returns>
    public boolean ChekAllKeys(List<KeyValue> keyValues) {
        for (KeyValue keyValue : keyValues)
            MarkKeyBlocks(keyValue.getKeyBlocks(), keyValue.getString());

        return false;
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
    public abstract List<TextBlock> FindValue(KeyValue keyBlock, List<Block> allBlocks, _Out<Boolean> isValueChecked, _Out<Boolean> checkResult);

    /// <summary>
    /// Vérifie si la valeur trouvée d'une clé est une bonne
    /// valeur ou non.
    /// </summary>
    /// <param name="keyValue">Bloc texte qui représente la valeur de la clé.</param>
    /// <param name="allBlocks">Liste de tous les blocs objects de la recherche.</param>
    /// <returns>True si la valeur est bonne. False sinon.</returns>
    public abstract boolean CheckValue(KeyValue keyValue, List<Block> allBlocks);

    /// <summary>
    /// Retourne le poids d'une zone comparé à une zone référence.
    /// </summary>
    /// <param name="zoneRef">Zone référence.</param>
    /// <param name="zone">Zone objet de l'évaluation.</param>
    /// <param name="document">Document en cours.</param>
    /// <returns>Nombre qui représente le taux d'eloignement entre les deux zones.</returns>
    public int GetZoneWeight(DocumentZone zoneRef, DocumentZone zone, _Document document) {
        Region zoneRefRegion = document.GetZoneRegions(zoneRef).get(0);
        List<Region> zoneRegions = document.GetZoneRegions(zone);

        int xRef = (zoneRefRegion.getLeft() + zoneRefRegion.getRight()) / 2;
        int yRef = (zoneRefRegion.getTop() + zoneRefRegion.getBottom()) / 2;

        int x, y, dx, dy;
        double d = 0;

        for (Region zoneRegion : zoneRegions) {
            x = (zoneRegion.getLeft() + zoneRegion.getRight()) / 2;
            y = (zoneRegion.getTop() + zoneRegion.getBottom()) / 2;

            dx = x - xRef;
            dy = y - yRef;

            d += Math.sqrt(dx * dx + dy * dy);
        }

        return (int) d;
    }

    /// <summary>
    /// Retourne le poids modifié d'une clé en se basant
    /// sur sa valeur associée.
    /// </summary>
    /// <param name="keyValue">L'instance clé/valeur en cours.</param>
    /// <returns>Poids modifié.</returns>
    public int GetWeight(KeyValue keyValue) {
        double zoneWeight = 0;
        _Document document = keyValue.getVirtualKeyBlock().getDocument();

        for (int i = 0; i < this.key.preferredZonesCount(); i++) {
            zoneWeight +=
                    WEIGHT_PREFERRED_ZONE_ACOEFF *
                            Math.pow(WEIGHT_PREFERRED_ZONE_BCOEFF, this.key.preferredZonesCount() - i) *
                            GetZoneWeight(this.key.GetPreferredZone(i), keyValue.getVirtualKeyBlock().getDocumentZone(), document);
        }

        int stepCount = (int) (
                keyValue.getString().getWeight() * WEIGHT_WEIGHT_COEFF +
                        keyValue.getWeight() * WEIGHT_ALIGN_DIST_COEFF +
                        zoneWeight * this.WeightZoneCoeff()) /
                WEIGHT_STEP;

        return stepCount * WEIGHT_STEP;
    }

    /// <summary>
    /// Vérifie les valeurs des clés trouvées. Ceci permet de vérifier l'intégrité
    /// des valeurs et d'annuler certaines clés/valeurs.
    /// </summary>
    /// <param name="keyValues">Tableau des valeurs des clés.</param>
    /// <param name="allBlocks">Liste de tous les blocs objects de la recherche.</param>
    public void CheckAllValues(List<KeyValue> keyValues, List<Block> allBlocks) {
        // Vérifier les clés/valeurs par rapport à l'apprentissage.
        CheckLearnedValues(keyValues);
        // Extraire/Classer les poids.
        Map<Float, List<KeyValue>> hWeights = new HashMap<Float, List<KeyValue>>();
        _Out<Float> weight = new _Out<>(0f);
        List<KeyValue> _keyValues = new ArrayList<KeyValue>();
        KeyValue nullKeyValue = null;

        for (KeyValue keyValue : keyValues)
        {
            if (keyValue.isValidValue())
            {
                if (keyValue.getString() == null)
                {
                    nullKeyValue = keyValue;
                }
                else
                {
                    weight.set((float)GetWeight(keyValue));

                    if (!(hWeights.containsKey(weight)))
                    {
                        _keyValues = new ArrayList<KeyValue> ();
                        hWeights.put(weight.get()  , _keyValues);
                    }

                    _keyValues.add(keyValue);
                }
            }
        }
        // Faire le tri.

        /* Ici nous avons ordonné le map par key et puis nous avons copié la liste des keys et vlaues dans leur tableau dédiés */

        Map<Float, List<KeyValue>> hWeightsSort = hWeights.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (o, n) -> o, LinkedHashMap::new));

        Float[] weights = new Float[hWeights.size()];
        List<KeyValue>[] __keyValues = (List<KeyValue>[]) new List[hWeights.size()];

        //hWeights.getKeys().CopyTo (weights, 0);

        weights = hWeights.keySet().toArray(new Float[0]);

        //hWeights.values().CopyTo (__keyValues, 0);

        __keyValues = hWeights.values().toArray((List<KeyValue>[]) new List[0]);

        //	Array.Sort<Float,List<KeyValue>> (weights, __keyValues);

        // Ajouter clé/valeur nulle.
        if (nullKeyValue != null) {
            Float[] tmpWeights = new Float[weights.length + 1];
            for (int k = 0; k < tmpWeights.length; k++) {
                tmpWeights[k] = 0.0f;
            }

            System.arraycopy(weights, 0, tmpWeights, 0, weights.length);
            tmpWeights[tmpWeights.length - 1] = -1.0f;

            List<KeyValue>[] tmpKeyValues = (List<KeyValue>[]) new List[__keyValues.length + 1];

            System.arraycopy(__keyValues, 0, tmpKeyValues, 0, __keyValues.length);

            List<KeyValue> lkv = new ArrayList<>();
            lkv.add(nullKeyValue);

            tmpKeyValues[tmpKeyValues.length - 1] = lkv;

            weights = tmpWeights;
            __keyValues = tmpKeyValues;
        }
        // Dévalider certaines valeurs.
        boolean invalidValues = false;

        for (int i = 0; i < weights.length; i++) {
            weight.set(weights[i]);
            _keyValues = __keyValues[i];

            if (!invalidValues) {
                if (_keyValues != null) {
                    if (_keyValues.size() > 1)
                        CheckSameWeightValues(_keyValues, weight);

                    invalidValues = true;
                }
            } else {
                if (_keyValues != null) {
                    for (KeyValue keyValue : _keyValues) {
                        keyValue.setValidValue(false);
                    }
                }
            }
        }
    }
    /// <summary>
    /// Vérifie les valeurs des clés trouvées en utilisant les données
    /// de l'apprentissage. Cette méthode sera appelée automatiqument
    /// par la méthode <see cref="CheckAllValues" />.
    /// </summary>
    /// <param name="keyValues">Tableau des instances de type <see cref="FacKeyValue" /> à vérifier.</param>
    public void CheckLearnedValues(List<KeyValue> keyValues) {
        if (this.learningEntry != null) {
            float maxWeight = this.LearningMaxWeight();

            for (KeyValue keyValue : keyValues) {
                if (keyValue.isValidValue() &&
                        this.learningEntry.GetWeight(keyValue.getVirtualValueBlock().getRegion(), keyValue.getVirtualValueBlock().getDocument().getRegion()) > maxWeight) {
                    keyValue.setValidValue(false);
                }
            }
        }
    }

    /// <summary>
    /// Valide plusieurs valeurs issues de plusieurs clés
    /// de même poids. Par défaut cette méthode dévalide tous les
    /// valeurs si le vote majoritaire n'aboutit pas.
    /// </summary>
    /// <param name="keyValues">Tableau des valeurs qui ont des clés de même poids.</param>
    /// <param name="weight">Valeur du poids.</param>
    public void CheckSameWeightValues(List<KeyValue> keyValues, _Out<Float> weight) {
        Map<Object, List<KeyValue>> votes = new HashMap<>();
        List<KeyValue> votesLists = new ArrayList<>();
        Object value;


        // Classer les votes.
        for (KeyValue keyValue : keyValues) {
            value = GetValue(keyValue);
            keyValue.setValue(value);


            votes.computeIfAbsent(key, __ -> new ArrayList<>())
                    .add(keyValue);

            votesLists.add(keyValue);
        }
        // Dévalider s'il n'y pas de vote majoritaire.
        List<KeyValue> maxVotes = new ArrayList<>();

        for (List<KeyValue> _votesLists : votes.values()) {
            if (maxVotes == null || _votesLists.size() > maxVotes.size())
                maxVotes = _votesLists;
            else {
                if (_votesLists.size() == maxVotes.size()) {
                    for (KeyValue keyValue : maxVotes) {
                        keyValue.setValidValue(false);
                    }
                }

                for (KeyValue keyValue : _votesLists) {
                    keyValue.setValidValue(false);
                }
            }
        }
    }

    /// <summary>
    /// Retourne l'instance clé/valeur qui a été choisie. Par défaut
    /// elle retourne la première instance dont la valeur est valide.
    /// </summary>
    /// <param name="keyValues"></param>
    /// <returns>Une instance non nulle si une valeur valide a été trouvée ou une valeur nulle sinon.</returns>
    public KeyValue GetKeyValue(List<KeyValue> keyValues) {
        for (KeyValue keyValue : keyValues) {
            if (keyValue.isValidValue())
                return keyValue;
        }

        return null;
    }

    /// <summary>
    /// Retourne la valeur trouvée de type {T}.
    /// </summary>
    /// <returns>Valeur trouvée.</returns>
    public T GetValue() {
        Object value = this.validValue.getValue();

        if (value == null) {
            value = GetValue(this.validValue);
            this.validValue.setValue(value);
        }
        if (value instanceof _Out<?>){
            value = ((_Out<?>) value).get();
        }

        return (T) value;
    }

    /// <summary>
    /// Retourne la valeur trouvée de type {T}.
    /// </summary>
    /// <param name="validKeyValue">La valeur valide de l'instance en cours.</param>
    /// <returns>Valeur trouvée.</returns>
    public abstract T GetValue(KeyValue validKeyValue);

    /// <summary>
    /// Retourne la sous-séquence d'un bloc texte.
    /// </summary>
    /// <param name="keyValue">Instance clé/valeur en cours.</param>
    /// <param name="textBlock">Bloc texte.</param>
    /// <returns>Tableau de <see cref="FacTextBlock" /> qui représente la sous-séquence.</returns>
    /// <remarks>Par défaut cette méthode utilise la séquence retournée par la propriété <see cref="FacTextBlock.Sequence" />.</remarks>
    public List<TextBlock> GetSubSequence(KeyValue keyValue, TextBlock textBlock) {
        return TextBlock.GetSubSequence(textBlock.getSequence(), textBlock);
    }

    /// <summary>
    /// Met la valeur qui se trouve à droite d'une séquence
    /// d'un bloc dans l'instance clé/valeur passée en paramètre.
    /// Une vérification sera lancée.
    /// </summary>
    /// <param name="keyValue">Instance clé/valeur en cours.</param>
    /// <param name="valueStartBlock">Bloc de départ de la valeur.</param>
    /// <param name="weight">Poids de la valeur.</param>
    /// <param name="allBlocks">Liste de tous les blocs objects de la recherche.</param>
    /// <returns>True si la valeur de la séquence est accpétée, False sinon.</returns>
    public boolean SetRightTextAsValue(KeyValue keyValue, Block valueStartBlock, float weight, List<Block> allBlocks) {
        TextBlock refTextBlock = null;
        List<TextBlock> textBlocks = new ArrayList<>();

        if (valueStartBlock instanceof TextBlock) {
            refTextBlock = (TextBlock) valueStartBlock;
            textBlocks = GetSubSequence(keyValue, refTextBlock);
        } else if (valueStartBlock instanceof TableCell && valueStartBlock.innerBlocksCount() > 0) {
            refTextBlock = ((TableCell) valueStartBlock).GetInnerBlock(0);
            textBlocks = GetSubSequence(keyValue, refTextBlock);
        }

        if (textBlocks != null && (this.ValueMaxWeight() < 0 || weight <= this.ValueMaxWeight())) {
            keyValue.setValueBaseBlock(refTextBlock);
            keyValue.setValueBlocks(textBlocks);
            keyValue.setWeight(weight);

            boolean checkResult = CheckValue(keyValue, allBlocks);

            if (checkResult)
                return true;
            else
                keyValue.setValueBlocks(null);
        }

        return false;
    }

    /// <summary>
    /// Retourne le poids d'un bloc.
    /// </summary>
    /// <param name="block">Bloc dont il faut retourner le poids.</param>
    /// <param name="align">Alignement.</param>
    /// <param name="distance">Distance.</param>
    /// <param name="shadow">Ombre.</param>
    /// <returns>Un <see cref="float" /> qui représente le poids du bloc.</returns>
    public float GetBlockWeight(Block block, int alignements, int distances, int shadows) {
        return
                WEIGHT_ALIGN_COEFF * (alignements > 0 ? WEIGHT_POSITIVE_ALIGN_COEFF * alignements : WEIGHT_NEGATIVE_ALIGN_COEFF * Math.abs(alignements)) +
                        WEIGHT_DISTANCE_COEFF * distances +
                        WEIGHT_SHADOW_COEFF * shadows;
    }

    /// <summary>
    /// Sélectionne une liste des blocs valides
    /// parmi la liste des blocs passée en paramètre <paramref name="blocks" />.
    /// </summary>
    /// <param name="keyValue">Instance clé/valeur en cours.</param>
    /// <param name="blocks">Tableau des blocs objet de la sélection.</param>
    /// <param name="alignements">Tableau qui contient les alignements.</param>
    /// <param name="distances">Tableau qui contient les distances.</param>
    /// <param name="shadows">Tableau qui contient les ombres.</param>
    /// <param name="allBlocks">Liste de tous les blocs objects de la recherche.</param>
    /// <param name="checkValue">Indique qu'il faut vérifier la valeur en utilisant la méthode <see cref="SetRightTextAsValue" />.</param>
    /// <param name="selWeights">Tableau qui va contenir les poids calculés.</param>
    /// <param name="selValueBlocks">Tableau qui va contenir les blocs textes qui représentent les valeurs.</param>
    /// <returns>Le bloc le plus pondéré ou null s'il n'y a pas de blocs.</returns>
    private List<Block> SelectBlocks(
            KeyValue keyValue, List<Block> blocks,
            _Out<List<Integer>> alignements, _Out<List<Integer>> distances, _Out<List<Integer>> shadows,
            List<Block> allBlocks,
            boolean checkValue,
            _Out<List<Float>> selWeights, _Out<List<List<TextBlock>>> selValueBlocks) {
        List<Block> selBlocks = new ArrayList<Block>();
        List<Float> _selWeights = new ArrayList<Float>();
        List<List<TextBlock>> _selValueBlocks = new ArrayList<>();

        Block block;
        Block valueBlock;
        float weight;

        for (int i = 0; i < blocks.size(); i++) {
            block = blocks.get(i);
            weight = GetBlockWeight(block, alignements.get().get(i), distances.get().get(i), shadows.get().get(i));


            if (block instanceof TextBlock && (!checkValue |
                    SetRightTextAsValue(keyValue, block, weight, allBlocks))) {
                _Out<Boolean> out = new _Out<>(true);
                _Out<Boolean> out2 = new _Out<>(keyValue.isValidValue());
                selBlocks.add(block);
                valueBlock = checkValue ? keyValue.getVirtualValueBlock() : block;
                _selWeights.add(GetLearningBlockWeight(GetLearningEntry(keyValue, checkValue ? out : out2), checkValue ? keyValue.getVirtualValueBlock() : block, checkValue ? keyValue.getWeight() : weight));
                _selValueBlocks.add(checkValue ? keyValue.getValueBlocks() : Arrays.asList((TextBlock) block));
            }
        }

        selWeights.set(_selWeights);
        selValueBlocks.set(_selValueBlocks);

        return selBlocks;
    }

    /// <summary>
    /// Révalue le poids d'un bloc
    /// en utilisant l'apprentissage.
    /// </summary>
    /// <param name="learningEntry">Entrée d'apprentissage.</param>
    /// <param name="block">Bloc dont il faut retourner le poids.</param>
    /// <param name="weight">Poids à révaluer.</param>
    /// <returns>Un <see cref="float" /> qui représente le poids du bloc.</returns>
    public float GetLearningBlockWeight(LearningEntry learningEntry, Block block, float weight) {
        float learningWeight;

        if (learningEntry != null)
            learningWeight = learningEntry.GetWeight(block.getRegion(), block.getDocument().getRegion());
        else
            learningWeight = 0;

        return this.LEARNING_COEFF * learningWeight + weight;
    }

    /// <summary>
    /// Sélectionne un bloc parmi la liste des blocs passée en paramètre <paramref name="blocks" />.
    /// </summary>
    /// <param name="keyValue">Une instance d'une la clé/valeur.</param>
    /// <param name="blocks">Tableau des blocs objet de la sélection.</param>
    /// <param name="alignements">Tableau qui contient les alignements.</param>
    /// <param name="distances">Tableau qui contient les distances.</param>
    /// <param name="shadows">Tableau qui contient les ombres.</param>
    /// <param name="allBlocks">Liste de tous les blocs objects de la recherche.</param>
    /// <param name="checkValue">Indique qu'il faut vérifier la valeur en utilisant la méthode <see cref="SetRightTextAsValue" />.</param>
    /// <param name="weight">Poids choisi.</param>
    /// <param name="valueBlocks">Liste des blocks de la valeur choisie.</param>
    /// <returns>True si une valeur a été choisie. False sinon.</returns>
    private boolean Select(
            KeyValue keyValue, List<Block> blocks,
            _Out<List<Integer>> alignements, _Out<List<Integer>> distances, _Out<List<Integer>> shadows,
            List<Block> allBlocks,
            boolean checkValue,
            _Out<Float> weight, _Out<List<TextBlock>> valueBlocks) {
        List<Block> selBlocks;
        _Out<List<Float>> selWeights = new _Out<>(new ArrayList<>());
        _Out<List<List<TextBlock>>> selValueBlocks = new _Out<>(new ArrayList<>());

        selBlocks = SelectBlocks(
                keyValue, blocks, alignements, distances, shadows,
                allBlocks, checkValue,
                selWeights, selValueBlocks);
        // Sélection selon poids.
        if (selWeights.get().size() > 0) {
            //Arrays.sort<Float,TextBlock [][]> (selWeights, selValueBlocks);

//            ArryasSort(selWeights, selValueBlocks);
			sortCollections(selWeights , selValueBlocks);
            weight.set(selWeights.get().get(0));
            valueBlocks.set(selValueBlocks.get().get(0));
            return true;
        } else {
            weight.set((float) -1);
            valueBlocks.set(null);
            return false;
        }
    }

    /// <summary>
    /// Recherche les valeurs pour une clé nulle.
    /// </summary>
    /// <param name="nullKeyValue">Instance qui contient la clé nulle.</param>
    /// <param name="allBlocks">Liste de tous les blocs objects de la recherche.</param>
    /// <returns>Liste des clés/valeurs trouvées.</returns>
    public List<KeyValue> FindValuesNullKey(KeyValue nullKeyValue, List<Block> allBlocks) {
        // Recherche la date dans tous les blocs.
        List<KeyValue> keyValues = new ArrayList<KeyValue>();

        List<TextBlock> seqBlocks;
        TextBlock textBlock = null;
        KeyValue keyValue;
        for (Block block : allBlocks) {
            if (block instanceof TextBlock)
                textBlock = (TextBlock) block;

            if (textBlock != null && !textBlock.is_isSeqMarked()) {
                seqBlocks = textBlock.getSequence();

                nullKeyValue.setValueBaseBlock(textBlock);
                nullKeyValue.setValueBlocks(seqBlocks);
                // Créer une nouvelle instance de la clé/valeur.
                if (CheckValue(nullKeyValue, allBlocks)) {
                    keyValue = nullKeyValue.getSuppKeyValue(seqBlocks);
                    keyValue.setValueBaseBlock(nullKeyValue.getValueBaseBlock());
                    keyValue.setValueBlocks(nullKeyValue.getValueBlocks());
                    keyValue.setValidValue(true);

                    keyValues.add(keyValue);
                }
                // Marquer la séquence.
                TextBlock.markSequence((List<Block>) (List<?>) seqBlocks, true);
            }
        }
        // Marquer les blocs.
        TextBlock.markSequence(allBlocks, false);
        nullKeyValue.setValueBaseBlock(null);
        nullKeyValue.setValueBlocks(null);

        return keyValues;
    }

    /// <summary>
    /// Trouve une valeur dans une clé
    /// en utilisant que la zone à droite.
    /// </summary>
    /// <param name="keyValue">Une instance d'une la clé/valeur.</param>
    /// <param name="allBlocks">Liste de tous les blocs objects de la recherche.</param>
    /// <param name="checkValue">Indique qu'il faut vérifier la valeur en utilisant la méthode <see cref="SetRightTextAsValue" />.</param>
    /// <param name="weight">Poids trouvé.</param>
    /// <param name="valueBlocks">Liste des blocks de la valeur trouvée.</param>
    /// <returns>True si une valeur a été trouvée. False sinon.</returns>
    public boolean FindValueInTextBlocUsingRight(
            KeyValue keyValue, List<Block> allBlocks, boolean checkValue,
            _Out<Float> weight, _Out<List<TextBlock>> valueBlocks) {
        TextBlock virtualKeyBlock = keyValue.getVirtualKeyBlock();
        // Faire la recherche.
        List<Block> blocks;
        _Out<List<Integer>> alignements = new _Out<>(new ArrayList<>());
        _Out<List<Integer>> distances = new _Out<>(new ArrayList<>());
        _Out<List<Integer>> shadows = new _Out<>(new ArrayList<>());

        blocks = GeometryHelper.GetRightBlocks(virtualKeyBlock, this.HAlignRate(), allBlocks, alignements, distances, shadows);
        return Select(keyValue, blocks, alignements, distances, shadows, allBlocks, checkValue, weight, valueBlocks);
    }

    /// Trouve une valeur dans une clé
    /// en utilisant que la zone à gauche.
    /// </summary>
    /// <param name="keyValue">Une instance d'une la clé/valeur.</param>
    /// <param name="allBlocks">Liste de tous les blocs objects de la recherche.</param>
    /// <param name="checkValue">Indique qu'il faut vérifier la valeur en utilisant la méthode <see cref="SetRightTextAsValue" />.</param>
    /// <param name="weight">Poids trouvé.</param>
    /// <param name="valueBlocks">Liste des blocks de la valeur trouvée.</param>
    /// <returns>True si une valeur a été trouvée. False sinon.</returns>
    public boolean FindValueInTextBlocUsingLeft(
            KeyValue keyValue, List<Block> allBlocks, boolean checkValue,
            _Out<Float> weight, _Out<List<TextBlock>> valueBlocks) {
        TextBlock virtualKeyBlock = keyValue.getVirtualKeyBlock();
        // Faire la recherche.
        List<Block> blocks;
        _Out<List<Integer>> alignements = new _Out<>();
        _Out<List<Integer>> distances = new _Out<>();
        _Out<List<Integer>> shadows = new _Out<>();

        blocks = GeometryHelper.GetLeftBlocks(virtualKeyBlock, this.HAlignRate(), allBlocks, alignements, distances, shadows);
        return Select(keyValue, blocks, alignements, distances, shadows, allBlocks, checkValue, weight, valueBlocks);
    }

    /// <summary>
    /// Trouve une valeur dans une clé
    /// en utilisant que la zone de bas.
    /// </summary>
    /// <param name="keyValue">Une instance d'une la clé/valeur.</param>
    /// <param name="allBlocks">Liste de tous les blocs objects de la recherche.</param>
    /// <param name="checkValue">Indique qu'il faut vérifier la valeur en utilisant la méthode <see cref="SetRightTextAsValue" />.</param>
    /// <param name="weight">Poids trouvé.</param>
    /// <param name="valueBlocks">Liste des blocks de la valeur trouvée.</param>
    /// <returns>True si une valeur a été trouvée. False sinon.</returns>
    public boolean FindValueInTextBlocUsingDown(
            KeyValue keyValue, List<Block> allBlocks, boolean checkValue,
            _Out<Float> weight, _Out<List<TextBlock>> valueBlocks) {
        TextBlock virtualKeyBlock = keyValue.getVirtualKeyBlock();
        // Faire la recherche.
        List<Block> blocks;
        _Out<List<Integer>> alignements = new _Out<>(new ArrayList<>());
        _Out<List<Integer>> distances = new _Out<>(new ArrayList<>());
        _Out<List<Integer>> shadows = new _Out<>(new ArrayList<>());

        blocks = GeometryHelper.GetDownBlocks(virtualKeyBlock, this.VAlignRate(), allBlocks, alignements, distances, shadows);
        return Select(keyValue, blocks, alignements, distances, shadows, allBlocks, checkValue, weight, valueBlocks);
    }

    /// <summary>
    /// Trouve une valeur dans une clé
    /// en utilisant que la zone de haut.
    /// </summary>
    /// <param name="keyValue">Une instance d'une la clé/valeur.</param>
    /// <param name="allBlocks">Liste de tous les blocs objects de la recherche.</param>
    /// <param name="checkValue">Indique qu'il faut vérifier la valeur en utilisant la méthode <see cref="SetRightTextAsValue" />.</param>
    /// <param name="weight">Poids trouvé.</param>
    /// <param name="valueBlocks">Liste des blocks de la valeur trouvée.</param>
    /// <returns>True si une valeur a été trouvée. False sinon.</returns>
    public boolean FindValueInTextBlocUsingUp(
            KeyValue keyValue, List<Block> allBlocks, boolean checkValue,
            _Out<Float> weight, _Out<List<TextBlock>> valueBlocks) {
        TextBlock virtualKeyBlock = keyValue.getVirtualKeyBlock();
        // Faire la recherche.
        List<Block> blocks;
        _Out<List<Integer>> alignements = new _Out<>(new ArrayList<>());
        _Out<List<Integer>> distances = new _Out<>(new ArrayList<>());
        _Out<List<Integer>> shadows = new _Out<>(new ArrayList<>());

        blocks = GeometryHelper.GetUpBlocks(virtualKeyBlock, this.VAlignRate(), allBlocks, alignements, distances, shadows);
        return Select(keyValue, blocks, alignements, distances, shadows, allBlocks, checkValue, weight, valueBlocks);
    }

    /// <summary>
    /// Trouve une valeur dans une clé
    /// en suivant certaines priorités géometriques.
    /// </summary>
    /// <param name="keyValue">Une instance d'une la clé/valeur.</param>
    /// <param name="allBlocks">Liste de tous les blocs objects de la recherche.</param>
    /// <param name="isValueChecked">Indique que la valeur a été vérifiée.</param>
    /// <param name="checkResult">Dans le cas où la valeur a été vérifiée ce paramètre contient le résultat.</param>
    /// <returns>Tableau de blocs textes qui contiennet la valeur du mot clé.</returns>
    /// <remarks>Seules certaines directions seront parcourues.</remarks>
//    public List<TextBlock> FindValueInTextBlock(KeyValue keyValue, List<Block> allBlocks, _Out<Boolean> isValueChecked, _Out<Boolean> checkResult) {
//        // Faire la recherche.
//        _Out<List<TextBlock>> valueBlocks = new _Out<>(new ArrayList<>());
//        List<TextBlock> selValueBlocks = new ArrayList<>();
//        float selWeight, _weight, _selWeight;
//        _Out<Float> weight = new _Out<>((float) 0);
//        PositionType selPosition;
//        boolean isValueFound, selIsValueFound;
//        // Droite.
//        keyValue.setPosition(PositionType.RIGHT);
//        isValueFound = FindValueInTextBlocUsingRight(keyValue, allBlocks, true, weight, valueBlocks);
//        _weight = WEIGHT_RIGHT_DIR_COEFF * weight.get();
//
//        selValueBlocks = new ArrayList<>();
//        selWeight = weight.get();
//        selPosition = PositionType.RIGHT;
//        _selWeight = _weight;
//        selIsValueFound = isValueFound;
//        // Bas.
//        keyValue.setPosition(PositionType.DOWN);
//        isValueFound = FindValueInTextBlocUsingDown(keyValue, allBlocks, true, weight, valueBlocks);
//        _weight = WEIGHT_DOWN_DIR_COEFF * weight.get();
//
//        if (!selIsValueFound || isValueFound && _weight < _selWeight) {
//            selValueBlocks = valueBlocks.get();
//            selWeight = weight.get();
//            selPosition = PositionType.DOWN;
//            _selWeight = _weight;
//            selIsValueFound = isValueFound;
//        }
//        // Haut.
//        keyValue.setPosition(PositionType.UP);
//        isValueFound = FindValueInTextBlocUsingUp(keyValue, allBlocks, true, weight, valueBlocks);
//        _weight = WEIGHT_UP_DIR_COEFF * weight.get();
//
//        if (!selIsValueFound || isValueFound && _weight < _selWeight) {
//            selValueBlocks = valueBlocks.get();
//            selWeight = weight.get();
//            selPosition = PositionType.UP;
//            _selWeight = _weight;
//            selIsValueFound = isValueFound;
//        }
//
//        keyValue.setPosition(selPosition);
//        keyValue.setWeight(selWeight);
//        // Retour.
//        isValueChecked.set(true);
//        checkResult.set(selIsValueFound);
//
//        return selValueBlocks;
//    }
    public List<TextBlock> FindValueInTextBlock(KeyValue keyValue, List<Block> allBlocks, _Out<Boolean> isValueChecked, _Out<Boolean> checkResult)
    {
        // Faire la recherche.
        List<TextBlock> valueBlocks, selValueBlocks;
        float weight, selWeight, _weight, _selWeight;
        PositionType selPosition;
        boolean isValueFound, selIsValueFound;
        // Droite.
        keyValue.setPosition(PositionType.RIGHT);
        _Out<Float> tempOut_weight = new _Out<>(0f);
        _Out<List<TextBlock>> tempOut_valueBlocks = new _Out<>(new ArrayList<>());
        isValueFound = FindValueInTextBlocUsingRight(keyValue, allBlocks, true, tempOut_weight, tempOut_valueBlocks);
        valueBlocks = tempOut_valueBlocks.get();
        weight = tempOut_weight.get();
        _weight = KeyFinder.WEIGHT_RIGHT_DIR_COEFF * weight;

        selValueBlocks = valueBlocks;
        selWeight = weight;
        selPosition = PositionType.RIGHT;
        _selWeight = _weight;
        selIsValueFound = isValueFound;
        // Bas.
        keyValue.setPosition(PositionType.DOWN);
        _Out<Float> tempOut_weight2 = new _Out<>(0f);
        _Out<List<TextBlock>> tempOut_valueBlocks2 = new _Out<>(new ArrayList<>());
        isValueFound = FindValueInTextBlocUsingDown(keyValue, allBlocks, true, tempOut_weight2, tempOut_valueBlocks2);
        valueBlocks = tempOut_valueBlocks2.get();
        weight = tempOut_weight2.get();
        _weight = KeyFinder.WEIGHT_DOWN_DIR_COEFF * weight;

        if (!selIsValueFound || isValueFound && _weight < _selWeight)
        {
            selValueBlocks = valueBlocks;
            selWeight = weight;
            selPosition = PositionType.DOWN;
            _selWeight = _weight;
            selIsValueFound = isValueFound;
        }
        // Haut.
        keyValue.setPosition(PositionType.UP);
        _Out<Float> tempOut_weight3 = new _Out<>(0f);
        _Out<List<TextBlock>> tempOut_valueBlocks3 = new _Out<>(new ArrayList<>());
        isValueFound = FindValueInTextBlocUsingUp(keyValue, allBlocks, true, tempOut_weight3, tempOut_valueBlocks3);
        valueBlocks = tempOut_valueBlocks3.get();
        weight = tempOut_weight3.get();
        _weight = KeyFinder.WEIGHT_UP_DIR_COEFF * weight;

        if (!selIsValueFound || isValueFound && _weight < _selWeight)
        {
            selValueBlocks = valueBlocks;
            selWeight = weight;
            selPosition = PositionType.UP;
            _selWeight = _weight;
            selIsValueFound = isValueFound;
        }

        keyValue.setPosition(selPosition);
        keyValue.setWeight(selWeight);
        // Retour.
        isValueChecked.set(true);
        checkResult.set(selIsValueFound);

        return selValueBlocks;
    }

    /// <summary>
    /// Retourne l'entrée d'apprentissage à utilisé pour calauler
    /// le poids d'apprentiisage
    /// </summary>
    /// <param name="keyValue">Une instance de clé/valeur <see cref="FacKeyValue" />.</param>
    /// <param name="isValidValue">Indique si la valeur est valide.</param>
    /// <returns>Une instance de type <see cref="FacLearningEntry" /> qui représente l'entrée de l'apprentissage.</returns>
    public LearningEntry GetLearningEntry(KeyValue keyValue, _Out<Boolean> isValidValue) {
        return this.learningEntry;
    }

    /// <summary>
    /// Recherche est ajouter des valeurs supplémentaires
    /// pour des montants.
    /// </summary>
    /// <param name="keyValue">Instance clé/valeur utilisée comme démarrage.</param>
    /// <param name="horizontalSeq">Indique qu'il faut faire une recherche horizontale.</param>
    /// <param name="allBlocks">Liste de tous les blocs objects de la recherche.</param>
    /// <returns>Liste supplémentaites des clés/valeurs.</returns>
    public List<KeyValue> FindSuppValues(_Out<KeyValue> keyValue, boolean horizontalSeq, List<Block> allBlocks) {
        List<KeyValue> suppKeyValues = new ArrayList<KeyValue>();
        KeyValue suppKeyValue;

        _Out<List<TextBlock>> valueBlocks = new _Out<>(new ArrayList<>());
        _Out<Float> weight = new _Out<>((float) 0);

        while (true) {
            suppKeyValue = keyValue.get().getSuppKeyValue(keyValue.get().getValueBlocks());
            suppKeyValue.setPosition(PositionType.DOWN);

            if ((horizontalSeq ?
                    FindValueInTextBlocUsingRight(suppKeyValue, allBlocks, true, weight, valueBlocks) :
                    FindValueInTextBlocUsingDown(suppKeyValue, allBlocks, true, weight, valueBlocks))) {
                // Tester le poids par rapport au poids MINI.
                if (weight.get() < (horizontalSeq ? HSUPP_MAX_WEIGHT : VSUPP_MAX_WEIGHT)) {
                    suppKeyValue.setWeight(weight.get());
                    suppKeyValue.setValueBlocks(valueBlocks.get());
                    suppKeyValue.setValidValue(true);

                    suppKeyValues.add(suppKeyValue);
                    keyValue.set(suppKeyValue);
                } else
                    break;
            } else
                break;
        }

        return suppKeyValues;
    }

    /// <summary>
    /// Retourne tous les instances clés/valeurs de l'instance
    /// en cours.
    /// </summary>
    /// <returns>Tableau de type <see cref="FacKeyValue" />.</returns>
    public List<KeyValue> GetAllKeyValues() {
        List<KeyValue> allKeyValues = this.keyValues.stream().collect(Collectors.toList());

        for (KeyValue keyValue : this.keyValues) {
            if (keyValue.getSuppKeyValues() != null)
                allKeyValues.addAll(keyValue.getSuppKeyValues());
        }

        return allKeyValues;
    }

    /// <summary>
    /// Trouve les valeurs supplémentaires des clés trouvées.
    /// </summary>
    /// <param name="keyValues">Tableau des valeurs des clés.</param>
    /// <param name="isSecondProcessing">Indique que le second traitement est activé.</param>
    /// <param name="allBlocks">Liste de tous les blocs objects de la recherche.</param>
    public void CheckSuppAllValues(List<KeyValue> keyValues, boolean isSecondProcessing, List<Block> allBlocks) {
        // Valeurs supplémentaires.
        List<KeyValue> hSuppKeyValues, vSuppKeyValues;
        List<KeyValue> suppKeyValues = new ArrayList<>();

        for (KeyValue keyValue : keyValues) {
            if (keyValue.isValidValue()) {
                _Out<KeyValue> keyValue_out = new _Out<>(keyValue);
                hSuppKeyValues = isSecondProcessing ? FindSuppValues(keyValue_out, true, allBlocks) : null;
                vSuppKeyValues = FindSuppValues(keyValue_out, false, allBlocks);
                keyValue = keyValue_out.get();
                if (hSuppKeyValues != null) {
                    suppKeyValues.addAll(vSuppKeyValues);
                    suppKeyValues.addAll(hSuppKeyValues);

                } else
                    suppKeyValues = vSuppKeyValues.stream().collect(Collectors.toList());

                keyValue.setSuppKeyValues(suppKeyValues);
            }
        }
    }

    /// <summary>
    /// Vérifie si une valeur donnée est un montant ou non.
    /// Si c'est un montant alors un bloc virtuelle sera crée et le
    /// poids peut être modifié.
    /// </summary>
    /// <param name="keyValue">Instance d'une clé/valeur.</param>
    /// <param name="allBlocks">Liste de tous les blocs.</param>
    /// <returns>True si la valeur est un montant. False sinon.</returns>
    public boolean CheckAmountValue(KeyValue keyValue, List<Block> allBlocks) throws Exception {
        List<TextBlock> cleanBlocks = NumberHelper.CleanFloatBlocks(keyValue.getValueBlocks(), keyValue.getValueBaseBlock() != null ? keyValue.getValueBaseBlock() : keyValue.getValueBlocks().get(0));
        String cleanStr = TextBlock.GetSequenceString(cleanBlocks);

        if (NumberHelper.IsAmount(NumberHelper.CleanFloatSubstring(cleanStr), true)) {
            // Créer un bloc virtuel + changement du poids.
            if (cleanStr.length() != TextBlock.GetSequenceString(keyValue.getValueBlocks()).length()) {
                TextBlock virtualValueBlock = new TextBlock(cleanBlocks);
                // Re-calculer les données du poids.
                _Out<Integer> alignement = new _Out<Integer>(0);
                _Out<Integer> distance = new _Out<Integer>(0);
                _Out<Integer> shadow = new _Out<Integer>(0);

                if (keyValue.getString() != null) {
                    switch (keyValue.getPosition()) {
                        case UP:
                            GeometryHelper.getUpBlockParams(keyValue.getVirtualKeyBlock(), virtualValueBlock, allBlocks, alignement, distance, shadow);
                            break;
                        case RIGHT:
                            GeometryHelper.getRightBlockParams(keyValue.getVirtualKeyBlock(), virtualValueBlock, allBlocks, alignement, distance, shadow);
                            break;
                        case DOWN:
                            GeometryHelper.getDownBlockParams(keyValue.getVirtualKeyBlock(), virtualValueBlock, allBlocks, alignement, distance, shadow);
                            break;
                        case LEFT:
                            GeometryHelper.getLeftBlockParams(keyValue.getVirtualKeyBlock(), virtualValueBlock, allBlocks, alignement, distance, shadow);
                            break;
                        default:
                            throw new Exception("Unsupported position " + keyValue.getPosition());
                    }
                } else {
                    alignement.set(0);
                    distance.set(0);
                    shadow.set(0);
                }
                // Modifier les paramètres.
                keyValue.setValueBlocks(cleanBlocks);
                keyValue.setWeight(GetBlockWeight(virtualValueBlock, alignement.get(), distance.get(), shadow.get()));
            }

            return true;
        } else
            return false;
    }

}