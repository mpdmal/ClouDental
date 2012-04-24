package com.mpdmal.cloudental.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.mpdmal.cloudental.util.CloudentUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Activity extends com.mpdmal.cloudental.entities.base.DBEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String description;

    @Temporal( TemporalType.TIMESTAMP)
	private java.util.Date enddate;
    @Temporal( TemporalType.TIMESTAMP)
	private java.util.Date startdate; 

    @NotNull
    @ManyToOne
	@JoinColumn(name="patienthistid")
	private Patienthistory patienthistory;
    @NotNull
    @ManyToOne
	@JoinColumn(name="priceable")
	private PricelistItem priceable;
    @NotNull
    @ManyToOne
	@JoinColumn(name="discount")
	private Discount discount;
    private BigDecimal price;
    @NotNull
    @OneToMany(cascade=CascadeType.ALL, mappedBy="activity")
	private Set<Visit> visits;
    @NotNull
    private boolean isOpen = true;
    
	public Activity() {    }

	public Integer getId() {
		return this.id;
	}

	public void setOpen(boolean isOpen) {	this.isOpen = isOpen;	}
	public void setId(Integer id) {		this.id = id;	}
	public void setDescription(String description) 	{	this.description = description;	}
	public void setEnddate(Date enddate) 		{	
		this.enddate = enddate;
		CloudentUtils.logMessage("Activity closed "+id);
		isOpen = false;
	}
	public void setStartdate(Date startdate) 	{
		this.startdate = startdate;	
	}
	public void setPriceable(PricelistItem priceable) 	{	this.priceable = priceable;	}
	public void setDiscount(Discount discount) 	{	this.discount= discount;	}
	public void setPatienthistory(Patienthistory patienthistory) {		this.patienthistory = patienthistory;	}	
    public void setPrice(BigDecimal price) 			{	this.price= price;	}
	public void setVisits(Set<Visit> visits) {	
		if (visits == null)
			visits = new HashSet<Visit>();
		else
			visits.clear();
		
		for (Visit visit : visits) {
			addVisit(visit);
		}
	}
	public void addVisit(Visit v) {
		if (visits == null)
			visits = new HashSet<Visit>();
		
		for (Visit visit : visits) {
			if (visit.getVisitdate().equals(v.getVisitdate())
					&& visit.getEnddate().equals(v.getEnddate()))
				CloudentUtils.logWarning("Visit already exists, wont add:"+visit.getComments());
			return;
		}
		v.setActivity(this);
		visits.add(v);
	}
	public String getDescription() 	{	return this.description;	}
	public Date getEnddate() 	{	return this.enddate;	}
	public Date getStartdate() {	return this.startdate;	}
	public PricelistItem getPriceable() 	{	return this.priceable;	}
	public Discount getDiscount() 	{	return this.discount;	}
	public Patienthistory getPatienthistory() {		return this.patienthistory;	}
	public Set<Visit> getVisits() {	return this.visits;	}
    public BigDecimal getPrice() 	{	return this.price;	}
    public boolean isOpen() {	return isOpen;	}

	@Override
	public String getXML() {
		StringBuilder ans= new StringBuilder("<activity></activity>");
		ans.insert(ans.indexOf("</activity"), "<description>"+description+"</description>");
		ans.insert(ans.indexOf("</activity"), "<price>"+priceable.getPrice()+"</price>");
		ans.insert(ans.indexOf("</activity"), "<isOpen>"+isOpen+"</isOpen>");
		ans.insert(ans.indexOf("</activity"), "<startdate>"+startdate+"</startdate>");
		ans.insert(ans.indexOf("</activity"), "<enddate>"+enddate+"</enddate>");
		ans.insert(ans.indexOf("</activity"), "<visits>");
		for (Visit visit : visits) {
			ans.insert(ans.indexOf("</activity"), visit.getXML());
		}
		ans.insert(ans.indexOf("</activity"), "</visits>");
		return ans.toString();
	}
}