package com.mpdmal.cloudental.web.beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.util.CloudentUtils;

@ManagedBean 
@SessionScoped
public class OfficeState {
	private Dentist currentUser;

	public Dentist getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(Dentist currentUser) {
		this.currentUser = currentUser;
	}

	@PostConstruct
	public void init() {
		CloudentUtils.logServicecall("new OfficeState");
	}


}
