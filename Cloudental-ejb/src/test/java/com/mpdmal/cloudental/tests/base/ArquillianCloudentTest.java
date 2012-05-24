package com.mpdmal.cloudental.tests.base;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.runner.RunWith;

import com.mpdmal.cloudental.beans.DentistBean;
import com.mpdmal.cloudental.beans.DentistServices;
import com.mpdmal.cloudental.beans.LoginBean;
import com.mpdmal.cloudental.beans.PatientServices;
import com.mpdmal.cloudental.tests.util.TestUtils;

@RunWith(Arquillian.class)
public class ArquillianCloudentTest {
	@Inject
	protected DentistBean dbean;
	@Inject
	protected DentistServices dsvcbean;
	@Inject
	protected PatientServices psvcbean;
	@Inject
	protected LoginBean loginbean;
	
	@Deployment
    public static Archive<?> createDeployment() {
        return TestUtils.createBasicTestDeployment();  	
    }
}
