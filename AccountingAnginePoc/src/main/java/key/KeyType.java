package key;

import java.io.Serializable;

public enum KeyType  implements Serializable {
	/// <summary>
			/// Numéro facture (Lettre : nf). 
			/// </summary>
			NUMERO_FACTURE,
			/// <summary>
			/// Numéro TVA (Lettre : nt).
			/// </summary>
			NUMERO_TVA,
			/// <summary>
			/// Numéro siret (Lettre : ns).
			/// </summary>
			NUMERO_SIRET,
			/// <summary>
			/// Numéro rcs (Lettre : nr).
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
			/// Montant tva supplémentaire (Lettre : ts).
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
			/// Numéro du page de la facture (Lettre : np).
			/// </summary>
			NUMERO_PAGE,
			/// <summary>
			/// Montant.
			/// </summary>
			AMOUNT

}
