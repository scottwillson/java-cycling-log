package com.butlerpress.cyclinglog;

import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.LockMode;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;

public class UserFactory extends HibernateDaoSupport {

  private PlatformTransactionManager transactionManager;
  
  public UserFactory() { }

  public User find(String username) {
    List users = getHibernateTemplate().find(
      "from com.butlerpress.cyclinglog.User " +
      "where username = ?",
      username,
      Hibernate.STRING
    );
    User user = null;
    if (users.size() > 1) {
      throw new RuntimeException("Found " + users.size() + " users for username " + username);
    }
    if (users.size() == 1) {
      user = (User)users.get(0);
    }
    return user;
  }

  public User find(String username, String password) {
    List users = getHibernateTemplate().find(
      "from com.butlerpress.cyclinglog.User " +
      "where (username = ? and password = ?) or" +
      "      (name = ? and password = ?)",
      new Object[] {username, password, username, password});
    User user = null;
    if (users.size() > 1) {
      throw new RuntimeException("Found " + users.size() + " users for username " + username);
    }
    if (users.size() == 1) {
      user = (User)users.get(0);
    }
    return user;
  }

  public List findAll() {
    return getHibernateTemplate().find("from User order by lastname, firstName");
  }
  
  public void save(User user) {
    getHibernateTemplate().save(user);
  }

  public PlatformTransactionManager getTransactionManager() {
    return transactionManager;
  }
  
  public void setTransactionManager(PlatformTransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  public void refresh(User user) {
    getHibernateTemplate().lock(user, LockMode.NONE);
  }
}
