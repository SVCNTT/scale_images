package com.scale.images.form;

public class LinkLoadImagesScaleForm {

	private String folder = "";
	private Integer year = 0;
	private Integer month = 0;
	private Integer day = 0;
	private String name = "";
	private String type;

	public LinkLoadImagesScaleForm() {
	}
	
	public LinkLoadImagesScaleForm(String folder, Integer year, Integer month, Integer day, String name, String type) {
		super();
		this.folder = folder;
		this.year = year;
		this.month = month;
		this.day = day;
		this.name = name;
		this.type = type;
	}
	
	public String getFolder() {
		return folder;
	}
	public void setFolder(String folder) {
		this.folder = folder;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
