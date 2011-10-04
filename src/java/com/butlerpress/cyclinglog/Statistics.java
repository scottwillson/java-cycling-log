package com.butlerpress.cyclinglog;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Statistics {

  static Calendar updateAfter = null;
  private static Map map = new HashMap();
  private static final Log log = LogFactory.getLog(Statistics.class);

  public static float getAverage(Long userId, Cycle cycle, Field field, String focus) {
    initializeIfNeeded();
    Key key = new Key(userId, cycle, field, Stat.AVERAGE, focus);
    Float average = (Float) map.get(key);
    if (average == null) {
      log.warn("Average was null");
      log.warn(map);
      return 0;
    }
    return average.floatValue();
  }

  public static float getStandardDeviation(Long userId, Cycle cycle, Field field, String focus) {
    initializeIfNeeded();
    Key key = new Key(userId, cycle, field, Stat.STANDARD_DEVIATION, focus);
    Float stdDev = (Float) map.get(key);
    if (stdDev == null) {
      log.warn("Standard deviation was null");
      log.warn(map);
      return 0;
    }
    return stdDev.floatValue();
  }

  static void initializeIfNeeded() {
    if (updateAfter != null) {
      Calendar now = new GregorianCalendar();
      if (now.before(updateAfter)) {
        return;
      }
    }
    Session session = null;
    try {
      session = SessionTool.getSession();
      initializeDuration(session);
      initializeWork(session);
      updateAfter = new GregorianCalendar();
      updateAfter.add(Calendar.DATE, 1);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } catch (HibernateException e) {
      throw new RuntimeException(e);
    } finally {
      if (session != null) {
        try {
          session.close();
        } catch (HibernateException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private static void initializeDuration(Session session) throws SQLException, HibernateException {
    Connection conn = session.connection();
    PreparedStatement stmt = conn.prepareStatement(
          "select userId, focus, stddev(duration), avg(duration), min(duration), max(duration) from weeks " +
          "where duration > 0 and startDate > ? group by focus, userID");
    Calendar yearAgo =  new GregorianCalendar();
    yearAgo.add(Calendar.YEAR, -1);
    stmt.setDate(1, new Date(yearAgo.getTimeInMillis()));
    ResultSet results = stmt.executeQuery();

    while(results.next()) {
      Long userId = new Long(results.getLong(1));
      String focus = results.getString(2);
      Key key = new Key(userId, Cycle.WEEK, Field.DURATION, Stat.AVERAGE, focus);
      map.put(key, new Float(results.getFloat(4)));
      key = new Key(userId, Cycle.WEEK, Field.DURATION, Stat.STANDARD_DEVIATION, focus);
      map.put(key, new Float(results.getFloat(3)));
      log.info(userId + " week " + focus + " duration avg: " + results.getFloat(4) + " +/- " + results.getFloat(3));
    }
  }

  private static void initializeWork(Session session) throws SQLException, HibernateException {
    Connection conn = session.connection();
    PreparedStatement stmt = conn.prepareStatement(
          "select userId, focus, stddev(work), avg(work), min(work), max(work) from weeks " +
          "where duration > 0 and startDate > ? group by focus, userID");
    Calendar yearAgo =  new GregorianCalendar();
    yearAgo.add(Calendar.YEAR, -1);
    stmt.setDate(1, new Date(yearAgo.getTimeInMillis()));
    ResultSet results = stmt.executeQuery();

    while(results.next()) {
      Long userId = new Long(results.getLong(1));
      String focus = results.getString(2);
      Key key = new Key(userId, Cycle.WEEK, Field.WORK, Stat.AVERAGE, focus);
      map.put(key, new Float(results.getFloat(4)));
      key = new Key(userId, Cycle.WEEK, Field.WORK, Stat.STANDARD_DEVIATION, focus);
      map.put(key, new Float(results.getFloat(3)));
      log.warn(userId + " week " + focus + " work avg: " + results.getFloat(4) + " +/- " + results.getFloat(3));
    }
  }

  static final class Key {
    private final Long userId;
    private final Cycle cycle;
    private final Field field;
    private final Stat stat;
    private final String identifier;

    Key(Long userId, Cycle cycle, Field field, Stat stat, String identifier) {
      if (userId == null) {
        throw new CyclingLogException("Cannot create static key with null userId");
      }
      this.userId = userId;
      this.cycle = cycle;
      this.field = field;
      this.stat = stat;
      this.identifier = identifier;
    }

    public int hashCode() {
      return (int) (userId.longValue() * 19 + cycle.hashCode() + field.hashCode() + stat.hashCode() + identifier.hashCode());
    }

    public boolean equals(Object o) {
        Key oKey = (Key) o;
        if (oKey.userId.equals(userId) && oKey.cycle == cycle && oKey.field == field && oKey.stat == stat && oKey.identifier.equals(identifier)) {
            return true;
        }
        return false;
    }
   
    public String toString() {
      return userId + " " + cycle + " " + field + " " + stat + " " + identifier;
    }
  }
}
