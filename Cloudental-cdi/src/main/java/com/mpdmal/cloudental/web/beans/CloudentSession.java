package com.mpdmal.cloudental.web.beans;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.mpdmal.cloudental.web.beans.base.BaseBean;


@Named("dentistSession")
@SessionScoped
public class CloudentSession extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private int dentistid , patientid ;

	public int getDentistid() {	return dentistid;	}
	public int getPatientid() {	return patientid;	}

	public void setDentistid(int dentistid) {	this.dentistid = dentistid;	}
	public void setPatientid(int patientid) {	this.patientid = patientid;	}
	
}
