package All;

import java.io.Serializable;

public class GenericException extends Exception implements Serializable {
			
			/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
			/// <summary>
			/// Obtient
			/// le message d'erreur qui explique la raison de l'exception.
			/// Le message de l'exception interne sera inclus.
			/// </summary>
			@Override
			public String getMessage(){
				
				
					 return super.getMessage();
			
			}

			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="message">Message de l'exception.</param>
			/// <param name="innerException">Exception interne.</param>
			public GenericException (String message, Exception innerException) {
				
				super (message, innerException);
			}
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="message">Message de l'exception.</param>
			public GenericException (String message) 
			{
				 this (message, null);
			}
}