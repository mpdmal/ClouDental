package com.mpdmal.cloudental.entities;

import java.io.Serializable;
import javax.persistence.*;

@Embeddable
public class PostitPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private String id;

    @Temporal( TemporalType.TIMESTAMP)
	private java.util.Date postdate;

    public PostitPK() {    }

    public String getId() {	return this.id;	}
	public java.util.Date getPostdate() {		return this.postdate;	}
	public void setPostdate(java.util.Date postdate) {		this.postdate = postdate;	}
	public void setId(String dentistid) {		id = dentistid;	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PostitPK)) {
			return false;
		}
		PostitPK castOther = (PostitPK)other;
		return 
			this.id.equals(castOther.id)
			&& this.postdate.equals(castOther.postdate);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.id.hashCode();
		hash = hash * prime + this.postdate.hashCode();
		
		return hash;
    }
}