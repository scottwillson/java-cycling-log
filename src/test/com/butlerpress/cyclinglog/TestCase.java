package com.butlerpress.cyclinglog;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.sql.DataSource;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

public abstract class TestCase extends junit.framework.TestCase {

  private static BeanFactory beanFactory;
  private static DataSource dataSource;

  protected void setUp() throws Exception {
    super.setUp();
    truncateTables(getDataSource());
  }

  /**
   * Lazily create test BeanFactory.
   * Side effect: create applicationContext
   */
  public BeanFactory getBeanFactory() throws Exception {
    if (beanFactory == null) {
      String[] configLocations = new String[] {
        "dataSource.xml", "applicationContext.xml", "frontcontroller-servlet.xml"
      };
      XmlWebApplicationContext appContext = new XmlWebApplicationContext();
      appContext.setConfigLocations(configLocations);
      MockServletContext mockServletContext = new MockServletContext("");
      appContext.setServletContext(mockServletContext);
      appContext.refresh();
      beanFactory = appContext;
    }
    return beanFactory;
  }

  /**
   * Lazily create test DataSource. Verify DataSource points at test data.
   * Side effect: create BeanFactory, truncate
   * FIXME: Refactor to use intention-revealing names
   */
  private DataSource getDataSource() throws Exception {
    if (dataSource == null) {
      dataSource = (DataSource) getBeanFactory().getBean("dataSource");
      Connection conn = null;
      try {
        conn = dataSource.getConnection();
        if (conn.getMetaData().getURL().indexOf("cycling_log_test") == -1) {
          dataSource = null;
          throw new RuntimeException("Halting test. May be running against production data. DataSource URL does not contain 'cycling_log_test'");
        }
      } catch (SQLException e) {
        throw new CyclingLogException(e);
      } finally {
        if (conn != null) {
          conn.close();
        }
      }
    }
    return dataSource;
  }
  
  public void truncateTables(DataSource dataSource) throws Exception {
    Connection conn = null;
    try {
      conn = dataSource.getConnection();
      if (conn.getMetaData().getURL().indexOf("cycling_log_test") == -1) {
        dataSource = null;
        throw new RuntimeException("Halting test. May be running against production data. DataSource URL does not contain 'cycling_log_test'");
      }
    } catch (SQLException e) {
      throw new CyclingLogException(e);
    } finally {
      if (conn != null) {
        conn.close();
      }
    }
    JdbcTemplate template = new JdbcTemplate(dataSource);
    template.update("truncate equipment");
    template.update("truncate users");
    template.update("truncate workouts");
    template.update("truncate weeks");
  }

	/**
	 * Assert that two dates have the same year, month, and day
	 */
	public void assertEqualsToDay(String message, Date expected, Date actual) {
		DateAssert.assertEqualsToDay(message, expected, actual);
	}

  /**
   * Create new User with Workout, Week and Equipment. Save
   */
  protected Workout createCyclistWorkoutWeek(String cyclistName) throws Exception {
    UserFactory userFactory = (UserFactory) getBeanFactory().getBean("userFactory");
    // Create cyclist
    User user = new User(cyclistName, "Test " + cyclistName);
    user.setPassword("lacourseentete");
    userFactory.save(user);
    
    Calendar weekStart = new GregorianCalendar();
    weekStart.set(2005, 1, 14);
    Week week = new Week(user, weekStart);
    
    Workout workout = new Workout(user);
    workout.setWeek(week);
    Calendar workoutDate = new GregorianCalendar();
    workoutDate.setTime(weekStart.getTime());
    workoutDate.add(Calendar.DATE, 1);
    workout.setDate(workoutDate.getTime());
    
    EquipmentFactory equipmentFactory = (EquipmentFactory) getBeanFactory().getBean("equipmentFactory");
    Equipment equipment = new Equipment("", user);
    equipmentFactory.save(equipment);
    workout.setEquipment(equipment);
    
    WorkoutFactory workoutFactory = (WorkoutFactory) getBeanFactory().getBean("workoutFactory");
    WeekFactory weekFactory = (WeekFactory) getBeanFactory().getBean("weekFactory");
    weekFactory.save(week);
    workoutFactory.save(workout);
    return workout;
  }
}
