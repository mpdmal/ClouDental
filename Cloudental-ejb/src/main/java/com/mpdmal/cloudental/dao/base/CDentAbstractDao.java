package com.mpdmal.cloudental.dao.base;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.mpdmal.cloudental.entities.base.DBEntity;
import com.mpdmal.cloudental.util.CloudentUtils;

public abstract class CDentAbstractDao {
	@PersistenceContext
	protected EntityManager _em;
	protected boolean _testmode = false;
	
	//CONSTRUCTORS
    public CDentAbstractDao() {}
    public CDentAbstractDao(EntityManager em) {
    	_em = em;
    	_testmode = true;
    }

    //INTERFACE
    public void delete(DBEntity dbe) {
    	if (_testmode)
    		_em.getTransaction().begin();
    	try {
			_em.remove(dbe);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	if (_testmode)
    		_em.getTransaction().commit();
    }

    public void updateCreate(DBEntity entity, boolean update) {
    	if (_testmode)
    		_em.getTransaction().begin();
    	try {
			if (update) _em.merge(entity);
			else _em.persist(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	if (_testmode)
    		_em.getTransaction().commit();
    }
    
    protected int executeSingleIntQuery(Query q) {
        try {
        	return (Integer)q.getSingleResult();
		} catch (NoResultException e) {
			CloudentUtils.logQueryString(q);
		} catch (Exception e) {
			e.printStackTrace();
		} 
        return -1;
    }
    
    protected long executeSingleLongQuery(Query q) {
        try {
        	return (Long)q.getSingleResult();
		} catch (NoResultException e) {
			CloudentUtils.logQueryString(q);
		} catch (Exception e) {
			e.printStackTrace();
		} 
        return -1;
    }
    
    protected Object executeSingleObjectQuery(Query q) {  
    	try {
             return q.getSingleResult();
 		} catch (NoResultException ignored) {
 			CloudentUtils.logQueryString(q);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}    	
    	return null;
    }

    protected int executeUpdateQuery(Query q) {  
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
