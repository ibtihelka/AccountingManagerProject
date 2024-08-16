package com.accounting_manager.bank_statement_engine.Classes.key;

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
	/// Numero de compte (Lettre : nc).
	/// </summary>
	ACCOUNT_NUMBER,
	/// <summary>
	/// BIC.
	/// </summary>
	BIC_NUMBER,
	/// <summary>
	/// Numero RIB (Lettre : ni).
	/// </summary>
	RIB_NUMBER,
	/// <summary>
	/// Numero IBAN (Lettre : ni).
	/// </summary>
	IBAN_NUMBER,
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
	/// Bank statement date (Lettre : da).
	/// </summary>
	DATE,
	/// <summary>
	/// Bank statement period start date (Lettre : dd).
	/// </summary>
	PERIOD_START_DATE,
	/// <summary>
	/// Bank statement period end date (Lettre : df).
	/// </summary>
	PERIOD_END_DATE,
	/// <summary>
	/// Type de facture : facture ou avoir (Lettre : tf).
	/// </summary>
	TYPE_FACTURE,
	/// <summary>
	/// Num�ro du page de la facture (Lettre : np).
	/// </summary>
	NUMERO_PAGE,

}
