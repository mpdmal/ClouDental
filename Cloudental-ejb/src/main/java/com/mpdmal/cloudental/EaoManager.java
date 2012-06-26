package com.mpdmal.cloudental;

import java.io.Serializable;

import javax.annotation.PreDestroy;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;

import com.mpdmal.cloudental.entities.base.DBEntity;
import com.mpdmal.cloudental.util.CloudentUtils;
import com.mpdmal.cloudental.util.exception.ValidationException;

@Stateless
@LocalBean
@Named
public class EaoManager implements Serializable {
	private static final long serialVersionUID = 1L;
	@PersistenceContext
	protected EntityManager _em;
	
	//CONSTRUCTORS
    public EaoManager() {}
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
    	_em.merge(entity);
    }

    public <T extends DBEntity> void persist(T entity) throws ValidationException {
        try {
			_em.persist(entity);
		} catch (ConstraintViolationException e) {
			throw CloudentUtils.createValidationException(e);
		}
    }
    
    public void delete(DBEntity dbe) {
    	_em.remove(dbe);
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
