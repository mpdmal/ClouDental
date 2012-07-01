package com.mpdmal.cloudental.web.beans.backingbeans;

import java.util.Vector;

import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.ScheduleEntrySelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.entities.Visit;
import com.mpdmal.cloudental.web.events.DentistScheduleEvent;

public class SchedulerBean {
	//MODEL
	private ScheduleModel _visitModel = new DefaultScheduleModel();
	private DentistServices _dsvc;
	private DentistScheduleEvent event = new DentistScheduleEvent();

	public SchedulerBean(DentistServices ds) {
		_dsvc = ds;
	}
	
	//GETTERS/SETTERS
	public ScheduleModel getModel() { return _visitModel; }
	public DentistScheduleEvent getEvent() {	return event;	}
	
	public void setEvent(DentistScheduleEvent event) {	this.event = event;	}

	//INTERFACE
	public void populateScheduler(int dentistid) {
		Vector<Visit> vsts;
		vsts = _dsvc.getDentistVisits(dentistid);
		_visitModel.clear();
		for (Visit visit : vsts) {
			_visitModel.addEvent(new DefaultScheduleEvent(visit.getTitle(),visit.getVisitdate(), visit.getEnddate()));
		}
	}
	
	public void onEventSelect(ScheduleEntrySelectEvent e) {
		ScheduleEvent ev = e.getScheduleEvent();
		event = new DentistScheduleEvent("", ev.getStartDate(), ev.getEndDate(), null, null );
	}
	public void onDateSelect(DateSelectEvent e) {
		event = new DentistScheduleEvent("", e.getDate(), e.getDate(), null, null );
	}
	
	public void addEvent() {
		if(event.getId() == null){
		}else{
		}
	}

}
