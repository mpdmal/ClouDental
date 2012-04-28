package com.mpdmal.cloudental.tdd;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
 
@RunWith(Suite.class)
@Suite.SuiteClasses({
	com.mpdmal.cloudental.tdd.tests.TestDB.class, //create db
	com.mpdmal.cloudental.tdd.tests.dao.DentistTests.class,
	com.mpdmal.cloudental.tdd.tests.dao.PricelistTests.class,
	com.mpdmal.cloudental.tdd.tests.dao.DiscountTests.class,
	com.mpdmal.cloudental.tdd.tests.dao.PostitTests.class,
	com.mpdmal.cloudental.tdd.tests.dao.PatientTests.class,
	com.mpdmal.cloudental.tdd.tests.dao.PatientToothTests.class,
//	com.mpdmal.cloudental.tdd.tests.dao.CascadeTests.class,
//	com.mpdmal.cloudental.tdd.tests.dao.MedHistoryEntryTests.class
//	com.mpdmal.cloudental.tdd.tests.dao.ContactinfoTests.class
//	com.mpdmal.cloudental.tdd.tests.dao.AddressTests.class,
	com.mpdmal.cloudental.tdd.tests.dao.ActivityTests.class,
	com.mpdmal.cloudental.tdd.tests.dao.VisitTests.class,
	com.mpdmal.cloudental.tdd.tests.bean.ServicesTests.class,
})

public class TestSuite {
 
}