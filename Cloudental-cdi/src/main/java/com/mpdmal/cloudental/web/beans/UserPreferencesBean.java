package com.mpdmal.cloudental.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.entities.UserPreferences;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.CloudentUtils.EventTitleFormatType;
import com.mpdmal.cloudental.util.exception.base.CloudentException;
import com.mpdmal.cloudental.web.beans.base.BaseBean;

@Named("userPrefs")
@RequestScoped
public class UserPreferencesBean extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private UserPreferences _prefs;

	@Inject
	private DentistServices _dsvc;
	@Inject
	OfficeReceptionBean _sess;

	@Override
	public void init() {
		super.init();
		try {
			_prefs = _dsvc.getUserPrefs(_sess.getUserID());
		} catch (CloudentException e) {
			e.printStackTrace();
		} 
	}
	
	public UserPreferences getPrefs() {	return _prefs;	}
	public void setPrefs(UserPreferences _prefs) {	this._prefs = _prefs;	}
	
	public Map<String, String> getThemes() {
		TreeMap<String, String> ans = new TreeMap<String, String>();  
        ans.put("Aristo", "aristo"); 
        ans.put("Cupertino", "cupertino");  
        ans.put("Redmond", "redmond");
        return ans;
	}
	
	public ArrayList<EventTitleFormatType> getEventtitleformatTypes() {
		ArrayList<EventTitleFormatType> ans= new ArrayList<EventTitleFormatType>();
		ans.add(CloudentUtils.EventTitleFormatType.FULL);
		ans.add(CloudentUtils.EventTitleFormatType.SHORT);
		ans.add(CloudentUtils.EventTitleFormatType.SURNAME);
		ans.add(CloudentUtils.EventTitleFormatType.NAME);
		return ans; 
	}
	
	public void save() {
		_dsvc.savePrefs(_prefs);
	}
}
