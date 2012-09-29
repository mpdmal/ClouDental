package com.mpdmal.cloudental.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.mpdmal.cloudental.entities.base.DBEntity;
import com.mpdmal.cloudental.util.CloudentUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
public class Activity extends DBEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_ACTIVITY_IDENTIFIER_DESCR= "def act| cdent";
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	private String description;
	
	@Temporal( TemporalType.TIMESTAMP)
	private java.util.Date enddate;
	
	@NotNull
    @Temporal( TemporalType.TIMESTAMP)
	private java.util.Date startdate; 

    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="patienthistid")
	private Patienthistory patienthistory;
    
    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="priceable")
	private PricelistItem priceable;
    
    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="discount")
	private Discount discount;
    
    @OneToMany(cascade=CascadeType.ALL, mappedBy="activity", fetch=FetchType.EAGER)
	private Collection<Visit> visits;
    
    @NotNull
    private boolean isopen = true;
    
	@Column(precision=131089)
    private BigDecimal price;
    
	public Activity() {    }

	public Integer getId() {
		return this.id;
	}

	public void setOpen(boolean isOpen) {
		if (enddate == null) {
			CloudentUtils.logError("wont close an activity without an enddate "+getId()+"|"+getDescription());
			return;
		}
		this.isopen = isOpen;	
	}
	public void setId(Integer id) {		this.id = id;	}
	public void setDescription(String description) 	{	this.description = description;	}
	public void setEnddate(Date enddate) 		{
		if (enddate != null && enddate.getTime() > startdate.getTime()) {
			this.enddate = enddate;
			CloudentUtils.logMessage("Activity closed "+id+"("+description+")");
			isopen = false;
		}
	}
	public void setStartdate(Date startdate) 	{
		this.startdate = startdate;	
	}
	public void setPriceable(PricelistItem priceable) 	{	this.priceable = priceable;	}
	public void setDiscount(Discount discount) 	{	this.discount= discount;	}
	public void setPatienthistory(Patienthistory patienthistory) {		this.patienthistory = patienthistory;	}	
    public void setPrice(BigDecimal price) 			{	this.price= price;	}
	public void setVisits(Collection<Visit> visits) {	
		if (visits == null)
			visits = new ArrayList<Visit>();
		else
			visits.clear();
		
		for (Visit visit : visits) {
			addVisit(visit);
		}
	}
	public void addVisit(Visit v) {
		if (visits == null)
			visits = new ArrayList<Visit>();
		
		visits.add(v);
	}
	
	public void removeVisit(Visit v) {
		if (visits.contains(v))
			visits.remove(v);
	}
	
	public String getDescription() 	{	return this.description;	}
	public Date getEnddate() 	{	return this.enddate;	}
	public Date getStartdate() {	return this.startdate;	}
	public PricelistItem getPriceable() 	{	return this.priceable;	}
	public Discount getDiscount() 	{	return this.discount;	}
	public Patienthistory getPatienthistory() {		return this.patienthistory;	}
	public Collection<Visit> getVisits() {	return this.visits;	}
    public BigDecimal getPrice() 	{	return this.price;	}
    public boolean isOpen() {	return isopen;	}

	@Override
	public String getXML() {
		StringBuilder ans= new StringBuilder("<activity></activity>");
		ans.insert(ans.indexOf("</activity"), "<description>"+description+"</description>");
		ans.insert(ans.indexOf("</activity"), "<price>"+priceable.getPrice()+"</price>");
		ans.insert(ans.indexOf("</activity"), "<isOpen>"+isopen+"</isOpen>");
		ans.insert(ans.indexOf("</activity"), "<startdate>"+startdate+"</startdate>");
		ans.insert(ans.indexOf("</activity"), "<enddate>"+enddate+"</enddate>");
		ans.insert(ans.indexOf("</activity"), "<visits>");
		if (visits != null)
		for (Visit visit : visits) {
			ans.insert(ans.indexOf("</activity"), visit.getXML());
		}
		ans.insert(ans.indexOf("</activity"), "</visits>");
		return ans.toString();
	}
}