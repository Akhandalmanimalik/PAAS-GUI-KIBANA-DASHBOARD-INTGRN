package com.getusroi.paas.vo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ACL {
	  private String srcIp;
	  private String destIP;
	  private String action;
	  private String aclName;
	  
	public ACL(String srcIp, String destIP, String action, String aclName) {
		this.srcIp = srcIp;
		this.destIP = destIP;
		this.action = action;
		this.aclName = aclName;
	}
	public String getSrcIp() {
		return srcIp;
	}
	public void setSrcIp(String srcIp) {
		this.srcIp = srcIp;
	}
	public String getDestIP() {
		return destIP;
	}
	public void setDestIP(String destIP) {
		this.destIP = destIP;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getAclName() {
		return aclName;
	}
	public void setAclName(String aclName) {
		this.aclName = aclName;
	}
	@Override
	public String toString() {
		return "ACL [srcIp=" + srcIp + ", destIP=" + destIP + ", action="
				+ action + ", aclName=" + aclName + "]";
	}
	
	  
	  
}
