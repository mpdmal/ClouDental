package com.mpdmal.cloudental.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class Toothhistory extends com.mpdmal.cloudental.entities.base.DBEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String comments;
	private Integer toothid;

    @ManyToOne
	@JoinColumn(name="visitid", insertable=false, updatable=false)
	private Visit visit;

    public Toothhistory() {    }

	public Integer getId() {		return this.id;	}
	public void setId(Integer id) {		this.id = id;	}
	public String getComments() {		return this.comments;	}
	public void setComments(String comments) {		this.comments = comments;	}
	public Integer getTooth() {		return this.toothid;	}
	public void setTooth(Integer tooth) {		this.toothid = tooth;	}	
	public Visit getVisit() {		return this.visit;	}
	public void setVisit(Visit visit) {		this.visit = visit;	}
	
	@Override
	public String getXML() {
		StringBuilder ans= new StringBuilder("<toothoperation></toothoperation>");
		ans.insert(ans.indexOf("</toothoperation"), "<toothid>"+toothid+"</toothid>");
		ans.insert(ans.indexOf("</toothoperation"), "<comments>"+comments+"</comments>");
		return ans.toString();
	}
}