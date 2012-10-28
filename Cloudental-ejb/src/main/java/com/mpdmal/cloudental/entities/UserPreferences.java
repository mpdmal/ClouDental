package com.mpdmal.cloudental.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.mpdmal.cloudental.entities.base.DBEntity;

@Entity
public class UserPreferences extends DBEntity {
	@Id
	@OneToOne
	@JoinColumn(name="userid") 
	private Dentist dentist;
	
	private String emailcontent;
	private boolean emailnotification;
	private boolean dailyreports;

	
	public String getEmailcontent() {	return emailcontent;	}
	public Dentist getDentist() 	{	return dentist;	}
	public boolean isEmailnotification() {	return emailnotification;	}
	public boolean isDailyreports() 	 {	return dailyreports;	}

	public void setEmailnotification(boolean emailnotification) {	this.emailnotification = emailnotification;	}
	public void setDailyreports(boolean dailyreports) {	this.dailyreports = dailyreports;	}
	public void setEmailcontent(String emailcontent)  {	this.emailcontent = emailcontent;	}
	public void setDentist(Dentist dentist) {	this.dentist = dentist;	}
	
	@Override
	public String getXML() {
		StringBuffer ans = new StringBuffer();
		return ans.toString();
	}

}
