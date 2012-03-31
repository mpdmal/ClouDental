package com.mpdmal.cloudental.dao;

import java.util.Date;
import java.util.Vector;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import com.mpdmal.cloudental.dao.base.CDentAbstractDao;
import com.mpdmal.cloudental.entities.Visit;

@Stateless
@LocalBean
public class VisitDAO  extends CDentAbstractDao {
	static final String COUNT_VISITS = "select count(v) from Visit v ";
	static final String GET_VISITS = "select v from Visit v";
	static final String WHERE_ACTIVITY = " where v.activity.id= :activityid";
	static final String WHERE_PATIENT = " where v.activity.patienthistory.patient.id= :patientid";
	static final String WHERE_EXT = " and v.visitdate >= :from and v.enddate <= :to";
	static final String COUNT_PATIENTVISITS = COUNT_VISITS + WHERE_PATIENT;
	static final String COUNT_ACTIVITYVISITS = COUNT_VISITS + WHERE_ACTIVITY;
	static final String GET_PATIENTVISITS = GET_VISITS+ WHERE_PATIENT;
	static final String GET_PATIENTVISITS_EXT = GET_VISITS+ WHERE_PATIENT+WHERE_EXT;
	static final String GET_ACTIVITYVISITS = GET_VISITS+ WHERE_ACTIVITY ;
	static final String GET_ACTIVITYVISITS_EXT = GET_VISITS+ WHERE_ACTIVITY + WHERE_EXT;
	
    public VisitDAO() { super(); }
    public VisitDAO(EntityManager em) { super(em); }
 
    public long countPatientVisits(int patientid) {
        return executeSingleLongQuery(_em.createQuery(COUNT_PATIENTVISITS)
        		.setParameter("patientid", patientid));
    }

    public long countActivityVisits(int activityid) {
        return executeSingleLongQuery(_em.createQuery(COUNT_ACTIVITYVISITS)
        		.setParameter("activityid", activityid));
    }

    public long countVisits() {
        return executeSingleLongQuery(_em.createQuery(COUNT_VISITS));
    }
    
    @SuppressWarnings("unchecked")
	public Vector<Visit> getVisits() {
    	return (Vector<Visit>)_em.createQuery(GET_VISITS).getResultList();
    }

    @SuppressWarnings("unchecked")
	public Vector<Visit> getPatientVisits(int patientid) {
    	return (Vector<Visit>)_em.createQuery(GET_PATIENTVISITS)
    			 .setParameter("patientid", patientid).getResultList();
    }
    
    @SuppressWarnings("unchecked")
	public Vector<Visit> getPatientVisits(int patientid, Date from, Date to) {
    	return (Vector<Visit>)_em.createQuery(GET_PATIENTVISITS_EXT)
    			 .setParameter("patientid", patientid)
    			 .setParameter("from", from)
    			 .setParameter("to", to).getResultList();
    }

    @SuppressWarnings("unchecked")
	public Vector<Visit> getActivityVisits(int activityid) {
    	return (Vector<Visit>)_em.createQuery(GET_ACTIVITYVISITS)
    			 .setParameter("activityid", activityid).getResultList();
    }
    
    @SuppressWarnings("unchecked")
	public Vector<Visit> getActivityVisits(int activityid, Date from, Date to) {
    	return (Vector<Visit>)_em.createQuery(GET_ACTIVITYVISITS_EXT)
    			 .setParameter("activityid", activityid)
    			 .setParameter("from", from)
    			 .setParameter("to", to).getResultList();
    }
   
}