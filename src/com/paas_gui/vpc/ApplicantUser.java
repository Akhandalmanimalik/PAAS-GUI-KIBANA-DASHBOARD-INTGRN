package com.paas_gui.vpc;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicantUser {
	

	private String applicantionName;
	private String description;
	
	

	public ApplicantUser() {

	}

	public ApplicantUser(String applicantionName, String description) {
		super();
		this.applicantionName = applicantionName;
		this.description = description;

	}
	
	public String getApplicantionName() {
		return applicantionName;
	}

	public void setApplicantionName(String applicantionName) {
		this.applicantionName = applicantionName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

	
}
