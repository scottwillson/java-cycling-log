package com.butlerpress.cyclinglog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

public class WeekFactory extends HibernateDaoSupport {

  private static final Logger logger = Logger.getLogger(WeekFactory.class);
  private UserFactory userFactory;

  public WeekFactory() { }

  public Week getPrevious(Week week) {
    return getPrevious(week, 1);
  }

  public Week getNext(Week week) {
    return getNext(week, 1);
  }

  public Week getPrevious(Week startWeek, int weeks) {
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(startWeek.getStartDate());
    calendar.add(Calendar.WEEK_OF_YEAR, -weeks);
    return get(calendar.getTime(), startWeek.getUser());
  }

  public Week getNext(Week startWeek, int weeks) {
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(startWeek.getStartDate());
    calendar.add(Calendar.WEEK_OF_YEAR, weeks);
    return get(calendar.getTime(), startWeek.getUser());
  }

  public Week get(Map map) {
    int day = Convertor.getInt("week_day", map);
    int month = Convertor.getInt("week_month", map);
    int year = Convertor.getInt("week_year", map);
    Date startDate = Convertor.getDate(day, month, year);
    if (logger.isDebugEnabled()) {
      logger.debug("get(Map) d, m, y: " + day + ", " + month + ", " + year);
      logger.debug("Convertor.getDate(): " + startDate);
    }
    String username = (String) map.get("username");
    Week week = get(startDate, username);
    week.update(map);
    return week;
  }

  public Week get(Date date, User cyclist) {
    return get(date, cyclist.getUsername());
  }

  public Week get(Date date, String username) {
    Week week = null;
    List result = getHibernateTemplate().find(
      "from Week week " +
      "left join fetch week.workouts " + 
      "where startDate <= ? and endDate >= ? and week.user.username = ?",
      new Object[] { date, date, username });

    // Remove any duplicate weeks caused by join
    SortedSet weeks = new TreeSet(result);
    
    if (weeks.size() == 1) {
      week = (Week) weeks.first();
    } else if (weeks.size() == 0 ) {
      Date startDate = Week.getStart(date);
      User user = getUserFactory().find(username); 
      Calendar cal = new GregorianCalendar();
      cal.setTime(startDate);
      week = new Week(user, cal);
    } else {
      logger.warn("Search for week of " + date + " for " + username + " returned: ");
      Iterator iter = weeks.iterator();
      while (iter.hasNext()) {
        Week badWeek = (Week) iter.next();
        logger.warn(badWeek);
        logger.warn(badWeek.getWorkouts());
      }
      throw new CyclingLogException("Found " + weeks.size() + " weeks for date of " + date + ". Expected one or zero.");
    }
    
    if (logger.isDebugEnabled()) {
      logger.debug("WeekFactory.get(Map) returning " + week);
    }
    return week;
  }
  
  public Week add(int quantity, User user) {
	  logger.info("Adding " + quantity + " weeks for " + user);
    List max = getHibernateTemplate().find(
            "select max(week.startDate) from com.butlerpress.cyclinglog.Week week WHERE week.user = ?",
            user);
    Date maxDate = (Date) max.get(0);
    List weeks = getHibernateTemplate().find(
        "from com.butlerpress.cyclinglog.Week as maxWeek " +
        "where maxWeek.startDate = ? and maxWeek.user.username = ?",
        new Object[] { maxDate, user.getUsername() });
    Week lastWeek = (Week) weeks.get(0);
    for (int i = 1; i <= quantity; i++) {
      lastWeek = getNext(lastWeek, 1);
      getHibernateTemplate().saveOrUpdate(lastWeek);
    }
    return lastWeek;
  }

  public Week add(int quantity, String user) {
	  logger.debug("Adding " + quantity + " weeks for " + user);
    List max = getHibernateTemplate().find(
        "select max(week.startDate) from Week week WHERE week.user.username = ?",
        user);
    Date maxDate = (Date)max.get(0);
    logger.debug("Max startdate: " + maxDate);
    List weeks = getHibernateTemplate().find(
    "from com.butlerpress.cyclinglog.Week as maxWeek " +
    "where maxWeek.startDate = ? AND maxWeek.user.username = ?",
    new Object[] {maxDate, user});
    Week lastWeek = (Week)weeks.get(0);
    for (int i = 1; i <= quantity; i++) {
      lastWeek = getNext(lastWeek, 1);
      getHibernateTemplate().saveOrUpdate(lastWeek);
    }
    return lastWeek;
  }

  public Week get(Workout workout) {
    return get(workout.getDate(), workout.getUser().getUsername());
  }

  public Week get(Long id) {
    return (Week) getHibernateTemplate().get(Week.class, id);
  }

  public List get(Date startDate, int numberOfWeeks, User cyclist) {
    if (logger.isDebugEnabled()) {
      logger.debug("Getting weeks " + startDate + " " + numberOfWeeks + " " + cyclist);
    }
    startDate = Week.getStart(startDate);
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(startDate);
    calendar.add(Calendar.WEEK_OF_YEAR, numberOfWeeks);
    Date queryEndDate = calendar.getTime();
    List weeks = new ArrayList();
    weeks = getHibernateTemplate().find(
      "from Week week " +
      "where startDate >= ? and endDate <= ? and " +
      "  week.user = ? " +
      "order by startDate asc",
      new Object[] { startDate, queryEndDate, cyclist });
    if (logger.isDebugEnabled()) {
      logger.debug("From " + startDate + " " + queryEndDate);
      logger.debug(weeks);
    }
    return weeks;
  }

  public UserFactory getUserFactory() {
    return userFactory;
  }
  
  public void setUserFactory(UserFactory userFactory) {
    this.userFactory = userFactory;
  }

  public void save(Week week) {
    logger.debug("Saving " + week);
	  getHibernateTemplate().save(week);
  }

}