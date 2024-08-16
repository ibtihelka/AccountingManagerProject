package com.accounting_manager.accounting_engine.Classes.key;

import java.io.Serializable;

public enum KeyType  implements Serializable {
	/// <summary>
			/// Num�ro facture (Lettre : nf). 
			/// </summary>
			NUMERO_FACTURE,
			/// <summary>
			/// Num�ro TVA (Lettre : nt).
			/// </summary>
			NUMERO_TVA,
			/// <summary>
			/// Num�ro siret (Lettre : ns).
			/// </summary>
			NUMERO_SIRET,
			/// <summary>
			/// Num�ro rcs (Lettre : nr).
			/// </summary>
			NUMERO_RCS,
			/// <summary>
			/// Montant TTC (Lettre : tt).
			/// </summary>
			TTC,
			/// <summary>
			/// Montant HT (Lettre : ht).
			/// </summary>
			HT,
			/// <summary>
			/// Montant TVA (Lettre : tv).
			/// </summary>
			TVA,
			/// <summary>
			/// Montant remise (Lettre : re).
			/// </summary>
			DISCOUNT,
			/// <summary>
			/// Montant tva suppl�mentaire (Lettre : ts).
			/// </summary>
			TVA_SUPP,
			/// <summary>
			/// Date facture (Lettre : da).
			/// </summary>
			DATE,
			/// <summary>
			/// Type de facture : facture ou avoir (Lettre : tf).
			/// </summary>
			TYPE_FACTURE,
			/// <summary>
			/// Num�ro du page de la facture (Lettre : np).
			/// </summary>
			NUMERO_PAGE,
			/// <summary>
			/// Montant.
			/// </summary>
			AMOUNT

}
