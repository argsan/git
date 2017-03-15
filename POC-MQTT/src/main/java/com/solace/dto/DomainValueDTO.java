package com.solace.dto;

import java.io.Serializable;

public class DomainValueDTO implements Serializable{
	String domainDetails;
	String domainDetailsValue;
	public String getDomainDetails() {
		return domainDetails;
	}
	public void setDomainDetails(String domainDetails) {
		this.domainDetails = domainDetails;
	}
	public String getDomainDetailsValue() {
		return domainDetailsValue;
	}
	public void setDomainDetailsValue(String domainDetailsValue) {
		this.domainDetailsValue = domainDetailsValue;
	}
	@Override
	public String toString() {
		return "DomainValueDTO [domainDetails=" + domainDetails + ", domainDetailsValue=" + domainDetailsValue + "]";
	}
	
	

}
