package com.accounting_manager.accounting_engine.Classes.key;

import com.accounting_manager.accounting_engine.Classes.document.DocumentZone;
import com.accounting_manager.accounting_engine.Classes.finders.NumberFactureFinder;

import java.io.Serializable;
import java.util.*;

public class Key implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -7381753795842174447L;
    /// <summary>
    /// Poids maximale d'une clé. Ce champ
    /// est utilisé principalement lors des affaiblissements.
    /// </summary>
    public static int MAX_WEIGHT = 100;
    /// <summary>
    /// Type de la clé.
    /// </summary>
    private KeyType type;
    /// <summary>
    /// Liste des chaines de caractères
    /// qui représentent la clé.
    /// </summary>
    private List<KeyString> strings;
    /// <summary>
    /// Liste des toutes les chaines de caractères
    /// des clés. Elles sont classéss par longueur.
    /// </summary>s
    private List<KeyString> allKeyStrings;
    /// <summary>
    /// Liste des zones préférées de la clé.
    /// Elles sont classées par ordre de préference.
    /// </summary>
    private List<DocumentZone> preferredZones;


    /// <summary>
    /// Initialise une nouvelle instance de la classe
    /// avec des paramètres spécifiés.
    /// </summary>
    /// <param name="type">Type de la clé.</param>
    /// <param name="strings">Liste de chaines de caractères de la clé.</param>
    /// <param name="preferredZones">Zones préférées de la clé.</param>
    public Key(KeyType type, List<KeyString> strings, List<DocumentZone> preferredZones) {
        // Modifier les poids des chaines de caractères.
        int currentWeight = -1;

        for (KeyString keyString : strings) {
            keyString.setWeight(currentWeight + (keyString.getWeight() > -1 ? keyString.getWeight() : (keyString.getWeight() == -1 ? 1 : 0)));
            currentWeight = keyString.getWeight();
        }
        // Fixer toutes les clés.
        this.allKeyStrings = Key.getAllKeyStrings(strings);
        // Vérifier les clés.
        checkKeyStrings();

        this.type = type;
        this.strings = strings;
        this.preferredZones = preferredZones;
    }

    /// <summary>
    /// Initialise une nouvelle instance de la classe
    /// avec des paramètres spécifiés.
    /// </summary>
    /// <param name="type">Type de la clé.</param>
    public Key(KeyType type) throws Exception {

        this(type, Key.GetStrings(type), Key.GetPreferredZones(type));

    }

    public static int getMAX_WEIGHT() {
        return MAX_WEIGHT;
    }

    public static void setMAX_WEIGHT(int mAX_WEIGHT) {
        MAX_WEIGHT = mAX_WEIGHT;
    }

    /// <summary>
    /// Retourne la liste complète des chaines de caractères d'une clé.
    /// </summary>
    /// <param name="keyStrings">Tableau des chaines de caractères orignales.</param>
    /// <returns>Tableau de toutes les chaines.</returns>
    private static List<KeyString> getAllKeyStrings(List<KeyString> keyStrings) {
        List<KeyString> allKeyStrings = new ArrayList<>();

        for (KeyString keyString : keyStrings)
            allKeyStrings.addAll(keyString.GetAllKeyStrings());


        Collections.sort(allKeyStrings, new Comparator<KeyString>() {
            public int compare(KeyString s1, KeyString s2) {
                return Integer.compare(s1.ToString(" ").length(), s2.ToString(" ").length());
            }
        });
        Collections.reverse(allKeyStrings);
        return allKeyStrings;
    }

    /// <summary>
    /// Retourne la liste de chaines de caractères
    /// de type <see cref="FacKeyString" /> pour un type
    /// de clé donné.
    /// </summary>
    /// <param name="keyType">Type de la clé.</param>
    /// <returns>Tableau de chaines de caractères.</returns>
    public static List<KeyString> GetStrings(KeyType keyType) throws Exception {
        switch (keyType) {
            case NUMERO_FACTURE:
                return Key.getNumeroFactureStrings();
            case DATE:
                return Key.getDateStrings();
            case TTC:
                return Key.getTtcStrings();
            case HT:
                return Key.getHtStrings();
            case TVA:
                return Key._GetTvaStrings();
            case TVA_SUPP:
                return Key._GetTvaSuppString();
            case DISCOUNT:
                return Key._GetDiscountStrings();
            case NUMERO_RCS:
                return Key.getNumeroRcsStrings();
            case NUMERO_SIRET:
                return Key._GetNumeroSiretStrings();
            case NUMERO_TVA:
                return Key._GetNumeroTvaStrings();
            case TYPE_FACTURE:
                return Key.getTypeFactureStrings();
            case NUMERO_PAGE:
                return Key._GetNumeroPageStrings();
            case AMOUNT:
                return Key.getAmountStrings();
            default:
                throw new Exception("Unknown key type '" + keyType + "'");
        }
    }

    /// <summary>
    /// Retourne la liste de chaines de caractères
    /// de type <see cref="FacKeyString" /> pour le type
    /// de clé <see cref="FacKeyType.NUMERO_FACTURE" />.
    /// </summary>
    /// <returns>Tableau de chaines de caractères.</returns>
    private static List<KeyString> getNumeroFactureStrings() {
        List<KeyString> keyStrings = new ArrayList<>();

        List<KeyString> excepted = NumberFactureFinder.GetExceptedStrings();
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("facture"), new KeyStringChar("en"), new KeyStringChar("euro"), new KeyStringChar("s", true), new KeyStringChar(":", true), new KeyStringChar("n"), new KeyStringChar("°")})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("facture"), new KeyStringChar("client", true), new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("facture"), new KeyStringChar("n."), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar(".", true), new KeyStringChar("facture"), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("numéro"), new KeyStringChar("piece", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°"), new KeyStringChar("piece")})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("numéro"), new KeyStringChar("de"), new KeyStringChar("facture")})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("document"), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("fac"), new KeyStringChar(".", true), new KeyStringChar(":")})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("AVOIR"), new KeyStringChar("n", true), new KeyStringChar("°", true)}), true));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("Avoir"), new KeyStringChar("n", true), new KeyStringChar("°", true)}), true));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("facture"), new KeyStringChar("partenaire", true), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°"), new KeyStringChar("bl", true, false, false)}), 2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n", false, true), new KeyStringChar("°", false, false, true, false)}), 10 /* Affaiblissement */));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("no", true, false, true)}), 0));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n0", true, false, true)}), 0));


        keyStrings .addAll(excepted);

        return keyStrings;
    }

    /// <summary>
    /// Retourne la liste de chaines de caractères
    /// de type <see cref="FacKeyString" /> pour le type
    /// de clé <see cref="FacKeyType.TYPE_FACTURE" />.
    /// </summary>
    /// <returns>Tableau de chaines de caractères.</returns>
    private static List<KeyString> getTypeFactureStrings() {
        List<KeyString> keyStrings = new ArrayList<>();

        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("facture", false, true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("avoir")})));
        return keyStrings;
    }

    /// <summary>
    /// Retourne la liste de chaines de caractères
    /// de type <see cref="FacKeyString" /> pour le type
    /// de clé <see cref="FacKeyType.DATE" />.
    /// </summary>
    /// <returns>Tableau de chaine de caractères.</returns>
    private static List<KeyString> getDateStrings() {
        List<KeyString> keyStrings = new ArrayList<>();
        /* Not OK */
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("date"), new KeyStringChar("reglement"), new KeyStringChar(":", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("date"), new KeyStringChar("limite"), new KeyStringChar(":", true)}), -2));
        /* OK */
        ;
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("date"), new KeyStringChar("facture"), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("date"), new KeyStringChar("ticket"), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("date"), new KeyStringChar("bl"), new KeyStringChar(":", true)}), 0));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("date"), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("emise"), new KeyStringChar("le"), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("le", true, false, false)}), 4 /* Affaiblissement */));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("du", true, false, false)}), 1 /* Affaiblissement */));

        return keyStrings;
    }

    /// <summary>
    /// Retourne la liste de chaines de caractères
    /// de type <see cref="FacKeyString" /> pour le type
    /// de clé <see cref="FacKeyType.TTC" />.
    /// </summary>
    /// <returns>Tableau de chaine de caractères.</returns>
    private static List<KeyString> getTtcStrings() {
        List<KeyString> keyStrings = new ArrayList<>();

        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("net"), new KeyStringChar("a"), new KeyStringChar("payer"), new KeyStringChar("t.t.c.", true), new KeyStringChar("€", true), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("net"), new KeyStringChar("a"), new KeyStringChar("paye"), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("total"), new KeyStringChar("facture", true), new KeyStringChar("t.t.c."), new KeyStringChar("en", true), new KeyStringChar("euros", true), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("total"), new KeyStringChar("a"), new KeyStringChar("payer"), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("a"), new KeyStringChar("payer"), new KeyStringChar("t.t.c."), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("prix"), new KeyStringChar("t.t.c."), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("a"), new KeyStringChar("payer"), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("montant"), new KeyStringChar("net", true), new KeyStringChar("t.t.c."), new KeyStringChar("€", true), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("net"), new KeyStringChar("t.t.c."), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("total"), new KeyStringChar("net"), new KeyStringChar("€", true), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("t.t.c."), new KeyStringChar("facture"), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("montant"), new KeyStringChar("facture"), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("montant"), new KeyStringChar("total"), new KeyStringChar("t.t.c", true), new KeyStringChar("a", true), new KeyStringChar("payer", true), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("t.t.c."), new KeyStringChar("€", true), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("facture", false, true), new KeyStringChar(":", true, true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("aimable"), new KeyStringChar("reglement")})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("aimablc"), new KeyStringChar("reglement")})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("total", false, true), new KeyStringChar("€", true), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("payer")})));
        return keyStrings;
    }

    /// <summary>
    /// Retourne la liste de chaines de caractères
    /// de type <see cref="FacKeyString" /> pour le type
    /// de clé <see cref="FacKeyType.HT" />.
    /// </summary>
    /// <returns>Tableau de chaine de caractères.</returns>
    private static List<KeyString> getHtStrings() {
        List<KeyString> keyStrings = new ArrayList<>();
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("total"), new KeyStringChar("hors"), new KeyStringChar("t.v.a."), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("montant", true), new KeyStringChar("net"), new KeyStringChar("h.t."), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("total"), new KeyStringChar("facture", true), new KeyStringChar("h.t."), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("total"), new KeyStringChar("march"), new KeyStringChar(".", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("total"), new KeyStringChar("marchandises"), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("total"), new KeyStringChar("hors"), new KeyStringChar("-"), new KeyStringChar("taxe"), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("total"), new KeyStringChar("marchandises"), new KeyStringChar("ttc"), new KeyStringChar(":", true)}), -2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("valeur"), new KeyStringChar("nette"), new KeyStringChar("marchandise"), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("montant"), new KeyStringChar("hors"), new KeyStringChar("taxe"), new KeyStringChar("s", true), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("total"), new KeyStringChar("hors"), new KeyStringChar("taxe"), new KeyStringChar("s", true), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("hors"), new KeyStringChar("taxe"), new KeyStringChar("s", true), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("base"), new KeyStringChar("h.t."), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("base"), new KeyStringChar("t.v.a."), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("base", false, true), new KeyStringChar(":", true, true)}), 0));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("commission"), new KeyStringChar("ht"), new KeyStringChar("(", true), new KeyStringChar("12", true), new KeyStringChar("%", true), new KeyStringChar("ttc", true), new KeyStringChar(")", true), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("montant"), new KeyStringChar("€", true), new KeyStringChar("h.t."), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("montant", false, true), new KeyStringChar("net", false, true), new KeyStringChar(":", true)}), 0));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("h.t."), new KeyStringChar(":", true)}), 2));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("ht")}), 0));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("T", false, true), new KeyStringChar("=", true, true), new KeyStringChar("5.5", false, true), new KeyStringChar("%", false, true)}), true));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("T", false, true), new KeyStringChar(":", false, true), new KeyStringChar("5.5", false, true), new KeyStringChar("%", false, true)}), true));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("19.6"), new KeyStringChar("0", true), new KeyStringChar("%"), new KeyStringChar("sur")})));

        return keyStrings;
    }

    /// <summary>
    /// Retourne la liste de chaines de caractères
    /// de type <see cref="FacKeyString" /> pour le type
    /// de clé <see cref="FacKeyType.TVA" />.
    /// </summary>
    /// <returns>Tableau de chaine de caractères.</returns>
    private static List<KeyString> _GetTvaStrings() {
        List<KeyString> keyStrings = new ArrayList<>();

        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("total"), new KeyStringChar("taxes"), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("total", false, true), new KeyStringChar("tva", false, true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("total", true), new KeyStringChar("t.v.a."), new KeyStringChar("a", true), new KeyStringChar("5.5"), new KeyStringChar("%", true), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("total", true), new KeyStringChar("t.v.a."), new KeyStringChar("a", true), new KeyStringChar("19.6"), new KeyStringChar("%", true), new KeyStringChar(":", true)}), 0));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("montant"), new KeyStringChar("t.v.a."), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("t.v.a."), new KeyStringChar("payee"), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("t.v.a."), new KeyStringChar("payee"), new KeyStringChar("sur"), new KeyStringChar("les"), new KeyStringChar("debits"), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("t.v.a.", false, true), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("T", true, false, true), new KeyStringChar("=", true, true), new KeyStringChar("5.5", false, true), new KeyStringChar("%", false, true)}), true));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("T", true, false, true), new KeyStringChar(":", false, true), new KeyStringChar("5.5", false, true), new KeyStringChar("%", false, true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("€"), new KeyStringChar("ht", true), new KeyStringChar(")"), new KeyStringChar("=")})));

        return keyStrings;
    }

    /// <summary>
    /// Retourne la liste de chaines de caractères
    /// de type <see cref="FacKeyString" /> pour le type
    /// de clé <see cref="FacKeyType.DISCOUNT" />.
    /// </summary>
    /// <returns>Tableau de chaine de caractères.</returns>
    private static List<KeyString> _GetDiscountStrings() {
        List<KeyString> keyStrings = new ArrayList<>();

        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("remise"), new KeyStringChar("sur"), new KeyStringChar("facture")})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("acompte"), new KeyStringChar("facture")})));
        return keyStrings;
    }

    /// <summary>
    /// Retourne la liste de chaines de caractères
    /// de type <see cref="FacKeyString" /> pour le type
    /// de clé <see cref="FacKeyType.TVA_SUPP" />.
    /// </summary>
    /// <returns>Tableau de chaine de caractères.</returns>
    private static List<KeyString> _GetTvaSuppString() {
        List<KeyString> keyStrings = new ArrayList<>();

        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("port"), new KeyStringChar("*", true), new KeyStringChar("ttc")})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("port"), new KeyStringChar("et"), new KeyStringChar("emballage")})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("cifog")})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("taxes"), new KeyStringChar("locales", true)})));
        return keyStrings;
    }

    /// <summary>
    /// Retourne la liste de chaines de caractères
    /// de type <see cref="FacKeyString" /> pour le type
    /// de clé <see cref="FacKeyType.NUMERO_TVA" />.
    /// </summary>
    /// <returns>Tableau de chaine de caractères.</returns>
    private static List<KeyString> _GetNumeroTvaStrings() {
        List<KeyString> keyStrings = new ArrayList<>();

        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("Id01A"), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n°", true), new KeyStringChar("id"), new KeyStringChar("ent.", true), new KeyStringChar("tva"), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("Code"), new KeyStringChar("TVA"), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("Code"), new KeyStringChar("TV"), new KeyStringChar("A", true, false, false), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°"), new KeyStringChar("de", true), new KeyStringChar("tva"), new KeyStringChar(":", true), new KeyStringChar("N", true), new KeyStringChar("°", true)}), 0));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("tva"), new KeyStringChar("n"), new KeyStringChar("°"), new KeyStringChar(":", true)}), 0));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("CEE"), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("C.E.E"), new KeyStringChar(".", true), new KeyStringChar(":", true)}), 0));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("CE", false, true), new KeyStringChar("E."), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("identifiant"), new KeyStringChar("CE"), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("identifiant"), new KeyStringChar("C.E"), new KeyStringChar(".", true), new KeyStringChar(":", true)}), 0));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("intra"), new KeyStringChar(".", true), new KeyStringChar("communautaire"), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("TVA"), new KeyStringChar("intra"), new KeyStringChar("-", true), new KeyStringChar("communautaire"), new KeyStringChar(":", true)}), 0));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("TV"), new KeyStringChar("A", true, false, false), new KeyStringChar("intra"), new KeyStringChar("-", true), new KeyStringChar("communautaire"), new KeyStringChar(":", true)}), 0));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("TVA"), new KeyStringChar("intra"), new KeyStringChar("com", true), new KeyStringChar(".", true), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("TV"), new KeyStringChar("A", true, false, false), new KeyStringChar("intra"), new KeyStringChar("com", true), new KeyStringChar(".", true), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("intracom"), new KeyStringChar(".", true), new KeyStringChar(":", true)}), 0));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("communaut"), new KeyStringChar("aire", true), new KeyStringChar(".", true), new KeyStringChar(":", true)}), 0));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("tracom"), new KeyStringChar(".", true), new KeyStringChar(":", true)}), 0));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("int", true, false, false), new KeyStringChar(".", true), new KeyStringChar(":", true)}), 0));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("TVA", false, true), new KeyStringChar(":", true, true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("TV", false, true), new KeyStringChar("A", true, false, true), new KeyStringChar(":", true, true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("T.V.A", false, true), new KeyStringChar(":", true, true)})));
        return keyStrings;
    }

    /// <summary>
    /// Retourne la liste de chaines de caractères
    /// de type <see cref="FacKeyString" /> pour le type
    /// de clé <see cref="FacKeyType.NUMERO_RCS" />.
    /// </summary>
    /// <returns>Tableau de chaine de caractères.</returns>
    private static List<KeyString> getNumeroRcsStrings() {
        List<KeyString> keyStrings = new ArrayList<>();

        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("siren"), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("siret", false, true), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("r.c.s."), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("r.c."), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("rc", true, false, false), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("rc", true, false, false), new KeyStringChar("s.", true, false, false), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("PCS"), new KeyStringChar(":", true)})));
        return keyStrings;
    }

    /// <summary>
    /// Retourne la liste de chaines de caractères
    /// de type <see cref="FacKeyString" /> pour le type
    /// de clé <see cref="FacKeyType.NUMERO_SIRET" />.
    /// </summary>
    /// <returns>Tableau de chaine de caractères.</returns>
    private static List<KeyString> _GetNumeroSiretStrings() {
        List<KeyString> keyStrings = new ArrayList<>();

        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("siret", false, true), new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar(":", true)}), 0));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("de", true), new KeyStringChar("siret", false, true), new KeyStringChar(":", true)})));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("siret", false, true), new KeyStringChar(":", true)})));
        return keyStrings;
    }


    /// <summary>
    /// Retourne la liste de chaines de caractères
    /// de type <see cref="FacKeyString" /> pour le type
    /// de clé <see cref="FacKeyType.NUMERO_PAGE" />.
    /// </summary>
    /// <returns>Tableau de chaine de caractères.</returns>
    private static List<KeyString> _GetNumeroPageStrings() {
        List<KeyString> keyStrings = new ArrayList<>();

        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("page"), new KeyStringChar("n", true), new KeyStringChar("°", true), new KeyStringChar(":", true)}), 0));
        keyStrings.add(new KeyString(KeyStringCharToList(new KeyStringChar[]{new KeyStringChar("n"), new KeyStringChar("°", true), new KeyStringChar("page"), new KeyStringChar(":", true)})));
        return keyStrings;
    }

    /// <summary>
    /// Retourne la liste de chaines de caractères
    /// de type <see cref="FacKeyString" /> pour le type
    /// de clé <see cref="FacKeyType.AMOUNT" />.
    /// </summary>
    /// <returns>Tableau de chaine de caractères.</returns>
    private static List<KeyString> getAmountStrings() {

        return new ArrayList<>();

    }

    /// <summary>
    /// Retourne la liste des zones préférées
    /// de type <see cref="FacDocumentZone" /> pour un type
    /// de clé donné.
    /// </summary>
    /// <param name="keyType">Type de la clé.</param>
    /// <returns>Tableau de zones.</returns>
    public static List<DocumentZone> GetPreferredZones(KeyType keyType) throws Exception {
        switch (keyType) {
            case NUMERO_FACTURE:
                return Key.getNumeroFacturePreferredZones();
            case DATE:
                return Key._GetDatePreferredZones();
            case NUMERO_RCS:
                return Key._GetNumeroRcsPreferredZones();
            case NUMERO_SIRET:
                return Key._GetNumeroSiretPreferredZones();
            case NUMERO_TVA:
                return Key._GetNumeroTvaPreferredZones();
            case TVA_SUPP:
                return Key._GetTvaSuppPreferredZones();
            case DISCOUNT:
                return Key._GetDiscountPreferredZones();
            case TTC:
                return Key._GetTtcPreferredZones();
            case HT:
                return Key._GetHtPreferredZones();
            case TVA:
                return Key._GetTvaPreferredZones();
            case TYPE_FACTURE:
                return Key._GetTypeFacturePreferredZones();
            case NUMERO_PAGE:
                return Key._GetNumeroPagePreferredZones();
            case AMOUNT:
                return Key._GetAmountPreferredZones();
            default:
                throw new Exception("Unknown key type '" + keyType + "'");
        }
    }

    private static List<DocumentZone> getNumeroFacturePreferredZones() {
        return DocumentZoneToList(new DocumentZone[]{
                DocumentZone.TOP_LEFT,
                DocumentZone.TOP_RIGHT,
                DocumentZone.BOTTOM_LEFT,
                DocumentZone.BOTTOM_RIGHT,
                DocumentZone.TOP_CENTER,
                DocumentZone.BOTTOM_CENTER,
                DocumentZone.MIDDLE_LEFT,
                DocumentZone.MIDDLE_CENTER,
                DocumentZone.MIDDLE_RIGHT});
    }

    /// <summary>
    /// Retourne la liste des zones préférées
    /// de type <see cref="FacDocumentZone" /> pour le type
    /// de clé <see cref="FacKeyType.TYPE_FACTURE" />.
    /// </summary>
    /// <returns>Tableau de zones.</returns>
    private static List<DocumentZone> _GetTypeFacturePreferredZones() {
        return DocumentZoneToList(new DocumentZone[]{
                DocumentZone.TOP_LEFT,
                DocumentZone.TOP_RIGHT,
                DocumentZone.BOTTOM_LEFT,
                DocumentZone.BOTTOM_RIGHT,
                DocumentZone.TOP_CENTER,
                DocumentZone.BOTTOM_CENTER,
                DocumentZone.MIDDLE_LEFT,
                DocumentZone.MIDDLE_CENTER,
                DocumentZone.MIDDLE_RIGHT});
    }

    /// <summary>
    /// Retourne la liste des zones préférées
    /// de type <see cref="FacDocumentZone" /> pour le type
    /// de clé <see cref="FacKeyType.DATE" />.
    /// </summary>
    /// <returns>Tableau de zones.</returns>
    private static List<DocumentZone> _GetDatePreferredZones() {
        return DocumentZoneToList(new DocumentZone[]{
                DocumentZone.TOP_LEFT,
                DocumentZone.TOP_RIGHT,
                DocumentZone.BOTTOM_RIGHT,
                DocumentZone.TOP_CENTER,
                DocumentZone.BOTTOM_LEFT,
                DocumentZone.BOTTOM_CENTER,
                DocumentZone.MIDDLE_LEFT,
                DocumentZone.MIDDLE_CENTER,
                DocumentZone.MIDDLE_RIGHT});
    }

    /// <summary>
    /// Retourne la liste des zones préférées
    /// de type <see cref="FacDocumentZone" /> pour le type
    /// de clé <see cref="FacKeyType.NUMERO_RCS" />.
    /// </summary>
    /// <returns>Tableau de zones.</returns>
    private static List<DocumentZone> _GetNumeroRcsPreferredZones() {
        return DocumentZoneToList(new DocumentZone[]{
                DocumentZone.TOP_LEFT,
                DocumentZone.TOP_RIGHT,
                DocumentZone.TOP_CENTER,
                DocumentZone.BOTTOM_LEFT,
                DocumentZone.BOTTOM_CENTER,
                DocumentZone.BOTTOM_RIGHT,
                DocumentZone.MIDDLE_LEFT,
                DocumentZone.MIDDLE_RIGHT,
                DocumentZone.MIDDLE_CENTER});
    }

    /// <summary>
    /// Retourne la liste des zones préférées
    /// de type <see cref="FacDocumentZone" /> pour le type
    /// de clé <see cref="FacKeyType.NUMERO_SIRET" />.
    /// </summary>
    /// <returns>Tableau de zones.</returns>
    private static List<DocumentZone> _GetNumeroSiretPreferredZones() {
        return DocumentZoneToList(new DocumentZone[]{
                DocumentZone.TOP_LEFT,
                DocumentZone.TOP_RIGHT,
                DocumentZone.TOP_CENTER,
                DocumentZone.BOTTOM_LEFT,
                DocumentZone.BOTTOM_CENTER,
                DocumentZone.BOTTOM_RIGHT,
                DocumentZone.MIDDLE_LEFT,
                DocumentZone.MIDDLE_RIGHT,
                DocumentZone.MIDDLE_CENTER});
    }

    /// <summary>
    /// Retourne la liste des zones préférées
    /// de type <see cref="FacDocumentZone" /> pour le type
    /// de clé <see cref="FacKeyType.NUMERO_TVA" />.
    /// </summary>
    /// <returns>Tableau de zones.</returns>
    private static List<DocumentZone> _GetNumeroTvaPreferredZones() {
        return DocumentZoneToList(new DocumentZone[]{
                DocumentZone.BOTTOM_LEFT,
                DocumentZone.BOTTOM_CENTER,
                DocumentZone.BOTTOM_RIGHT,
                DocumentZone.TOP_LEFT,
                DocumentZone.TOP_RIGHT,
                DocumentZone.TOP_CENTER,
                DocumentZone.MIDDLE_LEFT,
                DocumentZone.MIDDLE_RIGHT,
                DocumentZone.MIDDLE_CENTER});
    }

    /// <summary>
    /// Retourne la liste des zones préférées
    /// de type <see cref="FacDocumentZone" /> pour le type
    /// de clé <see cref="FacKeyType.TTC" />.
    /// </summary>
    /// <returns>Tableau de zones.</returns>
    private static List<DocumentZone> _GetTtcPreferredZones() {
        return DocumentZoneToList(new DocumentZone[]{
                DocumentZone.BOTTOM_LEFT,
                DocumentZone.BOTTOM_CENTER,
                DocumentZone.BOTTOM_RIGHT,
                DocumentZone.MIDDLE_RIGHT,
                DocumentZone.MIDDLE_CENTER,
                DocumentZone.MIDDLE_LEFT,
                DocumentZone.TOP_LEFT,
                DocumentZone.TOP_CENTER,
                DocumentZone.TOP_RIGHT});
    }

    /// <summary>
    /// Retourne la liste des zones préférées
    /// de type <see cref="FacDocumentZone" /> pour le type
    /// de clé <see cref="FacKeyType.HT" />.
    /// </summary>
    /// <returns>Tableau de zones.</returns>
    private static List<DocumentZone> _GetHtPreferredZones() {
        return DocumentZoneToList(new DocumentZone[]{
                DocumentZone.BOTTOM_LEFT,
                DocumentZone.BOTTOM_CENTER,
                DocumentZone.BOTTOM_RIGHT,
                DocumentZone.MIDDLE_RIGHT,
                DocumentZone.MIDDLE_CENTER,
                DocumentZone.MIDDLE_LEFT,
                DocumentZone.TOP_LEFT,
                DocumentZone.TOP_CENTER,
                DocumentZone.TOP_RIGHT});
    }

    /// <summary>
    /// Retourne la liste des zones préférées
    /// de type <see cref="FacDocumentZone" /> pour le type
    /// de clé <see cref="FacKeyType.TVA" />.
    /// </summary>
    /// <returns>Tableau de zones.</returns>
    private static List<DocumentZone> _GetTvaPreferredZones() {
        return DocumentZoneToList(new DocumentZone[]{
                DocumentZone.BOTTOM_LEFT,
                DocumentZone.BOTTOM_CENTER,
                DocumentZone.BOTTOM_RIGHT,
                DocumentZone.MIDDLE_RIGHT,
                DocumentZone.MIDDLE_CENTER,
                DocumentZone.MIDDLE_LEFT,
                DocumentZone.TOP_LEFT,
                DocumentZone.TOP_CENTER,
                DocumentZone.TOP_RIGHT});
    }

    /// <summary>
    /// Retourne la liste des zones préférées
    /// de type <see cref="FacDocumentZone" /> pour le type
    /// de clé <see cref="FacKeyType.DISCOUNT" />.
    /// </summary>
    /// <returns>Tableau de zones.</returns>
    private static List<DocumentZone> _GetDiscountPreferredZones() {
        return DocumentZoneToList(new DocumentZone[]{
                DocumentZone.BOTTOM_CENTER,
                DocumentZone.BOTTOM_LEFT,
                DocumentZone.BOTTOM_RIGHT,
                DocumentZone.MIDDLE_RIGHT,
                DocumentZone.MIDDLE_CENTER,
                DocumentZone.MIDDLE_LEFT,
                DocumentZone.TOP_LEFT,
                DocumentZone.TOP_CENTER,
                DocumentZone.TOP_RIGHT});
    }

    /// <summary>
    /// Retourne la liste des zones préférées
    /// de type <see cref="FacDocumentZone" /> pour le type
    /// de clé <see cref="FacKeyType.TVA_SUPP" />.
    /// </summary>
    /// <returns>Tableau de zones.</returns>
    private static List<DocumentZone> _GetTvaSuppPreferredZones() {
        return DocumentZoneToList(new DocumentZone[]{
                DocumentZone.BOTTOM_CENTER,
                DocumentZone.BOTTOM_LEFT,
                DocumentZone.BOTTOM_RIGHT,
                DocumentZone.MIDDLE_RIGHT,
                DocumentZone.MIDDLE_CENTER,
                DocumentZone.MIDDLE_LEFT,
                DocumentZone.TOP_LEFT,
                DocumentZone.TOP_CENTER,
                DocumentZone.TOP_RIGHT});
    }

    /// <summary>
    /// Retourne la liste des zones préférées
    /// de type <see cref="FacDocumentZone" /> pour le type
    /// de clé <see cref="FacKeyType.NUMERO_PAGE" />.
    /// </summary>
    /// <returns>Tableau de zones.</returns>
    private static List<DocumentZone> _GetNumeroPagePreferredZones() {
        return DocumentZoneToList(new DocumentZone[]{
                DocumentZone.TOP_RIGHT,
                DocumentZone.BOTTOM_RIGHT,
                DocumentZone.TOP_CENTER,
                DocumentZone.TOP_LEFT,
                DocumentZone.BOTTOM_CENTER,
                DocumentZone.BOTTOM_LEFT,
                DocumentZone.MIDDLE_RIGHT,
                DocumentZone.MIDDLE_CENTER,
                DocumentZone.MIDDLE_LEFT});
    }

    /// <summary>
    /// Retourne la liste des zones préférées
    /// de type <see cref="FacDocumentZone" /> pour le type
    /// de clé <see cref="FacKeyType.AMOUNT" />.
    /// </summary>
    /// <returns>Tableau de zones.</returns>
    private static List<DocumentZone> _GetAmountPreferredZones() {
        return DocumentZoneToList(new DocumentZone[]{
                DocumentZone.MIDDLE_CENTER,
                DocumentZone.MIDDLE_RIGHT,
                DocumentZone.BOTTOM_CENTER,
                DocumentZone.BOTTOM_RIGHT,
                DocumentZone.BOTTOM_LEFT,
                DocumentZone.MIDDLE_LEFT,
                DocumentZone.TOP_CENTER,
                DocumentZone.TOP_RIGHT,
                DocumentZone.TOP_LEFT});
    }

    /// Indique lors de la construction des dessins
    /// si un type de clé donné est
    /// un type qui peut être affiché en tant
    /// que valeur destinée à l'utilisateur.
    /// </summary>
    /// <param name="keyType">Type de la clé.</param>
    /// <returns>True si le type de la clé peut être dessiné à l'utilisateur.</returns>
    public static boolean IsValueDrawingType(KeyType keyType) {
        switch (keyType) {
            case NUMERO_FACTURE:
            case DATE:
            case TTC:
            case HT:
            case TVA:
            case NUMERO_RCS:
            case NUMERO_SIRET:
            case NUMERO_TVA:
            case TYPE_FACTURE:
                return true;
            default:
                return false;
        }
    }

    /// <summary>
    /// Retourne la chaine de caractères de debogage
    /// associée à un type donné de clé.
    /// </summary>
    /// <param name="keyType">Type de la clé.</param>
    /// <returns>Chaine de caractères associée au type <paramref name="keyType" />.</returns>
    public static String GetDebugTypeString(KeyType keyType) throws Exception {
        switch (keyType) {
            case NUMERO_FACTURE:
                return "nf";
            case DATE:
                return "da";
            case TTC:
                return "tt";
            case HT:
                return "ht";
            case TVA:
                return "tv";
            case TVA_SUPP:
                return "ts";
            case DISCOUNT:
                return "re";
            case NUMERO_RCS:
                return "nr";
            case NUMERO_SIRET:
                return "ns";
            case NUMERO_TVA:
                return "nt";
            case TYPE_FACTURE:
                return "tf";
            case NUMERO_PAGE:
                return "np";
            case AMOUNT:
                return "am";
            default:
                throw new Exception("Unknown key type '" + keyType + "'");
        }
    }

    /// <summary>
    /// Retourne la chaine de caractères associée
    /// à un type donné de clé.
    /// </summary>
    /// <param name="keyType">Type de la clé.</param>
    /// <returns>Chaine de caractères associée au type <paramref name="keyType" />.</returns>
    public static String GetTypeString(KeyType keyType) throws Exception {
        switch (keyType) {
            case NUMERO_FACTURE:
                return "N° Fac";
            case DATE:
                return "Date";
            case TTC:
                return "TTC";
            case HT:
                return "HT";
            case TVA:
                return "TVA";
            case TVA_SUPP:
                return "TVA SUPP";
            case DISCOUNT:
                return "Remise";
            case NUMERO_RCS:
                return "N° RCS";
            case NUMERO_SIRET:
                return "N° SIRET";
            case NUMERO_TVA:
                return "N° TVA";
            case TYPE_FACTURE:
                return "Type Fac";
            case NUMERO_PAGE:
                return "N° Page";
            case AMOUNT:
                return "Montant";
            default:
                throw new Exception("Unknown key type '" + keyType + "'");
        }
    }

    //Convert KeyStringChar Table to List
    public static List<KeyStringChar> KeyStringCharToList(KeyStringChar[] keyStringChars) {
        List<KeyStringChar> stringChars = new ArrayList<>();

        for (KeyStringChar k : keyStringChars)
            stringChars.add(k);

        return stringChars;
    }

    //Convert DocumentZone Table to List
    public static List<DocumentZone> DocumentZoneToList(DocumentZone[] documentZones) {
        List<DocumentZone> list = new ArrayList<>();

        for (DocumentZone d : documentZones)
            list.add(d);

        return list;
    }

    public KeyType getType() {
        return type;
    }

    public void setType(KeyType type) {
        this.type = type;
    }

    public List<KeyString> getStrings() {
        return strings;
    }

    public void setStrings(List<KeyString> strings) {
        this.strings = strings;
    }

    public List<KeyString> getAllKeyStrings() {
        return allKeyStrings;
    }

    public void setAllKeyStrings(List<KeyString> allKeyStrings) {
        this.allKeyStrings = allKeyStrings;
    }

    public int preferredZonesCount() {
        return preferredZones.size();
    }

    /// <summary>
    /// Vérifie les duplications des chaines de caractères.
    /// </summary>
    private void checkKeyStrings() {
        Map<String, Integer> hKeyStrings = new HashMap<String, Integer>();
        String keyStringStr;
        int weight;

        for (KeyString keyString : this.allKeyStrings) {
            keyStringStr = keyString.toString();

            if (hKeyStrings.containsKey(keyStringStr) && hKeyStrings.get(keyString) != null) {
                weight = hKeyStrings.get(keyString);

                if (keyString.getWeight() != weight)

                    throw new IllegalArgumentException("Duplicated key string '" + keyStringStr + "'");
            } else
                hKeyStrings.put(keyStringStr, keyString.getWeight());

        }

    }

    /// <summary>
    /// Retourne la chaine de caractères de la clé qui se trouve
    /// à la position <paramref name="index" /> de l'instance en cours.
    /// </summary>
    /// <param name="index">Entier qui représente l'indice de la chaine de caractères.</param>
    /// <returns>Une instance de type <see cref="FacKeyString" />.</returns>
    public KeyString GetString(int index) {

        return this.strings.get(index);

    }

    /// <summary>
    /// Retourne la zone préférée de la clé qui se trouve
    /// à la position <paramref name="index" /> de l'instance en cours.
    /// </summary>
    /// <param name="index">Entier qui représente l'indice de la zone préférée.</param>
    /// <returns>Une instance de type <see cref="FacDocumentZone" />.</returns>
    public DocumentZone GetPreferredZone(int index) {
        return this.preferredZones.get(index);

    }
}
