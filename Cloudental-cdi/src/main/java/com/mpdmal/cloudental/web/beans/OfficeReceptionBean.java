package com.mpdmal.cloudental.web.beans;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.mpdmal.cloudental.web.beans.base.BaseBean;

@Named("officeReception")
@SessionScoped
public class OfficeReceptionBean extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private int userID;
	private boolean loggedIn = false;
	
	public OfficeReceptionBean() {
		super();
		_baseName = "OfficeReception";
	}
	//GETTERS/SETTERS
	public boolean isLoggedIn() {	return loggedIn;	}
	public int getUserID() {	return userID;	}

	public void setUserID(int userID) {	
		this.userID = userID;
		loggedIn = true;
	}
}

