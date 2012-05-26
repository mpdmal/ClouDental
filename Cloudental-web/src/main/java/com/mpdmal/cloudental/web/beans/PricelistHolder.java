package com.mpdmal.cloudental.web.beans;

import java.io.Serializable;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.mpdmal.cloudental.entities.PricelistItem;


@ManagedBean(name="pricelistHolder")
@ViewScoped
public class PricelistHolder implements Serializable{

	public PricelistHolder() {
		super();
		System.out.println("PricelistHolder costructor"+this.hashCode());
	}
	private static final long serialVersionUID = 1L;
	private Collection<PricelistItem> pricelist;

	public Collection<PricelistItem> getPricelist() {
		return pricelist;
	}

	public void setPricelist(Collection<PricelistItem> pricelist) {
		this.pricelist = pricelist;
	}

	@PostConstruct
	public void initialize() {
		FacesContext context = FacesContext.getCurrentInstance();
		PricelistItemManagementBean pricelistItemManagementBean = (PricelistItemManagementBean)context.getApplication() .evaluateExpressionGet(context, "#{pricelistItemManagementBean}", PricelistItemManagementBean.class);
		if(pricelistItemManagementBean!=null)
			pricelist = pricelistItemManagementBean .loadPricelist();
	}

}