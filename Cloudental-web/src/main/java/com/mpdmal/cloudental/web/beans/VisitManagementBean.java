package com.mpdmal.cloudental.web.beans;

import java.util.Vector;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.entities.Visit;
import com.mpdmal.cloudental.util.exception.PatientNotFoundException;

public class VisitManagementBean extends Visit {

	private static final long serialVersionUID = 1L;

	UserHolder user = (UserHolder)FacesContext.getCurrentInstance().
			getExternalContext().getSessionMap().get("userHolder");
	
	
	@EJB
	DentistServices dentistService;
	@EJB
	PatientServices patientServices;
	
	private Integer patientID= new Integer("1");
	private Activity  selectedActivity;
	
	
	
	public Activity getSelectedActivity() {
		System.out.println("get activity: " );
		return selectedActivity;
	}
	public void setSelectedActivityID(Activity activity) {
		System.out.println("set activity: "+ activity.getId() );
		this.selectedActivity = activity;
	}


	public Vector<Activity> getOpenActivities() {
		Vector<Activity> result=null;
		try {
			result= patientServices.getPatientActivities(patientID);
		} catch (PatientNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

	

	public Vector<Visit> getVisitList() throws Exception {
		//Vector<Visit>  ans  = patientServices.getVisits(patientID);
		return  null;
	}

	public String createVisit() throws Exception{
		System.out.println("create visit: ");
		//patientServices.createVisit(activityID, getComments(), getVisitdate(), getEnddate() );
		return null;
	}

	public String updateVisit(){
		System.out.println("update visit: ");
//		dentistService.update(this);
		return null;
	}

	public Integer getPatientID() {
		return patientID;
	}

	public void setPatientID(Integer patientID) {
		this.patientID = patientID;
	}

	

}
