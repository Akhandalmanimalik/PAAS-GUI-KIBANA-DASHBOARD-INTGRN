package com.getusroi.paas.vo;

public class ContainerTypes {

	private String name;
	private int cpuShares;
	private int memory;
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCpuShares() {
		return cpuShares;
	}

	public void setCpuShares(int cpuShares) {
		this.cpuShares = cpuShares;
	}

	public int getMemory() {
		return memory;
	}

	public void setMemory(int memory) {
		this.memory = memory;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
