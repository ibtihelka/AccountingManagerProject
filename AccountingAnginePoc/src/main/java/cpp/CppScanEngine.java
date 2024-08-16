package cpp;

import All.ScanEngine;
import All.WrapHelper;

//Nouveau Classe En cours de correction
public  class CppScanEngine extends ScanEngine {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8568002464393452254L;
	/// <summary>
	/// Obtient
    /// le moteur du scan.
	/// </summary>
	Object EngineScanObject;
	
	/// <summary>
	/// Initialise une nouvelle instance de la classe
	/// avec des paramètres spécifiés.
	/// </summary>
	/// <param name="engineObject">Le moteur d'engine.</param>
	public CppScanEngine(Object engineObject) {
		super(engineObject);
		// TODO Auto-generated constructor stub
	}

	/// <summary>
	/// Crée en native une nouvelle instance
	/// de l'objet du scan en utilisant l'objet du moteur Engine.
	/// </summary>
	/// <param name="inPtr">Pointeur d'entrée contenant les données des paramètres de la fonction.</param>
	/// <returns>Pointeur de sortie contenant les données retournées par la fonction.</returns>
	public static  Integer NativeCreateScanManager (Integer inPtr) {
		return null;
		
	}
	/// <summary>
	/// Crée en native une nouvelle instance
	/// de l'objet du scan en utilisant l'objet du moteur Engine.
	/// </summary>
	/// <param name="engineObject">Objet du moteur.</param>
	/// <returns>Objet de scanner.</returns>
	
	@Override
	public Object NativeCreateScanManager (Object engineObject)
	{
		Integer		nativeIn = (Integer) WrapHelper.GetObjectWrapper (new Object [] {engineObject});
		Integer		nativeOut = CppScanEngine.NativeCreateScanManager (nativeIn);

		return WrapHelper.GetObject(nativeOut);
	}
	/// <summary>
	/// Trouve tous les scanners installés dans la machine.
	/// </summary>
	/// <param name="inPtr">Pointeur d'entrée contenant les données des paramètres de la fonction.</param>
	/// <returns>Pointeur de sortie contenant les données retournées par la fonction.</returns>
	public static  Integer nativeGetScannersList (Integer inPtr) {
		return null;
		
	}
	/// <summary>
	/// Trouve tous les scanners installés dans la machine.
	/// </summary>
	/// <returns>Liste des scanner.</returns>
	
	@Override
	public String [] NativeGetScannersList ()
	{
		Integer		nativeIn = (Integer) WrapHelper.GetObjectWrapper (new Object [] 
				{
				 this.EngineScanObject
				});
		Integer		nativeOut = CppScanEngine.nativeGetScannersList (nativeIn);

		return WrapHelper.GetObject(nativeOut);
	}
	/// <summary>
	/// Exécute le scan.
	/// </summary>
	/// <param name="inPtr">Pointeur d'entrée contenant les données des paramètres de la fonction.</param>
	/// <returns>Pointeur de sortie contenant les données retournées par la fonction.</returns>
	public static  Integer NativeScan (Integer inPtr) {
		return null;
		
	}
	/// <summary>
	/// Exécute le scan.
	/// </summary>
	/// <param name="selectedScanner">Nom du scanner choisie.</param>
	/// <param name="scanFolder">Répertoire résultat.</param>
	/// <param name="scanMultiplePage">Indique l'autirisation de scanner avec multipage.</param>
	
	@Override
	public void NativeScan (String selectedScanner, String scanFolder, boolean scanMultiplePage)
	{
		Integer		nativeIn = (Integer) WrapHelper.GetObjectWrapper (new Object [] 
				{
				this.EngineScanObject, selectedScanner, scanFolder, scanMultiplePage
				});
		CppScanEngine.NativeScan (nativeIn);
	}
	/// <summary>
	/// Met fin en native à l'utilisation de l'instance de l'ocr
	/// retourneé par la méthode <see cref="FacOcrEngine.NativeCreateEngine" />.
	/// </summary>
	/// <param name="inPtr">Pointeur d'entrée contenant les données des paramètres de la fonction.</param>
	/// <returns>Pointeur de sortie contenant les données retournées par la fonction.</returns>
	public static  void NativeRelease (Integer inPtr) {
	}
	/// <summary>
	/// Met fin à l'utilisation de l'instance de l'ocr
	/// retourneé par la méthode <see cref="FacOcrEngine.NativeCreateEngine" />.
	/// </summary>
	@Override
	public void NativeRelease ()
	{
		Object		nativeIn = WrapHelper.GetObjectWrapper (new Object [] {this.EngineScanObject});
		CppScanEngine.NativeRelease ((Integer) nativeIn);
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
	public  void Dispose ()
	{
		if (!this.isDisposed())
			NativeRelease ();

		this.Dispose ();
	}
}
	
