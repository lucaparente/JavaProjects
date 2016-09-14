package com.altevie.pdfModifier.service;

import com.altevie.pdfModifier.entity.PropertiesInput;
import com.altevie.pdfModifier.entity.TextInput;
import com.altevie.pdfModifier.exceptions.FontException;
import com.altevie.pdfModifier.utility.Utility;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.security.EncryptionAlgorithms;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


public class ModifyPDF {
	TextInput textInput = new TextInput();
	PropertiesInput propertiesInput = new PropertiesInput();
	String pathImmagineDaAggiungere;
	static boolean SALVASOPRA = true;
	Rectangle dimText = null;
	PdfContentByte contentByte;
	PdfContentByte contentByteOver;
	int numberPages = 0;
	PdfReader reader;
	boolean h = false;
	byte[] password;

	public void modifica(String pathFileDaModificare, List<TextInput> txtInput,
			List<PropertiesInput> properties) throws IOException,
			DocumentException, FontException {
		PdfReader.unethicalreading = true;

		if (((PropertiesInput) properties.get(0)).getPassword() != null)
			this.password = ((PropertiesInput) properties.get(0)).getPassword()					.getBytes();
		else {
			this.password = "".getBytes();
		}
		this.reader = new PdfReader(pathFileDaModificare, this.password);
		PdfStamper stamper = new PdfStamper(this.reader, new FileOutputStream(
				addPath(pathFileDaModificare)));
		stamper.setEncryption(this.password, this.password,PdfWriter.ALLOW_MODIFY_CONTENTS,PdfWriter.ENCRYPTION_AES_256);
		Document document = new Document();
		this.numberPages = (this.reader.getNumberOfPages() + 1);
		for (int i = 1; i < this.numberPages; ++i) {
			Rectangle dimPage = this.reader.getPageSizeWithRotation(this.reader
					.getPageN(i));
			this.contentByteOver = stamper.getOverContent(i);
			getFillOpacity((PropertiesInput) properties.get(0));

			for (int k = 0; k < txtInput.size(); ++k) {
				dimPage = validateDimPage(dimPage,
						((PropertiesInput) properties.get(k)).getPdfFormat());
				if (this.h) {
					((PropertiesInput) properties.get(k)).setPdfFormat("H");
				}
				Utility.writeText(this.contentByteOver, (TextInput) txtInput.get(k),
						(PropertiesInput) properties.get(k), dimPage, document);
				Utility.addImage(this.contentByteOver,
						(PropertiesInput) properties.get(k), dimPage,this.h);
			}

		}

		stamper.close();
		document.close();
	}

	private Rectangle validateDimPage(Rectangle dimPage, String pdfFormat) {
		float height = dimPage.getHeight();
		float width = dimPage.getWidth();

		if (height < width) {
			dimPage.setRight(height);
			dimPage.setTop(width);
			this.h = true;
		} else {
			dimPage.setRight(width);
			dimPage.setTop(height);
		}

		return dimPage;
	}

	private void getFillOpacity(PropertiesInput property) {
		PdfGState gState = new PdfGState();
		String fillOpacity = property.getFillOpacity();
		if ((fillOpacity != null) && (!(fillOpacity.equals(""))))
			gState.setFillOpacity(Float.valueOf(fillOpacity + "f").floatValue());
		else {
			gState.setFillOpacity(0.7F);
		}
		this.contentByteOver.setGState(gState);
	}

	private String addPath(String f) {
		f = f.substring(0, f.lastIndexOf(".")) + "_timbrato" + ".pdf";
		return f;
	}








}