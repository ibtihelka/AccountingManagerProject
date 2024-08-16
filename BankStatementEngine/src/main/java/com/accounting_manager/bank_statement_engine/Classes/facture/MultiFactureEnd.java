package com.accounting_manager.bank_statement_engine.Classes.facture;

@FunctionalInterface
public interface MultiFactureEnd {

	/// <summary>
	/// D�l�gu� sur une m�thode � �x�cuter lorsque le traitement
	/// sur une multi-facture vient de finir.
	/// </summary>
	/// <param name="multiFacture">L'instance de la multi-facture.</param>
	/// <param name="factures">Liste des factures � traiter.</param>
	/// <param name="factureInfos">Liste des informations trouv�es sur les factures.</param>
	public  void MultiFactureEndProcessing (MultiFacture multiFacture,Facture [] factures, FactureInfo [] factureInfos);

}
