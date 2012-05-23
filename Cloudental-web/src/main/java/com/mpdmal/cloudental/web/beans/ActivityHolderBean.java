package com.mpdmal.cloudental.web.beans;

import java.io.Serializable;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.util.exception.PatientNotFoundException;

@ManagedBean(name="activityHolderBean")
@ViewScoped
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




}
