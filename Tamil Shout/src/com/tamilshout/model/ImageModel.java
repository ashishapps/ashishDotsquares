package com.tamilshout.model;

import java.io.Serializable;

public class ImageModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String imagePath="";
	private int imageid=-1;
	private int  itemid=-1;
	private boolean  imageUpload=false;
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public int getImageid() {
		return imageid;
	}
	public void setImageid(int imageid) {
		this.imageid = imageid;
	}
	public int getItemid() {
		return itemid;
	}
	public void setItemid(int itemid) {
		this.itemid = itemid;
	}
	public boolean isImageUpload() {
		return imageUpload;
	}
	public void setImageUpload(boolean imageUpload) {
		this.imageUpload = imageUpload;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
