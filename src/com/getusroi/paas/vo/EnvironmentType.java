package com.getusroi.paas.vo;

public class EnvironmentType {
	
	private String name;
	private String description;
	private String acceptTag;
	private String promoteTag;
	private String action;
	private int restartInterval;
	private int quietPeriod;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAcceptTag() {
		return acceptTag;
	}
	public void setAcceptTag(String acceptTag) {
		this.acceptTag = acceptTag;
	}
	public String getPromoteTag() {
		return promoteTag;
	}
	public void setPromoteTag(String promoteTag) {
		this.promoteTag = promoteTag;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public int getRestartInterval() {
		return restartInterval;
	}
	public void setRestartInterval(int restartInterval) {
		this.restartInterval = restartInterval;
	}
	public int getQuietPeriod() {
		return quietPeriod;
	}
	public void setQuietPeriod(int quietPeriod) {
		this.quietPeriod = quietPeriod;
	}

}
