package com.mpdmal.cloudental.entities;

import java.io.Serializable;
import javax.persistence.*;

import com.mpdmal.cloudental.util.CloudentUtils;

@Entity
public class Contactinfo extends com.mpdmal.cloudental.entities.base.DBEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ContactinfoPK id;
	private String info;
    @ManyToOne
	@JoinColumn(name="id", insertable=false, updatable=false)
	private Patient patient;

    public Contactinfo() {}

	public ContactinfoPK getId(){	return this.id;	}
	public String getInfo() 	{	return this.info;	}
	public Patient getPatient() {	return this.patient;	}
	
	public void setPatient(Patient patient) {	this.patient = patient;	}
	public void setInfo(String info) 	{	this.info = info;	}
	public void setId(ContactinfoPK id) {	this.id = id;	}
	
	@Override
	public String getXML() {
		String type = "ct"+CloudentUtils.findContactInfoTypeDescr(id.getInfotype());
		return "<"+type+" value='"+info+"'/>";
	}
	
}