package com.mpdmal.cloudental.web.events;

import java.util.Date;

import org.primefaces.model.DefaultScheduleEvent;

import com.mpdmal.cloudental.entities.Patient;

public class DentistScheduleEvent extends DefaultScheduleEvent {
	private static final long serialVersionUID = 1L;

	//MODEL
	private Patient patient;
	private Integer activityId;
	private String nameNewPatient;
	private String surnameNewPatient;
	
	public DentistScheduleEvent() {	super();	}
	public DentistScheduleEvent(String title, Date startDate, Date endDate, Patient patient, Integer activityId) {
		super(title, startDate, endDate);
		this.patient=patient;
		this.activityId = activityId;
	}

	//GETTERS/SETTERS
	public Patient getPatient() {	return patient;	}
	public Integer getActivityId() {	return activityId;	}
	public String getNameNewPatient() {	return nameNewPatient;	}
	public String getSurnameNewPatient() {	return surnameNewPatient;	}

	public void setPatient(Patient patient) {	this.patient = patient;	}
	public void setActivityId(Integer activityId) {	this.activityId = activityId;	}
	public void setNameNewPatient(String nameNewPatient) {	this.nameNewPatient = nameNewPatient;	}
	public void setSurnameNewPatient(String surnameNewPatient) {	this.surnameNewPatient = surnameNewPatient;	}
}
