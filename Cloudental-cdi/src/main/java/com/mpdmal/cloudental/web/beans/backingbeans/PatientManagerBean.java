package com.mpdmal.cloudental.web.beans.backingbeans;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.web.controllers.Office;

public class PatientManagerBean {
	//MODEL
	DentistServices dentistService;
	PatientServices patientServices;
	Office office;
	Patient patient;
	
	public PatientManagerBean(Office office, DentistServices dsvc, PatientServices psvc) {
		dentistService = dsvc;
		patientServices = psvc;
		this.office = office;
		patient = new Patient();
	}

	//GETTERS/SETTERS
	public Patient getPatient() {	return patient;	}
	public void setPatient(Patient patient) {	this.patient = patient;	}

	//INTERFACE
	public String createPatient() {
		try {
			dentistService.createPatient(office.getOWnerID(), patient.getName(), patient.getSurname());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR, e.getMessage(),"" ));
			e.printStackTrace();
		} 
		return null;
	}
}

