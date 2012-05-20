package com.mpdmal.cloudental.web.beans;

import java.io.Serializable;
import java.util.Vector;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.mpdmal.cloudental.entities.Activity;

@ManagedBean(name="activityHolderBean")
@ViewScoped
public class ActivityHolderBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private Vector<Activity> activityListOfPatient;

	public Vector<Activity> getActivityListOfPatient() {
		return activityListOfPatient;
	}

	public void setActivityListOfPatient(Vector<Activity> activityListOfPatient) {
		this.activityListOfPatient = activityListOfPatient;
	}
	

}
