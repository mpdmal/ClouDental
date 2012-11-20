package com.mpdmal.cloudental.web.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.entities.Medicine;
import com.mpdmal.cloudental.entities.Patient;
import com.mpdmal.cloudental.entities.Prescription;
import com.mpdmal.cloudental.entities.Prescriptionrow;
import com.mpdmal.cloudental.entities.UserPreferences;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.CloudentUtils.MedIntakeRoute;
import com.mpdmal.cloudental.util.CloudentUtils.PrescrRowTimeunit;
import com.mpdmal.cloudental.util.exception.InvalidMedIntakeRouteException;
import com.mpdmal.cloudental.web.beans.base.BaseBean;

@Named("pharmacy")
@ViewScoped
public class PharmacyBean extends BaseBean implements Serializable {  
	private static final long serialVersionUID = 1L;
	{
		setBaseName("Pharmacy bean");
	}
	//MODEL
	private Vector<Prescriptionrow> _prescriptionModel = null;
	private ArrayList<MedIntakeRoute> _routedescrs;
	private ArrayList<PrescrRowTimeunit> _freqAndDurationunits;
	private Patient _autocompletepatient;
	@Inject
	private DentistServices _dsvc;
	@Inject
	private OfficeReceptionBean _sess;

	@PostConstruct
	public void init() {
		super.init();
		_prescriptionModel = new Vector<Prescriptionrow>(); 

		Vector<Medicine> meds = ((Vector<Medicine>) _dsvc.getMedicineList());
		for (Medicine medicine : meds) {
			Prescriptionrow row = new Prescriptionrow();
			row.setFrequency(0);
			row.setFrequnit(CloudentUtils.PrescrRowTimeunit.HOURS.getValue());
			row.setDuration(0);
			row.setDurunit(CloudentUtils.PrescrRowTimeunit.DAYS.getValue());
			try {
				row.setRoute(CloudentUtils.MedIntakeRoute.ORAL.getValue());
			} catch (InvalidMedIntakeRouteException e) {
				e.printStackTrace();
			}
			
			row.setMedicine(medicine);
			_prescriptionModel.add(row);
		}
		
		_routedescrs = new ArrayList<MedIntakeRoute>();
		for (MedIntakeRoute route :	CloudentUtils.MedIntakeRoute.values()) {
			_routedescrs.add(route);
		}
		
		_freqAndDurationunits = new ArrayList<PrescrRowTimeunit>();
		for (PrescrRowTimeunit unit : CloudentUtils.PrescrRowTimeunit.values()) {
			_freqAndDurationunits.add(unit);
		}
	}
	
	//GETTERS/SETTERS
	public ArrayList<MedIntakeRoute> getRoutes() { return _routedescrs; }
	public ArrayList<PrescrRowTimeunit> getFreqAndDurUnits() { return _freqAndDurationunits; }
	public  Vector<Prescriptionrow> getPresciprionSheet() { return _prescriptionModel; }
	public int getMedicineCount() { return _prescriptionModel.size(); }
	public Patient getAutocompletePatient() {	return _autocompletepatient;	}
	public void setAutocompletePatient(Patient _autocomplete) {	this._autocompletepatient = _autocomplete;	}
	
	/*PRIMEFACES INTERFACE
	 public void onEdit(RowEditEvent event) { 
		 Prescriptionrow row = ((Prescriptionrow) event.getObject());
		 CloudentWebUtils.showJSFWarnMessage("Row Edited", row.getUIFriendlyString());
    }*/
	 
	//INTERFACE
	 public StreamedContent fetchPrescription() throws Exception {
		 ArrayList<Prescriptionrow> ans = new ArrayList<Prescriptionrow>();
		 for (Prescriptionrow row : _prescriptionModel) {
			if (row.getFrequency() > 0)
				ans.add(row);
		 }
		 UserPreferences prefs = _dsvc.getUserPrefs(_sess.getUserID());
		 Prescription p = _dsvc.createPrescription(_sess.getUserID(), _autocompletepatient.getId(), ans);
		 String name = CloudentUtils.printPrescriptionReport(p.getId(), prefs.getPrescriptionHeader(), _autocompletepatient.getSurname());
		 StreamedContent content = null;
		 
		 try {
			 content = new DefaultStreamedContent(new FileInputStream(new File(name)), "application/pdf", "aza.pdf");
		 } catch (FileNotFoundException e) {
			 e.printStackTrace();
		 }
		 return content;
	 }
	 
	 //used by autocomplete
	 public List<Patient> completePatient(String query) {  
		 Vector<Patient>  patients  = (Vector<Patient>) _dsvc.getPatientlist(_sess.getUserID());
		 List<Patient> suggestions = new ArrayList<Patient>();  
	          
		 for(Patient p : patients) {  
			 query = query.toLowerCase(); // case insensitive 
			 if(p.getName().toLowerCase().startsWith(query) ||
					 p.getSurname().toLowerCase().startsWith(query) )  
				 suggestions.add(p);  
		 }  
		 return suggestions;  
	 }
	 
}

