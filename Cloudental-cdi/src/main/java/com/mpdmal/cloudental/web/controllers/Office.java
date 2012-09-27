package com.mpdmal.cloudental.web.controllers;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.web.beans.OfficeReceptionBean;
import com.mpdmal.cloudental.web.beans.backingbeans.PatientManagerBean;
import com.mpdmal.cloudental.web.beans.backingbeans.SchedulerBean;
import com.mpdmal.cloudental.web.beans.base.BaseBean;

@Named("office")
@ViewScoped
public class Office extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//CDI BEANS
	@Inject
	private DentistServices _dsvc;
	@Inject
	private PatientServices _psvc;
	@Inject
	private OfficeReceptionBean _session;
	
	//MODEL
	private SchedulerBean scheduleControler; //scheduler backing bean
	private PatientManagerBean patientManagment; //patient mngmnt backing bean
	
	@PostConstruct
	public void init () {
		super.init();
		scheduleControler = new SchedulerBean(this);
		patientManagment = new PatientManagerBean(this);
		refresh(_session.getUserID());
	}

	public Office() {
		super();
		_baseName = "Office";
	}
	//GETTERS/SETTERS
	public int getOwnerID() { return _session.getUserID(); }
	public SchedulerBean getScheduleControler() {	return scheduleControler;	}
	public PatientManagerBean getPatientManagment() {	return patientManagment;	}
	public DentistServices getDentistServices() { return _dsvc; }
	public PatientServices getPatientServices() { return _psvc; }
	public OfficeReceptionBean getSession() { return _session; }

	public void setPatientManagment(PatientManagerBean patientManagment) {	this.patientManagment = patientManagment;	}
	public void setScheduleControler(SchedulerBean scheduleControler) {	this.scheduleControler = scheduleControler;	}
	
	//INTERFACE
	public void refresh(int userID) {	
		populateScheduler();
		populatePatientList();
	}
	
	public void populateScheduler() {	
		scheduleControler.populateScheduler(getOwnerID());
	}
	
	public void populatePatientList() {
		patientManagment.populatePatients(getOwnerID());
	}
}

