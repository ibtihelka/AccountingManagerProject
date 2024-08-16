package facture;

import java.io.Serializable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import key.KeyValue;

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
			private KeyValue			numeroFactureKeyValue;
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
			private int					numeroPage;
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
			private double				ttc;
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
			private double				ht;
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
			private double				tva;
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
			private double				tvaSupp;
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
			private double				discount;
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
			private KeyValue []		numeroFactureUsedKeyValues;
			/// <summary>
			/// Liste des valeurs utilisées lors
			/// de l'extraction du numéro de TVA.
			/// </summary>
			private KeyValue []		numeroTvaUsedKeyValues;
			/// <summary>
			/// Liste des valeurs utilisées lors
			/// de l'extraction du numéro de siret.
			/// </summary>
			private KeyValue []		numeroSiretKeyValues;
			/// <summary>
			/// Liste des valeurs utilisées lors
			/// de l'extraction du numéro RCS.
			/// </summary>
			private KeyValue []		numeroRcsKeyValues;
			/// <summary>
			/// Liste des valeurs utilisées lors
			/// de l'extraction du numéro de la page.
			/// </summary>
			private KeyValue []		numeroPageKeyValues;
			/// <summary>
			/// Liste des valeurs utilisées lors
			/// de l'extraction du montant HT.
			/// </summary>
			private KeyValue []		htUsedKeyValues;
			/// <summary>
			/// Liste des valeurs utilisées lors
			/// de l'extraction du montant TVA.
			/// </summary>
			private KeyValue []		tvaUsedKeyValues;
			/// <summary>
			/// Liste des valeurs utilisées lors
			/// de l'extraction du montant TVA supplèmentaire.
			/// </summary>
			private KeyValue []		tvaSuppUsedKeyValues;
			/// <summary>
			/// Liste des valeurs utilisées lors
			/// de l'extraction du montant TTC.
			/// </summary>
			private KeyValue []		ttcUsedKeyValues;
			/// <summary>
			/// Liste des valeurs utilisées lors
			/// de l'extraction du montant remise.
			/// </summary>
			private KeyValue []		discountUsedKeyValues;
			/// <summary>
			/// Liste des valeurs utilisées lors
			/// de l'extraction de la date de la facture.
			/// </summary>
			private KeyValue []		dateUsedKeyValues;
			/// <summary>
			/// Liste des valeurs utilisées lors
			/// de l'extraction du type de la facture.
			/// </summary>
			private KeyValue []		typeFactureUsedKeyValues;
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
			public Facture getFacture() {
				return facture;
			}
			public boolean isHasNumeroFacture() {
				return hasNumeroFacture;
			}
			public String getNumeroFacture() {
				return numeroFacture;
			}
			public KeyValue getNumeroFactureKeyValue() {
				return numeroFactureKeyValue;
			}
			public boolean isHasNumeroTva() {
				return hasNumeroTva;
			}
			public String getNumeroTva() {
				return numeroTva;
			}
			public KeyValue getNumeroTvaKeyValue() {
				return numeroTvaKeyValue;
			}
			public boolean isHasNumeroSiret() {
				return hasNumeroSiret;
			}
			public String getNumeroSiret() {
				return numeroSiret;
			}
			public KeyValue getNumeroSiretKeyValue() {
				return numeroSiretKeyValue;
			}
			public boolean isHasNumeroRcs() {
				return hasNumeroRcs;
			}
			public String getNumeroRcs() {
				return numeroRcs;
			}
			public KeyValue getNumeroRcsKeyValue() {
				return numeroRcsKeyValue;
			}
			public boolean isHasNumeroPage() {
				return hasNumeroPage;
			}
			public int getNumeroPage() {
				return numeroPage;
			}
			public KeyValue getNumeroPageKeyValue() {
				return numeroPageKeyValue;
			}
			public boolean isHasTtc() {
				return hasTtc;
			}
			public double getTtc() {
				return ttc;
			}
			public KeyValue getTtcKeyValue() {
				return ttcKeyValue;
			}
			public boolean isHasHt() {
				return hasHt;
			}
			public double getHt() {
				return ht;
			}
			public KeyValue getHtKeyValue() {
				return htKeyValue;
			}
			public boolean isHasTva() {
				return hasTva;
			}
			public double getTva() {
				return tva;
			}
			public KeyValue getTvaKeyValue() {
				return tvaKeyValue;
			}
			public boolean isHasTvaSupp() {
				return hasTvaSupp;
			}
			public double getTvaSupp() {
				return tvaSupp;
			}
			public KeyValue getTvaSuppKeyValue() {
				return tvaSuppKeyValue;
			}
			public boolean isHasDiscount() {
				return hasDiscount;
			}
			public double getDiscount() {
				return discount;
			}
			public KeyValue getDiscountKeyValue() {
				return discountKeyValue;
			}
			public boolean isHasDate() {
				return hasDate;
			}
			public Date getDate() {
				return date;
			}
			public KeyValue getDateKeyValue() {
				return dateKeyValue;
			}
			public boolean isHasTypeFacture() {
				return hasTypeFacture;
			}
			public boolean isTypeFacture() {
				return typeFacture;
			}
			public KeyValue getTypeFactureKeyValue() {
				return typeFactureKeyValue;
			}
			public boolean isDetails() {
				return isDetails;
			}
			public KeyValue[] getNumeroFactureUsedKeyValues() {
				return numeroFactureUsedKeyValues;
			}
			public KeyValue[] getNumeroTvaUsedKeyValues() {
				return numeroTvaUsedKeyValues;
			}
			public KeyValue[] getNumeroSiretKeyValues() {
				return numeroSiretKeyValues;
			}
			public KeyValue[] getNumeroRcsKeyValues() {
				return numeroRcsKeyValues;
			}
			public KeyValue[] getNumeroPageKeyValues() {
				return numeroPageKeyValues;
			}
			public KeyValue[] getHtUsedKeyValues() {
				return htUsedKeyValues;
			}
			public KeyValue[] getTvaUsedKeyValues() {
				return tvaUsedKeyValues;
			}
			public KeyValue[] getTvaSuppUsedKeyValues() {
				return tvaSuppUsedKeyValues;
			}
			public KeyValue[] getTtcUsedKeyValues() {
				return ttcUsedKeyValues;
			}
			public KeyValue[] getDiscountUsedKeyValues() {
				return discountUsedKeyValues;
			}
			public KeyValue[] getDateUsedKeyValues() {
				return dateUsedKeyValues;
			}
			public KeyValue[] getTypeFactureUsedKeyValues() {
				return typeFactureUsedKeyValues;
			}
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
			public void setNumeroPage(int numeroPage) {
				this.numeroPage = numeroPage;
			}
			public void setNumeroPageKeyValue(KeyValue numeroPageKeyValue) {
				this.numeroPageKeyValue = numeroPageKeyValue;
				this.numeroPage = (int)this.numeroPageKeyValue.getValue();
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
			public void setDetails(boolean isDetails) {
				this.isDetails = isDetails;
			}
			public void setNumeroFactureUsedKeyValues(KeyValue[] numeroFactureUsedKeyValues) {
				this.numeroFactureUsedKeyValues = numeroFactureUsedKeyValues;
			}
			public void setNumeroTvaUsedKeyValues(KeyValue[] numeroTvaUsedKeyValues) {
				this.numeroTvaUsedKeyValues = numeroTvaUsedKeyValues;
			}
			public void setNumeroSiretKeyValues(KeyValue[] numeroSiretKeyValues) {
				this.numeroSiretKeyValues = numeroSiretKeyValues;
			}
			public void setNumeroRcsKeyValues(KeyValue[] numeroRcsKeyValues) {
				this.numeroRcsKeyValues = numeroRcsKeyValues;
			}
			public void setNumeroPageKeyValues(KeyValue[] numeroPageKeyValues) {
				this.numeroPageKeyValues = numeroPageKeyValues;
			}
			public void setHtUsedKeyValues(KeyValue[] htUsedKeyValues) {
				this.htUsedKeyValues = htUsedKeyValues;
			}
			public void setTvaUsedKeyValues(KeyValue[] tvaUsedKeyValues) {
				this.tvaUsedKeyValues = tvaUsedKeyValues;
			}
			public void setTvaSuppUsedKeyValues(KeyValue[] tvaSuppUsedKeyValues) {
				this.tvaSuppUsedKeyValues = tvaSuppUsedKeyValues;
			}
			public void setTtcUsedKeyValues(KeyValue[] ttcUsedKeyValues) {
				this.ttcUsedKeyValues = ttcUsedKeyValues;
			}
			public void setDiscountUsedKeyValues(KeyValue[] discountUsedKeyValues) {
				this.discountUsedKeyValues = discountUsedKeyValues;
			}
			public void setDateUsedKeyValues(KeyValue[] dateUsedKeyValues) {
				this.dateUsedKeyValues = dateUsedKeyValues;
			}
			public void setTypeFactureUsedKeyValues(KeyValue[] typeFactureUsedKeyValues) {
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
			public FactureInfo (Facture facture)
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
			public KeyValue [] GetAllUsedKeyValues ()
			{
				List<KeyValue>		allUsedKeyValues = new ArrayList<KeyValue> ();

				if (this.numeroFactureUsedKeyValues != null)
					Collections.addAll (allUsedKeyValues,this.numeroFactureUsedKeyValues);

				if (this.numeroTvaUsedKeyValues != null)
					Collections.addAll (allUsedKeyValues,this.numeroTvaUsedKeyValues);

				if (this.numeroSiretKeyValues != null)
					Collections.addAll (allUsedKeyValues,this.numeroSiretKeyValues);

				if (this.numeroRcsKeyValues != null)
					Collections.addAll (allUsedKeyValues,this.numeroRcsKeyValues);

				if (this.numeroPageKeyValues != null)
					Collections.addAll (allUsedKeyValues,this.numeroPageKeyValues);

				if (this.htUsedKeyValues != null)
					Collections.addAll (allUsedKeyValues,this.htUsedKeyValues);

				if (this.tvaUsedKeyValues != null)
					Collections.addAll (allUsedKeyValues,this.tvaUsedKeyValues);

				if (this.tvaSuppUsedKeyValues != null)
					Collections.addAll (allUsedKeyValues,this.tvaSuppUsedKeyValues);

				if (this.ttcUsedKeyValues != null)
					Collections.addAll (allUsedKeyValues,this.ttcUsedKeyValues);

				if (this.discountUsedKeyValues != null)
					Collections.addAll (allUsedKeyValues,this.discountUsedKeyValues);

				if (this.dateUsedKeyValues != null)
				Collections.addAll (allUsedKeyValues,this.dateUsedKeyValues);

				if (this.typeFactureUsedKeyValues != null)
					Collections.addAll (allUsedKeyValues,this.typeFactureUsedKeyValues);

				return (KeyValue[]) allUsedKeyValues.toArray ();
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
			private static void unvalidKeyValues (KeyValue [] allKeyValues, KeyValue validKeyValue)
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
