package cpp;


/// <summary>
	/// Enumère les types d'un appel aux méthodes natives.
	/// </summary>
public enum NativeCallType {
	
	/// <summary>
			/// Appel de la méthode <see cref="FacCppOcrEngine.NativeCreateEngine (IntPtr)" />.
			/// </summary>
			CreateEngine,
			/// <summary>
			/// Appel de la méthode <see cref="FacCppOcrEngine.NativePrepareDocuments (IntPtr)" />.
			/// </summary>
			PrepareDocuments,
			/// <summary>
			/// Appel de la méthode <see cref="FacCppOcrEngine.NativeCreateDocument (IntPtr)" />.
			/// </summary>
			CreateDocument,
			/// <summary>
			/// Appel de la méthode <see cref="FacCppOcrEngine.NativeRelease (IntPtr)" />.
			/// </summary>
			Release
			

}
