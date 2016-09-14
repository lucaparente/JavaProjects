package com.altevie.pdfModifier.service;


import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.altevie.pdfModifier.entity.PropertiesInput;
import com.altevie.pdfModifier.entity.TextInput;

public class Main {
	public static void main(String[] args) {
		String inputFilePath = "";
		String propertiesFilePath = "";
		String textInputPath = "";
		if (args.length > 0) {
			inputFilePath = args[0];
		} else {
			inputFilePath = "C:\\PdfModifier\\input.pdf";
			propertiesFilePath = "C:\\PdfModifier\\propertiesFile.txt";
			textInputPath = "C:\\PdfModifier\\textInput.txt";
		}

		List<TextInput> textInput = new ArrayList<TextInput>();
		ImportFile importFile = new ImportFile();
		List<PropertiesInput> propertiesInput = new ArrayList<PropertiesInput>();
		try {
			for (int i = 0; i < args.length - 2; i += 2) {
				propertiesFilePath = args[(i + 1)];
				textInputPath = args[(i + 2)];
				propertiesInput.add(importFile
						.importProperties(propertiesFilePath));
				textInput.add(importFile.importTextInput(textInputPath));
			}

			new ModifyPDF().modifica(inputFilePath, textInput, propertiesInput);
			if (args.length == 0) {
				JOptionPane.showMessageDialog(null,
						"Modifica effettuata con successo!");
				return;
			}
			System.out.println("Modifica effettuata con successo!");
		} catch (Exception e) {
			if (args.length == 0)
				JOptionPane.showMessageDialog(null,
						"Modifica fallita!" + e.getMessage());
			else
				System.out.println("Modifica fallita! /n" + e.getMessage());
		}
	}
}