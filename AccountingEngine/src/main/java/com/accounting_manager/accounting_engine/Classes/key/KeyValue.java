package com.accounting_manager.accounting_engine.Classes.key;

import com.accounting_manager.accounting_engine.Classes.geometry.PositionType;
import com.accounting_manager.accounting_engine.Classes.text.TextBlock;

import java.io.Serializable;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeyValue implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4095221120086384410L;
			/// <summary>
			/// Clé objet de la recherche.
			/// </summary>
			private Key				key;
			/// <summary>
			/// Blocs où la Clé a été trouvé.
			/// </summary>
			private List<TextBlock>		keyBlocks;
			/// <summary>
			/// Bloc virtuel qui représente la liste
			/// des blocs de la Clé.
			/// </summary>
			private TextBlock		virtualKeyBlock;
			/// <summary>
			/// Dans le cas où la Clé a été trouvé alors
			/// cet attribut représente la chaine de caractères
			/// utilisé lors de la recherche.
			/// </summary>
			private KeyString		string;
			/// <summary>
			/// Bloc virtuel qui représente la Clé
			/// de base de l'instance en cours. Cet attribut
			/// est utilisé lors de l'extraction des valeurs supplémentaires.
			/// </summary>
			private TextBlock		basicKeyBlock;
			/// <summary>
			/// Indique que la Clé est valide.
			/// </summary>
			private boolean				isValidKey = true;
			/// <summary>
			/// Valeur de la Clé après une recherche.
			/// </summary>
			private List<TextBlock>		valueBlocks;
			/// <summary>
			/// Bloc virtuel qui représente la liste
			/// des blocs de la valeur.
			/// </summary>
			private TextBlock		virtualValueBlock;
			/// <summary>
			/// Bloc texte qui a été utilisé comme bloc de base
			/// pour extraire les blocs de la valeur.
			/// </summary>
			private TextBlock		valueBaseBlock;
			/// <summary>
			/// Poids de la valeur trouvé.
			/// </summary>
			private float				weight;
			/// <summary>
			/// Dans le cas où une valeur a été trouvée
			/// cet attribut définit la position relative de
			/// la valeur par rapport à la Clé.
			/// </summary>
			private PositionType position;
			/// <summary>
			/// Indique que la valeur d'une Clé est valide.
			/// </summary>
			private boolean				isValidValue;
			/// <summary>
			/// Objet qui représente la valeur de la Clé.
			/// En général cet attribut est rempli si
			/// la valeur de l'instance en cours <see cref="_ValueBlocks" />
			/// est valide.
			/// </summary>
			private Object				value;
			/// <summary>
			/// Liste des Clés/valeurs supplémentaires
			/// associées à l'instance en cours.
			/// </summary>
			/// <remarks>
			/// Utilisez la méthode <see cref="GetSuppKeyValue ()" />
			/// pour obtenir une instance supplémentaire
			/// de l'instance en cours.
			/// </remarks>
			private List<KeyValue>		suppKeyValues;
			public Key getKey() {
				return key;
			}
			public List<TextBlock> getKeyBlocks() {
				return keyBlocks;
			}
			public TextBlock getVirtualKeyBlock() {
				return virtualKeyBlock;
			}
			public KeyString getString() {
				return string;
			}
			public TextBlock getBasicKeyBlock() {
				return basicKeyBlock;
			}
			public boolean isValidKey() {
				return isValidKey;
			}
			public List<TextBlock> getValueBlocks() {
				return valueBlocks;
			}
			public TextBlock getVirtualValueBlock() {
				return virtualValueBlock;
			}
			public TextBlock getValueBaseBlock() {
				return valueBaseBlock;
			}
			public float getWeight() {
				return weight;
			}
			public PositionType getPosition() {
				return position;
			}
			public boolean isValidValue() {
				return isValidValue;
			}
			public Object getValue() {
				return value;
			}
			public List<KeyValue> getSuppKeyValues() {
				return suppKeyValues;
			}
			public void setKey(Key key) {
				this.key = key;
			}
			public void setKeyBlocks(List<TextBlock> keyBlocks) {
				this.keyBlocks = keyBlocks;
			}
			public void setVirtualKeyBlock(TextBlock virtualKeyBlock) {
				this.virtualKeyBlock = virtualKeyBlock;
			}
			public void setString(KeyString string) {
				this.string = string;
			}
			public void setBasicKeyBlock(TextBlock basicKeyBlock) {
				this.basicKeyBlock = basicKeyBlock;
			}
			public void setValidKey(boolean isValidKey) {
				this.isValidKey = isValidKey;
			}

			public void setValueBlocks(List<TextBlock> valueBlocks) {

				this.valueBlocks = valueBlocks;

				if (valueBlocks != null && valueBlocks.size() > 0) {
					this.virtualValueBlock = new TextBlock(valueBlocks);
				}
				else
				{
					this.virtualValueBlock = null;
					this.valueBaseBlock = null;
					this.weight = -1;
				}
			}
			public void setVirtualValueBlock(TextBlock virtualValueBlock) {
				this.virtualValueBlock = virtualValueBlock;
			}
			public void setValueBaseBlock(TextBlock valueBaseBlock) {
				this.valueBaseBlock = valueBaseBlock;
			}
			public void setWeight(float weight) {
				this.weight = weight;
			}
			public void setPosition(PositionType position) {
				this.position = position;
			}
			public void setValidValue(boolean isValidValue) {
				this.isValidValue = isValidValue;
			}
			public void setValue(Object value) {
				this.value = value;
			}
			public void setSuppKeyValues(List<KeyValue> suppKeyValues) {
				this.suppKeyValues = suppKeyValues;
			}
			
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="key">Clé objet de la recherche.</param>
			/// <param name="keyBlocks">Blocs où la Clé a été trouvé.</param>
			/// <param name="str">Dans le cas où la Clé a été trouvé alors
			/// la chaine de caractères utilisé lors de la recherche.</param>
			/// <param name="basicKeyBlock">Bloc virtuel qui représente la Clé
			/// de base de l'instance en cours.</param>
			public KeyValue (Key key, List<TextBlock> keyBlocks, KeyString str, TextBlock basicKeyBlock){
				
				this.key = key;
				this.keyBlocks = keyBlocks;

				if (keyBlocks != null)
					this.virtualKeyBlock = new TextBlock (keyBlocks);

				this.string = str;

				if (str != null)
					this.isValidKey = !str.isExcepted();

				if (basicKeyBlock == null)
					this.basicKeyBlock = this.virtualKeyBlock;
				else
					this.basicKeyBlock = basicKeyBlock;
			}
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="key">Clé objet de la recherche.</param>
			/// <param name="keyBlocks">Blocs où la Clé a été trouvé.</param>
			/// <param name="str">Dans le cas où la Clé a été trouvé alors
			/// la chaine de caractères utilisé lors de la recherche.</param>
			public KeyValue (Key key, List<TextBlock> keyBlocks, KeyString str){
				
				this (key, keyBlocks, str, null);
			}
			
			/// <summary>
			/// Obtient une instance supplémentaire utilisée
			/// comme étant une Clé/valeur associée.
			/// </summary>
			/// <param name="keyBlocks">Liste des blocs de la Clé.</param>
			/// <returns>Instance de type en cours.</returns>
			public KeyValue getSuppKeyValue (List<TextBlock> keyBlocks)
			{return new KeyValue (this.key, keyBlocks, this.string, this.basicKeyBlock);}
			/// <summary>
			/// Obtient une instance supplémentaire utilisée
			/// comme étant une Clé/valeur associée. Les blocs
			/// de la Clé de l'instance en cours seront utilisé comme
			/// blocs de la Clé/valeur supplémentaire.
			/// </summary>
			/// <returns>Instance de type en cours.</returns>
			public KeyValue getSuppKeyValue ()
			{return getSuppKeyValue (this.keyBlocks);}
			
			/// <summary>
			/// Retourne la longueur de la Clé/valeur
			/// en cours. Cette méthode calcule la somme
			/// des Clés utilisées et des valeurs trouvées.
			/// </summary>
			/// <param name="minLength">Longueur minimale à partir de laquelle une chaine de caractères sera comptée.</param>
			/// <param name="charsDictionary">Dictionnaire qui contient les caractères utilisés.</param>
			/// <returns>Entier qui représente la longueur de l'instance en cours.</returns>
			public int getLength (int minLength, Map<Character,Object> charsDictionary)
			{
				int			length = 0;

				if (this.virtualKeyBlock != null)
				{
					if (this.virtualKeyBlock.getText().length() >= minLength)
					{
		                CharacterIterator it = new StringCharacterIterator(this.virtualKeyBlock.getText());
		                while(it.current() != CharacterIterator.DONE)
		                {
		                    if (!charsDictionary.containsKey (it.current()))
		                    	
		                    	charsDictionary.put(it.current(), null);
		                    length ++;
		                    it.next();
		                }
		                
				
					}
				}

				if (this.virtualValueBlock != null)
				{
					if (this.virtualValueBlock.getText().length() >= minLength)
					{
		                CharacterIterator it = new StringCharacterIterator(this.virtualKeyBlock.getText());
		                while(it.current() != CharacterIterator.DONE)
		                {
		                    if (!charsDictionary.containsKey (it.current()))
		                    	
		                    	charsDictionary.put(it.current(), null);
		                    length ++;
		                    it.next();
		                }
						
					}
				}

				return length;
			}
			
			
			/// <summary>
			/// Retourne la longueur de la Clé/valeur
			/// en cours. Cette méthode calcule la somme
			/// des Clés utilisées et des valeurs trouvées.
			/// </summary>
			/// <param name="charsDictionary">Dictionnaire qui contient les caractères utilisés.</param>
			/// <returns>Entier qui représente la longueur de l'instance en cours.</returns>
			public int getLength (Map<Character,Object> charsDictionary)
			{return getLength (0, charsDictionary);}
			/// <summary>
			/// Retourne la longueur totale des instances qui se trouvent
			/// dans le paramètre <paramref name="keyValues" />.
			/// </summary>
			/// <param name="keyValues">Tableau du type en cours.</param>
			/// <param name="minLength">Longueur minimale à partir de laquelle une chaine de caractères sera comptée.</param>
			/// <param name="charsDictionary">Dictionnaire qui contient les caractères utilisés.</param>
			/// <returns>Entier qui représente la somme des longueurs.</returns>
			public static int getLength (List<KeyValue> keyValues, int minLength, Map<Character,Object> charsDictionary)
			{
				int			length = 0;

				for(KeyValue keyValue : keyValues)
				{length += keyValue.getLength (minLength, charsDictionary);}

				return length;
			}
			/// <summary>
			/// Retourne la longueur totale des instances qui se trouvent
			/// dans le paramêtre <paramref name="keyValues" />.
			/// </summary>
			/// <param name="keyValues">Tableau du type en cours.</param>
			/// <param name="charsDictionary">Dictionnaire qui contient les caractères utilisés.</param>
			/// <returns>Entier qui représente la somme des longueurs.</returns>
			public static int getLength (List<KeyValue> keyValues, Map<Character,Object> charsDictionary)
			{return KeyValue.getLength (keyValues, 0, charsDictionary);}
			/// <summary>
			/// Fusionne deux tableaux de Clé/valeur.
			/// Les tableaux peuvent être nulls.
			/// </summary>
			/// <param name="keyValues1">Tableau 1 de <see cref="FacKeyValue" />.</param>
			/// <param name="keyValues2">Tableau 2 de <see cref="FacKeyValue" />.</param>
			/// <returns>Tableau résultat de la fusion.</returns>
			public static List<KeyValue> Merge (List<KeyValue> keyValues1, List<KeyValue> keyValues2)
			{
				if (keyValues1 == null && keyValues2 == null)
					return new ArrayList<>();
				else if (keyValues1 == null)
					return keyValues2;
				else if (keyValues2 == null)
					return keyValues1;
				else
				{
					List<KeyValue> mergedKeyValues =new ArrayList<>();
					mergedKeyValues.addAll(keyValues1);
					mergedKeyValues.addAll(keyValues2);
					

					return mergedKeyValues;
				}
			}
}
