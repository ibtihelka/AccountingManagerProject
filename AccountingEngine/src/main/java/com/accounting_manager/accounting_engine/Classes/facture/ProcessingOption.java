package com.accounting_manager.accounting_engine.Classes.facture;

import java.io.Serializable;

/// <summary>
/// Enum�re les options
/// du traitement d'un document.
/// </summary>
public enum ProcessingOption implements Serializable {
	
	/// <summary>
			/// Pas d'options.
			/// </summary>
			NONE (0),
			/// <summary>
			/// Indique qu'il faut utiliser le cache.
			/// </summary>
			USE_CACHE ( 2),
			/// <summary>
			/// Indique qu'il faut dessiner le document
			/// apr�s reconnaissance.
			/// </summary>
			DRAW_DOCUMENT (4),
			/// <summary>
			/// Indique qu'il faut dessiner les
			/// valeurs s�lectionn�es. Ce drapeau sera
			/// utilis� pour faire l'affichage destin�
			/// � l'utilisateur.
			/// </summary>
			DRAW_VALUES (8),
			/// <summary>
			/// Indique qu'il faut dessiner les
			/// cl�s/valeurs s�lectionn�es dans
			/// le but de faire le d�bogage.
			/// </summary>
			DEBUG_DRAW_KEYVALUES ( 16),
			/// <summary>
			/// Indique qu'il faut dessiner les
			/// cl�s/valeurs s�lectionn�es sur
			/// le document dessin� dans le but
			/// de faire du debogage.
			/// </summary>
			DEBUG_DOCUMENT_DRAW_KEYVALUES ( 32),
			/// <summary>
			/// Indique qu'il faut dessiner tous les
			/// cl�s/valeurs valides trouv�es.
			/// </summary>
			DEBUG_ALL_KEYVALUES ( 64),
			/// <summary>
			/// Options par d�faut c'est une combinaison
			/// entre les types <see cref="USE_CACHE" />,
			/// <see cref="DRAW_DOCUMENT" /> et <see cref="DRAW_VALUES" />.
			/// </summary>
			DEFAULT ( 2 | 4 | 8);
			private int processingOptioNum;

			private ProcessingOption(int processingOptioNum) {
				this.processingOptioNum = processingOptioNum;
			}
			private ProcessingOption() {
				
			}
			public int getProcessingOptioNum() {
				return processingOptioNum;
			}
			public void setProcessingOptioNum(int processingOptioNum) {
				this.processingOptioNum = processingOptioNum;
			}
			
			

}
