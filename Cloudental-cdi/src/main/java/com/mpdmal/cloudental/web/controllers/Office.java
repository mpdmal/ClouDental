package com.mpdmal.cloudental.web.controllers;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.ScheduleModel;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.web.beans.SchedulerBean;
import com.mpdmal.cloudental.web.beans.base.BaseBean;

@Named("office")
@SessionScoped
public class Office extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//CDI BEANS
	@Inject
	DentistServices _dsvc;
	
	//MODEL
	private SchedulerBean scheduleControler; //scheduler backing bean
	private int ownerID , selectedPatientID;
	
	@Override
	public void init () {
		scheduleControler = new SchedulerBean(_dsvc);
		//populateScheduler();
	}

	//GETTERS/SETTERS
	public int getOWnerID() {	return ownerID;	}
	public int getSelectedPatientID() {	return selectedPatientID;	}
	public ScheduleModel getVisitModel() { return scheduleControler.getModel(); }
	public SchedulerBean getScheduleControler() {	return scheduleControler;	}

	public void setScheduleControler(SchedulerBean scheduleControler) {	this.scheduleControler = scheduleControler;	}
	public void setSelectedPatientID(int patientid) {	this.selectedPatientID = patientid;	}
	public void setOwner(int ownerID) {	this.ownerID = ownerID;	}
	
	//INTERFACE
	public void setOwnerAndPopulate(int ownerID) {	
		this.ownerID = ownerID;
		populateScheduler();
	}
	
	public void populateScheduler() {	
		scheduleControler.populateScheduler(getOWnerID());
	}

}

