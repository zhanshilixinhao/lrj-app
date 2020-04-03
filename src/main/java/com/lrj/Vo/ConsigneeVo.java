package com.lrj.VO;

public class ConsigneeVo {
	private Integer consigneeId;
	private String consigneeName;
	private String consigneeMobile;
	private String name;
	private String address;
	private Integer isDefault;
	
	public Integer getConsigneeId() {
		return consigneeId;
	}
	public void setConsigneeId(Integer consigneeId) {
		this.consigneeId = consigneeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAppConsigneeId() {
		return consigneeId;
	}
	public void setAppConsigneeId(Integer appConsigneeId) {
		this.consigneeId = appConsigneeId;
	}
	public String getConsigneeName() {
		return consigneeName;
	}
	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}
	public String getConsigneeMobile() {
		return consigneeMobile;
	}
	public void setConsigneeMobile(String consigneeMobile) {
		this.consigneeMobile = consigneeMobile;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
	
	
}
