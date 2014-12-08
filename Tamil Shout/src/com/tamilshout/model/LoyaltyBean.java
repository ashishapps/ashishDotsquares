package com.tamilshout.model;

import java.io.Serializable;

public class LoyaltyBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String message,loyaltycount,special;

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the loyaltycount
	 */
	public String getLoyaltycount() {
		return loyaltycount;
	}

	/**
	 * @param loyaltycount the loyaltycount to set
	 */
	public void setLoyaltycount(String loyaltycount) {
		this.loyaltycount = loyaltycount;
	}

	
}
