package com.tamilshout.model;

import java.io.Serializable;

public class ItemForSaleBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String AddedOn;
	private String currencycode;
	private boolean IsActive;
	private String ItemName;
	private String Itemid;
	private String businessuserid;
	private String contact_email;
	private String contact_name;
	private String contact_phone;
	private String description;
	private String itemcategoryid;
	private String itemcategoryname;
	private String time;
	private String price;
	private String itempictureid;
	public String getAddedOn() {
		return AddedOn;
	}
	public void setAddedOn(String addedOn) {
		AddedOn = addedOn;
	}
	public boolean isIsActive() {
		return IsActive;
	}
	public void setIsActive(boolean isActive) {
		IsActive = isActive;
	}
	public String getItemName() {
		return ItemName;
	}
	public void setItemName(String itemName) {
		ItemName = itemName;
	}
	public String getItemid() {
		return Itemid;
	}
	public void setItemid(String itemid) {
		Itemid = itemid;
	}
	public String getBusinessuserid() {
		return businessuserid;
	}
	public void setBusinessuserid(String businessuserid) {
		this.businessuserid = businessuserid;
	}
	public String getContact_email() {
		return contact_email;
	}
	public void setContact_email(String contact_email) {
		this.contact_email = contact_email;
	}
	public String getContact_name() {
		return contact_name;
	}
	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}
	public String getContact_phone() {
		return contact_phone;
	}
	public void setContact_phone(String contact_phone) {
		this.contact_phone = contact_phone;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getItemcategoryid() {
		return itemcategoryid;
	}
	public void setItemcategoryid(String itemcategoryid) {
		this.itemcategoryid = itemcategoryid;
	}
	public String getItemcategoryname() {
		return itemcategoryname;
	}
	public void setItemcategoryname(String itemcategoryname) {
		this.itemcategoryname = itemcategoryname;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getItempictureid() {
		return itempictureid;
	}
	public void setItempictureid(String itempictureid) {
		this.itempictureid = itempictureid;
	}
	/**
	 * @return the currencycode
	 */
	public String getCurrencycode() {
		return currencycode;
	}
	/**
	 * @param currencycode the currencycode to set
	 */
	public void setCurrencycode(String currencycode) {
		this.currencycode = currencycode;
	}
	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}
	

}
