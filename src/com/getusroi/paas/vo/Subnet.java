package com.getusroi.paas.vo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Subnet {
	private String vpc_name;
	private String subnet_name;
	private String cidr;
	private String acl;
    private String subnetId;
    private String vpcId;
    private String environment;
    private int user_id;
	
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	
	public Subnet(){
		
	}
	

	public Subnet(String vpc_name, String subnet_name, String cidr, String acl,
			String subnetId,String vpcId, int user_id) {
		super();
		this.vpc_name = vpc_name;
		this.subnet_name = subnet_name;
		this.cidr = cidr;
		this.acl = acl;
		this.subnetId = subnetId;
		this.vpcId = vpcId;
		this.user_id = user_id;
	}
	public String getVpc_name() {
		return vpc_name;
	}
	public void setVpc_name(String vpc_name) {
		this.vpc_name = vpc_name;
	}
	public String getSubnet_name() {
		return subnet_name;
	}
	public void setSubnet_name(String subnet_name) {
		this.subnet_name = subnet_name;
	}
	public String getCidr() {
		return cidr;
	}
	public void setCidr(String cidr) {
		this.cidr = cidr;
	}
	public String getAcl() {
		return acl;
	}
	public void setAcl(String acl) {
		this.acl = acl;
	}
	public String getSubnetId() {
		return subnetId;
	}
	public void setSubnetId(String subnetId) {
		this.subnetId = subnetId;
	}
	
	public String getVpcId() {
		return vpcId;
	}
	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}
	@Override
	public String toString() {
		return "Subnet [vpc_name=" + vpc_name + ", subnet_name=" + subnet_name
				+ ", cidr=" + cidr + ", acl=" + acl + ", subnetId=" + subnetId
				+ ", vpcId=" + vpcId + ", environment=" + environment
				+ ", user_id=" + user_id + "]";
	}
	
	
}
