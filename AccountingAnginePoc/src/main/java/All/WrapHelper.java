package All;

public class WrapHelper {

			/// <summary>
			/// Retourne un objet à partir d'un handle
			/// contenu dans le pointeur <paramref name="objPtr" />.
			/// </summary>
			/// <param name="objPtr">Pointeur contenant le handle de l'objet.</param>
			/// <returns>Instance d'un objet de type {T}.</returns>
			/// <remarks>Cette méthode libère la mémoire allouée par le handle via la méthode <see cref="GCHandle.Alloc(object)" />.</remarks>
			public static <T> T GetObject (Object objPtr)
			{	
//				GCHandle		handle = GCHandle.FromIntPtr (objPtr);
//				T				target = (T)handle.Target;
//				handle.Free ();
//				return target;
				return null;
			}
			/// <summary>
			/// Retourne un pointeur vers un objet dans le but
			/// d'être utilisé dans une code non managé.
			/// </summary>
			/// <param name="obj">Objet managé.</param>
			/// <returns>Instance de type <see cref="IntPtr" />.</returns>
			public static Object GetObjectWrapper (Object [] obj)
			{
//				GCHandle	handle = GCHandle.Alloc (obj);
//				return GCHandle.ToIntPtr (handle);
				return null;
			}

}
