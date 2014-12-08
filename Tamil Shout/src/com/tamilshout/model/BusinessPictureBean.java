package com.tamilshout.model;

import java.io.Serializable;

public class BusinessPictureBean extends ImageList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String addedon;
	private String businessid;
	private String id;
	private String type;
	private String time;
	public String getAddedon() {
		return addedon;
	}
	public void setAddedon(String addedon) {
		this.addedon = addedon;
	}
	public String getBusinessid() {
		return businessid;
	}
	public void setBusinessid(String businessid) {
		this.businessid = businessid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
