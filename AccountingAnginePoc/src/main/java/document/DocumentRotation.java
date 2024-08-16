package document;

public enum DocumentRotation {

			/// <summary>
			/// Par de rotation.
			/// </summary>
			NONE (0),
			/// <summary>
			/// Rotation de Pi/2.
			/// </summary>
			ROTATION_1(1),
			/// <summary>
			/// Rotation de 3Pi/2.
			/// </summary>
			ROTATION_2(2);
			private int numDocumentRotation;

			private DocumentRotation(int numDocumentRotation) {
				this.numDocumentRotation = numDocumentRotation;
			}

			public int getNumDocumentRotation() {
				return numDocumentRotation;
			}
			
}
