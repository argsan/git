package com.solace.dto;

import java.io.Serializable;

public class Days implements Serializable{
	private String monDay;
	private String tuesDay;
	private String wedDay;
	private String thuDay;
	private String friDay;
	private String satDay;
	private String sunDay;
	public Days(String monDay, String tuesDay, String wedDay, String thuDay, String friDay, String satDay,
			String sunDay) {
		super();
		this.monDay = monDay;
		this.tuesDay = tuesDay;
		this.wedDay = wedDay;
		this.thuDay = thuDay;
		this.friDay = friDay;
		this.satDay = satDay;
		this.sunDay = sunDay;
	}
	public String getMonDay() {
		return monDay;
	}
	public void setMonDay(String monDay) {
		this.monDay = monDay;
	}
	public String getTuesDay() {
		return tuesDay;
	}
	public void setTuesDay(String tuesDay) {
		this.tuesDay = tuesDay;
	}
	public String getWedDay() {
		return wedDay;
	}
	public void setWedDay(String wedDay) {
		this.wedDay = wedDay;
	}
	public String getThuDay() {
		return thuDay;
	}
	public void setThuDay(String thuDay) {
		this.thuDay = thuDay;
	}
	public String getFriDay() {
		return friDay;
	}
	public void setFriDay(String friDay) {
		this.friDay = friDay;
	}
	public String getSatDay() {
		return satDay;
	}
	public void setSatDay(String satDay) {
		this.satDay = satDay;
	}
	public String getSunDay() {
		return sunDay;
	}
	public void setSunDay(String sunDay) {
		this.sunDay = sunDay;
	}
	
	
	

}
