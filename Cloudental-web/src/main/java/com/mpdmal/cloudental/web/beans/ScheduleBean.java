package com.mpdmal.cloudental.web.beans;


import java.util.Date;
import java.util.Vector;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.ScheduleEntrySelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.web.events.DentistScheduleEvent;

public class ScheduleBean {

	@EJB
	DentistServices dentistServices;

	UserHolder user = (UserHolder)FacesContext.getCurrentInstance().
			getExternalContext().getSessionMap().get("userHolder");
	private ScheduleModel model;
	private ScheduleModel lazyEventModel;



	private DentistScheduleEvent event = new DentistScheduleEvent();
	public ScheduleBean() {
		System.out.println("ScheduleBean: costructor");
		//		model = new ScheduleModel();
		model =  new DefaultScheduleModel();
		model.addEvent(new DefaultScheduleEvent("title", new Date(),	new Date()));

		lazyEventModel = new LazyScheduleModel() {
			private static final long serialVersionUID = 1L;
			@Override
			public void loadEvents(Date start, Date end) {

				System.out.println("loadEvents form: "+start.toString()+" to: "+end.toString());
				Patient patient = new Patient();
				patient.setName("p1");
				patient.setSurname("p1 sur");
				patient.setId(1);
				DentistScheduleEvent event1 = new DentistScheduleEvent("event1",start,start,patient,new Integer(0));

				addEvent(event1);
				addEvent(new DefaultScheduleEvent("Lazy Event a", start, start,true) );
			}
		};

	}
	public ScheduleModel getModel() {
		System.out.println("getModel: ");
		return model; 
	}

	public ScheduleModel getLazyEventModel() {
		return lazyEventModel;
	}
	public DentistScheduleEvent getEvent() {
		System.out.println("getEvent: "+event.getTitle() );
		return event; 
	}
	public void setEvent(DentistScheduleEvent event) {
		System.out.println("setEvent: "+event.getTitle());
		this.event = event; 
	}

	public void addEvent() {
		System.out.println("addEvent "+event.getTitle()+"start: "+event.getStartDate() +" end: " + event.getEndDate() + "date "+event.getData());
		if(event.getId() == null){
			//model.addEvent(event);
		}
		else{
			//model.updateEvent(event);
		}

	}



	public void onEventSelect(ScheduleEntrySelectEvent e) {
		event = (DentistScheduleEvent) e.getScheduleEvent();
		System.out.println("onEventSelect: "+event.getTitle());
	}
	public void onDateSelect(DateSelectEvent e) {
		Date d= e.getDate();
		System.out.println("onDateSelect: "+d.toString());
		event = new DentistScheduleEvent("", e.getDate(), e.getDate(), null, null );
	}

	public void onEventMove(ScheduleEntryMoveEvent event) {  
		System.out.println("onEventMove" + "");
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());  

		addMessage(message);  
	} 
	public void onEventMove() {  
		System.out.println("onEventMove no argument" + "");
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getStartDate() + ", Minute delta:" + event.getEndDate());  

		addMessage(message);  
	} 

	public void onEventResize(ScheduleEntryResizeEvent event) {  
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());  

		addMessage(message);  
	}  

	private void addMessage(FacesMessage message) {  
		FacesContext.getCurrentInstance().addMessage(null, message);  
	}  




	public String createPatient(){
		String name = event.getNameNewPatient() ;
		String surname = event.getSurnameNewPatient();
		System.out.println("create: "+name+" "+surname);
		dentistServices.createPatient(user.getCurrentUser().getUsername() , name, surname);
		Patient p = new Patient();
		p.setName(name);
		p.setSurname(surname);
		p.setId(new Integer(9))		;
		event.setPatient(p);
		return null;
	}




}
