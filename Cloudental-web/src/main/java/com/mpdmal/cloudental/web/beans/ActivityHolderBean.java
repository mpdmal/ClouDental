package com.mpdmal.cloudental.web.beans;

import java.io.Serializable;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.entities.Patient;


public class ActivityHolderBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private Vector<Activity> activityListOfPatient;



	public ActivityHolderBean() {
		super();
		System.out.println("ActivityHolderBean costructor"+this.hashCode());
	}

	public Vector<Activity> getActivityListOfPatient() {
		return activityListOfPatient;
	}

	public void setActivityListOfPatient(Vector<Activity> activityListOfPatient) {
		this.activityListOfPatient = activityListOfPatient;
	}

	@PostConstruct
	public void initialize() {
		//System.out.println("ActivityManagementBean PostConstruct");

		FacesContext context = FacesContext.getCurrentInstance();
		ActivityManagementBean activityManagementBean = (ActivityManagementBean)context.getApplication() .evaluateExpressionGet(context, "#{activityManagementBean}", ActivityManagementBean.class);
		if(activityManagementBean!=null)
			activityListOfPatient = activityManagementBean.loadActivityList();
	}
	
	public void loadActivityListOfPatient(Patient p){
		FacesContext context = FacesContext.getCurrentInstance();
		ActivityManagementBean activityManagementBean = (ActivityManagementBean)context.getApplication() .evaluateExpressionGet(context, "#{activityManagementBean}", ActivityManagementBean.class);
		activityManagementBean.setSelectedPatient(p);
		activityListOfPatient = activityManagementBean.loadActivityList();
		
	}




}
