package com.mpdmal.cloudental.web.beans;

import java.io.Serializable;
import java.util.Vector;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.mpdmal.cloudental.entities.Patient;


@ManagedBean(name="patientHolder")
@ViewScoped
public class PatientHolder implements Serializable{
	

	public PatientHolder() {
		super();
		FacesContext context = FacesContext.getCurrentInstance();
		PatientManagementBean patientManagementBean = (PatientManagementBean)context.getApplication() .evaluateExpressionGet(context, "#{patientManagementBean}", PatientManagementBean.class);
		patientsList = patientManagementBean .getPatientList();
	}
	private static final long serialVersionUID = 1L;
	private Vector<Patient> patientsList;
	public Vector<Patient> getPatientsList() {
		return patientsList;
	}
	public void setPatientsList(Vector<Patient> patientsList) {
		this.patientsList = patientsList;
	}

	

}