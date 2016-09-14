package com.altevie.pdfModifier.service;

import com.altevie.pdfModifier.entity.PropertiesInput;
import com.altevie.pdfModifier.entity.TextInput;
import com.altevie.pdfModifier.exceptions.FontException;
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
				writeText(this.contentByteOver, (TextInput) txtInput.get(k),
						(PropertiesInput) properties.get(k), dimPage, document);
				addImage(this.contentByteOver,
						(PropertiesInput) properties.get(k), dimPage);
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

	public void writeText(PdfContentByte contentOver, TextInput txtInput,
			PropertiesInput properties, Rectangle dimPage, Document document)
			throws FontException, IOException, DocumentException {
		float width;
		float height;
		if ((properties.getPdfFormat() != null)
				&& ("H".equalsIgnoreCase(properties.getPdfFormat()))) {
			height = dimPage.getWidth() / 3.0F;
			width = dimPage.getHeight() / 3.0F;
		} else {
			width = dimPage.getWidth() / 3.0F;
			height = dimPage.getHeight() / 3.0F;
		}

		List dimension = new ArrayList();
		dimension = getPosition(properties, width, height);
		document.open();

		PdfPTable table = new PdfPTable(1);

		if ((properties.getBoxWidth() != null)
				&& (!(properties.getBoxWidth().equals(""))))
			table.setTotalWidth(Float.valueOf(
					properties.getBoxWidth().toString()).floatValue());
		else {
			table.setTotalWidth(dimPage.getWidth() / 3.0F);
		}
		Phrase p = getPhraseInput(properties, txtInput.getText());
		PdfPCell cell = new PdfPCell(p);
		cell.setNoWrap(false);
		cell.setPhrase(p);
		cell.setBorderWidth(-1.0F);
		if ((((properties.getPathImage() == null) || (properties.getPathImage()
				.equals("")))) && (getBoxed(properties.getRectangle()))) {
			cell.setBorderWidth(1.0F);
			cell.setBorderColor(setColor(properties.getFontColor()));
		}

		cell.setHorizontalAlignment(3);
		cell.setRotation(getRotation(properties));

		BaseColor bc = addBackGroundColor(properties);
		if (bc != null) {
			cell.setBackgroundColor(new BaseColor(bc.getRed(), bc.getGreen(),
					bc.getBlue(), 255));
		}

		if ((getRotation(properties) == 90) || (getRotation(properties) == 270)) {
			cell.setFixedHeight(width);
		}
		table.addCell(cell);
		table.writeSelectedRows(0, -1,
				Float.valueOf(((Float) dimension.get(1)).toString())
						.floatValue(),
				Float.valueOf(((Float) dimension.get(0)).toString())
						.floatValue(), contentOver);
		document.add(table);
	}

	public BaseColor addBackGroundColor(PropertiesInput properties)
			throws MalformedURLException, IOException, DocumentException {
		return setColor(properties.getBackgroundColor());
	}

	public void addImage(PdfContentByte content, PropertiesInput properties,
			Rectangle dimPage) throws MalformedURLException, IOException,
			DocumentException {
		float height;
		float width;
		if (!(this.h)) {
			width = dimPage.getWidth() / 3.0F;
			height = dimPage.getHeight() / 3.0F;
		} else {
			height = dimPage.getWidth() / 3.0F;
			width = dimPage.getHeight() / 3.0F;
		}
		if ((properties.getPathImage() == null)
				|| (properties.getPathImage().equals("")))
			return;
		PdfPTable table = new PdfPTable(1);
		PdfPCell cell = new PdfPCell();
		cell.setNoWrap(true);
		Image image = Image.getInstance(properties.getPathImage());
		if ((properties.getBoxWidth() != null)
				&& (!(properties.getBoxWidth().equals("")))) {
			table.setTotalWidth(Float.valueOf(
					properties.getBoxWidth().toString()).floatValue());
		} else {
			table.setTotalWidth(width);
		}
		cell.setImage(image);
		if (getBoxed(properties.getRectangle())) {
			cell.setBorderWidth(1.0F);
			cell.setBorderColor(setColor(properties.getFontColor()));
		} else {
			cell.setBorderWidth(-1.0F);
		}
		cell.setRotation(getRotation(properties));
		table.addCell(cell);
		table.writeSelectedRows(
				0,
				-1,
				Float.valueOf(
						((Float) getPosition(properties, width, width).get(1))
								.toString()).floatValue(),
				Float.valueOf(
						((Float) getPosition(properties, width, height).get(0))
								.toString()).floatValue(), content);
	}

	public int getRotation(PropertiesInput properties) {
		int rotation = 0;
		try {
			if (Integer.valueOf(properties.getRotation()).intValue() % 90 == 0)
				return Integer.valueOf(properties.getRotation()).intValue();
		} catch (Exception e) {
			return rotation;
		}
		return rotation;
	}

	public Phrase getPhraseInput(PropertiesInput properties, List<String> input)
			throws FontException {
		Phrase phrase = new Phrase("", FontFactory.getFont(setFont(properties),
				setSizeFont(properties.getFontSize()),
				setColor(properties.getFontColor())));

		for (int i = 0; i < input.size(); ++i) {
			phrase.add(((String) input.get(i)) + "\n");
		}
		return phrase;
	}

	public String setFont(PropertiesInput properties) throws FontException {
		String fontName = "";
		try {
			if ((properties.getFontName() != null)
					&& (!(properties.getFontName().equals(""))))
				fontName = properties.getFontName();
			else {
				fontName = "Courier";
			}

			return fontName;
		} catch (Exception e) {
			throw new FontException();
		}
	}

	public BaseColor setColor(String fontColor)
  {
    BaseColor bc = null;
    String str;
    str = fontColor ;
    switch (str)
    {
    case  "ORANGE" :
    	bc = BaseColor.ORANGE;
    	break;
    case  "YELLOW" :
    	bc = BaseColor.YELLOW;
    	break;
    case  "RED" :
    	bc = BaseColor.RED;
    	break;
    case  "BLUE" :
    	bc = BaseColor.BLUE;
    	break;
    case  "CYAN" :
    	bc = BaseColor.CYAN;
    	break;
    case  "GRAY" :
    	bc = BaseColor.GRAY;
    	break;
    case  "PINK" :
    	bc = BaseColor.PINK;
    	break;
    case  "BLACK" :
    	bc = BaseColor.BLACK;
    	break;
    case  "LIGHT_GRAY":
    	bc = BaseColor.LIGHT_GRAY;
    	break;
    case  "DARK_GRAY" :
		bc = BaseColor.DARK_GRAY;
		break;   
	case  "MAGENTA" :
		bc = BaseColor.MAGENTA;
		break;
	case  "WHITE" :
		bc = BaseColor.WHITE;
		break;
	case  "GREEN" :
		bc = BaseColor.GREEN;
		break;		
    }
    	
    return bc; 
    
  }

	public float setSizeFont(String fontSize) throws FontException {
		float size;
		try {
			size = Float.valueOf(fontSize).floatValue();
		} catch (Exception e) {
			size = 18.0F;
			throw new FontException();
		}
		return size;
	}

	public boolean getBoxed(String boxed) {
		return ((boxed != null) && (boxed.equalsIgnoreCase("on")));
	}

	public List<Float> getPosition(PropertiesInput properties, float width, float height)
  {
    List<Float> coordinate = new ArrayList<Float>();
    float paddingTop = 0.0F;
    float paddingRight = 0.0F;
    try {
      paddingTop = Float.valueOf(properties.getPaddingTop()).floatValue();
      paddingRight = Float.valueOf(properties.getPaddingRight()).floatValue();
    } catch (Exception localException) {
    }
    switch (properties.getVertical())
    {
    case "botton":
    	coordinate.add(0, Float.valueOf(height - paddingTop));
    	break;
    case "middle":
    	coordinate.add(0, Float.valueOf(height * 2 - paddingTop));
    	break;
    case "top":
    	coordinate.add(Float.valueOf(height * 3 - paddingTop));
        break ;
     default:
      coordinate.add(0, Float.valueOf(height - paddingTop));
    }

    switch ( properties.getHorizontal())
    {
    case "center":
    	 coordinate.add(1, Float.valueOf(width  - paddingRight));
    	 break;
    case "left":
    	coordinate.add(1, Float.valueOf( 0 - paddingRight));   
    	break;
    case "right":
    	coordinate.add(1, Float.valueOf( width * 2 - paddingRight));
        break ;
    default:
      coordinate.add(1, Float.valueOf(width - paddingRight));
    }

   return coordinate;
  }


}