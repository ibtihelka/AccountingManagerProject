package key;

import java.io.Serializable;
import java.util.*;

public class KeyStringChar  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3662803815794672428L;
			/// <summary>
			/// Chaine de caractères qui représente
			/// le caractrère en cours.
			/// </summary>
			private String			string;
			/// <summary>
			/// Chaine de caractères majuscule
			/// qui représente le caractrère en cours.
			/// </summary>
			private String			upperString;
			/// <summary>
			/// Indique que le caractère doit être trouvé
			/// dans un bloc séparé.
			/// </summary>
			private boolean			isSingle;
			/// <summary>
			/// Indique que le caractère est optionnel.
			/// </summary>
			private boolean			isOptional;
			/// <summary>
			/// Indique que le caractère est partagé.
			/// Un caractère partagé influence le marquage
			/// des clés.
			/// </summary>
			private boolean			isShared;
			/// <summary>
			/// Indique que le caractère supporte
			/// une mauvaise qualtité de détection.
			/// </summary>
			private boolean			supportBadQuality;
			/// <summary>
			/// Taux qui permet de fixer la largeur
			/// minimale entre le caractère en cours
			/// et le caractère suivant.
			/// </summary>
			private float			minSeperatorWidth;
			/// <summary>
			/// Taux qui permet de fixer la largeur
			/// maximale entre le caractère en cours
			/// et le caractère suivant.
			/// </summary>
			private float			maxSeperatorWidth;
			/// <summary>
			/// Taux par défaut de la largeur
			/// minimale entre le caractère en cours
			/// et le caractère suivant.
			/// </summary>
			public static float		MIN_SEPERATOR_WIDTH = -1f;
			/// <summary>
			/// Taux par défaut de la largeur
			/// maximale entre le caractère en cours
			/// et le caractère suivant.
			/// </summary>
			public static float		MAX_SEPERATOR_WIDTH = 0.04f;
			/// <summary>
			/// Contient les caractères de remplacement
			/// dans le cas d'une mauvaise détection de l'OCR.
			/// </summary>
			 
			private static Map<String,List<String>>		_BadQualityChars = SetBadQualityChars ();
			
			
			public String getString() {
				return string;
			}
			public String getUpperString() {
				return upperString;
			}
			public boolean isSingle() {
				return isSingle;
			}
			public boolean isOptional() {
				return isOptional;
			}
			public boolean isShared() {
				return isShared;
			}
			public boolean isSupportBadQuality() {
				return supportBadQuality;
			}
			public float getMinSeperatorWidth() {
				return minSeperatorWidth;
			}
			public float getMaxSeperatorWidth() {
				return maxSeperatorWidth;
			}
			public void setString(String string) {
				this.string = string;
			}
			public void setUpperString(String upperString) {
				this.upperString = upperString;
			}
			public void setSingle(boolean isSingle) {
				this.isSingle = isSingle;
			}
			public void setOptional(boolean isOptional) {
				this.isOptional = isOptional;
			}
			public void setShared(boolean isShared) {
				this.isShared = isShared;
			}
			public void setSupportBadQuality(boolean supportBadQuality) {
				this.supportBadQuality = supportBadQuality;
			}
			public void setMinSeperatorWidth(float minSeperatorWidth) {
				this.minSeperatorWidth = minSeperatorWidth;
			}
			public void setMaxSeperatorWidth(float maxSeperatorWidth) {
				this.maxSeperatorWidth = maxSeperatorWidth;
			}
			
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="str">Chaine de caractères qui représente le caractrère en cours.</param>
			/// <param name="isSingle">Indique que le caractère doit être trouvé dans un bloc séparé.</param>
			/// <param name="isOptional">Indique que le caractère est optionnel.</param>
			/// <param name="isShared">Indique que le caractère est partagé.</param>
			/// <param name="supportBadQuality">Indique que le caractère supporte une mauvaise qualtité de détection.</param>
			/// <param name="minSeperatorWidth">Taux qui permet de fixer la largeur
			/// minimale entre le caractère en cours et le caractère suivant.</param>
			/// <param name="maxSeperatorWidth">Taux qui permet de fixer la largeur
			/// maximale entre le caractère en cours et le caractère suivant.</param>
			public KeyStringChar (String str, boolean isSingle, boolean isOptional, boolean isShared, boolean supportBadQuality, float minSeperatorWidth, float maxSeperatorWidth)
			{
				this.string = str;
				this.isSingle = isSingle;
				this.isOptional = isOptional;
				this.isShared = isShared;
				this.supportBadQuality = supportBadQuality;
				this.minSeperatorWidth = minSeperatorWidth;
				this.maxSeperatorWidth = maxSeperatorWidth;

				this.upperString = str.toUpperCase();
			}
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="str">Chaine de caractères qui représente le caractrère en cours.</param>
			/// <param name="isSingle">Indique que le caractère doit être trouvé dans un bloc séparé.</param>
			/// <param name="isOptional">Indique que le caractère est optionnel.</param>
			/// <param name="isShared">Indique que le caractère est partagé.</param>
			/// <param name="supportBadQuality">Indique que le caractère supporte une mauvaise qualtité de détection.</param>
			public KeyStringChar (String str, boolean isSingle, boolean isOptional, boolean isShared, boolean supportBadQuality) {
				
				 this (str, isSingle, isOptional, isShared, supportBadQuality, KeyStringChar.MIN_SEPERATOR_WIDTH, KeyStringChar.MAX_SEPERATOR_WIDTH);
			}
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="str">Chaine de caractères qui représente le caractrère en cours.</param>
			/// <param name="isSingle">Indique que le caractère doit être trouvé dans un bloc séparé.</param>
			/// <param name="isOptional">Indique que le caractère est optionnel.</param>
			/// <param name="isShared">Indique que le caractère est partagé.</param>
			public KeyStringChar (String str, boolean isSingle, boolean isOptional, boolean isShared){
				
				 this (str, isSingle, isOptional, isShared, true, KeyStringChar.MIN_SEPERATOR_WIDTH, KeyStringChar.MAX_SEPERATOR_WIDTH);
			}
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="str">Chaine de caractères qui représente le caractrère en cours.</param>
			/// <param name="isOptional">Indique que le caractère est optionnel.</param>
			/// <param name="isShared">Indique que le caractère est partagé.</param>
			public KeyStringChar (String str, boolean isOptional, boolean isShared){
				
				this (str, false, isOptional, isShared, true, KeyStringChar.MIN_SEPERATOR_WIDTH, KeyStringChar.MAX_SEPERATOR_WIDTH);
			}
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="str">Chaine de caractères qui représente le caractrère en cours.</param>
			/// <param name="isOptional">Indique que le caractère est optionnel.</param>
			public KeyStringChar (String str, boolean isOptional){
				
				this (str, false, isOptional, false, true, KeyStringChar.MIN_SEPERATOR_WIDTH, KeyStringChar.MAX_SEPERATOR_WIDTH);
			}
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="str">Chaine de caractères qui représente le caractrère en cours.</param>
			public KeyStringChar (String str){
				
				this(str, false, false, false, true, KeyStringChar.MIN_SEPERATOR_WIDTH, KeyStringChar.MAX_SEPERATOR_WIDTH);
			}
			
			
			
			
			/// <summary>
			/// Retourne la chaine de caractères qui représente
			/// l'instance en cours.
			/// </summary>
			/// <returns>Chaine de caractères de type <see cref="string" />.</returns>
			@Override
			public String toString (){
				
				return this.string;
				
			}
			
			
			
			/// <summary>
			/// Ajoute une mauvaise chaine de caractères à une chaine
			/// de caractères.
			/// </summary>
			/// <param name="badQualityChars">Liste des mauvaises chaines de caractères.</param>
			/// <param name="charString">Chaine de caractères.</param>
			/// <param name="badQualityChar">Mauvaise qualité d'une chaine de caractères.</param>
			private static void AddBadQualityChar (Map<String,List<String>> badQualityChars, String charString, String badQualityChar)
			{
				
				List<String> _badQualityChars = new ArrayList<String>();
				if(badQualityChars.containsKey(charString))
				{
					_badQualityChars=badQualityChars.get(charString);
				}
				    _badQualityChars.add(badQualityChar);
				
				
				badQualityChars.put(charString, _badQualityChars);
				
				
			}
			
			
			/// <summary>
			/// Met à jour la liste des chaines de caractères
			/// dans le cas de mauvaises détections.
			/// </summary>
			/// <returns>Tableau de clé/valeur des mauvaises détections.</returns>
			private static Map<String,List<String>> SetBadQualityChars ()
			{
				Map<String,List<String>>		badQualityChars = new HashMap<String,List<String>> ();

				KeyStringChar.AddBadQualityChar (badQualityChars, "°", "o");
				KeyStringChar.AddBadQualityChar (badQualityChars, "°", "0");
				KeyStringChar.AddBadQualityChar (badQualityChars, "°", "\"");
				KeyStringChar.AddBadQualityChar (badQualityChars, "°", "*");
				KeyStringChar.AddBadQualityChar (badQualityChars, "°", "'");
				KeyStringChar.AddBadQualityChar (badQualityChars, "siret", "sirent");
				KeyStringChar.AddBadQualityChar (badQualityChars, "siret", "s1ret");
				KeyStringChar.AddBadQualityChar (badQualityChars, "siret", "5iret");
				KeyStringChar.AddBadQualityChar (badQualityChars, "siret", "liret");
				KeyStringChar.AddBadQualityChar (badQualityChars, "siret", "sirft");
				KeyStringChar.AddBadQualityChar (badQualityChars, "siret", "sirbt");
				KeyStringChar.AddBadQualityChar (badQualityChars, "siret", "sihet");
				KeyStringChar.AddBadQualityChar (badQualityChars, "siret", "airet");
				KeyStringChar.AddBadQualityChar (badQualityChars, "siret", "sret");
				KeyStringChar.AddBadQualityChar (badQualityChars, "siret", "sket");
				KeyStringChar.AddBadQualityChar (badQualityChars, "date", "pate");
				KeyStringChar.AddBadQualityChar (badQualityChars, "communautaire", "cornmunautaire");
				KeyStringChar.AddBadQualityChar (badQualityChars, "intracom", "intracorn");
				KeyStringChar.AddBadQualityChar (badQualityChars, "intracommunautaire", "intracommuoautaire");
				KeyStringChar.AddBadQualityChar (badQualityChars, "payee", "pay6e");
				KeyStringChar.AddBadQualityChar (badQualityChars, "payee", "paver");
				KeyStringChar.AddBadQualityChar (badQualityChars, "montant", "mt");
				KeyStringChar.AddBadQualityChar (badQualityChars, "€", "eur");
				KeyStringChar.AddBadQualityChar (badQualityChars, "€", "euros");
				KeyStringChar.AddBadQualityChar (badQualityChars, "facture", "factur");
				KeyStringChar.AddBadQualityChar (badQualityChars, "facture", "acture");
				KeyStringChar.AddBadQualityChar (badQualityChars, "date", "datf");
				// Taux.
				KeyStringChar.AddBadQualityChar (badQualityChars, "12", "12.0");
				KeyStringChar.AddBadQualityChar (badQualityChars, "12", "12.00");
				KeyStringChar.AddBadQualityChar (badQualityChars, "12", "12,0");
				KeyStringChar.AddBadQualityChar (badQualityChars, "12", "12,00");
				KeyStringChar.AddBadQualityChar (badQualityChars, "5.5", "5,5");
				KeyStringChar.AddBadQualityChar (badQualityChars, "5.5", "5.50");
				KeyStringChar.AddBadQualityChar (badQualityChars, "5.5", "5,50");
				KeyStringChar.AddBadQualityChar (badQualityChars, "19.6", "19,6");
				KeyStringChar.AddBadQualityChar (badQualityChars, "19.6", "19.60");
				KeyStringChar.AddBadQualityChar (badQualityChars, "19.6", "19,60");
				KeyStringChar.AddBadQualityChar (badQualityChars, "a", "x");
				// HT.
				KeyStringChar.AddBadQualityChar (badQualityChars, "h.t.", "h.t");
				KeyStringChar.AddBadQualityChar (badQualityChars, "h.t.", "ht.");
				KeyStringChar.AddBadQualityChar (badQualityChars, "h.t.", "ht");
				KeyStringChar.AddBadQualityChar (badQualityChars, "r.c.", "r.c");
				KeyStringChar.AddBadQualityChar (badQualityChars, "r.c.", "rc.");
				// TVA.
				KeyStringChar.AddBadQualityChar (badQualityChars, "t.v.a.", "t.v.a");
				KeyStringChar.AddBadQualityChar (badQualityChars, "t.v.a.", "t.va.");
				KeyStringChar.AddBadQualityChar (badQualityChars, "t.v.a.", "t.va");
				KeyStringChar.AddBadQualityChar (badQualityChars, "t.v.a.", "tv.a.");
				KeyStringChar.AddBadQualityChar (badQualityChars, "t.v.a.", "tv.a");
				KeyStringChar.AddBadQualityChar (badQualityChars, "t.v.a.", "tva.");
				KeyStringChar.AddBadQualityChar (badQualityChars, "t.v.a.", "tva");
				KeyStringChar.AddBadQualityChar (badQualityChars, "t.v.a.", "7va");
				// TTC.
				KeyStringChar.AddBadQualityChar (badQualityChars, "t.t.c.", "t.t.c");
				KeyStringChar.AddBadQualityChar (badQualityChars, "t.t.c.", "t.tc.");
				KeyStringChar.AddBadQualityChar (badQualityChars, "t.t.c.", "t.tc");
				KeyStringChar.AddBadQualityChar (badQualityChars, "t.t.c.", "tt.c.");
				KeyStringChar.AddBadQualityChar (badQualityChars, "t.t.c.", "tt.c");
				KeyStringChar.AddBadQualityChar (badQualityChars, "t.t.c.", "ttc.");
				KeyStringChar.AddBadQualityChar (badQualityChars, "t.t.c.", "ttc");
				// RCS.
				KeyStringChar.AddBadQualityChar (badQualityChars, "r.c.s.", "r.c.s");
				KeyStringChar.AddBadQualityChar (badQualityChars, "r.c.s.", "r.cs.");
				KeyStringChar.AddBadQualityChar (badQualityChars, "r.c.s.", "r.cs");
				KeyStringChar.AddBadQualityChar (badQualityChars, "r.c.s.", "rc.s.");
				KeyStringChar.AddBadQualityChar (badQualityChars, "r.c.s.", "rc.s");
				KeyStringChar.AddBadQualityChar (badQualityChars, "r.c.s.", "rcs.");
				KeyStringChar.AddBadQualityChar (badQualityChars, "r.c.s.", "rcs");
				KeyStringChar.AddBadQualityChar (badQualityChars, "siren", "iren");

				return badQualityChars;
			}
			
			/// <summary>
			/// Retourne la liste des mauvaises chaines
			/// de caractères d'une chaine de caractères donnée.
			/// </summary>
			/// <param name="charStr">Chaine de caractères.</param>
			/// <returns>Tableau de chaines de caractères.</returns>
			public static String [] GetBadQualityChars (String charStr)
			{
				List<String>		badQualityChars;

				
				if(KeyStringChar._BadQualityChars.containsKey(charStr))
				{
					badQualityChars=KeyStringChar._BadQualityChars.get(charStr);
					String[] badQualityCharsArray = new String[badQualityChars.size()];
					badQualityChars.toArray(badQualityCharsArray);
					return  (badQualityCharsArray);
				}
				
				else
					return new String [0];
			}
		
			
			
			
			
}
