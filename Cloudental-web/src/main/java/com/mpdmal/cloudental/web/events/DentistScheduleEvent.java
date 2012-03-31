package com.mpdmal.cloudental.web.events;

import java.util.Date;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultScheduleEvent;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.web.beans.UserHolder;

public class DentistScheduleEvent extends DefaultScheduleEvent {
	

	private static final long serialVersionUID = 1L;

	private Patient patient;
	private Integer activityId;
	private String nameNewPatient;
	private String surnameNewPatient;
	
	
	public DentistScheduleEvent() {
		super();
	}

	public DentistScheduleEvent(String title, Date startDate, Date endDate, Patient patient, Integer activityId) {
		super(title, startDate, endDate);
		this.patient=patient;
		this.activityId = activityId;
		 
	}
	
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public Integer getActivityId() {
		return activityId;
	}
	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}
	
	public String getNameNewPatient() {
		System.out.println("getNameNewPatient: "+nameNewPatient);
		return nameNewPatient;
	}
	public void setNameNewPatient(String nameNewPatient) {
		this.nameNewPatient = nameNewPatient;
	}
	public String getSurnameNewPatient() {
		System.out.println("getSurnameNewPatient: "+surnameNewPatient);
		return surnameNewPatient;
	}
	public void setSurnameNewPatient(String surnameNewPatient) {
		this.surnameNewPatient = surnameNewPatient;
	}
	

	
	
		
	

}
