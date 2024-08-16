package facture;

@FunctionalInterface
public interface MultiFactureEnd {
	
	/// <summary>
	/// Délégué sur une méthode à éxécuter lorsque le traitement
	/// sur une multi-facture vient de finir.
	/// </summary>
	/// <param name="multiFacture">L'instance de la multi-facture.</param>
	/// <param name="factures">Liste des factures à traiter.</param>
	/// <param name="factureInfos">Liste des informations trouvées sur les factures.</param>
	public  void MultiFactureEndProcessing (MultiFacture multiFacture,Facture [] factures, FactureInfo [] factureInfos);

}
