package com.getusroi.paas.vo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ACL {
	 private int id;
	 private String aclName;
	 private String sourceIp;
	 private String destinationIp;
	 private String action;
	 private int port;
	 private int tenantId; 
	
	 public ACL() {
		// TODO Auto-generated constructor stub
	}

	public ACL(int id, String aclName, String sourceIp, String destinationIp,
			String action, int port, int tenantId) {
		super();
		this.id = id;
		this.aclName = aclName;
		this.sourceIp = sourceIp;
		this.destinationIp = destinationIp;
		this.action = action;
		this.port = port;
		this.tenantId = tenantId;
	}

	@Override
	public String toString() {
		return "ACL [id=" + id + ", aclName=" + aclName + ", sourceIp="
				+ sourceIp + ", destinationIp=" + destinationIp + ", action="
				+ action + ", port=" + port + ", tenantId=" + tenantId + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAclName() {
		return aclName;
	}

	public void setAclName(String aclName) {
		this.aclName = aclName;
	}

	public String getSourceIp() {
		return sourceIp;
	}

	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}

	public String getDestinationIp() {
		return destinationIp;
	}

	public void setDestinationIp(String destinationIp) {
		this.destinationIp = destinationIp;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTenantId() {
		return tenantId;
	}

	public void setTenantId(int tenantId) {
		this.tenantId = tenantId;
	}
	
	  
}
