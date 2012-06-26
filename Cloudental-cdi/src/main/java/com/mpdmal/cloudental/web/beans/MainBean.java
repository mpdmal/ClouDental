package com.mpdmal.cloudental.web.beans;
import java.io.Serializable;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.web.beans.base.BaseBean;

@Named("mainBean")
@RequestScoped
public class MainBean extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//MODEL
	private Vector<String> patientNames;
	private String selectedValue;
	@Inject
	private CloudentSession session;
	@Inject
	private DentistServices dsvc;

	@PostConstruct
	public void init() {
		if (patientNames == null)
			patientNames = new Vector<String>();
		
		for (Patient patient : dsvc.getPatientlist(session.getDentistid())) {
			patientNames.add(patient.getName().concat("|").concat(patient.getSurname()));
		}
	}
	
	//GETTERS/SETTERS
	public String getSelection () { return selectedValue; } 
	public Vector<String> getValues() {	return patientNames;	}

	//INTERFACE
	public void setSelection(String s) {	selectedValue = s;	}
	
	public String doStuff() {
		return "ok";
	}
}
