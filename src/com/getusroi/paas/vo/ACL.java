package com.getusroi.paas.vo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ACL {
	  private String srcIp;
	  private String destIP;
	  private String aclName;
	  private int tenant_id;
	  private int subnet_id;
	public ACL(String srcIp, String destIP, String aclName, int tenant_id,
			int subnet_id) {
		super();
		this.srcIp = srcIp;
		this.destIP = destIP;
		this.aclName = aclName;
		this.tenant_id = tenant_id;
		this.subnet_id = subnet_id;
	}
	public ACL() {
		super();
	}
	@Override
	public String toString() {
		return "ACL [srcIp=" + srcIp + ", destIP=" + destIP + ", aclName="
				+ aclName + ", tenant_id=" + tenant_id + ", subnet_id="
				+ subnet_id + "]";
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
	public String getAclName() {
		return aclName;
	}
	public void setAclName(String aclName) {
		this.aclName = aclName;
	}
	public int getTenant_id() {
		return tenant_id;
	}
	public void setTenant_id(int tenant_id) {
		this.tenant_id = tenant_id;
	}
	public int getSubnet_id() {
		return subnet_id;
	}
	public void setSubnet_id(int subnet_id) {
		this.subnet_id = subnet_id;
	}
	
	  
}
