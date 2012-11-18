package com.mpdmal.cloudental.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name="prescriptions")
public class Prescription extends com.mpdmal.cloudental.entities.base.DBEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private Timestamp created;
	@OneToMany(cascade=CascadeType.ALL, mappedBy="prescription", fetch=FetchType.LAZY)
	private Collection<Prescriptionrow> prescriptionrows;
    @ManyToOne
	@JoinColumn(name="dentistid")
	private Dentist dentist;
    @ManyToOne
	@JoinColumn(name="patienthistid") 
	private Patienthistory patienthistory;

	public Integer getId() {	return this.id;	}
	public Dentist getDentist() {	return this.dentist;	}
	public Timestamp getCreated() {		return this.created;	}
	public Collection<Prescriptionrow> getPrescriptionrows() {	return this.prescriptionrows;	}
	public Patienthistory getPatienthistory() {		return this.patienthistory;	}

	public void setId(Integer id) {	this.id = id;	}
	public void setCreated(Timestamp created) {	this.created = created;	}
	public void setDentist(Dentist dentist) {	this.dentist = dentist;	}
	public void setPatienthistory(Patienthistory patienthistory) {	this.patienthistory = patienthistory;	}
	public void setPrescriptionrows(Collection<Prescriptionrow> prescriptionrows) {
		for (Prescriptionrow prescriptionrow : prescriptionrows) {
			addPrescriptionRow(prescriptionrow);
		}
	}

	public void addPrescriptionRow(Prescriptionrow row) {
		if (prescriptionrows == null)
			prescriptionrows = new ArrayList<Prescriptionrow>();
		
		row.setPrescription(this);
		prescriptionrows.add(row);
	}
	
	@Override
	public String getXML() {
		return null;
	}
	
}