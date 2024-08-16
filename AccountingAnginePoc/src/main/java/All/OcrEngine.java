package All;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import document.Document;
import document.DocumentProcessing;
import document.DocumentRotation;

public abstract class OcrEngine {
	
			/// <summary>
			/// Moteur de réconnaissance de caractères.
			/// </summary>
			protected Object			engineObject;
			/// <summary>
			/// Indique que l'instance en cours a été disposée.
			/// </summary>
			protected boolean				isDisposed = true;
			/// <summary>
			/// Taux de la largeur de la zone gauche
			/// du filtre B <see cref="FacDocumentProcessing.FILTER_B" />.
			/// </summary>
			public static float			FILTER_B_LEFT_WIDTH_RATE = 0.1f;
			/// <summary>
			/// Taux de la largeur de la zone droite
			/// du filtre B <see cref="FacDocumentProcessing.FILTER_B" />.
			/// </summary>
			public static float			FILTER_B_RIGHT_WIDTH_RATE = 0.1f;

			// <summary>
			/// Obtient
	        /// le moteur de réconnaissance de caractères.
			/// </summary>
		
			public Object getEngineObject() {
				return engineObject;
			}

			public boolean isDisposed() {
				return isDisposed;
			}

			public void setEngineObject(Object engineObject) {
				this.engineObject = engineObject;
			}

			public void setDisposed(boolean isDisposed) {
				this.isDisposed = isDisposed;
			}
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="developerSN">Numéro de série du developpeur.</param>
			/// <param name="engineLibPath">Chemin de la librairie.</param>
			/// <param name="createEngineObj">Indique qu'il faut créer l'objet di moteur OCR.</param>
			public OcrEngine (String developerSN, String engineLibPath, boolean createEngineObj)
			{
				if (createEngineObj)
					this.engineObject = NativeCreateEngine (developerSN, engineLibPath);

				this.isDisposed = false;
			}
			/// <summary>
			/// Crée en native une nouvelle instance
			/// du moteur en utilisant un numéro de série.
			/// </summary>
			/// <param name="developerSN">Numéro de série du developpeur.</param>
			/// <param name="engineLibPath">Chemin de la librairie.</param>
			public abstract Object NativeCreateEngine (String developerSN, String engineLibPath);
			/// <summary>
			/// Prépare en native les documents en utilisant passée en paramètre pour être traité
			/// par la suite. Elle décompose le fichier en plusieurs pages
			/// de types images.a
			/// </summary>
			/// <param name="fileName">Nom du fichier à traiter.</param>
			/// <param name="outputDir">Nom du dossier destination.</param>
			/// <returns>Nom des fichiers images des différentes pages.</returns>
			public abstract String [] NativePrepareDocuments (String fileName, String outputDir);
			/// <summary>
			/// Applique le filtre A <see cref="FacDocumentProcessing.FILTER_A" />
			/// en native sur une image dont les données des ses pixels sont pass�es
			/// en arguments.
			/// </summary>
			/// <param name="rgb">Tableau des données des composantes r/g/b.</param>
			/// <param name="width">Largeur de l'image.</param>
			/// <param name="height">Hauteur de l'image.</param>
			/// <remarks>Les tableaux d'entrées seront utilisées comme tableaux de sorties.</remarks>
			public abstract void NativeApplyFilterA (byte [] rgb, int width, int height);
			/// <summary>
			/// Applique le filtre B <see cref="FacDocumentProcessing.FILTER_B" />
			/// en native sur une image dont les données des ses pixels sont pass�es
			/// en arguments.
			/// </summary>
			/// <param name="rgb">Tableau des données des composantes r/g/b.</param>
			/// <param name="width">Largeur de l'image.</param>
			/// <param name="height">Hauteur de l'image.</param>
			/// <param name="newRgb">Nouvelles données après application du filtre.</param>
			/// <param name="newWidth">Nouvelle largeur après application du filtre.</param>
			public void NativeApplyFilterB (byte [] rgb, int width, int height,  byte [] newRgb,  int newWidth)
			{
				int			leftWidth = (int)(width * OcrEngine.FILTER_B_LEFT_WIDTH_RATE);
				int			rightWidth = (int)(width * OcrEngine.FILTER_B_RIGHT_WIDTH_RATE);

				newWidth = leftWidth + rightWidth;
				newRgb = new byte [3 * height * newWidth];

				int			p, _p;
				// Zone gauche.
				for (int w = 0; w < leftWidth; w ++)
				{
					for (int h = 0; h < height; h ++)
					{
						p = 3 * (h * width + w);
						_p = 3 * (h * newWidth + w);

						newRgb [_p] = rgb [p];
						newRgb [_p + 1] = rgb [p + 1];
						newRgb [_p + 2] = rgb [p + 2];
					}
				}
				// Zone droite.
				for (int w = 0; w < rightWidth; w ++)
				{
					for (int h = 0; h < height; h ++)
					{
						p = 3 * (h * width + width - 1 - w);
						_p = 3 * (h * newWidth + newWidth - 1 - w);

						newRgb [_p] = rgb [p];
						newRgb [_p + 1] = rgb [p + 1];
						newRgb [_p + 2] = rgb [p + 2];
					}
				}
			}
			
			/// <summary>
			/// Crée l'instance racine des blocs <see cref="FacDocument" /> après
			/// l'analyse du fichier pass� en paramètre <paramref name="imageFileName" />.
			/// </summary>
			/// <param name="imageFileName">Nom du fichier de l'image à traiter.</param>
			/// <param name="rotationType">Indique le type de rotation.</param>
			/// <returns>
			/// Une instance de type <see cref="FacDocument" /> qui contient les blocs
			/// de l'image après analyse.
			/// </returns>
			public abstract Document NativeCreateDocument (String imageFileName, DocumentRotation rotationType);
			/// <summary>
			/// Crée l'instance racine des blocs <see cref="FacDocument" /> après
			/// l'analyse du fichier passé en paramètre <paramref name="imageFileName" />.
			/// </summary>
			/// <param name="imageFileName">Nom du fichier de l'image à traiter.</param>
			/// <returns>
			/// Une instance de type <see cref="FacDocument" /> qui contient les blocs
			/// de l'image après analyse.
			/// </returns>
			public Document NativeCreateDocument (String imageFileName)
			{return NativeCreateDocument (imageFileName, DocumentRotation.NONE);}
			/// <summary>
			/// Met fin à l'utilisation de l'instance de l'ocr
			/// retourneé par la méthode <see cref="NativeCreateEngine" />.
			/// </summary>
			public abstract void NativeRelease ();
			
			///TODO
			/// <summary>
			/// Extraire les compostantes r,g et b d'une image.
			/// </summary>
			/// <param name="image">Une image bitmap.</param>
			/// <param name="width">Largeur de l'image.</param>
			/// <param name="height">Hauteur de l'image.</param>
			/// <param name="rgb">Tableau des données des composantes r/g/b.</param>
			
			
			/// <summary>
			/// Applique un filtre à une image donnée par son nom de fichier.
			/// </summary>
			/// <param name="imageFileName">Nom du fichier de l'image. Il doit être complet.</param>
			/// <param name="outputDir">Nom du dossier de sortie. Il est possible qu'il soit null.</param>
			/// <param name="filterType">Type du filtre.</param>
			/// <param name="createNewFile">Indique qu'il faut créer un fichier quelques soit le type du filtre.</param>
			/// <returns>Nom du fichier de la nouvelle image.</returns>
			@SuppressWarnings("null")
			public String ApplyFilter (String outputDir, String imageFileName, DocumentProcessing filterType, boolean createNewFile)
			{	try {
				// Aucun filtre.
				if (filterType == DocumentProcessing.NONE && !createNewFile)
					return imageFileName;

				//Image			image = Image.fromFile(imageFileName);
				BufferedImage			srcImage = ImageIO.read(new File(imageFileName));
				BufferedImage			destImage = null;
				// Extraire les compostantes r,g,b.
				int				srcWidth = srcImage.getWidth();
				int				srcHeight = srcImage.getHeight();
				// Appliquer le traitement.
				String			filterName;
				// Filtre A.
				if (filterType == DocumentProcessing.NONE)
				{
					filterName = "none";
					destImage = srcImage;
				}
				else if (filterType == DocumentProcessing.FILTER_A)
				{
					filterName = "a";
					// Extraire les données de l'image..
					int			srcWh = srcWidth * srcHeight;
					byte[]		srcRgb = new byte [3 * srcWh];
					
					int rgb=srcImage.getRGB(srcWidth, srcHeight);
					// Alliquer le filtre.
					NativeApplyFilterA (srcRgb, srcWidth, srcHeight);
					// Modifie l'image.
					srcImage.setRGB (srcWidth, srcHeight, rgb);
					destImage = srcImage;
				}
				// Filtre Bé.
				else if (filterType == DocumentProcessing.FILTER_B)
				{
					filterName = "b";
					// Extraire les données de l'image..
					int			srcWh = srcWidth * srcHeight;
					byte []		srcRgb = new byte [3 * srcWh];

					int rgb=srcImage.getRGB (srcWidth, srcHeight);
					// Alliquer le filtre.
					byte []		destRgb = null;
					int			destWidth = 0;

					NativeApplyFilterB (srcRgb, srcWidth, srcHeight,  destRgb,  destWidth);
					// Modifie l'image.
					//destImage = new BufferedImage  (srcImage, destWidth, srcHeight);
					destImage.setRGB (destWidth, srcHeight, rgb);
					destImage = srcImage;

				}
				else
					throw new IllegalArgumentException ("Unknown filter type");
				// Extraire le dossier de sortie et le nom du fichier.
				int			lastIndexOf = imageFileName.lastIndexOf ('\\');
				String		imageName = imageFileName.substring (lastIndexOf + 1, imageFileName.length() - 1 - lastIndexOf);
				if (outputDir == null)
					outputDir = imageFileName.substring (0, lastIndexOf);
				// Créer la nouvelle image et la stocker.
				@SuppressWarnings("unused")
				String		newFileName = outputDir + "\\" + imageName + "-" + filterName + "-filter" + ".jpg";
				ImageIO.write(destImage, "jpeg", new File(imageFileName));
				//image.Dispose ();
				
				//srcImage.Dispose ();
				srcImage.flush();
				//destImage.Dispose ();
				destImage.flush();
				
				//GC.Collect ();
				System.gc();
		
			}
			catch (IOException e) {
				
			}
			return imageFileName;
				
			}
			/// <summary>
			/// Applique un filtre à une image donnée par son nom de fichier.
			/// </summary>
			/// <param name="imageFileName">Nom du fichier de l'image. Il doit être complet.</param>
			/// <param name="outputDir">Nom du dossier de sortie. Il est possible qu'il soit null.</param>
			/// <param name="filterType">Type du filtre.</param>
			/// <returns>Nom du fichier de la nouvelle image.</returns>
			public String ApplyFilter (String outputDir, String imageFileName, DocumentProcessing filterType)
			{return ApplyFilter (outputDir, imageFileName, filterType, false);}
			/// <summary>
			/// Applique un filtre à une image donnée par son nom de fichier.
			/// </summary>
			/// <param name="imageFileName">Nom du fichier de l'image. Il doit être complet.</param>
			/// <param name="filterType">Type du filtre.</param>
			/// <param name="createNewFile">Indique qu'il faut créer un fichier quelques soit le type du filtre.</param>
			/// <returns>Nom du fichier de la nouvelle image.</returns>
			public String ApplyFilter (String imageFileName,DocumentProcessing filterType, boolean createNewFile)
			{return ApplyFilter (null, imageFileName, filterType, createNewFile);}
			/// <summary>
			/// Applique un filtre à une image donnée par son nom de fichier.
			/// </summary>
			/// <param name="imageFileName">Nom du fichier de l'image. Il doit être complet.</param>
			/// <param name="filterType">Type du filtre.</param>
			/// <returns>Nom du fichier de la nouvelle image.</returns>
			public String ApplyFilter (String imageFileName, DocumentProcessing filterType)
			{return ApplyFilter (null, imageFileName, filterType, false);}
			/// <summary>
			/// Libère les ressources utilisées par l'instance en cours.
			/// </summary>

			
			public void Dispose() {
				this.isDisposed = true;
				}

}
