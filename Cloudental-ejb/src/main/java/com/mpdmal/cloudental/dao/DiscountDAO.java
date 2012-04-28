package com.mpdmal.cloudental.dao;

import java.util.Vector;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import com.mpdmal.cloudental.dao.base.CDentAbstractDao;
import com.mpdmal.cloudental.entities.Discount;

@Stateless
@LocalBean
public class DiscountDAO  extends CDentAbstractDao {
	static final String COUNT_DISCOUNTS = "select count(d) from Discount d ";
	static final String GET_DISCOUNTS = "select d from Discount d ";
	static final String WHERE_SIMPLE = " where d.id = :discountid";
	static final String WHERE = " where d.dentist.username = :dentistid";
	static final String COUNT_DENTISTDISCOUNTS = COUNT_DISCOUNTS + WHERE;
	static final String GET_DENTISTDISCOUNTS = GET_DISCOUNTS+ WHERE;
	static final String GET_DISCOUNT = GET_DISCOUNTS+ WHERE_SIMPLE;
	
    public DiscountDAO() { super(); }
    public DiscountDAO(EntityManager em) { super(em); }
 
    public long countDiscounts(String username) {
        return executeSingleLongQuery(_em.createQuery(COUNT_DENTISTDISCOUNTS)
        		.setParameter("dentistid", username));
    }
    
    public long countDiscounts() {
        return executeSingleLongQuery(_em.createQuery(COUNT_DISCOUNTS));
    }
    
    @SuppressWarnings("unchecked")
	public Vector<Discount> getDiscounts() {
    	return (Vector<Discount>)_em.createQuery(GET_DISCOUNTS).getResultList();
    }
    
    @SuppressWarnings("unchecked")
	public Vector<Discount> getDiscounts(String username) {
    	return (Vector<Discount>)_em.createQuery(GET_DENTISTDISCOUNTS)
    			.setParameter("dentistid", username).getResultList();
    }
    
	public Discount getDiscount(int discountid) {
    	return (Discount) _em.createQuery(GET_DISCOUNT)
    			.setParameter("discountid", discountid).getSingleResult();
    }
}
