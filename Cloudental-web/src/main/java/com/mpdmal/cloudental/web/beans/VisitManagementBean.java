package com.mpdmal.cloudental.web.beans;

import java.util.Vector;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.entities.Visit;

public class VisitManagementBean extends Visit {

	private static final long serialVersionUID = 1L;

	UserHolder user = (UserHolder)FacesContext.getCurrentInstance().
			getExternalContext().getSessionMap().get("userHolder");
	
	
	@EJB
	DentistServices dentistService;
	@EJB
	PatientServices patientServices;
	
	private Integer patientID= new Integer("1");
	private Integer activityID= new Integer("1");
	
	
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
		System.out.println("update visit: "+activityID);
//		dentistService.update(this);
		return null;
	}

	public Integer getPatientID() {
		return patientID;
	}

	public void setPatientID(Integer patientID) {
		this.patientID = patientID;
	}

	public Integer getActivityID() {
		return activityID;
	}

	public void setActivityID(Integer activityID) {
		this.activityID = activityID;
	}
	
	
	

}
