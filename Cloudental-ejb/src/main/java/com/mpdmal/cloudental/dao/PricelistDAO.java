package com.mpdmal.cloudental.dao;

import java.util.Vector;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import com.mpdmal.cloudental.dao.base.CDentAbstractDao;
import com.mpdmal.cloudental.entities.PricelistItem;

@Stateless
@LocalBean
public class PricelistDAO  extends CDentAbstractDao {
	static final String COUNT_PCITEMS = "select count(p) from Pricelist p ";
	static final String GET_PCITEMS = "select p from Pricelist p";
	static final String WHERE = " where p.dentist.username = :dentistid";
	static final String COUNT_DENTISTPCITEMS = COUNT_PCITEMS + WHERE;
	static final String GET_DENTISTPCITEMS = GET_PCITEMS+ WHERE;
	
    public PricelistDAO() { super(); }
    public PricelistDAO(EntityManager em) { super(em); }
 
    public long countPricelistItems(String username) {
        return executeSingleLongQuery(_em.createQuery(COUNT_DENTISTPCITEMS)
        		.setParameter("dentistid", username));
    }
    
    public long countPricelistItems() {
        return executeSingleLongQuery(_em.createQuery(COUNT_PCITEMS));
    }
    
    @SuppressWarnings("unchecked")
	public Vector<PricelistItem> getPricelistItems() {
    	return (Vector<PricelistItem>)_em.createQuery(GET_PCITEMS).getResultList();
    }
    
    @SuppressWarnings("unchecked")
	public Vector<PricelistItem> getPricelistItems(String username) {
    	return (Vector<PricelistItem>)_em.createQuery(GET_DENTISTPCITEMS)
    			.setParameter("dentistid", username).getResultList();
    }
}
