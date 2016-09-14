package com.altevie.pdfModifier.utility;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

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

public class Utility {

	 
	public static List<Float> getPosition(PropertiesInput properties, float width, float height){
		
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
	
	public static BaseColor setColor(String fontColor)
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
	
	public  static void writeText(PdfContentByte contentOver, TextInput txtInput,
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
		dimension = Utility.getPosition(properties, width, height);
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
			cell.setBorderColor(Utility.setColor(properties.getFontColor()));
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
	

	public static Phrase getPhraseInput(PropertiesInput properties, List<String> input)
			throws FontException {
		Phrase phrase = new Phrase("", FontFactory.getFont(setFont(properties),
				setSizeFont(properties.getFontSize()),
				Utility.setColor(properties.getFontColor())));

		for (int i = 0; i < input.size(); ++i) {
			phrase.add(((String) input.get(i)) + "\n");
		}
		return phrase;
	}
	public static float setSizeFont(String fontSize) throws FontException {
		float size;
		try {
			size = Float.valueOf(fontSize).floatValue();
		} catch (Exception e) {
			size = 18.0F;
			throw new FontException();
		}
		return size;
	}

	public static boolean getBoxed(String boxed) {
		return ((boxed != null) && (boxed.equalsIgnoreCase("on")));
	}

	public static String setFont(PropertiesInput properties) throws FontException {
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

	public static int getRotation(PropertiesInput properties) {
		int rotation = 0;
		try {
			if (Integer.valueOf(properties.getRotation()).intValue() % 90 == 0)
				return Integer.valueOf(properties.getRotation()).intValue();
		} catch (Exception e) {
			return rotation;
		}
		return rotation;
	}
	public static BaseColor addBackGroundColor(PropertiesInput properties)
			throws MalformedURLException, IOException, DocumentException {
		return Utility.setColor(properties.getBackgroundColor());
	}
	
	public static void addImage(PdfContentByte content, PropertiesInput properties,
			Rectangle dimPage,boolean h) throws MalformedURLException, IOException,
			DocumentException {
		float height;
		float width;
		if (!(h)) {
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
			cell.setBorderColor(Utility.setColor(properties.getFontColor()));
		} else {
			cell.setBorderWidth(-1.0F);
		}
		cell.setRotation(getRotation(properties));
		table.addCell(cell);
		table.writeSelectedRows(
				0,
				-1,
				Float.valueOf(
						((Float) Utility.getPosition(properties, width, width).get(1))
								.toString()).floatValue(),
				Float.valueOf(
						((Float) Utility.getPosition(properties, width, height).get(0))
								.toString()).floatValue(), content);
	}

	



}
