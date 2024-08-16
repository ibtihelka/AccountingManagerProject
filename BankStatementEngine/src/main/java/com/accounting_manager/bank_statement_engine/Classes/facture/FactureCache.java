package com.accounting_manager.bank_statement_engine.Classes.facture;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/// <summary>
/// Classe qui repr�sente le cache
/// d'une facture � traiter.
/// </summary>

public class FactureCache implements Serializable {
	/// <summary>
	/// Flux de donn�es de l'image de la facture.
	/// </summary>
	private byte []							imageStream;
	/// <summary>
	/// Flux de donn�es du document de la facture.
	/// Les flux seront stock�s en utilisant un mot
	/// cl� indentifiant les differ�ntes m�thodes de filtrage
	/// et rotation.
	/// </summary>
	private Map<String,byte []>		docStreams;

	// <summary>
	/// Obtient ou d�finit
	/// le flux de donn�es de l'image de la facture.
	/// </summary>
	public byte []		getImageStream()
	{
		return this.imageStream;
	}
	public void setImageStream(byte [] imageStream) {
		this.imageStream = imageStream;
	}

	// <summary>
	/// Initialise une nouvelle instance de la classe
	/// avec des param�tres sp�cifi�s.
	/// </summary>
	public FactureCache()
	{
		this.docStreams = new HashMap<String,byte []> ();

	}

	/// <summary>
	/// Retourne le flux de donn�es du document
	/// nomm� <paramref name="streamName" />.
	/// </summary>
	/// <param name="streamName">Nom du flux de donn�es.</param>
	/// <returns>Tableau qui contient les donn�es dux flux. Null si le flux n'a pas �t� stock�.</returns>
	public byte [] GetDocStream (String streamName)
	{
		if (this.docStreams.containsKey(streamName))
			return docStreams.get(streamName);
		else
			throw new RuntimeException("Key Not found Exception");
	}
	/// <summary>
	/// Stocke un flux de donn�es du document
	/// en utilisant un mot cl� <paramref name="streamName" />.
	/// </summary>
	/// <param name="streamName">Nom du flux de donn�es.</param>
	/// <param name="streamBytes">Flux de donn�es � stocker.</param>
	public void SetDocStream (String streamName, byte [] streamBytes)
	{this.docStreams.put(streamName, streamBytes) ;}


}
