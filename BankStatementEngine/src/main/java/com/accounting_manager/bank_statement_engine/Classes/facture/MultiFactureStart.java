package com.accounting_manager.bank_statement_engine.Classes.facture;

@FunctionalInterface
public interface MultiFactureStart {

	/// <summary>
	/// D�l�gu� sur une m�thode � �x�cuter lorsque le traitement
	/// sur une multi-facture vient de commencer.
	/// </summary>
	/// <param name="multiFacture">L'instance de la multi-facture.</param>
	/// <param name="factures">Liste des factures � traiter.</param>
	public  void MultiFactureStartProcessing (MultiFacture multiFacture, Facture [] factures);


}
