package com.mpdmal.cloudental.tdd.util;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class Util {
	 
    public static EntityManager getTDDEntityManager() {
        return Persistence.createEntityManagerFactory("CloudentalTDD").createEntityManager();
    }
}