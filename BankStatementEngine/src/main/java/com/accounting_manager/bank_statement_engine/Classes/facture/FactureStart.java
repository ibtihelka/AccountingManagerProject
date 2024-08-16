package com.accounting_manager.bank_statement_engine.Classes.facture;

@FunctionalInterface
public interface FactureStart {

	/// <summary>
	/// D�l�gu� sur une m�thode � �x�cuter lorsque le traitement
	/// sur une facture vient de commencer.
	/// </summary>
	/// <param name="facture">L'instance de la facture.</param>
	public  void FactureStartProcessing (Facture facture);
}
