package com.mpdmal.cloudental.web.beans.backingbeans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Vector;

import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.ScheduleEntrySelectEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.Visit;
import com.mpdmal.cloudental.util.exception.VisitNotFoundException;
import com.mpdmal.cloudental.util.exception.base.CloudentException;
import com.mpdmal.cloudental.web.controllers.Office;
import com.mpdmal.cloudental.web.events.DentistScheduleEvent;
import com.mpdmal.cloudental.web.util.CloudentWebUtils;

public class SchedulerBean implements Serializable {
	private static final long serialVersionUID = 1L;
	//MODEL
	private ScheduleModel _visitModel = new DefaultScheduleModel();
	private DentistScheduleEvent _event = new DentistScheduleEvent();
	private Office _office;
	private Vector<Visit> _visits;
	private String _GMT = "";
	
	public SchedulerBean(Office office) {
		_office = office;
		TimeZone tz = Calendar.getInstance().getTimeZone();
		int offset= (tz.getRawOffset()+tz.getDSTSavings())/3600000;
		_GMT = "GMT+";
		_GMT = (offset > 9) ? _GMT+offset+":00" :  _GMT+"0"+offset+":00";
	}
	
	//GETTERS/SETTERS
	public String getLocalTimeZone() { return _GMT;	}
	public ScheduleModel getModel()  { return _visitModel; }
	public Vector<Visit> getVisits() { return _visits;	}
	public DentistScheduleEvent getEvent() {	return _event;	}

	public void setVisits(Vector<Visit> visits) {	this._visits = visits;	}
	public void setEvent(DentistScheduleEvent event) {	this._event = event;	}

	//INTERFACE
	public void populateScheduler(int dentistid) {
		_visits = _office.getDentistServices().getDentistVisits(dentistid);
		_visitModel.clear();
		for (Visit visit : _visits) {
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
		try {
			_office.getPatientServices().deleteVisit(_event.getVisitId());
			_event = new DentistScheduleEvent();
			populateScheduler(_office.getOwnerID());
		} catch (VisitNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
	public void addEvent() throws Exception {
		if(_event.getTitle() == null)
			return;

		try {
			Patient p = _office.getScheduleControler().getEvent().getAutocompletePatient();
			if (p == null)
				throw new CloudentException("No patient selected");
			// default activity ID, see Patient.boxPatient(string);
			int activityID = p.getDentalHistory().getActivities().iterator().next().getId(); 
			
			_event.setVisitId(_office.getPatientServices().createVisit(activityID, "", 
					_event.getTitle(), 
					_event.getStartDate(), 
					_event.getEndDate(), 
					0, 0).getId());
			populateScheduler(_office.getOwnerID());
		} catch (CloudentException e) {
			CloudentWebUtils.showJSFErrorMessage("Cannot create Visit", e.getMessage());
			throw new Exception (e.getMessage());
		}
	}
}
