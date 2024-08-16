package com.accounting_manager.accounting_engine.Classes.fontcolor;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

public class FacFont implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4834853394709445342L;
	/// <summary>
			/// Instance réelle de la police de caractères.
			/// </summary>
			private Font trueFont;
			/// <summary>
			/// Correction des tailles des polices.
			/// </summary>
			public static float			FONT_SIZE_CORRECTION = 0.9f;
			/// <summary>
			/// Police par défaut.
			/// </summary>
			public static FacFont		DEFAULT_FONT = new FacFont ("Times New Roman", 10f, FontStyle.REGULAR);
//			private Graphics2D g2d;
			private FontStyle style;
			
			
			/// <summary>
			/// Obtient
	        /// l'instance réelle de la police de caractères.
			/// </summary>
			public Font getTrueFont() {
				return trueFont;
			}
			
			/// <summary>
			/// Initialise une nouvelle instance de la classe
			/// avec des paramètres spécifiés.
			/// </summary>
			/// <param name="fontName">Nom de la police de caractères.</param>
			/// <param name="fontSize">Taille de la police.</param>
			/// <param name="fontType">Type de la police.</param>
			/// <param name="fontStyle">Style de la police.</param>
			public FacFont (String fontName, float fontSize, FontStyle fontStyle)
			{
				// Size.
				fontSize = FacFont.FONT_SIZE_CORRECTION * fontSize;

				if (fontSize < 16)
					fontSize = 16;
				style = fontStyle;
				if ((fontStyle.getNumFontStyle() & FontStyle.BOLD.getNumFontStyle()) == FontStyle.BOLD.getNumFontStyle())
					style.setNumFontStyle(style.getNumFontStyle() | FontStyle.BOLD.getNumFontStyle()) ;
				if ((fontStyle.getNumFontStyle() & FontStyle.ITALIC.getNumFontStyle()) == FontStyle.ITALIC.getNumFontStyle())
					style.setNumFontStyle(style.getNumFontStyle() | FontStyle.ITALIC.getNumFontStyle());
				if ((fontStyle.getNumFontStyle() & FontStyle.REGULAR.getNumFontStyle()) == FontStyle.REGULAR.getNumFontStyle())
					style.setNumFontStyle(style.getNumFontStyle() | FontStyle.REGULAR.getNumFontStyle());
				if ((fontStyle.getNumFontStyle() & FontStyle.STRIKEOUT.getNumFontStyle()) == FontStyle.STRIKEOUT.getNumFontStyle())
					style.setNumFontStyle( style.getNumFontStyle() | FontStyle.STRIKEOUT.getNumFontStyle());
				if ((fontStyle.getNumFontStyle() & FontStyle.UNDERLINE.getNumFontStyle()) == FontStyle.UNDERLINE.getNumFontStyle())
					style.setNumFontStyle(style.getNumFontStyle() | FontStyle.UNDERLINE.getNumFontStyle());

				this.trueFont = new Font (fontName, style.getNumFontStyle(), (int)fontSize);
			}
			
			/// <summary>
			/// Retourne la largeur d'une zone de texte.
			/// </summary>
			/// <param name="text">Chaine de caractères qui représente le texte. Une chaine vide est acceptée.</param>
			/// <returns>Hauteur en pixels de la zone de texte.</returns>
			public int GetWidth (String text)
			{
//				g2d = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
//				return (g2d.getFontMetrics(this.trueFont).stringWidth(text));
				FontRenderContext frc = new FontRenderContext(new AffineTransform(),true,true);
				return (int)(this.trueFont.getStringBounds(text, frc).getWidth());
				}
		
				
}