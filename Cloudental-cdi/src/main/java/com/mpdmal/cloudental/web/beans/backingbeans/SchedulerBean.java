package com.mpdmal.cloudental.web.beans.backingbeans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.ScheduleEntrySelectEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

import com.mpdmal.cloudental.entities.Visit;
import com.mpdmal.cloudental.util.exception.VisitNotFoundException;
import com.mpdmal.cloudental.util.exception.base.CloudentException;
import com.mpdmal.cloudental.web.controllers.Office;
import com.mpdmal.cloudental.web.events.DentistScheduleEvent;

public class SchedulerBean implements Serializable {
	private static final long serialVersionUID = 1L;
	//MODEL
	private ScheduleModel _visitModel = new DefaultScheduleModel();
	private DentistScheduleEvent _event = new DentistScheduleEvent();
	private Office _office;

	public SchedulerBean(Office office) {
		_office = office;
	}
	
	//GETTERS/SETTERS
	public ScheduleModel getModel() { return _visitModel; }
	public DentistScheduleEvent getEvent() {	return _event;	}
	
	public void setEvent(DentistScheduleEvent event) {	this._event = event;	}

	//INTERFACE
	public void populateScheduler(int dentistid) {
		System.out.println("POPULATE SCHEDULER");
		Vector<Visit> vsts;
		vsts = _office.getDentistServices().getDentistVisits(dentistid);
		_visitModel.clear();
		for (Visit visit : vsts) {
			System.out.println(visit.getTitle()+visit.getVisitdate()+visit.getEnddate());
			_visitModel.addEvent(new DentistScheduleEvent(
					visit.getTitle(), visit.getVisitdate(), visit.getEnddate(), visit.getId()));
		}
	}
	
	public void onVisitSelect(ScheduleEntrySelectEvent e) {
		_event = (DentistScheduleEvent) e.getScheduleEvent();
	}
	
	public void onDateSelect(DateSelectEvent e) {
		_event = new DentistScheduleEvent("", e.getDate(), e.getDate(), null);
	}
	
	public void deleteVisit() {
		System.out.println("!delete visit:");
		try {
			_office.getPatientServices().deleteVisit(_event.getVisitId());
			_event = new DentistScheduleEvent();
		} catch (VisitNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
	public void addEvent() {
		System.out.println("add event");
		if(_event.getTitle() == null){
			System.out.println("event NULL");
		}else{
			System.out.println("add event:"+_event.getTitle());
			try {
				_event.setVisitId(_office.getPatientServices().createVisit(7, "", 
						_event.getTitle(), 
						_event.getStartDate(), 
						_event.getEndDate(), 
						1, 
						2).getId());
				populateScheduler(_office.getOwnerID());
			} catch (CloudentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Calendar c = new GregorianCalendar();
		int mins = (c.get(Calendar.ZONE_OFFSET) + c.get(Calendar.DST_OFFSET)) / (60 * 1000);
		System.out.println(mins);
	}
}
