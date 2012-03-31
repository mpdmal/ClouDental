package com.mpdmal.cloudental.entities;

import java.io.Serializable;
import javax.persistence.*;

@Embeddable
public class PatienttoothPK implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer toothid;
	private Integer patientid;

    public PatienttoothPK() {}
	public Integer getToothid()   {	return this.toothid;	}
	public Integer getPatientid() {	return this.patientid;	}
	public void setPatientid(Integer patientid) {	this.patientid = patientid;	}
	public void setToothid(Integer toothid) 	{	this.toothid = toothid;	}
	
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PatienttoothPK)) {
			return false;
		}
		PatienttoothPK castOther = (PatienttoothPK)other;
		return 
			this.toothid.equals(castOther.toothid)
			&& this.patientid.equals(castOther.patientid);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.toothid.hashCode();
		hash = hash * prime + this.patientid.hashCode();
		
		return hash;
    }
}