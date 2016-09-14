package com.altevie.pdfModifier.service;

import com.altevie.pdfModifier.entity.PropertiesInput;
import com.altevie.pdfModifier.entity.TextInput;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ImportFile {
	public TextInput importTextInput(String fileName) throws IOException {
		TextInput textInput = new TextInput();
		List text = new ArrayList();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(fileName)));
		try {
			String line;
			while ((line = br.readLine()) != null) {
				line = new String(line.getBytes(), "UTF-8");
				text.add(line);
			}
		} finally {
			br.close();
		}
		textInput.setText(text);

		return textInput;
	}

	public PropertiesInput importProperties(String fileName) throws IOException {
		PropertiesInput properties = new PropertiesInput();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(fileName)));
		try {
			String line;
			while ((line = br.readLine()) != null) {
				
				if (line.contains("FontType")) {
					properties.setFontName(getValueProperties(line));
				}
				if (line.contains("FontSize")) {
					properties.setFontSize(getValueProperties(line));
				}
				if (line.contains("FontColor")) {
					properties.setFontColor(getValueProperties(line)
							.toUpperCase());
				}
				if (line.contains("Vertical")) {
					properties.setVertical(getValueProperties(line));
				}

				if (line.contains("Horizontal")) {
					properties.setHorizontal(getValueProperties(line));
				}
				if (line.contains("Rotate")) {
					properties.setRotation(getValueProperties(line));
				}
				if (line.contains("BOX")) {
					properties.setRectangle(getValueProperties(line));
				}
				if (line.contains("PdfType")) {
					properties.setPdfFormat(getValueProperties(line));
				}
				if (line.contains("PaddingTop")) {
					properties.setPaddingTop(getValueProperties(line));
				}
				if (line.contains("PaddingRight")) {
					properties.setPaddingRight(getValueProperties(line));
				}
				if (line.contains("ImagePath")) {
					properties.setPathImage(getValueProperties(line));
				}
				if (line.contains("ImageAbsolutePosition")) {
					properties.setImagePosition(getValueProperties(line).split(
							","));
				}
				if (line.contains("NumberPage")) {
					properties.setImagePosition(getValueProperties(line).split(
							","));
				}
				if (line.contains("BackgroundColor")) {
					properties.setBackgroundColor(getValueProperties(line)
							.toUpperCase());
				}
				if (line.contains("BoxWidth")) {
					properties.setBoxWidth(getValueProperties(line)
							.toUpperCase());
				}
				if (line.contains("Password")) {
					properties.setPassword(getValuePassword(line));
				}
				if (line.contains("FillOpacity")) {
					properties.setFillOpacity(getValueProperties(line)
							.toUpperCase());
				}
			}
		} finally {
			br.close();
		}

		return properties;
	}

	String getValueProperties(String line) {
		int beginIndex = line.indexOf("<") + 1;
		int endIndex = line.indexOf(">");
		return line.substring(beginIndex, endIndex).toLowerCase();
	}

	String getValuePassword(String line) {
		int beginIndex = line.indexOf("<") + 1;
		int endIndex = line.indexOf(">");
		return line.substring(beginIndex, endIndex);
	}
}