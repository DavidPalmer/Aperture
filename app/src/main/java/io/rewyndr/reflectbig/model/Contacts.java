package io.rewyndr.reflectbig.model;

import java.io.Serializable;

public class Contacts implements Serializable{
	private String contactId;
	private String contactName;
	private String email;
	private String phNo;

	public Contacts() {
		super();
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhNo() {
		return phNo;
	}

	public void setPhNo(String phNo) {
		this.phNo = phNo;
	}

	public Contacts(String contactName, String email, String id) {
		super();
		this.contactName = contactName;
		this.email = email;
		this.contactId = id;
	}
}
