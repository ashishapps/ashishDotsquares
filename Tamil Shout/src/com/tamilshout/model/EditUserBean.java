package com.tamilshout.model;

import java.io.Serializable;

public class EditUserBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String email;
	private String userid;
	private String usersname;
	private String password;
	private String dob;
	private String firstname;
	private String gender;
	private String lastname;
	private String mobileno;
	

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
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

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
