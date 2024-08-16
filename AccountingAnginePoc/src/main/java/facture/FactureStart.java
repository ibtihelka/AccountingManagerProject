package facture;

@FunctionalInterface
public interface FactureStart {

			/// <summary>
			/// Délégué sur une méthode à éxécuter lorsque le traitement
			/// sur une facture vient de commencer.
			/// </summary>
			/// <param name="facture">L'instance de la facture.</param>
			public  void FactureStartProcessing (Facture facture);
}
