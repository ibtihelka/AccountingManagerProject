package com.accounting_manager.accounting_engine.Classes.learning;

import com.accounting_manager.accounting_engine.Classes.key.KeyType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class LearningEngine implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -8539189693938610644L;
    /// <summary>
    /// Nombre d'octes qu'il faut réserver pour la version
    /// <see cref="Version" />.
    /// </summary>
    private static int VERSION_MAX_BYTES = 10;
    /// <summary>
    /// Données de l'apprentissage classées
    /// par type de clé.
    /// </summary>
    private Map<KeyType, Map<String, LearningEntry>>
            learningEntries;
    /// <summary>
    /// Indique que l'apprenstissage est activé.
    /// Par défaut cet attribut vaut "True".
    /// </summary>
    private boolean isEnabled = true;
    /// <summary>
    /// Version du format de la persistance
    /// des données de l'apprentissage.
    /// </summary>
    private String version = "1.0";

    private Map<String, LearningEntry> entries;


    /// <summary>
    /// Initialise une nouvelle instance de la classe
    /// avec des paramètres par défaut.
    /// </summary>
    public LearningEngine() {
        this.learningEntries = new HashMap<KeyType, Map<String, LearningEntry>>();

    }

    /// <summary>
    /// Restaure des données de l'apprentissage
    /// à partir d'un flux de données. Une exception
    /// sera générée si la version n'est pas supportée.
    /// </summary>
    /// <param name="stream">Flux de données.</param>
    /// <returns>Une instance de la classe en cours.</returns>
    public static LearningEngine FromStream(ObjectInputStream stream) throws Exception {
        // Lire la version.
        byte[] reservedBytes = new byte[LearningEngine.VERSION_MAX_BYTES];
        stream.read(reservedBytes, 0, reservedBytes.length);
        String version = new String(reservedBytes, StandardCharsets.UTF_8);
        //List<Character>			cleanedVersionChars = new ArrayList<Character> ();


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < version.length(); i++) {
            char ch = version.charAt(i);
            if (ch == '\0') {
                break;
            } else {
                sb.append(ch);
            }
        }


        String cleanedVersion = sb.toString();
        // Lire les données.
        switch (cleanedVersion) {
            case "1.0": {
                return (LearningEngine) stream.readObject();
            }
            default:
                throw new Exception("Unsupported version '" + cleanedVersion + "' !!");
        }
    }

    /// <summary>
    /// Restaure des données de l'apprentissage
    /// à partir d'un fichier. Une exception
    /// sera générée si la version n'est pas supportée.
    /// </summary>
    /// <param name="fileName">Nom complet du fichier.</param>
    /// <returns>Une instance de la classe en cours.</returns>
    public static LearningEngine FromStream(String fileName) throws Exception {
        ObjectInputStream stream = new ObjectInputStream(new FileInputStream(fileName));
        LearningEngine learningEngine = FromStream(stream);
        stream.close();
        return learningEngine;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    /// <summary>
    /// Sauvegarde des données de l'apprentissage
    /// dans un flux de données. La version <see cref="Version" />
    /// sera incluse.
    /// </summary>
    /// <param name="stream">Flux de données.</param>
    public void ToStream(FileOutputStream stream) throws IOException {
        // Ecrire la version.
        byte[] versionBytes = this.version.getBytes(StandardCharsets.UTF_8);
        byte[] reservedBytes = new byte[LearningEngine.VERSION_MAX_BYTES];

        for (int i = 0; i < versionBytes.length; i++) {
            reservedBytes[i] = versionBytes[i];
        }

        stream.write(reservedBytes, 0, reservedBytes.length);

    }

    /// <summary>
    /// Sauvegarde des données de l'apprentissage
    /// dans un fichier. La version <see cref="Version" />
    /// sera incluse.
    /// </summary>
    /// <param name="fileName">Nom complet du fichier.</param>
    public void ToStream(String fileName) throws IOException {
        FileOutputStream stream = new FileOutputStream(fileName);
        ToStream(stream);
        stream.close();
    }

    /// <summary>
    /// Ajoute une entrée aux données de l'apprentissage.
    /// </summary>
    /// <param name="keyType">Type de la clé.</param>
    /// <param name="entry">Entrée d'apprentissage.</param>
    public void AddEntry(KeyType keyType, LearningEntry entry) {
        if (this.isEnabled) {
            // Rechercher par type de clé.
            if (!learningEntries.containsKey(keyType)) {
                entries = new HashMap<String, LearningEntry>();

                this.learningEntries.put(keyType, entries);
            }
            // Rechercher par fournisseur.
            entries.put(entry.getFournisseur(), entry);
        }
    }

    /// <summary>
    /// Retourne l'entrée d'un type de clé donnée
    /// et pour un fournisseur donné.
    /// </summary>
    /// <param name="keyType">Type de la clé.</param>
    /// <param name="fournisseur">Numréo du fournisseur.</param>
    /// <returns>
    /// Une instance non nulle de type <see cref="FacLearningEntry" />
    /// si la valeur l'entrée a été trouvée, False sinon.
    /// </returns>
    public LearningEntry GetEntry(KeyType keyType, String fournisseur) {
//        if (this.isEnabled) {
//            for (Entry<KeyType, Map<String, LearningEntry>> m : learningEntries.entrySet()) {
//                if (m.getKey().equals(keyType))
//                    entries = (Map<String, LearningEntry>) m.getValue();
//            }
//            for (Entry<String, LearningEntry> m : entries.entrySet()) {
//                if (m.getKey().equals(fournisseur))
//                    return (LearningEntry) m.getValue();
//            }
//        }
        return null;

    }
//    public LearningEntry GetEntry(KeyType keyType, String fournisseur)
//    {
//        if (this.isEnabled)
//        {
//            HashMap<String,LearningEntry> entries;
//
//            _Out<Map<String,LearningEntry>> tempOut_entries = new _Out<>(new HashMap<>());
//            if (this._LearningEntries.TryGetValue(keyType, tempOut_entries))
//            {
//                entries = tempOut_entries.outArgValue;
//                FacLearningEntry entry;
//
//                if (entries.containsKey(fournisseur) && (entry = entries.get(fournisseur)) == entry)
//                {
//                    return entry;
//                }
//            }
//            else
//            {
//                entries = tempOut_entries.outArgValue;
//            }
//        }
//
//        return null;
//    }
}