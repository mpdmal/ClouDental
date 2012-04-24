package com.mpdmal.cloudental.tdd.base;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;

import com.mpdmal.cloudental.beans.DentistBean;
import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.beans.LoginBean;
import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.dao.ActivityDAO;
import com.mpdmal.cloudental.dao.DentistDAO;
import com.mpdmal.cloudental.dao.DiscountDAO;
import com.mpdmal.cloudental.dao.MedicalhistoryentryDAO;
import com.mpdmal.cloudental.dao.PatientDAO;
import com.mpdmal.cloudental.dao.PostitDAO;
import com.mpdmal.cloudental.dao.PricelistDAO;
import com.mpdmal.cloudental.dao.VisitDAO;
import com.mpdmal.cloudental.entities.Discount;
import com.mpdmal.cloudental.tdd.util.Util;

public class CDentAbstractBeanTest {
	private static EntityManager _em = Util.getTDDEntityManager();
    
    //testing services ...
    protected DentistBean _dbean;
    protected DentistServices _dsvcbean;
    protected PatientServices _psvcbean;
    protected LoginBean _lbean;
    
    public CDentAbstractBeanTest() {
		try {
			setUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    @Before
    public void setUp() throws Exception {
    	//simulate injetion
    	DentistDAO dao = new DentistDAO(_em);
    	PatientDAO pdao = new PatientDAO(_em);
    	MedicalhistoryentryDAO mdao = new MedicalhistoryentryDAO(_em);
    	ActivityDAO acvdao = new ActivityDAO(_em); 
    	VisitDAO vdao = new VisitDAO(_em);
    	PostitDAO ptdao = new PostitDAO(_em);
    	DiscountDAO dcdao = new DiscountDAO(_em);
    	PricelistDAO pcdao = new PricelistDAO(_em);
    	
        _dbean = new DentistBean();
        _dbean.setDentistDao(dao);
        _dsvcbean = new DentistServices();
        _dsvcbean.setDentistDao(dao);
        _dsvcbean.setPatientDao(pdao);
        _dsvcbean.setPostitDao(ptdao);
        _dsvcbean.setDiscountDao(dcdao);
        _dsvcbean.setPricelistDao(pcdao);
        
        _lbean = new LoginBean();
        _lbean.setDentistDao(dao);
        _psvcbean = new PatientServices();
        _psvcbean.setPatientDao(pdao);
        _psvcbean.setMedhistentryDao(mdao);
        _psvcbean.setDentistDao(dao);
        _psvcbean.setActivityDao(acvdao);
        _psvcbean.setVisitDao(vdao);
    }
    
	@Before
	public void initTestEnv()  {
		;
	}
	
	@After
	public void closeTestEnv()  {
		;
	}

}
