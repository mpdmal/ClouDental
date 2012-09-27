package com.mpdmal.cloudental.web.events;

import java.util.Date;

import org.primefaces.model.DefaultScheduleEvent;

public class DentistScheduleEvent extends DefaultScheduleEvent {
	private static final long serialVersionUID = 1L;

	//MODEL
	private Integer _visitid;
	
	public DentistScheduleEvent() {	super();	}
	public DentistScheduleEvent(String title, Date startDate, Date endDate, Integer visitid) {
		super(title, startDate, endDate);
		_visitid = visitid;
	}

	//GETTERS/SETTERS
	public Integer getVisitId() {	return _visitid;	}

	public void setVisitId(Integer visitid) {	_visitid = visitid;	}
}
