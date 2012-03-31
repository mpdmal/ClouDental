package com.mpdmal.cloudental.entities;

import java.io.Serializable;
import javax.persistence.*;

import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.InvalidMedEntryAlertException;


@Entity
public class Medicalhistoryentry extends com.mpdmal.cloudental.entities.base.DBEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MedicalhistoryentryPK id;
	private Integer alert;
	private String comments;

	@ManyToOne (cascade=CascadeType.ALL)
	@JoinColumn(name="id", insertable=false, updatable=false)
	private Medicalhistory medicalhistory;

    public Medicalhistoryentry() {    }

	public Integer getAlert() 	{	return this.alert;	}
	public String getComments() {	return this.comments;	}
	public MedicalhistoryentryPK getId() {	return this.id;	}
	public Medicalhistory getMedicalhistory() {	return this.medicalhistory;	}
	
	public void setMedicalhistory(Medicalhistory medicalhistory) {	this.medicalhistory = medicalhistory;	}
	public void setId(MedicalhistoryentryPK id) {	this.id = id;	}
	public void setComments(String comments)	{	this.comments = comments;	}
	public void setAlert(Integer alert) throws InvalidMedEntryAlertException {
		if (CloudentUtils.isMedEntryAlertValid(alert)) {
	    	this.alert = alert;
	    	return;
		}
    	CloudentUtils.logError("Cannot set unknown medical history entry alert :"+alert);
    	throw new InvalidMedEntryAlertException(alert);	
	}
	
	
	@Override
	public String getXML() {
		StringBuilder ans= new StringBuilder("<entry></entry>");
		ans.insert(ans.indexOf("</entry"), "<added>"+id.getAdded()+"</added>");
		ans.insert(ans.indexOf("</entry"), "<comments>"+comments+"</comments>");
		ans.insert(ans.indexOf("</entry"), "<alert>"+CloudentUtils.findMedEntryAlertDescr(alert)+"</alert>");
		return ans.toString();
	}
}