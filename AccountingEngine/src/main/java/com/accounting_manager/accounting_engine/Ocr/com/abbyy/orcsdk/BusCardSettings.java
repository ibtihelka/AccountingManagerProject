/**
 * 
 */
package com.accounting_manager.accounting_engine.Ocr.com.abbyy.orcsdk;

/**
 * Business card processing settings.
 * 
 * For all possible settings see
 * https://ocrsdk.com/documentation/apireference/processBusinessCard/
 */
public class BusCardSettings {

	public String asUrlParams() {
		// For all possible parameters, see documentation at
		// https://ocrsdk.com/documentation/apireference/processBusinessCard/
		return String.format("language=%s&exportFormat=%s", language, outputFormat);
	}

	public enum OutputFormat {
		vCard, xml, csv
	}

	public void setOutputFormat(OutputFormat format) {
		outputFormat = format;
	}

	public OutputFormat getOutputFormat() {
		return outputFormat;
	}

	
	/*
	 * Set recognition language. You can set any language listed at
	 * https://ocrsdk.com/documentation/specifications/recognition-languages/ or
	 * set comma-separated combination of them.
	 * 
	 * Examples: English English,ChinesePRC English,French,German
	 */
	public void setLanguage(String newLanguage) {
		language = newLanguage;
	}

	public String getLanguage() {
		return language;
	}

	private String language = "English";
	private OutputFormat outputFormat = OutputFormat.vCard;
}
