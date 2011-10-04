package com.butlerpress.cyclinglog;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.LockMode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;

public class EquipmentFactory extends HibernateDaoSupport {

  protected static final Log log = LogFactory.getLog(EquipmentFactory.class);
  
  private PlatformTransactionManager transactionManager;
  
  public EquipmentFactory() { }

  public void delete(Equipment equipment) {
   	log.info("delete " + equipment);
   	if (!usedInWorkout(equipment)) {
   		getHibernateTemplate().delete(equipment);
   	} else {
   		throw new CyclingLogException("Cannot delete " + equipment.getName() + 
   			". It is referenced by an existing workout.");
   	}
  }

private boolean usedInWorkout(Equipment equipment) {
	Collection results = getHibernateTemplate().find(
	    "from Workout workout where workout.equipment = ?", 
	    equipment, Hibernate.entity(Equipment.class));
	return !results.isEmpty();
}

public List find(User user) {
    return getHibernateTemplate().find(
        "from com.butlerpress.cyclinglog.Equipment equipment where equipment.user = ? order by name", 
        user, 
        Hibernate.entity(User.class));
  }

  public Equipment find(Long equipmentId) {
    return (Equipment) getHibernateTemplate().get(Equipment.class, equipmentId);
  }

  public void save(Equipment equipment) {
    log.debug("save " + equipment);
    getHibernateTemplate().save(equipment); 
  }
  
  public void update(Collection equipment) {
		Iterator iterator = equipment.iterator();
		while (iterator.hasNext()) {
			Equipment e = (Equipment) iterator.next();
	   	log.debug("update " + e);
	   	try {
  			getHibernateTemplate().update(e);
  		} catch (Exception exc) {
  			throw new CyclingLogException("Could not update " + e, exc);
  		}
		}
  }
  
  public PlatformTransactionManager getTransactionManager() {
    return transactionManager;
  }
  
  public void setTransactionManager(PlatformTransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

public void refresh(Equipment equipment) {
	getHibernateTemplate().lock(equipment, LockMode.NONE);
}
}
