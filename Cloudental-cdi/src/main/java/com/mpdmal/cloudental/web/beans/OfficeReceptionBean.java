package com.mpdmal.cloudental.web.beans;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.util.exception.DentistNotFoundException;
import com.mpdmal.cloudental.web.beans.base.BaseBean;
import com.mpdmal.cloudental.web.util.CloudentWebUtils;

@Named("officeReception")
@SessionScoped
public class OfficeReceptionBean extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	{
		setBaseName("Reception bean (session)");
	}

	//MODEL
	private boolean loggedIn = false;
	private Dentist _dentist = null;
	private String theme = CloudentWebUtils.DEFAULT_PRIMEFACES_THEME;
	
	//CDI BEANS
	@Inject
	private DentistServices _dsvc;
	private String _receptionmsg;

	//CONSTRUCTOR
	public OfficeReceptionBean() {
		super();
		_baseName = "OfficeReception";
	}
	//GETTERS/SETTERS
	public String getTheme() {	return theme;	}
	public Dentist getUser() { return _dentist; }
	public boolean isLoggedIn() {	return loggedIn;	}
	public int getUserID() {	return _dentist.getId();	}
	public String getReceptionMessage() { 
		return (_dentist == null) ? "My office" : _receptionmsg;
	}

	public void setTheme(String theme) {	this.theme = theme;	}
	public void setUserID(int userID) {	
		try {
			_dentist = _dsvc.findDentist(userID);
			_receptionmsg = _dentist.getUIFriendlyString();
			loggedIn = true;
		} catch (DentistNotFoundException e) {
			loggedIn = false;
			e.printStackTrace();
		}
	}
}

