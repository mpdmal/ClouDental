package com.mpdmal.cloudental.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.entities.Medicine;
import com.mpdmal.cloudental.entities.UserPreferences;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.CloudentUtils.EventTitleFormatType;
import com.mpdmal.cloudental.util.exception.InvalidTitleFormatTypeException;
import com.mpdmal.cloudental.util.exception.base.CloudentException;
import com.mpdmal.cloudental.web.beans.base.BaseBean;
import com.mpdmal.cloudental.web.util.CloudentWebUtils;

@Named("pharmacy")
@RequestScoped
public class PharmacyBean extends BaseBean implements Serializable {  
	private static final long serialVersionUID = 1L;

	private Collection<Medicine> _meds = null;
	
	@Inject
	private DentistServices _dsvc;

	@Override
	public void init() {
		super.init();
		_meds = _dsvc.getMedicineList();
	}
	
	public Vector<Medicine> getMeds() {
		return (Vector<Medicine>)_meds;
	}
}

