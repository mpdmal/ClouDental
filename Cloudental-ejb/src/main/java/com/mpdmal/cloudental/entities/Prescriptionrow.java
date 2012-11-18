package com.mpdmal.cloudental.entities;

import java.io.Serializable;

import javax.persistence.*;

import com.mpdmal.cloudental.entities.base.DBEntity;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.InvalidMedIntakeRouteException;

@Entity
@Table(name="prescriptionrows")
public class Prescriptionrow extends DBEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private Integer duration;
	private Integer durunit;
	private Integer frequency;
	private Integer frequnit;
	@OneToOne
	@JoinColumn(name="medicine")
	private Medicine medicine;
	
	private Integer route;

	//bi-directional many-to-one association to Prescription
    @ManyToOne
	@JoinColumn(name="prescriptionid")
	private Prescription prescription;

    //GETTERS/SETTERS
	public Integer getId() {	return this.id;	}
	public int getRoute() {	return this.route;	}
	public String getRouteDescription() {	return CloudentUtils.findMedIntakeRouteDescr(this.route);	}
	public String getFreqUnitDescription() {	return CloudentUtils.findPrescrRowTimeunitFreqDescr(this.frequnit);	}
	public String getDurUnitDescription() {	return CloudentUtils.findPrescrRowTimeunitDurDescr(this.durunit);	}
	public int getDuration() {	return this.duration;	}
	public int getDurunit() {	return this.durunit;	}
	public int getFrequency() {	return this.frequency;	}
	public int getFrequnit() {	return this.frequnit;	}
	public Medicine getMedicine() {	return this.medicine;	}
	public Prescription getPrescription() {	return this.prescription;	}

	public void setId(Integer id) {	this.id = id;	}
	public void setRoute(int route) throws InvalidMedIntakeRouteException {
		if (!CloudentUtils.isMedIntakeRouteValid(route))
			throw new InvalidMedIntakeRouteException(route);
		
		this.route = route;	
	}
	public void setDuration(int duration) {		this.duration = duration;	}
	public void setDurunit(int durunit) {		this.durunit = durunit;	}
	public void setFrequency(int frequency) {	this.frequency = frequency;	}
	public void setFrequnit(int frequnit) {		this.frequnit = frequnit;	}
	public void setMedicine(Medicine medicine) {	this.medicine = medicine;	}
	public void setPrescription(Prescription prescription) {	this.prescription = prescription;	}	
	
	@Override
	public String getXML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUIFriendlyString() {
		return medicine.getUIFriendlyString()+" | "+getRouteDescription()
				+" ["+frequency+"("+frequnit+")] "
				+"["+duration+"("+durunit+")]";
	}
}