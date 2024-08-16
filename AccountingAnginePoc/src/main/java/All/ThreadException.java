package All;

import java.io.Serializable;

public class ThreadException extends Exception implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="threadException">Exception qui a été provoquée dans le thread.</param>
			public ThreadException (Exception threadException)
			{
				 super ("Exception was occured in thread function", threadException);
			}

}
