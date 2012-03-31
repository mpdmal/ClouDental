package com.mpdmal.cloudental.web.beans;

import com.mpdmal.cloudental.entities.Dentist;

public class UserHolder {
	private Dentist currentUser;

	public Dentist getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(Dentist currentUser) {
		this.currentUser = currentUser;
	}



}
