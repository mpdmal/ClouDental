package com.mpdmal.cloudental.web.beans;
import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.entities.Activity;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.Visit;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.ActivityNotFoundException;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;
import com.mpdmal.cloudental.util.exception.PatientNotFoundException;
import com.mpdmal.cloudental.util.exception.base.CloudentException;
import com.mpdmal.cloudental.web.beans.base.BaseBean;

@Named("mainBean")
@RequestScoped
public class MainBean extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String SEPERATOR = "|";
	//MODEL
	private Vector<String> patientNames;
	private Vector<String> activityNames;
	private Vector<String> visitNames;
	private String selectedPatient;
	private String selectedActivity;
	private String selectedVisit;
	private String visitTitle;
	private int visitDeposit;
	
	//OTHER CDI BEANS
	@Inject
	private CloudentSession session;
	@Inject
	private DentistServices dsvc;
	@Inject
	private PatientServices psvc;

	@PostConstruct
	public void init() {
		loadPatients(session.getDentistid());
		loadActivities(session.getPatientid());
		loadVisits(-1);
	}
	
	//GETTERS/SETTERS
	public String getVisitTitle() {	return visitTitle;	}
	public int getVisitDeposit()  {	return visitDeposit;	}
	public String getSelectedPatient () { return selectedPatient; } 
	public String getSelectedActivity() { return selectedActivity; }
	public String getSelectedVisit() { return selectedVisit; }
	public Vector<String> getPatientNames()  {	return patientNames;	}
	public Vector<String> getActivityNames() {	return activityNames;	}
	public Vector<String> getVisitNames() {	return visitNames;	}

	public void setSelectedVisit(String s)  {	selectedVisit = s;	}
	public void setSelectedPatient(String s)  {	selectedPatient = s;	}
	public void setSelectedActivity(String s) {	selectedActivity = s;	}
	public void setVisitTitle(String visitTitle)  {	this.visitTitle = visitTitle;	}
	public void setVisitDeposit(int visitDeposit) {	this.visitDeposit = visitDeposit;	}
	
	//INTERFACE
	public String selectPatient() {
		// grab the name and get the id from it ...
		try {
			int id = Integer.parseInt(selectedPatient.substring(0, selectedPatient.indexOf(SEPERATOR)));
			//set the id to the session			
			session.setPatientid(id);
			//get the activities for the ensuing getActivityNames call from JSF
			loadActivities(id);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return "ok";
	}
	
	public String createVisit() {
		int id;
		try {
			id = Integer.parseInt(selectedActivity.substring(0, selectedActivity.indexOf(SEPERATOR)));
			Long start = psvc.findActivity(id).getStartdate().getTime();
			psvc.createVisit(id, "auto gen", visitTitle, new Date(start+100000), new Date(start+300000), visitDeposit, 1);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return "Invalid activity id";
		} catch (CloudentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getMessage();
		}
		loadVisits(id);
		return "created";
	}
	
	public String getDentistName() {
		Dentist d;
		try {
			d = dsvc.findDentist(session.getDentistid());
		} catch (DentistNotFoundException e) {
			e.printStackTrace();
			return "Dentist not found";
		}
		return d.getName() + " " +d.getSurname();
	}
	//PRIVATE
	private void loadVisits (int aid) {
		if (visitNames == null)
			visitNames = new Vector<String>();
		visitNames.clear();
		
		try {
			for (Visit visit: psvc.getActivityVisits(aid)) {
				String val = visit.getId()+SEPERATOR+visit.getTitle().concat(SEPERATOR).concat(visit.getDeposit().toString());
				visitNames.add(val);
			}
		} catch (ActivityNotFoundException e) {
			CloudentUtils.logError(e.getMessage());
		}
	}
	

	private void loadActivities (int pid) {
		if (activityNames == null)
			activityNames = new Vector<String>();
		activityNames.clear();
		
		try {
			for (Activity act: psvc.getPatientActivities(pid)) {
				String val = act.getId()+SEPERATOR+act.getDescription().concat(SEPERATOR).concat(act.getDiscount().getDiscount().toString());
				activityNames.add(val);
			}
		} catch (PatientNotFoundException e) {
			CloudentUtils.logError(e.getMessage());
		}
	}
	
	private void loadPatients(int dentid) {
		if (patientNames == null)
			patientNames = new Vector<String>();
		patientNames.clear();
		
		for (Patient patient : dsvc.getPatientlist(dentid)) {
			String val = patient.getId()+SEPERATOR+patient.getName().concat(SEPERATOR).concat(patient.getSurname());
			patientNames.add(val);
		}
	}
}
