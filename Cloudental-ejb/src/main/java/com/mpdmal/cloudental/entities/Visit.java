package com.mpdmal.cloudental.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.Set;

@Entity
public class Visit extends com.mpdmal.cloudental.entities.base.DBEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String comments;
	private int color;
	@NotNull
	private String title;
	@Temporal( TemporalType.TIMESTAMP)
	private Date visitdate;
	@Temporal( TemporalType.TIMESTAMP)
	private Date enddate;
    @ManyToOne
	@JoinColumn(name="activityid")
	private Activity activity;
    @OneToMany(cascade=CascadeType.ALL, mappedBy="visit")
	private Set<Toothhistory> toothhistory;
    private BigDecimal deposit;

    public Visit() {    }

	public Date getVisitdate() {	return this.visitdate;	}
	public Date getEnddate() 	{	return this.enddate;	}
	public String getComments() 	{	return this.comments;	}
	public Integer getId() 			{	return this.id;	}
	public Activity getActivity() 	{	return this.activity;	}
	public Set<Toothhistory> getToothhistory() {	return this.toothhistory;	}
    public BigDecimal getDeposit() 	{	return this.deposit;	}
	public String getTitle() 	{	return this.title;	}
	public Integer getColor() 			{	return this.color;	}
	
    public void setToothhistory(Set<Toothhistory> toothhistory) {	this.toothhistory= toothhistory;	}
	public void setActivity(Activity activity) 	{	this.activity = activity;	}
	public void setComments(String comments) 	{	this.comments = comments;	}
	public void setEnddate(Date enddate) 	{	this.enddate = enddate;	}
	public void setVisitdate(Date visitdate) {		this.visitdate = visitdate;	}
	public void setId(Integer id) {		this.id = id;	}
    public void setDeposit(BigDecimal deposit) 			{	this.deposit= deposit;	}
	public void setTitle(String title) 	{	this.title = title;	}
	public void setColor(Integer color) {		this.color = color;	}


	@Override
	public String getXML() {
		StringBuilder ans= new StringBuilder("<visit></visit>");
		ans.insert(ans.indexOf("</visit"), "<start>"+visitdate+"</start>");
		ans.insert(ans.indexOf("</visit"), "<end>"+enddate+"</end>");
		ans.insert(ans.indexOf("</visit"), "<title>"+title+"</title>");
		ans.insert(ans.indexOf("</visit"), "<color>"+color+"</color>");
		ans.insert(ans.indexOf("</visit"), "<comments>"+comments+"</comments>");
		
		ans.insert(ans.indexOf("</visit"), "<ToothOperations>");
		for (Toothhistory history : toothhistory) {
			ans.insert(ans.indexOf("</visit"), history.getXML());
		}		
		ans.insert(ans.indexOf("</visit"), "</ToothOperations>");
		
		return ans.toString();
	}	
}