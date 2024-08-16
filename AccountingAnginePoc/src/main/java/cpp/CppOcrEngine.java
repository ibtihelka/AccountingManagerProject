package cpp;

import All.OcrEngine;
import All.WrapHelper;
import document.Document;
import document.DocumentRotation;

//Nouveau Classe En cours de correction
public class CppOcrEngine extends OcrEngine  //IDisposable{
{
			
			/// <summary>
			/// Objet qui synchronise la creation du thread
			/// qui permet l'exécution des méthodes natives.
			/// </summary>
			private static Object				sync_NativeThread = new Object ();
			/// <summary>
			/// Thread à partir duquel on fait l'accès aux méthodes
			/// natives du moteur.
			/// </summary>
			private static Thread				NativeThread;
			/// <summary>
			/// L'objet de synchronisation qui permet le
			/// lancement de l'appel native par le thread
			/// des appels natives <see cref="_NativeThread" />.
			/// </summary>
			private static AutoResetEvent		sync_BeginNativeCall = new AutoResetEvent (false);
			/// <summary>
			/// L'objet de synchronisation qui permet d'indiquer
			/// la fin de l'appel native du thread
			/// des appels natives <see cref="_NativeThread" />.
			/// </summary>
			private static AutoResetEvent		sync_EndNativeCall = new AutoResetEvent (false);
			/// <summary>
			/// Type de l'appel native. Ce attribut est utilisé
			/// comme argument par le thread d'appel native <see cref="_NativeThread" />.
			/// </summary>
			private static NativeCallType	NativeCallType;
			/// <summary>
			/// Arguments d'entrée de l'appel native.
			/// </summary>
			private static Object				NativeCallInArgs;
			/// <summary>
			/// Arguments de sortie de l'appel native.
			/// </summary>
			private static Object				NativeCallOutArgs;
			private Object EngineObject;


			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="developerSN">Numéro de série du developpeur.</param>
			/// <param name="engineLibPath">Chemin de la librairie.</param>
			public CppOcrEngine (String developerSN, String engineLibPath) 
			{
				super(developerSN,engineLibPath,true);
			
			}

			/// <summary>
			/// Appel native en utilisant le thread <see cref="_NativeThread" />.
			/// </summary>
			/// <param name="callType">Le type de l'appel.</param>
			/// <param name="inPtr">Pointeur vers les arguments d'entrée.</param>
			/// <returns>Pointeur vers les arguments de sortie.</returns>
			private static Object NativeCall (NativeCallType callType, Object inPtr) throws InterruptedException
			{
				synchronized (CppOcrEngine.sync_NativeThread)
				{
					CppOcrEngine.NativeCallType = callType;
					CppOcrEngine.NativeCallInArgs = inPtr;

					CppOcrEngine.sync_BeginNativeCall.set ();
					CppOcrEngine.sync_EndNativeCall.waitOne ();

					return CppOcrEngine.NativeCallOutArgs;
				}
			}
			/// <summary>
			/// Arrête le thread des appels natives.
			/// </summary>
			public static void StopNativeThread () throws InterruptedException
			{
				synchronized (CppOcrEngine.sync_NativeThread)
				{
					if (CppOcrEngine.NativeThread != null)
					{
						CppOcrEngine.NativeThread.stop();
						CppOcrEngine.NativeThread.join ();
					}
				}
			}
			/// <summary>
			/// Crée une nouvelle instance du moteur en utilisant
			/// un numéro de série.
			/// </summary>
			/// <param name="inPtr">Pointeur d'entrée contenant les données des paramètres de la fonction.</param>
			/// <returns>Pointeur de sortie contenant les données retournées par la fonction.</returns>
			//[DllImport ("Projects.FacDetector.OcrWrapper.dll", EntryPoint="nativeCreateEngine")]
			public  static Object NativeCreateEngine (Object inPtr) {
				// TODO Auto-generated method stub
				return null;
			}
			/// <summary>
			/// Crée en native une nouvelle instance
			/// du moteur en utilisant un numéro de série.
			/// </summary>
			/// <param name="developerSN">Numéro de série du developpeur.</param>
			/// <param name="engineLibPath">Chemin de la librairie.</param>
			@Override 
			public Object NativeCreateEngine (String developerSN, String engineLibPath)
			{
				Object		nativeIn = WrapHelper.GetObjectWrapper (new Object [] {developerSN, engineLibPath});
				Object nativeOut = null;
				try {
					nativeOut = CppOcrEngine.NativeCall (cpp.NativeCallType.CreateEngine, nativeIn);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return WrapHelper.GetObject(nativeOut);
			}
			/// <summary>
			/// Prépare en native le fichier passée en paramètre pour être traité
			/// par la suite.
			/// </summary>
			/// <param name="inPtr">Pointeur d'entrée contenant les données des paramètres de la fonction.</param>
			/// <returns>Pointeur de sortie contenant les données retournées par la fonction.</returns>
			public   static Object NativePrepareDocuments (Object inPtr) {
				// TODO Auto-generated method stub
				return null;
			}
			/// <summary>
			/// Prépare en native les documents en utilisant passée en paramètre pour être traité
			/// par la suite. Elle décompose le fichier en plusieurs pages
			/// de types images.
			/// </summary>
			/// <param name="fileName">Nom du fichier à traiter.</param>
			/// <param name="outputDir">Nom du dossier destination.</param>
			/// <returns>Nom des fichiers images des différentes pages.</returns>
			@Override
			public  String [] NativePrepareDocuments (String fileName, String outputDir)
			{
//				Object		nativeIn = WrapHelper.GetObjectWrapper (new Object [] {this.EngineObject.Value, fileName, outputDir});
//				Object		nativeOut = CppOcrEngine.NativeCall (NativeCallType.PrepareDocuments, nativeIn);
//
//				return WrapHelper.GetObject<String []> (nativeOut);
				return null;
			}
			/// <summary>
			/// Applique le filtre A <see cref="FacDocumentProcessing.FILTER_A" />
			/// en native sur une image dont les données des ses pixels sont passées
			/// en arguments.
			/// </summary>
			/// <param name="rgb">Tableau des données des composantes r/g/b.</param>
			/// <param name="width">Largeur de l'image.</param>
			/// <param name="height">Hauteur de l'image.</param>
			/// <remarks>Les tableaux d'entrées seront utilisées comme tableaux de sorties.</remarks>
			@Override
			public void NativeApplyFilterA (byte [] rgb, int width, int height)
			{
				BlurFilter		blurFilter = new BlurFilter (2, 2);
				blurFilter.Blur (rgb, width, height);
			}
			/// <summary>
			/// Crée en native un document en analysant le fichier passé en paramètre.
			/// </summary>
			/// <param name="inPtr">Pointeur d'entrée contenant les données des paramètres de la fonction.</param>
			/// <returns>Pointeur de sortie contenant les données retournées par la fonction.</returns>
			//[DllImport ("Projects.FacDetector.OcrWrapper.dll", EntryPoint="nativeCreateDocument")]
			public  static Object NativeCreateDocument (Object inPtr) {
				// TODO Auto-generated method stub
				return null;
			}
			
			/// <summary>
			/// Crée l'instance racine des blocs <see cref="FacDocument" /> après
			/// l'analyse du fichier passé en paramètre <paramref name="imageFileName" />.
			/// </summary>
			/// <param name="imageFileName">Nom du fichier de l'image à traiter.</param>
			/// <param name="rotationType">Indique le type de rotation.</param>
			/// <returns>
			/// Une instance de type <see cref="FacDocument" /> qui contient les blocs
			/// de l'image après analyse.
			/// </returns>
			
			@Override
			public  Document NativeCreateDocument (String imageFileName, DocumentRotation rotationType)
			{
				Object		nativeIn = WrapHelper.GetObjectWrapper (new Object [] {this.EngineObject, imageFileName, rotationType});
				Object nativeOut = null;
				try {
					nativeOut = CppOcrEngine.NativeCall (cpp.NativeCallType.CreateDocument, nativeIn);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return WrapHelper.GetObject (nativeOut);
			}
			/// <summary>
			/// Met fin en native à l'utilisation de l'instance de l'ocr
			/// retourneé par la méthode <see cref="FacOcrEngine.NativeCreateEngine" />.
			/// </summary>
			/// <param name="inPtr">Pointeur d'entrée contenant les données des paramètres de la fonction.</param>
			/// <returns>Pointeur de sortie contenant les données retournées par la fonction.</returns>
			//[DllImport ("Projects.FacDetector.OcrWrapper.dll", EntryPoint="nativeReleaseEngine")]
			public  static void NativeRelease (Object inPtr) {
				// TODO Auto-generated method stub
				
			}
			
			/// <summary>
			/// Met fin à l'utilisation de l'instance de l'ocr
			/// retourneé par la méthode <see cref="FacOcrEngine.NativeCreateEngine" />.
			/// </summary>
			
			@Override
			public void NativeRelease ()
			{
				if (this.EngineObject != null)
				{
					Integer nativeIn = (Integer) WrapHelper.GetObjectWrapper (new Object [] {this.EngineObject});
					try {
						CppOcrEngine.NativeCall (cpp.NativeCallType.Release, nativeIn);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			/// <summary>
			/// Exécute les tâches définies par l'application associées
			/// à la libération ou à la redéfinition des ressources non managées.
			/// </summary>
			/// <remarks>
			/// Cette méthode met libère la mémoire de l'instance en cours
			/// du moteur. Elle s'exécute automatiquement
			/// lors de la deconstruction de l'instance en cours.
			/// </remarks>
			@Override
			public void Dispose ()
			{
				if (!this.isDisposed)
					NativeRelease ();

				this.Dispose ();
			}
			/// <summary>
			/// Libère les ressources non mamangées utilisées
			/// par l'instance en cours. Le deconstructeur exécute
			/// la méthode <see cref="Dispose" />.
			/// </summary>
			public CppOcrEngine()
			{	super();
				Dispose ();
			}
			

			
		}
	

