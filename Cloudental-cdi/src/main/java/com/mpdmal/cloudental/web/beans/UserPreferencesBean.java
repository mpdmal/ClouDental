package com.mpdmal.cloudental.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.entities.UserPreferences;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.CloudentUtils.EventTitleFormatType;
import com.mpdmal.cloudental.util.exception.InvalidTitleFormatTypeException;
import com.mpdmal.cloudental.util.exception.base.CloudentException;
import com.mpdmal.cloudental.web.beans.base.BaseBean;
import com.mpdmal.cloudental.web.util.CloudentWebUtils;

@Named("userPrefs")
@RequestScoped
public class UserPreferencesBean extends BaseBean implements Serializable { //unfortunately cannot extends userprefs ... 
	private static final long serialVersionUID = 1L;
	{
		setBaseName("User Prefs bean");
	}

	private UserPreferences _prefs;
	ArrayList<EventTitleFormatType> _titlecombovalues;
	TreeMap<String, String> _themes;
	@Inject
	private DentistServices _dsvc;
	@Inject
	OfficeReceptionBean _sess;

	@Override
	public void init() {
		super.init();
		try {
			_prefs = _dsvc.getUserPrefs(_sess.getUserID());
		} catch (CloudentException e) {
			e.printStackTrace();
		}
		_titlecombovalues= new ArrayList<EventTitleFormatType>();
		_titlecombovalues.add(CloudentUtils.EventTitleFormatType.FULL);
		_titlecombovalues.add(CloudentUtils.EventTitleFormatType.SHORT);
		_titlecombovalues.add(CloudentUtils.EventTitleFormatType.SURNAME);
		_titlecombovalues.add(CloudentUtils.EventTitleFormatType.NAME);
		
		_themes = new TreeMap<String, String>();  
        _themes.put("Aristo", "aristo"); 
        _themes.put("Cupertino", "cupertino");  
        _themes.put("Redmond", "redmond");
	}
	
	public Map<String, String> getThemes() {	return _themes;	}
	public ArrayList<EventTitleFormatType> getEventtitleformatTypes() {	return _titlecombovalues;	}
	public void save() {	
		_dsvc.savePrefs(_prefs);
		_sess.setTheme(_prefs.getTheme());
	}
	
	public void sendPatientsReport() {
		try {
			_dsvc.sendOnDemandReport(_sess.getUserID(),_prefs.getReportemail(), CloudentUtils.REPORTTYPE_PATIENTS);
		} catch (Exception e) {
			CloudentWebUtils.showJSFErrorMessage(e.getMessage());
			e.printStackTrace();
			return;
		} 
		CloudentWebUtils.showJSFInfoMessage("Your patients report is in the mail");
	}
	
	public void sendPharmacyReport() {
		//_dsvc.sendOnDemandReport(_sess.getUserID(),_prefs.getReportemail(), -1);
	}
	
	//USER PREFS INTERFACE
	public String getTheme() {	return _prefs.getTheme();	}
	public String getEmailcontent() {	return _prefs.getEmailcontent();	}
	public int getSchedulerMinHour() {	return _prefs.getSchedulerMinHour(); }
	public int getSchedulerMaxHour() {	return _prefs.getSchedulerMaxHour(); }
	public int getSchedulerStartHour() {	return _prefs.getSchedulerStartHour(); }
	public int getSchedulerSlotMins() {	return _prefs.getSchedulerSlotMins(); }
	public int getEventTitleFormatType() {	return _prefs.getEventTitleFormatType(); }
	public boolean isEmailnotification() {	return _prefs.isEmailnotification();	}
	public boolean isDailyreports() 	 {	return _prefs.isDailyreports();	}
	public String getReportemail() {	return _prefs.getReportemail();	}
	public String getPrescriptionHeader(){	return _prefs.getPrescriptionHeader();	}

	public void setPrescriptionHeader(String header)  {	_prefs.setPrescriptionHeader(header);	}
	public void setEventTitleFormatType (int type) throws InvalidTitleFormatTypeException { _prefs.setEventTitleFormatType(type);	}
	public void setReportemail(String reportemail) {	_prefs.setReportemail(reportemail);	}
	public void setSchedulerMaxHour(int hour) { _prefs.setSchedulerMaxHour(hour); }
	public void setSchedulerMinHour(int hour) { _prefs.setSchedulerMinHour(hour); }
	public void setSchedulerStartHour(int hour) { _prefs.setSchedulerStartHour(hour); }
	public void setSchedulerSlotMins(int hour) { _prefs.setSchedulerSlotMins(hour); }
	public void setTheme(String theme) {	_prefs.setTheme(theme);	}
	public void setEmailnotification(boolean emailnotification) {	_prefs.setEmailnotification(emailnotification);	}
	public void setDailyreports(boolean dailyreports) {	_prefs.setDailyreports(dailyreports);	}
	public void setEmailcontent(String emailcontent)  {	_prefs.setEmailcontent(emailcontent);	}
}
