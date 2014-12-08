package com.tamilshout.model;

import java.io.Serializable;

public class UserBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Email;
	private String userid;
	private String usersname;
	private String Password;

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return Password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		Password = password;
	}

	/**
	 * @return the usersname
	 */
	public String getUsersname() {
		return usersname;
	}

	/**
	 * @param usersname
	 *            the usersname to set
	 */
	public void setUsersname(String usersname) {
		this.usersname = usersname;
	}

}
