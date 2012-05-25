package com.mpdmal.cloudental.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.tests.services.ActivityTest;
import com.mpdmal.cloudental.tests.services.DentistServicesTest;
import com.mpdmal.cloudental.tests.services.DentistTest;
import com.mpdmal.cloudental.tests.services.DiscountTest;
import com.mpdmal.cloudental.tests.services.PatientTest;
import com.mpdmal.cloudental.tests.services.PricelistTest;
import com.mpdmal.cloudental.tests.services.VisitTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	DentistServicesTest.class,
	DentistTest.class,
	DiscountTest.class,
	PricelistTest.class,
	PatientTest.class,
	ActivityTest.class,
	VisitTest.class
})

public class TestSuite {
 
}