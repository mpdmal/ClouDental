package com.mpdmal.cloudental.web.beans;

import java.util.Vector;

import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.entities.Visit;

public class SchedulerControler {
	//MODEL
	private ScheduleModel _visitModel = new DefaultScheduleModel();
	private DentistServices _dsvc;

	public SchedulerControler(DentistServices ds) {
		_dsvc = ds;
	}
	
	//GETTERS/SETTERS
	public ScheduleModel getModel() { return _visitModel; }
	
	//INTERFACE
	public void populateScheduler(int dentistid) {
		Vector<Visit> vsts;
		vsts = _dsvc.getDentistVisits(dentistid);
		for (Visit visit : vsts) {
			_visitModel.addEvent(new DefaultScheduleEvent(visit.getTitle(),visit.getVisitdate(), visit.getEnddate()));
		}
	}
}
