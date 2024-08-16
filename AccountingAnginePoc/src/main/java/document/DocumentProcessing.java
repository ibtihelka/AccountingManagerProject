package document;

import java.io.Serializable;

/// <summary>
	/// Enumère les types de traitement d'un document.
/// </summary>
public enum DocumentProcessing implements Serializable {
	
	/// <summary>
	/// Pas de filtre.
	/// </summary>
	NONE(0),
	/// <summary>
	/// Filtre A.
	/// </summary>
	FILTER_A(1),
	/// <summary>
	/// Filtre B.
	/// </summary>
	FILTER_B(2),
	/// <summary>
		/// bitwise or Filtre A et Filtre B
		/// </summary>
	FILTER_C(3);
	
	
	
	private int numDocumentProcessing;

	private DocumentProcessing(int numDocumentProcessing) {
		this.numDocumentProcessing = numDocumentProcessing;
	}

	public int getNumDocumentProcessing() {
		return numDocumentProcessing;
	}

	public void setNumDocumentProcessing(int numDocumentProcessing) {
		this.numDocumentProcessing = numDocumentProcessing;
	}
	
	
	
}
