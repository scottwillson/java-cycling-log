package com.butlerpress.cyclinglog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

public class WorkoutFactory extends HibernateDaoSupport {

  private static final Logger logger = Logger.getLogger(WorkoutFactory.class);

  private WeekFactory weekFactory;

  public WorkoutFactory() { }

  public WeekFactory getWeekFactory() {
    return weekFactory;
  }

  public void setWeekFactory(WeekFactory weekFactory) {
    this.weekFactory = weekFactory;
  }
  
  public List findLatest() throws HibernateException {
    Session session = null;
    List workouts = new ArrayList();
    try {
      session = getSession();
      Query query = session.createQuery(
          "select max(workout.date), workout.user " + 
          "from com.butlerpress.cyclinglog.Workout workout " +
          "where workout.date <= :now and workout.date >= :lastWeek " +
          "and workout.publicNotes <> '' and workout.publicNotes is not null " +
          "group by workout.user");
      query.setDate("now", new Date());
      Calendar calendar = new GregorianCalendar();
      calendar.add(Calendar.DATE, -7);
      query.setCalendarDate("lastWeek", calendar);
      for (Iterator iter = query.iterate(); iter.hasNext();) {
        Object[] results = (Object[]) iter.next();
        Date date = (Date) results[0];
        User user = (User) results[1];
        query = session.createQuery("from com.butlerpress.cyclinglog.Workout workout " +
	          "where workout.publicNotes <> '' and workout.publicNotes is not null " +
            "and workout.user = :user and workout.date = :date");
        query.setEntity("user", user);
        query.setDate("date", date);
        List userWorkouts = query.list();
        workouts.addAll(userWorkouts);
      }
      Collections.sort(workouts, new DateNameComparator());
      return workouts;
    } finally {
      if (session != null) session.close();
    }
  }

  public Workout newWorkout(Map map) {
    Workout workout = null;
  	workout = new Workout(map);
    Week week = getWeekFactory().get(workout);
    week.addWorkout(workout);
    getHibernateTemplate().saveOrUpdate(week);
    getHibernateTemplate().save(workout);
    return workout;
  }

  public void fillInDays(User user) {
    List results = getHibernateTemplate().find(
        "select min(week.startDate) from com.butlerpress.cyclinglog.Week week WHERE week.user.username = ?",
        user.getUsername(),
        Hibernate.STRING
    );
    Date firstDay = (Date)results.get(0);
    Calendar day = new GregorianCalendar();
    day.setTime(firstDay);
    results = getHibernateTemplate().find(
        "select max(week.endDate) from com.butlerpress.cyclinglog.Week week WHERE week.user.username = ?",
        user.getUsername(),
        Hibernate.STRING
    );
    Date lastDay = (Date)results.get(0);
    logger.debug("Last day: " + lastDay);
    results = getHibernateTemplate().find(
        "from com.butlerpress.cyclinglog.Equipment equipment WHERE equipment.user.username = ? and equipment.name=''",
        user.getUsername(),
        Hibernate.STRING
    );
    Equipment equipment = (Equipment) results.get(0);
    while (!(day.getTime().after(lastDay))) {
      results = getHibernateTemplate().find(
          "from workout in class com.butlerpress.cyclinglog.Workout WHERE date = ? AND workout.user.username = ?",
          new Object[] {day.getTime(), user.getUsername()},
          new Type[] {Hibernate.DATE, Hibernate.STRING}
      );
      if (results.size() == 0) {
        Map map = new HashMap();
        map.put("workout_user", user);
        map.put("workout_date_object", day);
        map.put("workout_equipment", equipment);
        Workout newWorkout = newWorkout(map);
        logger.debug("+ " + newWorkout.getDate());
      } else {
        Workout workout = (Workout) results.get(0);
        logger.debug("  " + workout.getDate());
      }
      day.add(Calendar.DATE, 1);
    }
  }
  
  // @TODO Associate with correct week and save
  public void save(Workout workout) {
  	logger.debug("save " + workout);
  	getHibernateTemplate().save(workout);
  }

  public Workout find(Long id)  {
    return (Workout) getHibernateTemplate().get(Workout.class, id);
  }

  public List findAll(User user) {
    return getHibernateTemplate().find("from Workout workout where workout.user = ? order by date", user);
  }
  
  public void delete(Workout workout) {
    getHibernateTemplate().delete(workout);
  }

  static final class DateNameComparator implements Comparator {

    public DateNameComparator() {
      super();
    }

    public int compare(Object o1, Object o2) {
      Workout w1 = (Workout) o1;
      Workout w2 = (Workout) o2;
      int difference = 0;
      difference = w2.getDate().compareTo(w1.getDate()) * 10000;
      difference = difference + w1.getUser().getLastName().compareTo(w2.getUser().getLastName()) * 100;
      difference = difference + w1.getUser().getFirstName().compareTo(w2.getUser().getFirstName());
      return difference;
    }
  }
  
}