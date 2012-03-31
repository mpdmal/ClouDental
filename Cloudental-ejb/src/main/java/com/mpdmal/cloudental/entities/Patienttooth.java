package com.mpdmal.cloudental.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class Patienttooth extends com.mpdmal.cloudental.entities.base.DBEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PatienttoothPK id;
	private String comments;
	@Lob
	private byte[] image;

    @ManyToOne
	@JoinColumn(name="patientid", insertable=false, updatable=false)
	private Patient patient;
    @ManyToOne
	@JoinColumn(name="toothid", insertable=false, updatable=false)
	private Tooth tooth;

    public Patienttooth() {
    }

	public PatienttoothPK getId() {
		return this.id;
	}

	public void setId(PatienttoothPK id) {
		this.id = id;
	}
	
	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public byte[] getImage() {
		return this.image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Patient getPatient() {
		return this.patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	public Tooth getTooth() {
		return this.tooth;
	}

	public void setTooth(Tooth tooth) {
		this.tooth = tooth;
	}

	@Override
	public String getXML() {
		StringBuilder ans= new StringBuilder("<tooth></tooth>");
		ans.insert(ans.indexOf("</tooth"), "<position>"+tooth.getPosition()+"</position>");
		ans.insert(ans.indexOf("</tooth"), "<name>"+tooth.getName()+"</name>");
		ans.insert(ans.indexOf("</tooth"), "<comments>"+comments+"</comments>");
		return ans.toString();
	}
	
}