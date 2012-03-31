package com.mpdmal.cloudental.entities;

import java.io.Serializable;
import javax.persistence.*;

import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.InvalidContactInfoTypeException;

@Embeddable
public class ContactinfoPK implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer infotype;

    public ContactinfoPK() {}

    public Integer getId() 		{	return this.id;	}
	public Integer getInfotype(){	return this.infotype;	}
	public void setInfotype(Integer infotype) throws InvalidContactInfoTypeException {
		if (CloudentUtils.isContactInfoTypeValid(infotype)) {
			this.infotype = infotype;	
	    	return;
		}
    	CloudentUtils.logError("Cannot set unknown contact info type :"+infotype);
    	throw new InvalidContactInfoTypeException(infotype);	
	}
	
	public void setId(Integer id) {		this.id = id;	}
	
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ContactinfoPK)) {
			return false;
		}
		ContactinfoPK castOther = (ContactinfoPK)other;
		return 
			this.id.equals(castOther.id)
			&& this.infotype.equals(castOther.infotype);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.id.hashCode();
		hash = hash * prime + this.infotype.hashCode();
		
		return hash;
    }
}