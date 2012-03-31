package com.mpdmal.cloudental.web.demoaza;


import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import com.mpdmal.cloudental.entities.Patienttooth;
import com.mpdmal.cloudental.entities.PatienttoothPK;
import com.mpdmal.cloudental.entities.Tooth;

public class GalleriaBean {

    private List<Patienttooth> teeth;

    @PostConstruct
    public void init() {
    	teeth = new ArrayList<Patienttooth>();
    	for (int i = 0; i < 10; i++) {
        	Patienttooth th = new Patienttooth();
        	PatienttoothPK id = new PatienttoothPK();
        	id.setToothid(i);
        	th.setId(id);
        	th.setComments("a tooth description");
        	Tooth t = new Tooth();
        	//t.setName("Tooth "+i);
        	th.setTooth(t);
        	teeth.add(th);
		}
    }

    public List<Patienttooth> getTeeth() {
        return teeth;
    }
}
                    