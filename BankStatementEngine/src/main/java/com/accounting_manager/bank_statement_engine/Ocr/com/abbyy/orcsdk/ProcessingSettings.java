package com.accounting_manager.bank_statement_engine.Ocr.com.abbyy.orcsdk;

public class ProcessingSettings {

	public String asUrlParams() {
		return String.format("language=%s&exportFormat=%s&profile=%s&xml:writeFormatting=%s&xml:writeRecognitionVariants=%s", language,
				outputFormat , profile ,writeFormatting, writeRecognitionVariants);
	}

	public enum OutputFormat {
		txt, rtf, docx, xlsx, pptx, pdfSearchable, pdfTextAndImages, xml ,xmlForCorrectedImage
	}

	public void setOutputFormat(OutputFormat format) {
		outputFormat = format;
	}

	public OutputFormat getOutputFormat() {
		return outputFormat;
	}

	public String getOutputFileExt() {
		switch( outputFormat ) {
			case txt: return ".txt";
			case rtf: return ".rtf";
			case docx: return ".docx";
			case xlsx: return ".xlsx";
			case pptx: return ".pptx";
			case pdfSearchable:
			case pdfTextAndImages: return ".pdf";
			case xmlForCorrectedImage:
			case xml: return ".xml";
		}
		return ".ocr";
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

	public String getTextType() {
		return textType;
	}

	public void setTextType(String textType) {
		this.textType = textType;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public boolean isWriteFormatting() {
		return writeFormatting;
	}

	public void setWriteFormatting(boolean writeFormatting) {
		this.writeFormatting = writeFormatting;
	}

	public boolean isWriteRecognitionVariants() {
		return writeRecognitionVariants;
	}

	public void setWriteRecognitionVariants(boolean writeRecognitionVariants) {
		this.writeRecognitionVariants = writeRecognitionVariants;
	}

	private String textType;
	private String profile;
	private boolean writeFormatting = true;
	private boolean writeRecognitionVariants = false;

	private String language;
	private OutputFormat outputFormat = OutputFormat.xmlForCorrectedImage;
}
