package com.mpdmal.cloudental.dao;

import java.util.Vector;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.mpdmal.cloudental.dao.base.CDentAbstractDao;
import com.mpdmal.cloudental.entities.Dentist;

@Stateless
@LocalBean
public class DentistDAO extends CDentAbstractDao {
	static final String COUNT_DENTISTS = "select count(co) from Dentist co";
	static final String GET_DENTISTS = "select co from Dentist co";
	static final String GET_DENTIST = GET_DENTISTS+ " where co.username= :username";
    public DentistDAO() { super(); }
    public DentistDAO(EntityManager em) { super(em); }
 
    public long countDentists() {
        return executeSingleLongQuery(_em.createQuery(COUNT_DENTISTS));
    }
    @SuppressWarnings("unchecked")
	public Vector<Dentist> getDentists() {
    	return (Vector<Dentist>)_em.createQuery(GET_DENTISTS).getResultList();
    }

    public Dentist getDentist(String username) {
   	 Query q = _em.createQuery(GET_DENTIST).setParameter("username", username);
        return  (Dentist)executeSingleObjectQuery(q);
   }
}
