package com.mpdmal.cloudental.web.events;

import java.util.Date;

import org.primefaces.model.DefaultScheduleEvent;

import com.mpdmal.cloudental.entities.Patient;

public class DentistScheduleEvent extends DefaultScheduleEvent {
	private static final long serialVersionUID = 1L;

	//MODEL
	private Integer _visitid;
	private Patient _autocomplete;
	public DentistScheduleEvent() {	super();	}
	public DentistScheduleEvent(String title, Date startDate, Date endDate, Integer visitid) {
		super(title, startDate, endDate);
		_visitid = visitid;
	}

	//GETTERS/SETTERS
	public Integer getVisitId() {	return _visitid;	}
	public Patient getAutocompletePatient() {	return _autocomplete;	}

	public void setAutocompletePatient(Patient _autocomplete) {	this._autocomplete = _autocomplete;	}
	public void setVisitId(Integer visitid) {	_visitid = visitid;	}
}
