package com.mpdmal.cloudental.dao;

import java.util.Vector;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import com.mpdmal.cloudental.dao.base.CDentAbstractDao;
import com.mpdmal.cloudental.entities.Tooth;

@Stateless
@LocalBean
public class TeethDAO extends CDentAbstractDao {
	static final String GET_DEFAULTTEETH= "select t from Tooth t";
	
    public TeethDAO(EntityManager em) { super(em); }
    
   @SuppressWarnings("unchecked")
	public Vector<Tooth> getDefaultTeethSet() {
    	return (Vector<Tooth>)_em.createQuery(GET_DEFAULTTEETH).getResultList();
    }

}
