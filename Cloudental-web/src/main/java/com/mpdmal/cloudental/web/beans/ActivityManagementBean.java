package com.mpdmal.cloudental.web.beans;



import java.util.Date;
import java.util.Vector;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.entities.Activity;

public class ActivityManagementBean {

	UserHolder user = (UserHolder)FacesContext.getCurrentInstance().
			getExternalContext().getSessionMap().get("userHolder");
	
	@EJB
	DentistServices dentistService;
	@EJB
	PatientServices patientServices;
	
	private final static int demoPatientId=1;
	private Integer activityID;
	private Integer patientID = new Integer(demoPatientId);
	private String description;
	private Date startDate;
	private Date endDate;

	public Vector<Activity> getActivityListOfPatient() throws Exception {
		Vector<Activity>  ans  = patientServices.getActivities(patientID);
		return  ans;
	}

	public String createActivity() throws Exception{
		System.out.println("create activity: "+ description);
		patientServices.createActivity(patientID, description, startDate, endDate, -1, -1);
		return null;
	}

	public String updateActivity(){
		System.out.println("update activity: "+activityID);
//		dentistService.update(this);
		return null;
	}

	public void setCurrentActivity(ActivityManagementBean  a){
//		System.out.println("setCurrentActivity: "+a.getActivityID());
		
	}

	public Integer getActivityID() {
		return activityID;
	}

	public void setActivityID(Integer activityID) {
		this.activityID = activityID;
	}

	public Integer getPatientID() {
		return patientID;
	}

	public void setPatientID(Integer patientID) {
		this.patientID = patientID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
