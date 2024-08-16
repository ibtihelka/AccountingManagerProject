package com.accounting_manager.accounting_engine.Classes.facture;

@FunctionalInterface
public interface FactureEnd {


	/// <summary>
	/// D�l�gu� sur une m�thode � �x�cuter lorsque le traitement
	/// sur une facture vient de finir.
	/// </summary>
	/// <param name="facture">L'instance de la facture.</param>
	/// <param name="factureInfo">L'instance qui contient les informations de la facture.</param>
	public  void FactureEndProcessing (Facture facture, FactureInfo factureInfo);

}
