package com.mpdmal.cloudental.web.beans.backingbeans;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.entities.Patient;

public class PatientManagerBean {
	//MODEL
	DentistServices dentistService;
	PatientServices patientServices;
	Patient patient;
	
	public PatientManagerBean(DentistServices dsvc, PatientServices psvc) {
		dentistService = dsvc;
		patientServices = psvc;
		patient = new Patient();
	}

	//GETTERS/SETTERS
	public Patient getPatient() {	return patient;	}
	public void setPatient(Patient patient) {	this.patient = patient;	}

	public String createPatient(int dentistid) {
		try {
			dentistService.createPatient(dentistid , patient.getName(), patient.getSurname());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR, e.getMessage(),"" ));
			e.printStackTrace();
		} 
		return null;
	}
}

