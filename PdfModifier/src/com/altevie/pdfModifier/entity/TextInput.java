package com.altevie.pdfModifier.entity;

import java.util.List;

public class TextInput extends File {
	private List<String> text;

	public List<String> getText() {
		return this.text;
	}

	public void setText(List<String> text) {
		this.text = text;
	}
}