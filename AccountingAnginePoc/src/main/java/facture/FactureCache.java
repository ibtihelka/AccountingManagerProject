package facture;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

	/// <summary>
	/// Classe qui représente le cache
	/// d'une facture à traiter.
	/// </summary>
	
public class FactureCache implements Serializable {
			/// <summary>
			/// Flux de données de l'image de la facture.
			/// </summary>
			private byte []							imageStream;
			/// <summary>
			/// Flux de données du document de la facture.
			/// Les flux seront stockés en utilisant un mot
			/// clé indentifiant les differéntes méthodes de filtrage
			/// et rotation.
			/// </summary>
			private Map<String,byte []>		docStreams;
			
			// <summary>
			/// Obtient ou définit
	        /// le flux de données de l'image de la facture.
			/// </summary>
			public byte []		getImageStream()
			{
				return this.imageStream;
				
			}
			public void setImageStream(byte [] imageStream) {
				this.imageStream = imageStream;
			}

			// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			public FactureCache ()
			{
				this.docStreams = new HashMap<String,byte []> ();
			
			}
			
			/// <summary>
			/// Retourne le flux de données du document
			/// nommé <paramref name="streamName" />.
			/// </summary>
			/// <param name="streamName">Nom du flux de données.</param>
			/// <returns>Tableau qui contient les données dux flux. Null si le flux n'a pas été stocké.</returns>
			public byte [] GetDocStream (String streamName)
			{
				byte []		streamBytes=null;

				if (this.docStreams.containsKey (streamName))
					return streamBytes;
				else
					return null;
			}
			/// <summary>
			/// Stocke un flux de données du document
			/// en utilisant un mot clé <paramref name="streamName" />.
			/// </summary>
			/// <param name="streamName">Nom du flux de données.</param>
			/// <param name="streamBytes">Flux de données à stocker.</param>
			public void SetDocStream (String streamName, byte [] streamBytes)
			{this.docStreams.put(streamName, streamBytes) ;}
			
			
}
