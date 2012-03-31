package com.mpdmal.cloudental.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

@Entity
public class Medicalhistory extends com.mpdmal.cloudental.entities.base.DBEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@OneToOne
	@JoinColumn(name="id")
	private Patient patient;
	private String comments;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="medicalhistory")
	private Set<Medicalhistoryentry> entries;

    public Medicalhistory() {}

	public Integer getId() {		return patient.getId();	}
	public String getComments() {		return this.comments;	}
	public Patient getPatient() {		return this.patient;	}
	public Set<Medicalhistoryentry> getEntries() {	return this.entries;	}
	
	public void setPatient(Patient patient) {		this.patient = patient;	}
	public void setComments(String comments) {		this.comments = comments;	}
	public void setEntries(Set<Medicalhistoryentry> entries) {	this.entries= entries;	}
	@Override
	public String getXML() {
		StringBuilder ans= new StringBuilder("<medhistory></medhistory>");
		ans.insert(ans.indexOf("</medhistory"), "<comments>"+comments+"</comments>");
		Set<Medicalhistoryentry> entries= getEntries();
		for (Medicalhistoryentry entry : entries) {
			ans.insert(ans.indexOf("</medhistory"), entry.getXML());
		}
		return ans.toString();
	}
}