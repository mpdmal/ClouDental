package com.mpdmal.cloudental.web.beans;

import java.util.Date;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.ScheduleEntrySelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import com.mpdmal.cloudental.entities.Postit;


public class ScheduleController {  
    
    private ScheduleModel eventModel;  
      
  
    private ScheduleEvent event = new DefaultScheduleEvent();  
      
//    private String theme;  
    
    Set<Postit> postits;
  
    public ScheduleController() {  
    	
        eventModel = new DefaultScheduleModel();  
        eventModel.addEvent(new DefaultScheduleEvent("Champions League Match", previousDay8Pm(), previousDay11Pm()));  
        eventModel.addEvent(new DefaultScheduleEvent("Birthday Party", today1Pm(), today6Pm()));  
        eventModel.addEvent(new DefaultScheduleEvent("Breakfast at Tiffanys", nextDay9Am(), nextDay11Am()));  
        eventModel.addEvent(new DefaultScheduleEvent("Plant the new garden stuff", theDayAfter3Pm(), fourDaysLater3pm()));  
          
    }  
      
    


	public Set<Postit> getPostits() {
		return postits;
	}




	public void setPostits(Set<Postit> postits) {
		this.postits = postits;
	}




	private Date nextDay11Am() {
		// TODO Auto-generated method stub
		return getDateExample(11);
	}

	private Date theDayAfter3Pm() {
		// TODO Auto-generated method stub
		return getDateExample(3);
	}

	private Date fourDaysLater3pm() {
		// TODO Auto-generated method stub
		return getDateExample(3);
	}

	private Date nextDay9Am() {
		// TODO Auto-generated method stub
		return getDateExample(3);
	}

	private Date today6Pm() {
		// TODO Auto-generated method stub
		return getDateExample(3);
	}

	private Date today1Pm() {
		// TODO Auto-generated method stub
		return getDateExample(3);
	}

	private Date previousDay8Pm() {
		// TODO Auto-generated method stub
		return getDateExample(3);
	}

	private Date previousDay11Pm() {
		// TODO Auto-generated method stub
		return getDateExample(3);
	}
	
	private Date getDateExample(int i) {
		
		Date result = new Date( System.currentTimeMillis());
		return result;
	}

	public void addEvent(ActionEvent actionEvent) {  
        if(event.getId() == null)  
            eventModel.addEvent(event);  
        else  
            eventModel.updateEvent(event);  
          
        event = new DefaultScheduleEvent();  
    }  
      
    public void onEventSelect(ScheduleEntrySelectEvent selectEvent) {  
        event = selectEvent.getScheduleEvent();  
    }  
      
    public void onDateSelect(DateSelectEvent selectEvent) {  
        event = new DefaultScheduleEvent(Math.random() + "", selectEvent.getDate(), selectEvent.getDate());  
    }  
      
    public void onEventMove(ScheduleEntryMoveEvent event) {  
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());  
          
        addMessage(message);  
    }  
      
    public void onEventResize(ScheduleEntryResizeEvent event) {  
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());  
          
        addMessage(message);  
    }  
      
    private void addMessage(FacesMessage message) {  
        FacesContext.getCurrentInstance().addMessage(null, message);  
    }

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}  
	
	
    
    
      
    //Getters and Setters  
}  