package com.mpdmal.cloudental.beans;

import java.math.BigDecimal;
import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.jws.WebService;
import javax.persistence.Query;

import com.mpdmal.cloudental.EaoManager;
import com.mpdmal.cloudental.entities.Dentist;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.entities.PricelistItem;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.InvalidPostitAlertException;

@Named
@Stateless
@LocalBean
@WebService
public class DentistServices {
	@EJB
	private EaoManager emgr;
	
    public DentistServices() {}
    public DentistServices(EaoManager mgr) { 
    	this.emgr = mgr;
    }

    public void close() {
    	emgr.closeEM();
    }

    //DISCOUNTS
    public long countDiscounts() {
    	Query q = emgr.getEM().createQuery("select count(d) from Discount d");
        return emgr.executeSingleLongQuery(q);
    }

    @SuppressWarnings("unchecked")
	public Collection<Discount> getDiscounts(int dentistid) {
    	Query q = emgr.getEM().
    			createQuery("select d from Discount d where d.dentist.id =:dentistid").
    			setParameter("dentistid", dentistid);
        return (Collection<Discount>) emgr.executeMultipleObjectQuery(q);
    }

	public Discount createDiscount(int dentistid, String title, String description, double value) throws InvalidPostitAlertException {
		Dentist dentist = emgr.findOrFail(Dentist.class, dentistid);
		if (dentist == null) {
    		CloudentUtils.logWarning("Dentist does not exist:"+dentistid+", discount bined:"+title);
			return null;
		}
		
		Discount d = new Discount();
		d.setDescription(description);
		d.setTitle(title);
		d.setDiscount(BigDecimal.valueOf(value));
		d.setDentist(dentist);
		dentist.addDiscount(d);

		emgr.persist(d);
		return d;
	}

	public void deleteDiscount(int id) {
		Discount d= emgr.findOrFail(Discount.class, id);
		if (d== null) {
			//TODO
			return ;
		}
		d.getDentist().removeDiscount(d);
		emgr.delete(d);
	}

    public void updateDiscount(int id, String description, String title) {
		Discount d= emgr.findOrFail(Discount.class, id);
		if (d == null) {
			//TODO
			return ;
		}
		for (Discount ds : d.getDentist().getDiscounts()) {
			if (ds.getId() == d.getId()) {
				ds.setDescription(description);
				ds.setTitle(title);
				break;
			}
		}
		emgr.update(d);
    }

    //PRICABLES
    public long countPricelistItems() {
    	Query q = emgr.getEM().createQuery("select count(pi) from PricelistItem pi");
        return emgr.executeSingleLongQuery(q);
    }

    @SuppressWarnings("unchecked")
	public Collection<PricelistItem> getPricelist(int dentistid) {
    	Query q = emgr.getEM().
    			createQuery("select pi from PricelistItem pi where pi.dentist.id =:dentistid").
    			setParameter("dentistid", dentistid);
        return (Collection<PricelistItem>) emgr.executeMultipleObjectQuery(q);
    }

	public PricelistItem createPricelistItem(int dentistid, String title, String description, double value) throws InvalidPostitAlertException {
		Dentist dentist = emgr.findOrFail(Dentist.class, dentistid);
		if (dentist == null) {
    		CloudentUtils.logWarning("Dentist does not exist:"+dentistid+", pc item bined:"+title);
			return null;
		}
		
		PricelistItem item = new PricelistItem();
		item.setDescription(description);
		item.setTitle(title);
		item.setPrice(BigDecimal.valueOf(value));
		item.setDentist(dentist);
		dentist.addPricelistItem(item);

		emgr.persist(item);
		return item;
	}
	
	public Collection<PricelistItem> deletePricelistItem(int id) {
		PricelistItem item = emgr.findOrFail(PricelistItem.class, id);
		if (item == null) {
			//TODO
			return null;
		}
		int iD = item.getDentist().getId();
		item.getDentist().removePricelistItem(item);
		emgr.delete(item);
		
		return getPricelist(iD);
	}

    public Collection<PricelistItem> updatePricelistItem(int id, String description, String title) {
		PricelistItem item = emgr.findOrFail(PricelistItem.class, id);
		if (item == null) {
			//TODO
			return null;
		}
		int iD = item.getDentist().getId();
		item.getDentist().removePricelistItem(item);
		item.setDescription(description);
		item.setTitle(title);
		item.getDentist().addPricelistItem(item);
		emgr.update(item);
		return getPricelist(iD);
    }


    //POST-IT
}
