package com.kaushik.contactManager.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="CONTACT")
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cId;
	
	@NotBlank(message = "This field is required")
	@Size(min=2,message = "Name must have characters more then 2")
	private String cName;

	private String cSecondName;
	
	@NotBlank(message = "This field is required")
	private String cWork;
	
	@NotBlank(message = "This field is required")
	@Pattern(regexp = "^(.+)@(\\S+)$", message = "Enter a valid Email Address !!")
	private String cEmail;
	
	
	@Size(min=1,max=12,message = "Phone number is invalid")
	@NotBlank(message = "This field is required")
	private String cPhoneNum;
	
	private String cImageUrl;
	@Column(length = 500)
	@NotBlank(message = "This field is required")
	private String cDescription;
	
	@ManyToOne 
	@JsonIgnore
	private User user;
	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Contact() {
		super();
	}

	public int getcId() {
		return cId;
	}
	public void setcId(int cId) {
		this.cId = cId;
	}
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	public String getcSecondName() {
		return cSecondName;
	}
	public void setcSecondName(String cSecondName) {
		this.cSecondName = cSecondName;
	}
	public String getcWork() {
		return cWork;
	}
	public void setcWork(String cWork) {
		this.cWork = cWork;
	}
	public String getcEmail() {
		return cEmail;
	}
	public void setcEmail(String cEmail) {
		this.cEmail = cEmail;
	}
	public String getcPhoneNum() {
		return cPhoneNum;
	}
	public void setcPhoneNum(String cPhoneNum) {
		this.cPhoneNum = cPhoneNum;
	}
	public String getcImageUrl() {
		return cImageUrl;
	}
	public void setcImageUrl(String cImageUrl) {
		this.cImageUrl = cImageUrl;
	}
	public String getcDescription() {
		return cDescription;
	}
	public void setcDescription(String cDescription) {
		this.cDescription = cDescription;
	}

	@Override
	public String toString() {
		return "Contact [cId=" + cId + ", cName=" + cName + ", cSecondName=" + cSecondName + ", cWork=" + cWork
				+ ", cEmail=" + cEmail + ", cPhoneNum=" + cPhoneNum + ", cImageUrl=" + cImageUrl + ", cDescription="
				+ cDescription + ", user=" + user + "]";
	}
	
	
	
}
