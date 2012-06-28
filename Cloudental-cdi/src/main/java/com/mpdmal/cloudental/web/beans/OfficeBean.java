package com.mpdmal.cloudental.web.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.ScheduleModel;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.web.beans.base.BaseBean;

@Named("office")
@SessionScoped
public class OfficeBean extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//CDI BEANS
	@Inject
	DentistServices _dsvc;
	
	//MODEL
	private SchedulerControler scheduleControler; //scheduler backing bean
	private int ownerID , selectedPatientID;
	@PostConstruct
	public void init () {
		scheduleControler = new SchedulerControler(_dsvc);
	}

	//GETTERS/SETTERS
	public int getOWnerID() {	return ownerID;	}
	public int getSelectedPatientID() {	return selectedPatientID;	}
	public ScheduleModel getVisitModel() { return scheduleControler.getModel(); }
	
	public void setOwnerAndPopulate(int ownerID) {	
		this.ownerID = ownerID;			
		scheduleControler.populateScheduler(getOWnerID());
	}
	
	public void setSelectedPatientID(int patientid) {	this.selectedPatientID = patientid;	}
	
}

