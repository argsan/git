package com.solace.dto;

import java.io.Serializable;

public class DomainDetailsDTO implements Serializable{
	public Integer domainDetailsId;
	public Integer domainId;
	public String domainName;
	
	
	
	public DomainDetailsDTO(Integer domainDetailsId, Integer domainId, String domainName) {
		super();
		this.domainDetailsId = domainDetailsId;
		this.domainId = domainId;
		this.domainName = domainName;
	}
	public Integer getDomainDetailsId() {
		return domainDetailsId;
	}
	public void setDomainDetailsId(Integer domainDetailsId) {
		this.domainDetailsId = domainDetailsId;
	}
	public Integer getDomainId() {
		return domainId;
	}
	public void setDomainId(Integer domainId) {
		this.domainId = domainId;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	
	

}
