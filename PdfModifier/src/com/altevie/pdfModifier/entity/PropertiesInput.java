package com.altevie.pdfModifier.entity;


public class PropertiesInput extends File {
	private String pdfFormat;
	private String fontName;
	private String fontColor;
	private String fontPosition;
	private String fontSize;
	private String rectangle;
	private String rotation;
	private String vertical;
	private String horizontal;
	private String paddingTop;
	private String paddingRight;
	private String pathImage;
	private String[] imagePosition;
	private String[] numberPages;
	private String backgroundColor;
	private String boxWidth;
	private String password;
	private String fillOpacity;

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBackgroundColor() {
		return this.backgroundColor;
	}

	public String getBoxWidth() {
		return this.boxWidth;
	}

	public void setBoxWidth(String boxWidth) {
		this.boxWidth = boxWidth;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String[] getNumberPages() {
		return this.numberPages;
	}

	public void setNumberPages(String[] numberPages) {
		this.numberPages = numberPages;
	}

	public String[] getImagePosition() {
		return this.imagePosition;
	}

	public void setImagePosition(String[] imagePosition) {
		this.imagePosition = imagePosition;
	}

	public String getPathImage() {
		return this.pathImage;
	}

	public void setPathImage(String pathImage) {
		this.pathImage = pathImage;
	}

	public String getPaddingTop() {
		return this.paddingTop;
	}

	public void setPaddingTop(String paddingTop) {
		this.paddingTop = paddingTop;
	}

	public String getPaddingRight() {
		return this.paddingRight;
	}

	public void setPaddingRight(String paddingRight) {
		this.paddingRight = paddingRight;
	}

	public String getPdfFormat() {
		return this.pdfFormat;
	}

	public void setPdfFormat(String pdfFormat) {
		this.pdfFormat = pdfFormat;
	}

	public String getVertical() {
		return this.vertical;
	}

	public void setVertical(String vertical) {
		this.vertical = vertical;
	}

	public String getHorizontal() {
		return this.horizontal;
	}

	public void setHorizontal(String horizontal) {
		this.horizontal = horizontal;
	}

	public String getRectangle() {
		return this.rectangle;
	}

	public String getFontColor() {
		return this.fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public String getFontPosition() {
		return this.fontPosition;
	}

	public void setFontPosition(String fontPosition) {
		this.fontPosition = fontPosition;
	}

	public String getFontSize() {
		return this.fontSize;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}

	public String isRectangle() {
		return this.rectangle;
	}

	public void setRectangle(String rectangle) {
		this.rectangle = rectangle;
	}

	public String getRotation() {
		return this.rotation;
	}

	public void setRotation(String rotation) {
		this.rotation = rotation;
	}

	public String getFontName() {
		return this.fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public String getFillOpacity() {
		if ((this.fillOpacity != null) && (!(this.fillOpacity.equals("")))) {
			float fill = Float.valueOf(this.fillOpacity).floatValue() / 10.0F;
			return String.valueOf(fill);
		}
		return null;
	}

	public void setFillOpacity(String fillOpacity) {
		this.fillOpacity = fillOpacity;
	}
}