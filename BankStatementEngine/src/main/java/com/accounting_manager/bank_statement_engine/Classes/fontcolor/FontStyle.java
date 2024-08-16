package com.accounting_manager.bank_statement_engine.Classes.fontcolor;

import java.io.Serializable;

public enum FontStyle implements Serializable {
	/// <summary>
	/// Texte gras.
	/// </summary>
	BOLD (1),
	/// <summary>
	/// Texte italique.
	/// </summary>
	ITALIC (2),
	/// <summary>
	/// Texte normal.
	/// </summary>
	REGULAR (4),
	/// <summary>
	/// Texte barr� d'une ligne � mi-hauteur.
	/// </summary>
	STRIKEOUT (8),
	/// <summary>
	/// Texte soulign�.
	/// </summary>
	UNDERLINE (16);

	public void setNumFontStyle(int numFontStyle) {
		this.numFontStyle = numFontStyle;
	}

	private int numFontStyle;

	private FontStyle(){


	}
	private FontStyle(int numFontStyle) {
		this.numFontStyle = numFontStyle;
	}

	public int getNumFontStyle() {
		return numFontStyle;
	}





}
