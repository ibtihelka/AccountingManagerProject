package facture;

@FunctionalInterface
public interface MultiFactureStart {

			/// <summary>
			/// Délégué sur une méthode à éxécuter lorsque le traitement
			/// sur une multi-facture vient de commencer.
			/// </summary>
			/// <param name="multiFacture">L'instance de la multi-facture.</param>
			/// <param name="factures">Liste des factures à traiter.</param>
			public  void MultiFactureStartProcessing (MultiFacture multiFacture, Facture [] factures);
			
			
}
