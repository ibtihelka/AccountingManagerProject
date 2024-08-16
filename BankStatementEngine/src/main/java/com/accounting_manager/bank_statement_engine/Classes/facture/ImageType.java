package com.accounting_manager.bank_statement_engine.Classes.facture;

import java.io.Serializable;

public enum ImageType implements Serializable {
	/// <summary>
	/// Image dont seulement les
	/// valeurs s�lectionn�es sont marqu�es.
	/// </summary>
	VALUES,
	/// <summary>
	/// Image dont seulement les
	/// cl�s/valeurs s�lectionn�es sont marqu�es.
	/// </summary>
	KEYVALUES,
	/// <summary>
	/// Image dont toutes les
	/// cl�s/valeurs valides sont marqu�es.
	/// </summary>
	ALL_KEYVALUES

}
