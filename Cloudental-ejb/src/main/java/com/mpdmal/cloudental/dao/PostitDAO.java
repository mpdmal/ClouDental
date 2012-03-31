package com.mpdmal.cloudental.dao;

import java.util.Vector;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import com.mpdmal.cloudental.dao.base.CDentAbstractDao;
import com.mpdmal.cloudental.entities.Postit;

@Stateless
@LocalBean
public class PostitDAO  extends CDentAbstractDao {
	static final String COUNT_POSTITS = "select count(p) from Postit p ";
	static final String GET_POSTITS = "select p from Postit p ";
	static final String WHERE = " where p.dentist.username = :dentistid";
	static final String COUNT_DENTISTPOSTITS = COUNT_POSTITS + WHERE;
	static final String GET_DENTISTPOSTITS = GET_POSTITS+ WHERE;
	
    public PostitDAO() { super(); }
    public PostitDAO(EntityManager em) { super(em); }
 
    public long countPostits(String username) {
        return executeSingleLongQuery(_em.createQuery(COUNT_DENTISTPOSTITS)
        		.setParameter("dentistid", username));
    }
    
    public long countPostits() {
        return executeSingleLongQuery(_em.createQuery(COUNT_POSTITS));
    }
    
    @SuppressWarnings("unchecked")
	public Vector<Postit> getPostits() {
    	return (Vector<Postit>)_em.createQuery(GET_POSTITS).getResultList();
    }
    
    @SuppressWarnings("unchecked")
	public Vector<Postit> getPostits(String username) {
    	return (Vector<Postit>)_em.createQuery(GET_DENTISTPOSTITS)
    			.setParameter("dentistid", username).getResultList();
    }
}
