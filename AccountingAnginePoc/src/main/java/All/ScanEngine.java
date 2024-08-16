package All;

import java.io.Serializable;

public abstract class ScanEngine implements Serializable {
			/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
			/// <summary>
			/// Moteur du scan.
			/// </summary>
			private	Object				engineScanObject;
			/// <summary>
			/// Indique que l'instance en cours a été disposée.
			/// </summary>
			protected boolean				isDisposed = true;
			
			public Object getEngineScanObject() {
				return engineScanObject;
			}
			public boolean isDisposed() {
				return isDisposed;
			}
			public void setEngineScanObject(Object engineScanObject) {
				this.engineScanObject = engineScanObject;
			}
			public void setDisposed(boolean isDisposed) {
				this.isDisposed = isDisposed;
			}
			
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="engineObject">Le moteur d'engine.</param>
			public ScanEngine (Object engineObject)
			{
				this.engineScanObject = NativeCreateScanManager (engineObject);
				this.isDisposed = false;
			}
			/// <summary>
			/// Crée en native une nouvelle instance
			/// de l'objet scan en utilisant l'objet du moteur Engine.
			/// </summary>
			/// <param name="engineObject">Le moteur d'engine.</param>
			/// <returns>Le moteur du scan.</returns>
			public abstract Object	NativeCreateScanManager (Object engineObject);
			
			/// <summary>
			/// Trouve tous les scanners installés dans la machine.
			/// </summary>
			/// <returns>Liste des scanners.</returns>
			public abstract String [] NativeGetScannersList ();
			/// <summary>
			/// Exécute le scan.
			/// </summary>
			/// <param name="selectedScanner">Nom du scanner choisie.</param>
			/// <param name="scanFolder">Répertoire résultat.</param>
			/// <param name="scanMultiplePage">Indique l'autorisation du scan avec multipage.</param>
			public abstract void NativeScan (String selectedScanner, String scanFolder, boolean scanMultiplePage);
			/// <summary>
			/// Met fin à l'utilisation de l'instance du scan
			/// retourneé par la méthode <see cref="NativeCreateScanManager" />.
			/// </summary>
			public abstract void NativeRelease ();
			/// <summary>
			/// Libère les ressources utilisées par l'instance en cours.
			/// </summary>
			public void Dispose (){
				
				this.isDisposed = true;
				
			}
			
			

}