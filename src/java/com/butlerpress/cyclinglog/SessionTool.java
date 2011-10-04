package com.butlerpress.cyclinglog;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;

public class SessionTool {

  private static SessionFactory sessionFactory;

  private static void initializeSessionFactory() {
    try {
      Configuration configuration = new Configuration()
        .addClass(User.class)
        .addClass(Week.class)
        .addClass(Equipment.class)
        .addClass(Workout.class);
      sessionFactory = configuration.buildSessionFactory();
    } catch (HibernateException he) {
      throw new RuntimeException("Could not initialize SessionFactory for Hibernate.", he);
    }
  }

  public static Session getSession() throws HibernateException {
    if (sessionFactory == null) {
      initializeSessionFactory();
    }
    return sessionFactory.openSession();
  }


}
