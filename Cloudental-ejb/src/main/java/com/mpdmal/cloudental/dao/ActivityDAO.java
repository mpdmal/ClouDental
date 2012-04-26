package com.mpdmal.cloudental.dao;

import java.util.Date;
import java.util.Vector;

import com.mpdmal.cloudental.dao.base.CDentAbstractDao;
import com.mpdmal.cloudental.entities.Activity;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
@LocalBean
public class ActivityDAO extends CDentAbstractDao {
	static final String COUNT_ACTIVITIES = "select count(ac) from Activity ac ";
	static final String GET_ACTIVITIES = "select ac from Activity ac";
	static final String SIMPLE_WHERE = " where ac.id= :activityid";
	static final String WHERE = " where ac.patienthistory.patient.id= :patientid"; 
	static final String WHERE_EXT = WHERE + " and ac.startdate >= :from and ac.enddate <= :to";
	static final String COUNT_PATIENTACTIVITIES = COUNT_ACTIVITIES + WHERE;
	static final String GET_PATIENTACTIVITIES = GET_ACTIVITIES+ WHERE;
	static final String GET_PATIENTACTIVITIES_EXT = GET_ACTIVITIES+ WHERE_EXT;
	static final String GET_ACTIVITY = GET_ACTIVITIES+ SIMPLE_WHERE;
	
    public ActivityDAO() {        super();    }
    public ActivityDAO(EntityManager em) {    	super(em);    }

    public long countActivities(int patientid) {
        return executeSingleLongQuery(_em.createQuery(COUNT_PATIENTACTIVITIES)
        		.setParameter("patientid", patientid));
    }
    
    public long countActivities() {
        return executeSingleLongQuery(_em.createQuery(COUNT_ACTIVITIES));
    }

    @SuppressWarnings("unchecked")
	public Vector<Activity> getActivities() {
    	return (Vector<Activity>)_em.createQuery(GET_ACTIVITIES).getResultList();
    }

    @SuppressWarnings("unchecked")
	public Vector<Activity> getActivities(int patientid) {
    	return (Vector<Activity>)_em.createQuery(GET_PATIENTACTIVITIES)
    			 .setParameter("patientid", patientid).getResultList();
    }

	public Activity getActivity(int activityid) {
    	return (Activity)_em.createQuery(GET_PATIENTACTIVITIES)
    			.setParameter("activityid", activityid).getSingleResult();
    }

    @SuppressWarnings("unchecked")
	public Vector<Activity> getActivities(int patientid, Date from, Date to) {
    	return (Vector<Activity>)_em.createQuery(GET_PATIENTACTIVITIES_EXT)
    			 .setParameter("patientid", patientid)
    			 .setParameter("from", from)
    			 .setParameter("to", to).getResultList();
    }

}
