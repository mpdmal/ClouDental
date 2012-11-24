package com.mpdmal.cloudental.web.beans;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.beans.LoginBean;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.base.CloudentException;
import com.mpdmal.cloudental.web.beans.base.BaseBean;
import com.mpdmal.cloudental.web.util.CloudentWebUtils;

@Named("loginService")
@RequestScoped
public class LoginServiceBean extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	{
		setBaseName("Login bean (request)");
	}

	//CDI BEANS
	@Inject
	LoginBean _loginBean;
	@Inject
	OfficeReceptionBean _sess;
	@Inject
	private DentistServices _dsvc;
	private String _emailMessage;
	private String _emailTitle;
	
	public LoginServiceBean() {
		super();
		_baseName = "Login Service";
	}
	//MODEL 
	private String _name = "", _password = "";
	private boolean _direct = true;
	//GETTERS/SETTERS
	public String getName() {	return _name;	}
	public String getPassword() {	return _password;	}
	public boolean isDirectlyToOffice () { return _direct; }
	public String getEmailMessage() {	return _emailMessage;	}
	public String getEmailTitle() {	return _emailTitle;	}

	public void setEmailTitle(String emailTitle) {	this._emailTitle= emailTitle;	}
	public void setEmailMessage(String emailMessage) {	this._emailMessage = emailMessage;	}
	public void setName(String name) {	this._name = name;	}
	public void setPassword(String password) {	this._password = password;	}
	public void setDirectlyToOffice(boolean directly) { _direct = directly; }
	//INTERFACE
	public String login() {  
		Dentist d = null;
		try {
			d = _loginBean.doLogin(_name, _password);
			_sess.setTheme(_dsvc.getUserPrefs(d.getId()).getTheme());
		} catch (CloudentException e) {
            CloudentWebUtils.showJSFErrorMessage("", e.getMessage());
            CloudentUtils.logError(e.getMessage());
            return null;
		} catch (Exception e) {
			e.printStackTrace();
			CloudentUtils.logError(e.getMessage());
			return null;
		}
		//CloudentWebUtils.showJSFInfoMessage("Welcome to Cloud.M", d.getUsername());
		_sess.setUserID(d.getId());
        return (_direct) ? "office" : "reception";
    }
	
	public void contactCloudental() {
		CloudentWebUtils.showJSFInfoMessage("Thank you for contacting Cloudental");
		try {
			_loginBean.contactCloudM(_emailTitle, _emailMessage);
		} catch (CloudentException e) {
			
		}
	}
}
