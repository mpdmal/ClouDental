package com.mpdmal.cloudental.tdd.base;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;

import com.mpdmal.cloudental.dao.ActivityDAO;
import com.mpdmal.cloudental.dao.AddressDAO;
import com.mpdmal.cloudental.dao.ContactInfoDAO;
import com.mpdmal.cloudental.dao.DentistDAO;
import com.mpdmal.cloudental.dao.DiscountDAO;
import com.mpdmal.cloudental.dao.MedicalhistoryentryDAO;
import com.mpdmal.cloudental.dao.PatientDAO;
import com.mpdmal.cloudental.dao.PostitDAO;
import com.mpdmal.cloudental.dao.PricelistDAO;
import com.mpdmal.cloudental.dao.VisitDAO;
import com.mpdmal.cloudental.tdd.util.Util;

public class CDentAbstractDaoTest {
	private static EntityManager _em = Util.getTDDEntityManager();
    
    //testing daos ...
    protected DentistDAO _dentistdao;
    protected PatientDAO _patientdao;
    protected PostitDAO _postitdao;
    protected ContactInfoDAO _cinfodao;
    protected AddressDAO _adrdao;
    protected VisitDAO _vdao;
    protected ActivityDAO _acdao;
    protected PricelistDAO _pcdao;
    protected DiscountDAO _discountdao;
    
    
    
    protected MedicalhistoryentryDAO _medhentrydao;
    public CDentAbstractDaoTest() {
		try {
			setUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    @Before
    public void setUp() throws Exception {
        _dentistdao = new DentistDAO(_em);
        _patientdao = new PatientDAO(_em);
        _postitdao = new PostitDAO(_em);
        _medhentrydao = new MedicalhistoryentryDAO(_em);
        _cinfodao = new ContactInfoDAO(_em);
        _adrdao = new AddressDAO (_em);
        _vdao = new VisitDAO (_em);
        _acdao = new ActivityDAO (_em);
        _discountdao = new DiscountDAO(_em);
        _pcdao = new PricelistDAO(_em);
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
