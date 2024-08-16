package facture;

import java.io.Serializable;

public enum ImageType implements Serializable {
			/// <summary>
			/// Image dont seulement les
			/// valeurs sélectionnées sont marquées.
			/// </summary>
			VALUES,
			/// <summary>
			/// Image dont seulement les
			/// clés/valeurs sélectionnées sont marquées.
			/// </summary>
			KEYVALUES,
			/// <summary>
			/// Image dont toutes les
			/// clés/valeurs valides sont marquées.
			/// </summary>
			ALL_KEYVALUES

}
