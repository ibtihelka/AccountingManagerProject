package com.accounting_manager.accounting_engine.Classes.facture;


import com.accounting_manager.accounting_engine.Classes.key.KeyType;
import com.accounting_manager.accounting_engine.Classes.key.KeyValue;
import com.accounting_manager.accounting_engine.Classes.learning.LearningEngine;
import com.accounting_manager.accounting_engine.Classes.learning.LearningEntry;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiFacture implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/// <summary>
			/// Nom du fichier de l'image de la multi-factures.
			/// Le chemin doit être complet.
			/// </summary>
			private String								fileName;
			/// <summary>
			/// Dossier de sortie des images à traiter.
			/// </summary>
			private String								outputDir;
			/// <summary>
			/// Numéro de série du developpeur utilisée pour créer l'instance de l'OCR.
			/// </summary>
			private String								developerSN;
			/// <summary>
			/// Chemin de la librairie utilisée pour créer l'instance de l'OCR.
			/// </summary>
			private String								engineLibPath;
			/// <summary>
			/// Indique qu'il faut créer un processus de traitement
			/// pour l'extraction des données. La création des processus
			/// de traitement permet d'améliorer la performane en exploitant
			/// les coeurs du microprocesseur.
			/// </summary>
			private boolean								createProcess;
			/// <summary>
			/// Nom de l'exécutable du processus de traitement
			/// des doucments.
			/// </summary>
			private String								processFileName;
			/// <summary>
			/// Moteur d'apprentissage qui contient
			/// les données apprises des factures.
			/// </summary>
			private LearningEngine					 learningEngine;
			/// <summary>
			/// Cache des noms des factures.
			/// </summary>
			private String []							imageNamesCache;
			/// <summary>
			/// Cache des factures.
			/// </summary>
			private Map<Integer,FactureCache>		facturesCache;
			/// <summary>
			/// Indique qu'il faut sauvegarder le cache à la fin
			/// de l'opération.
			/// </summary>
			private boolean								mustFlushCache;
			/// <summary>
			/// Liste des informations sur les factures aprés lancement
			/// du traitement via la méthode <see cref="StartProcessing (FacProcessingOptions)" />.
			/// </summary>
			private FactureInfo []					factureInfos;
			/// <summary>
			/// Délégué sur une méthode à éxécuter lorsque le traitement
			/// sur une multi-facture vient de commencer.
			/// </summary>
			private MultiFactureStart multiFactureStartProcessing;
			/// <summary>
			/// Délégué sur une méthode à éxécuter lorsque le traitement
			/// sur une multi-facture vient de finir.
			/// </summary>
			private MultiFactureEnd multiFactureEndProcessing;
			/// <summary>
			/// Délégué sur une méthode à éxécuter lorsque le traitement
			/// sur une facture vient de commencer.
			/// </summary>
			private FactureStart factureStartProcessing;
			/// <summary>
			/// Délégué sur une méthode à éxécuter lorsque le traitement
			/// sur une facture vient de finir.
			/// </summary>
			private FactureEnd factureEndProcessing;
			public String getFileName() {
				return fileName;
			}
			public String getOutputDir() {
				return outputDir;
			}
			public String getDeveloperSN() {
				return developerSN;
			}
			public String getEngineLibPath() {
				return engineLibPath;
			}
			public boolean isCreateProcess() {
				return createProcess;
			}
			public String getProcessFileName() {
				return processFileName;
			}
			public LearningEngine getLearningEngine() {
				return learningEngine;
			}
			public String[] getImageNamesCache() {
				return imageNamesCache;
			}
			public Map<Integer, FactureCache> getFacturesCache() {
				return facturesCache;
			}
			public boolean isMustFlushCache() {
				return mustFlushCache;
			}
			public FactureInfo[] getFactureInfos() {
				return factureInfos;
			}
			public MultiFactureStart getMultiFactureStartProcessing() {
				return multiFactureStartProcessing;
			}
			public MultiFactureEnd getMultiFactureEndProcessing() {
				return multiFactureEndProcessing;
			}
			public FactureStart getFactureStartProcessing() {
				return factureStartProcessing;
			}
			public FactureEnd getFactureEndProcessing() {
				return factureEndProcessing;
			}
			public void setFileName(String fileName) {
				this.fileName = fileName;
			}
			public void setOutputDir(String outputDir) {
				this.outputDir = outputDir;
			}
			public void setDeveloperSN(String developerSN) {
				this.developerSN = developerSN;
			}
			public void setEngineLibPath(String engineLibPath) {
				this.engineLibPath = engineLibPath;
			}
			public void setCreateProcess(boolean createProcess) {
				this.createProcess = createProcess;
			}
			public void setProcessFileName(String processFileName) {
				this.processFileName = processFileName;
			}
			public void setLearningEngine(LearningEngine learningEngine) {
				this.learningEngine = learningEngine;
			}
			public void setImageNamesCache(String[] imageNamesCache) {
				this.imageNamesCache = imageNamesCache;
			}
			public void setFacturesCache(Map<Integer, FactureCache> facturesCache) {
				this.facturesCache = facturesCache;
			}
			public void setMustFlushCache(boolean mustFlushCache) {
				this.mustFlushCache = mustFlushCache;
			}
			public void setFactureInfos(FactureInfo[] factureInfos) {
				this.factureInfos = factureInfos;
			}
			public void setMultiFactureStartProcessing(MultiFactureStart multiFactureStartProcessing) {
				this.multiFactureStartProcessing = multiFactureStartProcessing;
			}
			public void setMultiFactureEndProcessing(MultiFactureEnd multiFactureEndProcessing) {
				this.multiFactureEndProcessing = multiFactureEndProcessing;
			}
			public void setFactureStartProcessing(FactureStart factureStartProcessing) {
				this.factureStartProcessing = factureStartProcessing;
			}
			public void setFactureEndProcessing(FactureEnd factureEndProcessing) {
				this.factureEndProcessing = factureEndProcessing;
			}
	

			// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="fileName">Nom du fichier de l'image de la multi-factures.</param>
			/// <param name="outputDir">Dossier de sortie des images à traiter.</param>
			/// <param name="developerSN">Numéro de série du developpeur utilisée pour créer l'instance de l'OCR.</param>
			/// <param name="engineLibPath">Chemin de la librairie utilisée pour créer l'instance de l'OCR.</param>
			/// <param name="createProcess">Induque qu'il faut créer un processus de traitement pour l'extraction des données.</param>
			/// <param name="processFileName">Nom de l'exécutable du processus de traitement des doucments.</param>
			/// <param name="learningEngine">Moteur d'apprentissage qui contient les données apprises des factures.</param>
			public MultiFacture (
						String fileName,
						String outputDir,
						String developerSN,
//						String engineLibPath,
						boolean createProcess,
						String processFileName,
						LearningEngine learningEngine)
			{
				this.fileName = fileName;
				this.outputDir = outputDir;
				this.developerSN = developerSN;
//				this.engineLibPath = engineLibPath;
				this.createProcess = createProcess; 
				this.processFileName = processFileName;
				this.learningEngine = learningEngine;
			
				this.facturesCache = new HashMap<> ();
			}
			
			/// <summary>
			/// Crée une instance de la classe en cours à partir
			/// d'un fichier XML.
			/// </summary>
			/// <param name="xmlFileName">Nom complet du fichier XML.</param>
			/// <returns>Une instance de la classe en cours.</returns>
			public static MultiFacture FromXmlFile (String xmlFileName) throws Exception
			{
				// Charger le document.
				//XmlDocument			xmlDocument = new XmlDocument ();
				//xmlDocument.Load (xmlFileName);
				
				DocumentBuilderFactory dbf= DocumentBuilderFactory.newInstance();
	            DocumentBuilder db = dbf.newDocumentBuilder();
	            Document xmlDocument = db.parse(xmlFileName);
				
				// Noeud racine.
				NodeList childNodes = xmlDocument.getChildNodes();

				if (childNodes.getLength() != 1 && (childNodes.getLength() != 2 ))
					throw new Exception ("Xml document must have only one root node");

			    Node xmlRootNode = childNodes.getLength() == 1 ? childNodes.item(0) : childNodes.item(1);

				if (xmlRootNode.getNodeName() != "factures")
					throw new Exception ("Root xml node must be 'factures'");
				// Créer les factures-infos.
				childNodes = xmlDocument.getElementsByTagName("factures");
				FactureInfo []	factureInfos = new FactureInfo [childNodes.getLength()];
				Node xmlNode;

				for (int i = 0; i < factureInfos.length; i ++)
				{
					xmlNode = childNodes.item(0);

					if (xmlNode.getNodeName() != "facture")
						throw new Exception ("Node name must be 'facture'");

					factureInfos [i] = FactureInfo.FromXmlNode (xmlNode);
				}
				// Créer l'instance de la classe en cours.
				MultiFacture		multiFacture = new MultiFacture (null,
						null,
						null,
//						null,
						false,
						null,
						null
				);
				multiFacture.factureInfos = factureInfos;


				return multiFacture;
			}
			
			
			/// <summary>
			/// Charge les données du cache de l'instance en cours.
			/// </summary>
			/// <returns>True si des données de cache on été trouvée, False sinon.</returns>
//			public boolean LoadCache () throws FileNotFoundException, IOException, ClassNotFoundException
//			{
//				String		cacheFileName = this.fileName + ".stream";
//
//				File f =new File(cacheFileName);
//				if (f.exists())
//				{
//
//					String	outputProcessFileName = Facture.GetOutputProcessFileName(fileName);
//
//					ObjectInputStream stream= new ObjectInputStream(new FileInputStream(outputProcessFileName));
//
//					Object [] data = (Object [])stream.readObject();
//
//					stream.close();
//
//
//
//					this.imageNamesCache =(String [])data [0];
//					this.facturesCache = (HashMap<Integer,FactureCache>) data [1];
//
//					return true;
//				}
//				else
//					return false;
//			}
			
			/// <summary>
			/// Prépare l'image ou l'ensemble des images.
			/// </summary>
			/// <param name="fileName">Nom du fichier de l'image.</param>
			/// <returns>Liste des noms/chemins des images partielles de l'image global.</returns>
//			private String [] _Prepare (String fileName)
//			{
////				String []		docFullFileNames = this.ocrEngine.NativePrepareDocuments (fileName, this.outputDir);
//				String			docFullFileName;
//				// Exttraire le nom du fichier.
//				int				lastIndexOf = fileName.lastIndexOf ('\\');
//				String			multiFactureFileName = fileName.substring (lastIndexOf + 1, fileName.length() - 1 - lastIndexOf);
//				// Renommer les documents.
//				String []		newDocFileNames = new String [docFullFileNames.length];
//				String			newFileName;
//				String			docPath, docFileName;
//
//				for (int i = 0; i < docFullFileNames.length; i ++)
//				{
//					// Construire le nouveau nom.
//					docFullFileName = docFullFileNames [i];
//					lastIndexOf = docFullFileName.lastIndexOf ('\\');
//					docPath = docFullFileName.substring (0, lastIndexOf + 1);
//					docFileName = docFullFileName.substring (lastIndexOf + 1, docFullFileName.length() - 1 - lastIndexOf);
//
//					newFileName = docPath + multiFactureFileName + "." + (i + 1) + "." + docFileName;
//					// Renommer physiquement le fichier.
//					File f = new File(docFullFileName);
//					File newname = new File(newFileName);
//					f.renameTo(newname);
//
//					newDocFileNames [i] = newFileName;
//				}
//
//				return newDocFileNames;
//			}
			
			/// <summary>
			/// Prépare les images.
			/// </summary>
			/// <param name="options">Options du traitement.</param>
			/// <returns>Liste des noms complets des images.</returns>
//			private String [] _PrepareImages (ProcessingOption options) throws FileNotFoundException, IOException
//			{
//				String []			images;
//				String []			imageNames;
//				String				imageFullName;
//				int					lastIndex;
//				FactureCache		factureCache;
//				// Pas de cache.
//				if (this.imageNamesCache == null)
//				{
//					images = _Prepare (this.fileName);
//					imageNames = new String [images.length];
//
//					for (int i = 0; i < images.length; i ++)
//					{
//						imageFullName = images [i];
//						lastIndex = imageFullName.lastIndexOf ("\\");
//						imageNames [i] = imageFullName.substring (lastIndex + 1, imageFullName.length() - 1 - lastIndex);
//						// Sauvegarder les données de l'image dans le cache.
//						factureCache = new FactureCache ();
//						Path path = Paths.get(imageFullName);
//
//						factureCache.setImageStream ( Files.readAllBytes (path));
//						this.facturesCache.put(i, factureCache) ;
//					}
//
//					this.mustFlushCache = true;
//				}
//				// Using cache.
//				else
//				{
//					images = new String [this.imageNamesCache.length];
//					imageNames = new String [images.length];
//					String			imageNamesCache;
//
//					for (int i = 0; i < images.length; i ++)
//					{
//						factureCache = this.facturesCache.get(i);
//						imageNamesCache = this.imageNamesCache [i];
//						imageFullName = this.outputDir + "\\" + imageNamesCache;
//						File f=new File(imageFullName);
//						// Tester le présence du fichier.
//						if (!f.exists ())
//						{
//							imageFullName = Facture.getUniqueFileName (imageFullName);
//							lastIndex = imageFullName.lastIndexOf ("\\");
//							imageNames [i] = imageFullName.substring (lastIndex + 1, imageFullName.length() - 1 - lastIndex);
//
//
//							try (FileOutputStream fos = new FileOutputStream("pathname")) {
//								   fos.write(factureCache.getImageStream());
//								   //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
//								}
//							this.mustFlushCache = true;
//						}
//						else
//							imageNames [i] = imageNamesCache;
//
//						images [i] = imageFullName;
//					}
//				}
//
//				this.imageNamesCache = imageNames;
//				return images;
//			}
			/// <summary>
			/// Commence le traitement simple ou double.
			/// </summary>
			/// <param name="options">Options du traitement.</param>
//			public void StartProcessing (ProcessingOption options) throws Exception
//			{
//				this.ocrEngine = new CppOcrEngine (this.developerSN, this.engineLibPath);
//				this.mustFlushCache = false;
//
//				try
//				{
//					// Préparer les images.
//					String []		images = _PrepareImages (options);
//					// Créer les factures.
//					Facture []	factures = new Facture [images.length];
//
//					for (int i = 0; i < factures.length; i ++)
//					{factures [i] = new Facture (this, i, images [i]);}
//					// Evnénement du début.
//					if (this.multiFactureStartProcessing != null)
//						this.multiFactureStartProcessing.MultiFactureStartProcessing (this, factures);
//					// Lancer le traitement.
//					this.factureInfos = new FactureInfo [factures.length];
//
//					for (int i = 0; i < factures.length; i ++)
//					{this.factureInfos [i] = factures [i].startProcessing (options);}
//					// Vérifier les multi-pages.
//					checkForMultiplePages ();
//					// Dévalider les instances des clés/valeurs non choisies.
//					for(FactureInfo factureInfo : this.factureInfos)
//					{factureInfo.UnvalidKeyValues ();}
//					// Mettre à jour l'apprentissage.
//					learn ();
//					// Mettre à jour les données de l'apprentissage.
//					boolean		drawValues = (options.getProcessingOptioNum() & ProcessingOption.DRAW_VALUES.getProcessingOptioNum()) == ProcessingOption.DRAW_VALUES.getProcessingOptioNum();
//					boolean		debugDrawKeyValues = (options.getProcessingOptioNum() & ProcessingOption.DEBUG_DRAW_KEYVALUES.getProcessingOptioNum()) == ProcessingOption.DEBUG_DRAW_KEYVALUES.getProcessingOptioNum();
//
//					// Dessiner les marques.
//					if (drawValues || debugDrawKeyValues)
//					{
//						boolean			debugDocumentDrawKeyValues = (options.getProcessingOptioNum() & ProcessingOption.DEBUG_DOCUMENT_DRAW_KEYVALUES.getProcessingOptioNum()) == ProcessingOption.DEBUG_DOCUMENT_DRAW_KEYVALUES.getProcessingOptioNum();
//						boolean			debugAllKeyValues = (options.getProcessingOptioNum() & ProcessingOption.DEBUG_ALL_KEYVALUES.getProcessingOptioNum()) == ProcessingOption.DEBUG_ALL_KEYVALUES.getProcessingOptioNum();
//						Facture		facture;
//
//						for(FactureInfo factureInfo : this.factureInfos)
//						{
//							facture = factureInfo.getFacture();
//							// Valeurs destinées à l'utilisateur.
//							if (drawValues)
//								Facture.DrawKeyValues (factureInfo, facture.getFullFileName(), true, false);
//							// Clés/Valeurs de débogages.
//							if (debugDrawKeyValues)
//							{
//								Facture.DrawKeyValues (factureInfo, facture.getFullFileName(), false, debugAllKeyValues);
//								// Images de la détection.
//								if (debugDocumentDrawKeyValues)
//								{
//									Facture.DrawKeyValues (factureInfo, facture.GetDrawFileName (DocumentProcessing.NONE, DocumentRotation.NONE), false, debugAllKeyValues);
//									Facture.DrawKeyValues (factureInfo, facture.GetDrawFileName (DocumentProcessing.NONE, DocumentRotation.ROTATION_1), false, debugAllKeyValues);
//									Facture.DrawKeyValues (factureInfo, facture.GetDrawFileName (DocumentProcessing.NONE, DocumentRotation.ROTATION_2), false, debugAllKeyValues);
//									Facture.DrawKeyValues (factureInfo, facture.GetDrawFileName (DocumentProcessing.FILTER_A, DocumentRotation.NONE), false, debugAllKeyValues);
//									Facture.DrawKeyValues (factureInfo, facture.GetDrawFileName (DocumentProcessing.FILTER_A, DocumentRotation.ROTATION_1), false, debugAllKeyValues);
//									Facture.DrawKeyValues (factureInfo, facture.GetDrawFileName (DocumentProcessing.FILTER_A, DocumentRotation.ROTATION_2), false, debugAllKeyValues);
//									Facture.DrawKeyValues (factureInfo, facture.GetDrawFileName (DocumentProcessing.FILTER_B, DocumentRotation.NONE), false, debugAllKeyValues);
//									Facture.DrawKeyValues (factureInfo, facture.GetDrawFileName (DocumentProcessing.FILTER_B, DocumentRotation.ROTATION_1), false, debugAllKeyValues);
//									Facture.DrawKeyValues (factureInfo, facture.GetDrawFileName (DocumentProcessing.FILTER_B, DocumentRotation.ROTATION_2), false, debugAllKeyValues);
//
//									//Ici J'ai ajouté un autre filtre avec valeur 3 comme résultat de sur | (bitwise or)
//
//									Facture.DrawKeyValues (factureInfo, facture.GetDrawFileName (DocumentProcessing.FILTER_C, DocumentRotation.NONE), false, debugAllKeyValues);
//									Facture.DrawKeyValues (factureInfo, facture.GetDrawFileName (DocumentProcessing.FILTER_C, DocumentRotation.ROTATION_1), false, debugAllKeyValues);
//									Facture.DrawKeyValues (factureInfo, facture.GetDrawFileName (DocumentProcessing.FILTER_C, DocumentRotation.ROTATION_2), false, debugAllKeyValues);
//								}
//							}
//						}
//
//					}
//					// Evnènement de la fin.
//					if (this.multiFactureEndProcessing != null)
//						this.multiFactureEndProcessing.MultiFactureEndProcessing(this, factures, this.factureInfos);
//					// Flusher les données.
//					if (this.mustFlushCache)
//						flushCache (options);
//				}
//				finally
//				{
//					this.ocrEngine.Dispose ();
//					}
//			}
			/// <summary>
			/// Commence un traitement simple.
			/// </summary>
//			public void StartProouticessing () throws Exception
//			{
//				StartProcessing (ProcessingOption.DEFAULT);
//			}
			/// <summary>
			/// Vérifie les factures réparties
			/// sur plusieurs pages. Ceci permet d'éleminer
			/// les factures vides.
			/// </summary>
			private void checkForMultiplePages ()
			{
				List<FactureInfo>		pages = new ArrayList<FactureInfo> ();
				FactureInfo				factureInfo = this.factureInfos [0];
				pages.add (factureInfo);

				FactureInfo				_factureInfo;

				for (int i = 1; i < this.factureInfos.length; i ++)
				{
					factureInfo = this.factureInfos [i];
					_factureInfo = this.factureInfos [i - 1];
					// Continuité de pages.
					if (factureInfo.isHasNumeroFacture() && _factureInfo.isHasNumeroFacture() && factureInfo.getNumeroFacture() == _factureInfo.getNumeroFacture() &&
						factureInfo.isHasDate() && _factureInfo.isHasDate() && factureInfo.getDate() == _factureInfo.getDate() ||
						factureInfo.isHasNumeroPage() && _factureInfo.isHasNumeroPage() && factureInfo.getNumeroPage() == _factureInfo.getNumeroPage() + 1)
					{
						pages.add (factureInfo);
						// Fin.
						if (i == this.factureInfos.length - 1)
							MultiFacture.setHtTvaTtcMultiplesPages (pages);
					}
					// Discontinuité de pages.
					else
					{
						// Mettre à jour les montants.
						MultiFacture.setHtTvaTtcMultiplesPages (pages);

						pages.clear ();
						pages.add (factureInfo);
					}
				}
			}
			
			/// <summary>
			/// Met à jour les montans HT, TVA et TTC de plusieurs pages
			/// supposées appartenir à la même facture.
			/// </summary>
			/// <param name="pages">Liste des pages.</param>
			private static  <A> void setHtTvaTtcMultiplesPages ( List<FactureInfo>  pages)
			{
				// Extraire la facture valide.
				FactureInfo		validFactureInfo = null;

				for(FactureInfo factureInfo : pages)
				{
					if (!factureInfo.isPartialRejected())
					{
						if (validFactureInfo == null || factureInfo.getTtc() > validFactureInfo.getTtc())
							validFactureInfo = factureInfo;
					}
				}
				// Affecter les factures non valides.
				if (validFactureInfo != null)
				{
					for(FactureInfo factureInfo : pages)
					{
						if (validFactureInfo.getHtKeyValue() != null)
							factureInfo.setHtKeyValue( validFactureInfo.getHtKeyValue());
						else
							factureInfo.setHt( validFactureInfo.getHt());

						if (validFactureInfo.getTvaKeyValue() != null)
							factureInfo.setTvaKeyValue( validFactureInfo.getTvaKeyValue());
						else
							factureInfo.setTva ( validFactureInfo.getTva());

						factureInfo.setTtcKeyValue( validFactureInfo.getTtcKeyValue());

						factureInfo.setDetails( factureInfo != validFactureInfo);
					}
				}
			}
			/// <summary>
			/// Met à jour les données de l'apprentissage.
			/// </summary>
//			private void learn ()
//			{
//				if (this.learningEngine.isEnabled())
//				{
//					String				rcs;
//
//					for(FactureInfo factureInfo : this.factureInfos)
//					{
//						rcs = null;
//						// Fixer le fournisseur.
//						if (factureInfo.isHasNumeroRcs())
//							rcs = factureInfo.getNumeroRcs();
//						else if (factureInfo.isHasNumeroSiret())
//							rcs = NumberHelper.GetRcsFromSiret (factureInfo.getNumeroSiret());
//						else if (factureInfo.isHasNumeroTva())
//							rcs = NumberHelper.GetRcsFromTva (factureInfo.getNumeroTva());
//						// Créer ou mettre à jour l'entrée.
//						if (rcs != null)
//						{
//							learn (KeyType.NUMERO_FACTURE, rcs, FactureInfo.getNumeroFactureKeyValue());
//							learn (KeyType.NUMERO_TVA, rcs, factureInfo.getNumeroTvaKeyValue());
//							learn (KeyType.NUMERO_SIRET, rcs, factureInfo.getNumeroSiretKeyValue());
//							learn (KeyType.NUMERO_RCS, rcs, factureInfo.getNumeroRcsKeyValue());
//							learn (KeyType.NUMERO_PAGE, rcs, factureInfo.getNumeroPageKeyValue());
//							learn (KeyType.TTC, rcs, factureInfo.getTtcKeyValue());
//							learn (KeyType.HT, rcs, factureInfo.getHtKeyValue());
//							learn (KeyType.TVA, rcs, factureInfo.getTvaKeyValue());
//							learn (KeyType.TVA_SUPP, rcs, factureInfo.getTvaSuppKeyValue());
//							learn (KeyType.DISCOUNT, rcs, factureInfo.getDiscountKeyValue());
//							learn (KeyType.DATE, rcs, factureInfo.getDateKeyValue());
//							learn (KeyType.TYPE_FACTURE, rcs, factureInfo.getTypeFactureKeyValue());
//						}
//					}
//				}
//			}
			/// <summary>
			/// Traite l'instance clé/valeur <paramref name="keyValue" /> comme
			/// une entrée d'apprentissage.
			/// </summary>
			/// <param name="keyType">Type de la clé.</param>
			/// <param name="rcs">Numéro rcs du fournisseur.</param>
			/// <param name="keyValue">L'instance clé/valeur qui contient la valeur valide. Ce paramètre peut être null.</param>
			private void learn (KeyType keyType, String rcs, KeyValue keyValue)
			{
				if (keyValue != null)
				{
					LearningEntry learningEntry = this.learningEngine.GetEntry (keyType, rcs);
					
					if (learningEntry == null)
					{
						learningEntry = new LearningEntry (rcs);
						this.learningEngine.AddEntry (keyType, learningEntry);
					}

					learningEntry.Update (keyValue.getVirtualValueBlock().getRegion(), keyValue.getVirtualValueBlock().getDocument().getRegion());
				}
			}
			/// <summary>
			/// Retourne l'instance de l'information sur une facture qui se trouve
			/// à la position <paramref name="index" />.
			/// </summary>
			/// <param name="index">Entier qui représente l'indice de l'info d'une facture.</param>
			/// <returns>Une instance de type <see cref="FacFactureInfo" />.</returns>
			public FactureInfo GetFactureInfo (int index)
			{return this.factureInfos [index];}
			/// <summary>
			/// Indique qu'il faut "flusher" les données du cache
			/// et ainsi libérer la mémoire utilisée.
			/// </summary>
			/// <param name="options">Les options du traitement en cours.</param>
			private void flushCache (ProcessingOption options)
			{
				if ((options.getProcessingOptioNum() & ProcessingOption.USE_CACHE.getProcessingOptioNum()) == ProcessingOption.USE_CACHE.getProcessingOptioNum())
				{
					try
					{
						// Sauvegarder le cache.
						
						ObjectOutputStream stream= new ObjectOutputStream(new FileOutputStream(this.fileName + ".stream"));
						
						stream.writeObject(this.imageNamesCache);
						stream.writeObject(this.facturesCache);
	
						
						stream.close();
						
						
					}
					catch (Exception ex)
					{
						flushCache (options);
						}
				}
			}
}
