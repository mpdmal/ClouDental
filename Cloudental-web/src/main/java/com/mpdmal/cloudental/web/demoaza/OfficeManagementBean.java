package com.mpdmal.cloudental.web.demoaza;

import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.entities.Visit;
import com.mpdmal.cloudental.util.exception.ActivityNotFoundException;
import com.mpdmal.cloudental.web.beans.OfficeState;

@ManagedBean
@RequestScoped
public class OfficeManagementBean {
	//CLOUDENT SERVICES
	@Inject
	private DentistServices _dsvc;
	
	@ManagedProperty ("#{officeState}") 
	OfficeState ostate;
	public void setOstate(OfficeState ostate) { this.ostate = ostate; }
	
	//MODEL
	private int _dentistid;
	private ScheduleModel _visitmodel;
	
	//CONSTRUCTORS
	public OfficeManagementBean() {
		_visitmodel = new DefaultScheduleModel();
	}
	
	
	@PostConstruct
	protected void initializeOffice() {
		//session scoped bean SessionState holds logged in dentistid..
		_dentistid = ostate.getCurrentUser().getId();

		try {
			populateScheduler();
		} catch (ActivityNotFoundException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
		}
	}

	//INTERFACE
	public ScheduleModel getEventModel() {
		return _visitmodel;
	}
	

	//PRIVATE METHODS
	private void populateScheduler() throws ActivityNotFoundException {
		Vector<Visit> vsts = _dsvc.getDentistVisits(_dentistid);
		for (Visit visit : vsts) {
			_visitmodel.addEvent(new DefaultScheduleEvent(visit.getTitle(),visit.getVisitdate(), visit.getEnddate()));
		}
	}
}
