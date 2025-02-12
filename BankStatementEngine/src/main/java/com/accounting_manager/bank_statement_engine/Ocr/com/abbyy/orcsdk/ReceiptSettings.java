package com.accounting_manager.bank_statement_engine.Ocr.com.abbyy.orcsdk;

/**
 * Receipt processing settings.
 * 
 * For all possible settings see
 * https://ocrsdk.com/documentation/apireference/processReceipt/
 */

public class ReceiptSettings {
	
	public String asUrlParams() {
		// For all possible parameters, see documentation at
		// https://ocrsdk.com/documentation/apireference/processReceipt/
		return String.format("country=%s", receiptCountry);
	}
	
	/*
	 * Set country where receipt was printed. You can set any country listed at
	 * https://ocrsdk.com/documentation/apireference/processReceipt/ or
	 * set comma-separated combination of them.
	 * 
	 * Examples: Usa Usa,Spain
	 */
	public void setReceiptCountry(String newCountry) {
		receiptCountry = newCountry;
	}

	public String getReceiptCountry() {
		return receiptCountry;
	}

	private String receiptCountry = "Usa";
}
