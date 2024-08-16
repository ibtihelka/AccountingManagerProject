package com.accounting_manager.accounting_engine.Classes.document;

import java.io.Serializable;

public enum DocumentZone implements Serializable {
			/// <summary>
			/// Pas de zone pr�cise.
			/// </summary>
			UNDEFINED (0),
			/// <summary>
			/// C'est une region pr�f�r� situe en haut et � gauche.
			/// </summary>
			TOP_LEFT (1),
			/// <summary>
			/// C'est une region pr�f�r� situe en haut et au centre.
			/// </summary>
			TOP_CENTER (2),
			/// <summary>
			/// C'est une region pr�f�r� situe en haut et � droite.
			/// </summary>
			TOP_RIGHT (4),
			/// <summary>
			/// C'est une region pr�f�r� situe � la moyenne et � gauche.
			/// </summary>
			MIDDLE_LEFT (8),
			/// <summary>
			/// C'est une region pr�f�r� situe � la moyenne et au centre.
			/// </summary>
			MIDDLE_CENTER (16),
			/// <summary>
			/// C'est une region pr�f�r� situe � la moyenne et � droite.
			/// </summary>
			MIDDLE_RIGHT (32),
			/// <summary>
			/// C'est une region pr�f�r� situe en bas et � gauche.
			/// </summary>
			BOTTOM_LEFT (64),
			/// <summary>
			/// C'est une region pr�f�r� situe en bas et au centre.
			/// </summary>
			BOTTOM_CENTER (128),
			/// <summary>
			/// C'est une region pr�f�r� situe en bas et � gauche.
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
