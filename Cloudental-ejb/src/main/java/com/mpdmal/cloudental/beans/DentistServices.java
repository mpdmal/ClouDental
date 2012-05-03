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

    public PricelistItem getPricelistItem (int id) {
    	return emgr.findOrFail(PricelistItem.class, id);
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

    public void close() {
    	emgr.clostEM();
    }

}
