package document;

import java.io.Serializable;

public enum DocumentZone implements Serializable {
			/// <summary>
			/// Pas de zone précise.
			/// </summary>
			UNDEFINED (0),
			/// <summary>
			/// C'est une region préféré situe en haut et à gauche.
			/// </summary>
			TOP_LEFT (1),
			/// <summary>
			/// C'est une region préféré situe en haut et au centre.
			/// </summary>
			TOP_CENTER (2),
			/// <summary>
			/// C'est une region préféré situe en haut et à droite.
			/// </summary>
			TOP_RIGHT (4),
			/// <summary>
			/// C'est une region préféré situe à la moyenne et à gauche.
			/// </summary>
			MIDDLE_LEFT (8),
			/// <summary>
			/// C'est une region préféré situe à la moyenne et au centre.
			/// </summary>
			MIDDLE_CENTER (16),
			/// <summary>
			/// C'est une region préféré situe à la moyenne et à droite.
			/// </summary>
			MIDDLE_RIGHT (32),
			/// <summary>
			/// C'est une region préféré situe en bas et à gauche.
			/// </summary>
			BOTTOM_LEFT (64),
			/// <summary>
			/// C'est une region préféré situe en bas et au centre.
			/// </summary>
			BOTTOM_CENTER (128),
			/// <summary>
			/// C'est une region préféré situe en bas et à gauche.
			/// </summary>
			BOTTOM_RIGHT (256);
	
			private int numDocumentZone;

			private DocumentZone(int numDocumentZone) {
				this.numDocumentZone = numDocumentZone;
			}
			private DocumentZone() {
				
			}

			public int getNumDocumentZone() {
				return numDocumentZone;
			}

			public void setNumDocumentZone(int numDocumentZone) {
				this.numDocumentZone = numDocumentZone;
			}
			
			
			

}
