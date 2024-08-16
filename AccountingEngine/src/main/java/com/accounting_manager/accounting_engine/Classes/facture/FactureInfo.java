package com.accounting_manager.accounting_engine.Classes.facture;

import com.accounting_manager.accounting_engine.Classes.key.KeyValue;
import lombok.Getter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
@Getter
public class FactureInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7133884585912400289L;
	
			/// <summary>
			/// L'instance de la facture associée à l'instance
			/// en cours.
			/// </summary>
			private Facture			facture;
			/// <summary>
			/// Indique que la facture a un numéro.
			/// </summary>
			private boolean			hasNumeroFacture;
			/// <summary>
			/// Numéro de la facture.
			/// </summary>
			private String				numeroFacture;
			/// <summary>
			/// L'instance de <see cref="KeyValue" />
			/// qui a été utilisée pour extraire
			/// le numéro de la facture.
			/// </summary>
			private KeyValue numeroFactureKeyValue;
			/// <summary>
			/// Indique que la facture a un numéro de TVA.
			/// </summary>
			private boolean				hasNumeroTva;
			/// <summary>
			/// Numéro TVA.
			/// </summary>
			private String				numeroTva;
			/// <summary>
			/// L'instance de <see cref="KeyValue" />
			/// qui a été utilisée pour extraire
			/// le numéro TVA.
			/// </summary>
			private KeyValue			numeroTvaKeyValue;
			/// <summary>
			/// Indique que la facture a un numéro siret.
			/// </summary>
			private boolean				hasNumeroSiret;
			/// <summary>
			/// Numéro Siret.
			/// </summary>
			private String				numeroSiret;
			/// <summary>
			/// L'instance de <see cref="KeyValue" />
			/// qui a été utilisée pour extraire
			/// le numéro Siret.
			/// </summary>
			private KeyValue			numeroSiretKeyValue;
			/// <summary>
			/// Indique que la facture a un numéro rcs.
			/// </summary>
			private boolean				hasNumeroRcs;
			/// <summary>
			/// Numéro rcs.
			/// </summary>
			private String				numeroRcs;
			/// <summary>
			/// L'instance de <see cref="KeyValue" />
			/// qui a été utilisée pour extraire
			/// le numéro rcs.
			/// </summary>
			private KeyValue			numeroRcsKeyValue;
			/// <summary>
			/// Indique que la facture a un numéro de page.
			/// </summary>
			private boolean				hasNumeroPage;
			/// <summary>
			/// Numéro de la page.
			/// </summary>
			private Long					numeroPage;
			/// <summary>
			/// L'instance de <see cref="KeyValue" />
			/// qui a été utilisée pour extraire
			/// le numéro de la page.
			/// </summary>
			private KeyValue			numeroPageKeyValue;
			/// <summary>
			/// Indique que la facture a un total TTC.
			/// </summary>
			private boolean				hasTtc;
			/// <summary>
			/// Total TTC.
			/// </summary>
			private Double				ttc;
			/// <summary>
			/// L'instance de <see cref="KeyValue" />
			/// qui a été utilisée pour extraire
			/// le total TTC.
			/// </summary>
			private KeyValue			ttcKeyValue;
			/// <summary>
			/// Indique que la facture a un montant HT.
			/// </summary>
			private boolean				hasHt;
			/// <summary>
			/// Montant HT.
			/// </summary>
			private Double				ht;
			/// <summary>
			/// L'instance de <see cref="KeyValue" />
			/// qui a été utilisée pour extraire
			/// le montant HT.
			/// </summary>
			private KeyValue			htKeyValue;
			/// <summary>
			/// Indique que la facture a un montant de TVA.
			/// </summary>
			private boolean				hasTva;
			/// <summary>
			/// Montant TVA.
			/// </summary>
			private Double				tva;
			/// <summary>
			/// L'instance de <see cref="KeyValue" />
			/// qui a été utilisée pour extraire
			/// le montant TVA.
			/// </summary>
			private KeyValue			tvaKeyValue;
			/// <summary>
			/// Indique que la facture a un montant
			/// de TVA supplèmentaire.
			/// </summary>
			private boolean				hasTvaSupp;
			/// <summary>
			/// Montant TVA supplèmentaire.
			/// </summary>
			private Double				tvaSupp;
			/// <summary>
			/// L'instance de <see cref="KeyValue" />
			/// qui a été utilisée pour extraire
			/// le montant TVA supplèmentaire.
			/// </summary>
			private KeyValue			tvaSuppKeyValue;
			/// <summary>
			/// Indique que la facture a un montant de remise.
			/// </summary>
			private boolean				hasDiscount;
			/// <summary>
			/// Montant remise.
			/// </summary>
			private Double				discount;
			/// <summary>
			/// L'instance de <see cref="KeyValue" />
			/// qui a été utilisée pour extraire
			/// le montant remise.
			/// </summary>
			private KeyValue			discountKeyValue;
			/// <summary>
			/// Indique que la facture a une date.
			/// </summary>
			private boolean				hasDate;
			/// <summary>
			/// Date de la facture.
			/// </summary>
			private Date			date;
			/// <summary>
			/// L'instance de <see cref="KeyValue" />
			/// qui a été utilisée pour extraire
			/// la date de la facture.
			/// </summary>
			private KeyValue			dateKeyValue;
			/// <summary>
			/// Indique que la facture a un type.
			/// </summary>
			private boolean				hasTypeFacture;
			/// <summary>
			/// Type de la facture. True si la facture
			/// est un avoir.
			/// </summary>
			private boolean				typeFacture;
			/// <summary>
			/// L'instance de <see cref="KeyValue" />
			/// qui a été utilisée pour extraire
			/// le type de la facture.
			/// </summary>
			private KeyValue			typeFactureKeyValue;
			/// <summary>
			/// Indique que les informations de la facture
			/// sont des détails. Ceci permet d'ingoner
			/// les informations de l'instance en cours.
			/// </summary>
			private boolean				isDetails;
			/// <summary>
			/// Liste des valeurs utilisées lors
			/// de l'extraction du numéro de la facture.
			/// </summary>
			private List<KeyValue>		numeroFactureUsedKeyValues;
			/// <summary>
			/// Liste des valeurs utilisées lors
			/// de l'extraction du numéro de TVA.
			/// </summary>
			private List<KeyValue>		numeroTvaUsedKeyValues;
			/// <summary>
			/// Liste des valeurs utilisées lors
			/// de l'extraction du numéro de siret.
			/// </summary>
			private List<KeyValue>		numeroSiretKeyValues;
			/// <summary>
			/// Liste des valeurs utilisées lors
			/// de l'extraction du numéro RCS.
			/// </summary>
			private List<KeyValue>		numeroRcsKeyValues;
			/// <summary>
			/// Liste des valeurs utilisées lors
			/// de l'extraction du numéro de la page.
			/// </summary>
			private List<KeyValue>		numeroPageKeyValues;
			/// <summary>
			/// Liste des valeurs utilisées lors
			/// de l'extraction du montant HT.
			/// </summary>
			private List<KeyValue>		htUsedKeyValues;
			/// <summary>
			/// Liste des valeurs utilisées lors
			/// de l'extraction du montant TVA.
			/// </summary>
			private List<KeyValue>		tvaUsedKeyValues;
			/// <summary>
			/// Liste des valeurs utilisées lors
			/// de l'extraction du montant TVA supplèmentaire.
			/// </summary>
			private List<KeyValue>		tvaSuppUsedKeyValues;
			/// <summary>
			/// Liste des valeurs utilisées lors
			/// de l'extraction du montant TTC.
			/// </summary>
			private List<KeyValue>		ttcUsedKeyValues;
			/// <summary>
			/// Liste des valeurs utilisées lors
			/// de l'extraction du montant remise.
			/// </summary>
			private List<KeyValue>		discountUsedKeyValues;
			/// <summary>
			/// Liste des valeurs utilisées lors
			/// de l'extraction de la date de la facture.
			/// </summary>
			private List<KeyValue>		dateUsedKeyValues;
			/// <summary>
			/// Liste des valeurs utilisées lors
			/// de l'extraction du type de la facture.
			/// </summary>
			private List<KeyValue>		typeFactureUsedKeyValues;
			/// <summary>
			/// Longueur minimale d'une chaine de caractères
			/// de type 1.
			/// </summary>
			public static int			STRONG_REJECTION_TYPE1_STR_MIN_LENGTH = 3;
			/// <summary>
			/// Longueur minimale du type 1
			/// au dessus de la quelle la facture
			/// est considérée comme fortement rejétée.
			/// </summary>
			public static int			STRONG_REJECTION_TYPE1_MIN_LENGTH = 10;
			/// <summary>
			/// Longueur minimale d'une chaine de caractères
			/// de type 2.
			/// </summary>
			public static int			STRONG_REJECTION_TYPE2_STR_MIN_LENGTH = 2;
			/// <summary>
			/// Longueur minimale du type 2
			/// au dessus de la quelle la facture
			/// est considérée comme fortement rejétée.
			/// </summary>
			public static int			STRONG_REJECTION_TYPE2_MIN_LENGTH = 15;

			public void setFacture(Facture facture) {
				this.facture = facture;
			}
			public void setHasNumeroFacture(boolean hasNumeroFacture) {
				this.hasNumeroFacture = hasNumeroFacture;
			}
			public void setNumeroFacture(String numeroFacture) {
				this.numeroFacture = numeroFacture;
			}
			public void setNumeroFactureKeyValue(KeyValue numeroFactureKeyValue) {
				this.numeroFactureKeyValue = numeroFactureKeyValue;
				this.numeroFacture = (String)this.numeroFactureKeyValue.getValue();
				this.hasNumeroFacture = true;
			}
			public void setHasNumeroTva(boolean hasNumeroTva) {
				this.hasNumeroTva = hasNumeroTva;
			}
			public void setNumeroTva(String numeroTva) {
				this.numeroTva = numeroTva;
			}
			public void setNumeroTvaKeyValue(KeyValue numeroTvaKeyValue) {
				this.numeroTvaKeyValue = numeroTvaKeyValue;
				this.numeroTva = (String)this.numeroTvaKeyValue.getValue();
				this.hasNumeroTva = true;
			}
			public void setHasNumeroSiret(boolean hasNumeroSiret) {
				this.hasNumeroSiret = hasNumeroSiret;
			}
			public void setNumeroSiret(String numeroSiret) {
				this.numeroSiret = numeroSiret;
			}
			public void setNumeroSiretKeyValue(KeyValue numeroSiretKeyValue) {
				this.numeroSiretKeyValue =  numeroSiretKeyValue;
				this.numeroSiret = (String)this.numeroSiretKeyValue.getValue();
				this.hasNumeroSiret = true;
			}
			public void setHasNumeroRcs(boolean hasNumeroRcs) {
				this.hasNumeroRcs = hasNumeroRcs;
			}
			public void setNumeroRcs(String numeroRcs) {
				this.numeroRcs = numeroRcs;
			}
			public void setNumeroRcsKeyValue(KeyValue numeroRcsKeyValue) {
				this.numeroRcsKeyValue = numeroRcsKeyValue;
				this.numeroRcs = (String)this.numeroRcsKeyValue.getValue();
				this.hasNumeroRcs = true;
			}
			public void setHasNumeroPage(boolean hasNumeroPage) {
				this.hasNumeroPage = hasNumeroPage;
			}

			public boolean hasNumeroPage() {
				return this.hasNumeroPage;
			}

			public void setNumeroPage(Long numeroPage) {
				this.numeroPage = numeroPage;
			}
			public void setNumeroPageKeyValue(KeyValue numeroPageKeyValue) {
				this.numeroPageKeyValue = numeroPageKeyValue;
				this.numeroPage = (Long) this.numeroPageKeyValue.getValue();
				this.hasNumeroPage = true;
			}
			public void setHasTtc(boolean hasTtc) {
				this.hasTtc = hasTtc;
			}
			public void setTtc(double ttc) {
				this.ttc = ttc;
			}
			public void setTtcKeyValue(KeyValue ttcKeyValue) {
				this.ttcKeyValue = ttcKeyValue;
				this.ttc = (double)this.ttcKeyValue.getValue();
				this.hasTtc = true;
			}
			public void setHasHt(boolean hasHt) {
				this.hasHt = hasHt;
			}
			public void setHt(double ht) {
				this.ht = ht;
				this.hasHt = true;
			}
			public void setHtKeyValue(KeyValue htKeyValue) {
				this.htKeyValue = htKeyValue;
				this.ht = (double)this.htKeyValue.getValue();
				this.hasHt = true;
			}
			public void setHasTva(boolean hasTva) {
				this.hasTva = hasTva;
			}
			public void setTva(double tva) {
				this.tva = tva;
				this.hasTva= true;
			}
			public void setTvaKeyValue(KeyValue tvaKeyValue) {
				this.tvaKeyValue = tvaKeyValue;
				this.tva = (double)this.tvaKeyValue.getValue();
				this.hasTva = true;
			}
			public void setHasTvaSupp(boolean hasTvaSupp) {
				this.hasTvaSupp = hasTvaSupp;
			}
			public void setTvaSupp(double tvaSupp) {
				this.tvaSupp = tvaSupp;
			}
			public void setTvaSuppKeyValue(KeyValue tvaSuppKeyValue) {
				this.tvaSuppKeyValue = tvaSuppKeyValue;
				this.tvaSupp = (double)this.tvaSuppKeyValue.getValue();
				this.hasTvaSupp = true;
			}
			public void setHasDiscount(boolean hasDiscount) {
				this.hasDiscount = hasDiscount;
			}
			public void setDiscount(double discount) {
				this.discount = discount;
			}
			public void setDiscountKeyValue(KeyValue discountKeyValue) {
				this.discountKeyValue = discountKeyValue;
				this.discount = (double)this.discountKeyValue.getValue();
				this.hasDiscount = true;
			}
			public void setHasDate(boolean hasDate) {
				this.hasDate = hasDate;
			}
			public void setDate(Date date) {
				this.date = date;
			}
			public void setDateKeyValue(KeyValue dateKeyValue) {
				this.dateKeyValue = dateKeyValue;
				this.date = (Date)this.dateKeyValue.getValue();
				this.hasDate = true;
			}
			public void setHasTypeFacture(boolean hasTypeFacture) {
				this.hasTypeFacture = hasTypeFacture;
			}
			public void setTypeFacture(boolean typeFacture) {
				this.typeFacture = typeFacture;
			}
			public void setTypeFactureKeyValue(KeyValue typeFactureKeyValue) {
				this.typeFactureKeyValue = typeFactureKeyValue;
				this.typeFacture = (boolean)this.typeFactureKeyValue.getValue();
				this.hasTypeFacture = true;
			}
			void setDetails(boolean isDetails) {
				this.isDetails = isDetails;
			}
			public void setNumeroFactureUsedKeyValues(List<KeyValue> numeroFactureUsedKeyValues) {
				this.numeroFactureUsedKeyValues = numeroFactureUsedKeyValues;
			}
			public void setNumeroTvaUsedKeyValues(List<KeyValue> numeroTvaUsedKeyValues) {
				this.numeroTvaUsedKeyValues = numeroTvaUsedKeyValues;
			}
			public void setNumeroSiretKeyValues(List<KeyValue> numeroSiretKeyValues) {
				this.numeroSiretKeyValues = numeroSiretKeyValues;
			}
			public void setNumeroRcsKeyValues(List<KeyValue> numeroRcsKeyValues) {
				this.numeroRcsKeyValues = numeroRcsKeyValues;
			}
			public void setNumeroPageKeyValues(List<KeyValue> numeroPageKeyValues) {
				this.numeroPageKeyValues = numeroPageKeyValues;
			}
			public void setHtUsedKeyValues(List<KeyValue> htUsedKeyValues) {
				this.htUsedKeyValues = htUsedKeyValues;
			}
			public void setTvaUsedKeyValues(List<KeyValue> tvaUsedKeyValues) {
				this.tvaUsedKeyValues = tvaUsedKeyValues;
			}
			public void setTvaSuppUsedKeyValues(List<KeyValue> tvaSuppUsedKeyValues) {
				this.tvaSuppUsedKeyValues = tvaSuppUsedKeyValues;
			}
			public void setTtcUsedKeyValues(List<KeyValue> ttcUsedKeyValues) {
				this.ttcUsedKeyValues = ttcUsedKeyValues;
			}
			public void setDiscountUsedKeyValues(List<KeyValue> discountUsedKeyValues) {
				this.discountUsedKeyValues = discountUsedKeyValues;
			}
			public void setDateUsedKeyValues(List<KeyValue> dateUsedKeyValues) {
				this.dateUsedKeyValues = dateUsedKeyValues;
			}
			public void setTypeFactureUsedKeyValues(List<KeyValue> typeFactureUsedKeyValues) {
				this.typeFactureUsedKeyValues = typeFactureUsedKeyValues;
			}
			/// <summary>
			/// Obtient
	        /// une valeur indiquant que les informations
			/// de la facture sont valides.
			/// </summary>
			public boolean				isValid()
			{
				
					return
						(this.hasNumeroTva || this.hasNumeroSiret || this.hasNumeroRcs) &&
						this.hasTtc && this.hasHt && this.hasTva &&
						this.hasTypeFacture;
				
			}
			/// <summary>
			/// Obtient
	        /// une valeur indiquant que les informations
			/// de la facture sont rejetées.
			/// </summary>
			public boolean					isRejected()
			{
				
				return !this.isValid();
			
			}
			/// <summary>
			/// Obtient
	        /// une valeur indiquant que les informations
			/// de la facture sont totalement valides.
			/// Une facture est totale valide si tous
			/// les valeurs ont été trouvées.
			/// </summary>
			public boolean					isFullValid()
			{
				
					return
						this.hasNumeroFacture && this.hasNumeroTva && this.hasNumeroSiret &&
						this.hasNumeroRcs && this.hasTtc && this.hasHt && this.hasTva &&
						this.hasDate && this.hasTypeFacture;
				
			}
			
			/// <summary>
			/// Obtient
	        /// une valeur indiquant que les informations
			/// de la facture sont totalement rejetées.
			/// Une facture est totale rejetée si au moins
			/// une valeur n'a pas été trouvée.
			/// </summary>
			public boolean					isFullRejected()
			{
				return !this.isFullValid();
				}
			/// <summary>
			/// Obtient
	        /// une valeur indiquant que les informations
			/// de type 1 (Numéros du fournisseur)
			/// de la facture sont fortement rejetées.
			/// </summary>
			public boolean					isType1StronglyRejected()
			{
				
					Map<Character,Object>		charsDictionary = new HashMap<Character,Object> ();

					return
						!this.hasNumeroTva && !this.hasNumeroSiret && !this.hasNumeroRcs && (
						KeyValue.getLength (this.numeroSiretKeyValues, FactureInfo.STRONG_REJECTION_TYPE1_STR_MIN_LENGTH, charsDictionary) +
						KeyValue.getLength (this.numeroRcsKeyValues, FactureInfo.STRONG_REJECTION_TYPE1_STR_MIN_LENGTH, charsDictionary)) < FactureInfo.STRONG_REJECTION_TYPE1_MIN_LENGTH;
				
			}
			/// <summary>
			/// Obtient
	        /// une valeur indiquant que les informations
			/// de type 2 de la facture sont fortement rejetées.
			/// </summary>
			public boolean					isType2StronglyRejected()
			{
				
					Map<Character,Object>		charsDictionary = new HashMap<Character,Object> ();

					return (
						KeyValue.getLength (this.numeroFactureUsedKeyValues, FactureInfo.STRONG_REJECTION_TYPE2_STR_MIN_LENGTH, charsDictionary) +
						KeyValue.getLength (this.numeroPageKeyValues, FactureInfo.STRONG_REJECTION_TYPE2_STR_MIN_LENGTH, charsDictionary) +
						KeyValue.getLength (this.htUsedKeyValues, FactureInfo.STRONG_REJECTION_TYPE2_STR_MIN_LENGTH, charsDictionary) +
						KeyValue.getLength (this.tvaUsedKeyValues, FactureInfo.STRONG_REJECTION_TYPE2_STR_MIN_LENGTH, charsDictionary) +
						(this.tvaSuppUsedKeyValues == null ? 0 : KeyValue.getLength (this.tvaSuppUsedKeyValues, FactureInfo.STRONG_REJECTION_TYPE2_STR_MIN_LENGTH, charsDictionary)) +
						KeyValue.getLength (this.ttcUsedKeyValues, FactureInfo.STRONG_REJECTION_TYPE2_STR_MIN_LENGTH, charsDictionary) +
						KeyValue.getLength (this.discountUsedKeyValues, FactureInfo.STRONG_REJECTION_TYPE2_STR_MIN_LENGTH, charsDictionary) +
						KeyValue.getLength (this.dateUsedKeyValues, FactureInfo.STRONG_REJECTION_TYPE2_STR_MIN_LENGTH, charsDictionary) +
						KeyValue.getLength (this.typeFactureUsedKeyValues, FactureInfo.STRONG_REJECTION_TYPE2_STR_MIN_LENGTH, charsDictionary)) < FactureInfo.STRONG_REJECTION_TYPE2_MIN_LENGTH;
				}
			/// <summary>
			/// Obtient
	        /// une valeur indiquant que la facture
			/// est partiellement rejétée.
			/// </summary>
			public boolean					isPartialRejected()
			{
				return !this.hasHt || !this.hasTva || !this.hasTtc;
				
			}
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="facture">L'instance de la facture associée à l'instance
			/// en cours.</param>
			public FactureInfo(Facture facture)
			{this.facture = facture;}

			/// <summary>
			/// Crée une instance de la classe en cours à partir
			/// d'un noeud XML.
			/// </summary>
			/// <param name="node">Noued racine dont les enfants contiennent les informations sur une facture.</param>
			/// <returns>Une instance de la classe en cours.</returns>
			public static FactureInfo FromXmlNode (Node node) throws Exception
			{
				FactureInfo	info = new FactureInfo (null);
				NodeList childNodes = node.getChildNodes();

				List<Node> nodeList = IntStream.range(0, childNodes.getLength())
						.mapToObj(childNodes::item)
						.collect(Collectors.toList());		
				
					 for (Node childNode : nodeList)
					 {	
					 
					if (! (childNode.getTextContent().isEmpty()|| childNode==null ))
					{
						switch (childNode.getNodeName())
						{
							case "numeroFacture" :
							{
								info.numeroFacture = childNode.getTextContent();
								break;
							}
							case "numeroTva" :
							{
								info.numeroTva = childNode.getTextContent();
								break;
							}
							case "numeroSiret" :
							{
								info.numeroSiret = childNode.getTextContent();
								break;
							}
							case "numeroRcs" :
							{
								info.numeroRcs = childNode.getTextContent();
								break;
							}
							case "ttc" :
							{
								info.ttc = Double.parseDouble(childNode.getTextContent().replaceAll(".", ","));
								break;
							}
							case "ht" :
							{
								info.ht = Double.parseDouble (childNode.getTextContent().replaceAll(".", ","));
								break;
							}
							case "tva" :
							{
								info.tva = Double.parseDouble (childNode.getTextContent().replaceAll(".", ","));
								break;
							}
							case "date" :
							{
								info.date = new SimpleDateFormat("dd/MM/yyyy").parse (childNode.getTextContent());
								break;
							}
							case "typeFacture" :
							{
								info.typeFacture = Boolean.parseBoolean (childNode.getTextContent());
								break;
							}
							default :
							{
								throw new Exception ("Unknown facture info type '" + childNode.getNodeName() + "'");
							}
						}
					}
				}

				return info;
			}
			
			/// <summary>
			/// Retourne la liste totale des clés/valeurs
			/// utilisées.
			/// </summary>
			/// <returns>Tableau de type <see cref="FacKeyValue" />.</returns>
			public List<KeyValue> GetAllUsedKeyValues ()
			{
				List<KeyValue>		allUsedKeyValues = new ArrayList<KeyValue> ();

				if (this.numeroFactureUsedKeyValues != null)
					allUsedKeyValues.addAll(this.numeroFactureUsedKeyValues);

				if (this.numeroTvaUsedKeyValues != null)
					allUsedKeyValues.addAll(this.numeroTvaUsedKeyValues);

				if (this.numeroSiretKeyValues != null)
					allUsedKeyValues.addAll(this.numeroSiretKeyValues);

				if (this.numeroRcsKeyValues != null)
					allUsedKeyValues.addAll(this.numeroRcsKeyValues);

				if (this.numeroPageKeyValues != null)
					allUsedKeyValues.addAll(this.numeroPageKeyValues);

				if (this.htUsedKeyValues != null)
					allUsedKeyValues.addAll(this.htUsedKeyValues);

				if (this.tvaUsedKeyValues != null)
					allUsedKeyValues.addAll(this.tvaUsedKeyValues);

				if (this.tvaSuppUsedKeyValues != null)
					allUsedKeyValues.addAll(this.tvaSuppUsedKeyValues);

				if (this.ttcUsedKeyValues != null)
					allUsedKeyValues.addAll(this.ttcUsedKeyValues);

				if (this.discountUsedKeyValues != null)
					allUsedKeyValues.addAll(this.discountUsedKeyValues);

				if (this.dateUsedKeyValues != null)
					allUsedKeyValues.addAll(this.dateUsedKeyValues);

				if (this.typeFactureUsedKeyValues != null)
					allUsedKeyValues.addAll(this.typeFactureUsedKeyValues);

				return allUsedKeyValues;
			}
			/// <summary>
			/// Dévalide les instances des clés/valeurs excepté celle
			/// qui a été choisie.
			/// </summary>
			public void UnvalidKeyValues ()
			{
				FactureInfo.unvalidKeyValues (this.numeroFactureUsedKeyValues, this.numeroFactureKeyValue);
				FactureInfo.unvalidKeyValues (this.numeroTvaUsedKeyValues, this.numeroTvaKeyValue);
				FactureInfo.unvalidKeyValues (this.numeroSiretKeyValues, this.numeroSiretKeyValue);
				FactureInfo.unvalidKeyValues (this.numeroRcsKeyValues, this.numeroRcsKeyValue);
				FactureInfo.unvalidKeyValues (this.numeroPageKeyValues, this.numeroPageKeyValue);
				FactureInfo.unvalidKeyValues (this.htUsedKeyValues, this.htKeyValue);
				FactureInfo.unvalidKeyValues (this.tvaUsedKeyValues, this.tvaKeyValue);
				FactureInfo.unvalidKeyValues (this.tvaSuppUsedKeyValues, this.tvaSuppKeyValue);
				FactureInfo.unvalidKeyValues (this.ttcUsedKeyValues, this.ttcKeyValue);
				FactureInfo.unvalidKeyValues (this.discountUsedKeyValues, this.discountKeyValue);
				FactureInfo.unvalidKeyValues (this.dateUsedKeyValues, this.dateKeyValue);
				FactureInfo.unvalidKeyValues (this.typeFactureUsedKeyValues, this.typeFactureKeyValue);
			}
			/// <summary>
			/// Dévalide les instances des clés/valeurs différentes
			/// par rapport à une instance valide.
			/// </summary>
			/// <param name="allKeyValues">Liste de toutes les instances clé/valeurs utilisées.</param>
			/// <param name="validKeyValue">L'instance de la clé/valeur valide.</param>
			private static void unvalidKeyValues (List<KeyValue> allKeyValues, KeyValue validKeyValue)
			{
				if (allKeyValues != null)
				{
					for(KeyValue keyValue : allKeyValues)
					{
						if (validKeyValue == null || keyValue != validKeyValue)
							keyValue.setValidValue(false);
					}
				}
			}
}
