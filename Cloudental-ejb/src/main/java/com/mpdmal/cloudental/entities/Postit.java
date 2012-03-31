package com.mpdmal.cloudental.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.InvalidPostitAlertException;

@Entity
public class Postit extends com.mpdmal.cloudental.entities.base.DBEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PostitPK id;
	@NotNull
	private Integer alert;
	@NotNull
	private String post;

    @ManyToOne
	@JoinColumn(name="id", insertable=false, updatable=false)
	private Dentist dentist;

    public Postit() { }

	public PostitPK getId() 	{	return this.id;	}
	public Integer getAlert() 	{	return this.alert;	}
	public String getPost() 	{	return this.post;	}
	public Dentist getDentist() {	return dentist;	}

	public String getAlertDescr() { return CloudentUtils.findPostitAlertDescr(alert);}
	public void setPost(String post) 	{	this.post = post;	}
	public void setAlert(Integer alert) throws InvalidPostitAlertException {
		if (CloudentUtils.isPostitAlertValid(alert)) {
	    	this.alert = alert;
	    	return;
		}
    	CloudentUtils.logError("Cannot set unkown postit alert:"+alert);
    	throw new InvalidPostitAlertException(alert);
    }
	
	public void setId(PostitPK id) 		{	this.id = id;	}	
	public void setDentist(Dentist dentist) {	this.dentist = dentist;	}

	@Override
	public String getXML() {
		StringBuilder ans= new StringBuilder("<postit></postit>");
		ans.insert(ans.indexOf("</postit"), "<post>"+post+"</post>");
		ans.insert(ans.indexOf("</postit"), "<created>"+id.getPostdate()+"</created>");
		ans.insert(ans.indexOf("</postit"), "<alert>"+CloudentUtils.findPostitAlertDescr(alert)+"</alert>");
		return ans.toString();
	}
}