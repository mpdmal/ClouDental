package com.mpdmal.cloudental.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.mpdmal.cloudental.tests.services.ActivityTestSet;
import com.mpdmal.cloudental.tests.services.DentistServicesTestSet;
import com.mpdmal.cloudental.tests.services.DentistTestSet;
import com.mpdmal.cloudental.tests.services.DiscountTestSet;
import com.mpdmal.cloudental.tests.services.PatientTestSet;
import com.mpdmal.cloudental.tests.services.PricelistTestSet;
import com.mpdmal.cloudental.tests.services.VisitTestSet;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	DentistServicesTestSet.class,
	DentistTestSet.class,
	DiscountTestSet.class,
	PricelistTestSet.class,
	PatientTestSet.class,
	ActivityTestSet.class,
	VisitTestSet.class
})

public class TestSuite {
 
}