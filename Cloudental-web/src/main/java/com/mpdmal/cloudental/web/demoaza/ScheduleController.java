package com.mpdmal.cloudental.web.demoaza;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.ScheduleEntrySelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import com.mpdmal.cloudental.entities.Postit;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.InvalidPostitAlertException;

public class ScheduleController {
	private ScheduleModel eventModel;
	private ScheduleEvent event = new DefaultScheduleEvent();
	private static Vector<Postit> v = new Vector<Postit>();

	static {
		try {
			for (int i = 0; i < 9; i++) {
				Postit p = new Postit();
				if (i <3) {
					p.setPost("remember to ...."+i);
					p.setAlert(CloudentUtils.PostitAlertType.NOTE.getValue());
				} else 
				if (i <7) {
					p.setPost("notify patient of ..."+i);
					p.setAlert(CloudentUtils.PostitAlertType.ALARM.getValue());
				} else {
					p.setPost("call ...."+i);
					p.setAlert(CloudentUtils.PostitAlertType.TODO.getValue());
				}
				v.add(p);
			}
		} catch (InvalidPostitAlertException e) {
			e.printStackTrace();
		}
	}

	public ScheduleController() {
		eventModel = new DefaultScheduleModel();
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
	
	public Vector<Postit> getPostits() {
		return v;
	}
	
	public ScheduleModel getEventModel() {
		return eventModel;
	}
	//Getters and Setters
}
