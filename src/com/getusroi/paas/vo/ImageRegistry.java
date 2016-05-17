package com.getusroi.paas.vo;

public class ImageRegistry {

	private String name;
	private String location;
	private String version;
	private String private_cloud;
	private String user_name;
	private String password;

	
	public ImageRegistry(String name, String location, String version,String private_cloud, String user_name, String password) {
		this.name = name;
		this.location = location;
		this.version = version;
		this.private_cloud = private_cloud;
		this.user_name = user_name;
		this.password = password;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPrivate_cloud() {
		return private_cloud;
	}

	public void setPrivate_cloud(String private_cloud) {
		this.private_cloud = private_cloud;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "ImageRegistryVO [name=" + name + ", location=" + location
				+ ", version=" + version + ", private_cloud=" + private_cloud
				+ ", user_name=" + user_name + ", password=" + password + "]";
	}
	
	

}
