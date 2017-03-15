package com.solace.dto;

import java.io.Serializable;

public class RailQueryDTO implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private String fromStn;
	private String toStn;
	private String jClass;
	private String jDate;
	public String getFromStn() {
		return fromStn;
	}
	public void setFromStn(String fromStn) {
		this.fromStn = fromStn;
	}
	public String getToStn() {
		return toStn;
	}
	public void setToStn(String toStn) {
		this.toStn = toStn;
	}
	public String getjClass() {
		return jClass;
	}
	public void setjClass(String jClass) {
		this.jClass = jClass;
	}
	public String getjDate() {
		return jDate;
	}
	public void setjDate(String jDate) {
		this.jDate = jDate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
