package cpp;

import All.WrapHelper;

//Nouveau Classe En cours de correction
public class BlurFilter {

/// <summary>
/// Rayon horizontal du flou.
/// </summary>
private double				Rx;
/// <summary>
/// Rayon vertical du flou.
/// </summary>
private double				Ry;

/// <summary>
/// Initialise une nouvelle instance de la classe
/// avec des paramètres spécifiés.
/// </summary>
/// <param name="rx">Rayon horizontal du flou.</param>
/// <param name="ry">Rayon vertical du flou.</param>
public BlurFilter (double rx, double ry)
{
	this.Rx = rx;
	this.Ry = ry;
}

/// <summary>
/// Rendre une image flou en utilisant
/// un filtre gaussien.
/// </summary>
/// <param name="inPtr">Pointeur d'entrée contenant les données des paramètres de la fonction.</param>
/// <returns>Pointeur de sortie contenant les données retournées par la fonction.</returns>
//[DllImport ("Projects.FacDetector.OcrWrapper.dll", EntryPoint="nativeBlur")]
public static  void NativeBlur (Object inPtr) {}

/// <summary>
/// Rendre une image flou. Les composantes
/// du coleur sont passés en paramètre
/// par le tableau <paramref name="rgb" />.
/// </summary>
/// <param name="rgb">Tableau des données des composantes r/g/b.</param>
/// <param name="width">Largeur de l'image.</param>
/// <param name="height">Hauteur de l'image.</param>
public void Blur (byte [] rgb, int width, int height)
{
	
	Object []	args = new Object [] {new Object (), this.Rx, this.Ry, width, height};
	Object		nativeIn = WrapHelper.GetObjectWrapper (args);
	BlurFilter.NativeBlur (nativeIn);
		
	
}

}