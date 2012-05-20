package com.mpdmal.cloudental.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.persistence.*;

@Entity
public class Medicalhistory extends com.mpdmal.cloudental.entities.base.DBEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@OneToOne
	@JoinColumn(name="id")
	private Patient patient;
	
	@Column(nullable=true, length=1024)
	private String comments;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="medicalhistory")
	private Collection<Medicalhistoryentry> entries;

    public Medicalhistory() {}

	public Integer getId() {	return patient.getId();	}
	public String getComments() {	return this.comments;	}
	public Patient getPatient() {	return this.patient;	}
	public Collection<Medicalhistoryentry> getEntries() {	return this.entries;	}
	
	public void addMedicalEntry(Medicalhistoryentry entry) {
		if (entries == null)
			entries = new ArrayList<Medicalhistoryentry>();
				
		entries.add(entry);
	}
	
	public void setEntries(Set<Medicalhistoryentry> entries) {
		if (entries != null)
			entries.clear();
		
		for (Medicalhistoryentry medicalhistoryentry : entries) {
			addMedicalEntry(medicalhistoryentry);
		}
	}
	
	public void deleteMedicalEntry(Medicalhistoryentry entry) {
		if (entries.contains(entry))
			entries.remove(entry);
	}
	
	public void setPatient(Patient patient) {		this.patient = patient;	}
	public void setComments(String comments) {		this.comments = comments;	}
	
	@Override
	public String getXML() {
		StringBuilder ans= new StringBuilder("<medhistory></medhistory>");
		ans.insert(ans.indexOf("</medhistory"), "<comments>"+comments+"</comments>");
		Collection<Medicalhistoryentry> entries= getEntries();
		for (Medicalhistoryentry entry : entries) {
			ans.insert(ans.indexOf("</medhistory"), entry.getXML());
		}
		return ans.toString();
	}
}