package com.mpdmal.cloudental;

import java.util.Iterator;

import javax.annotation.PreDestroy;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.mpdmal.cloudental.entities.base.DBEntity;
import com.mpdmal.cloudental.util.CloudentUtils;

@Stateless
@LocalBean
public class EaoManager {
	@PersistenceContext
	protected EntityManager _em;
	protected boolean _testmode = false;
	
	//CONSTRUCTORS
    public EaoManager() {}
    public EaoManager(EntityManager em) {
    	_em = em;
    	_testmode = true;
    }

    public EntityManager getEM() {
    	return _em;
    }
    
    @PreDestroy
    public void closeEM () {
    	_em.close();
    }
    //INTERFACE
    public <T extends DBEntity> T findOrFail(Class<T> clazz, Integer id)  {
        T e = _em.find(clazz, id);
        if (e == null) {
            return null;
        }
        return e;
    }
 
    public <T extends DBEntity> void update(T entity) {
    	if (_testmode)
    		_em.getTransaction().begin();

    	_em.merge(entity);
    	
    	if (_testmode)
    		_em.getTransaction().commit();
    }

    public <T extends DBEntity> void persist(T entity) {
    	if (_testmode)
    		_em.getTransaction().begin();

        try {
			_em.persist(entity);
		} catch (ConstraintViolationException e) {
			Iterator<?> it = e.getConstraintViolations().iterator();
			while (it.hasNext()) {
				System.out.println(it.next().toString());
			}
			
		}
        //TODO RETURN WITH FINAL DBENTITY
//        if (entity.getId() == null) {
//            _em.flush();
//        }
        
    	if (_testmode)
    		_em.getTransaction().commit();
    }
    
    public void delete(DBEntity dbe) {
    	if (_testmode)
    		_em.getTransaction().begin();

    	_em.remove(dbe);
    	if (_testmode)
    		_em.getTransaction().commit();
    }

    public int executeSingleIntQuery(Query q) {
        try {
        	return (Integer)q.getSingleResult();
		} catch (NoResultException e) {
			CloudentUtils.logQueryString(q);
		} catch (Exception e) {
			e.printStackTrace();
		} 
        return -1;
    }
    
    public long executeSingleLongQuery(Query q) {
        try {
        	return (Long)q.getSingleResult();
		} catch (NoResultException e) {
			CloudentUtils.logQueryString(q);
		} catch (Exception e) {
			e.printStackTrace();
		} 
        return -1;
    }
    
    public Object executeSingleObjectQuery(Query q) {
    	try {
             return q.getSingleResult();
 		} catch (NoResultException ignored) {
 			CloudentUtils.logQueryString(q);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}    	
    	return null;
    }

    public Object executeMultipleObjectQuery(Query q) {
    	try {
             return q.getResultList();
 		} catch (NoResultException ignored) {
 			CloudentUtils.logQueryString(q);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}    	
    	return null;
    }

    public int executeUpdateQuery(Query q) {  
   	 	try {
            return q.executeUpdate();
		} catch (NoResultException ignored) {
			CloudentUtils.logQueryString(q);
		} catch (Exception e) {
			e.printStackTrace();
		}    	
   	 	return -1;
    }
}
