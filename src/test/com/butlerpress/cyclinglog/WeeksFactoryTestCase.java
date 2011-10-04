package com.butlerpress.cyclinglog;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

public class WeeksFactoryTestCase extends TestCase {
	
	  public void testGetWeeks() throws Exception {
	    UserFactory userFactory = (UserFactory) getBeanFactory().getBean("userFactory");

	    // Cyclist
	    User cyclist = new User("cyclist", "Test Cyclist");
	    userFactory.save(cyclist);
	    assertNotNull(cyclist.getName() + " should be saved to database (id != null)", cyclist.getId());

      // Add week
	    Calendar cal = Calendar.getInstance();
	    cal.set(2001, 11, 17, 0, 0, 0);
	    Date startDate = cal.getTime();
	    Date rangeStartDate = startDate;
	    cal.add(Calendar.DATE, 6);
	    Date endDate = cal.getTime();
	    Week week = new Week();
	    week.setUser(cyclist);
	    week.setStartDate(startDate);
	    week.setEndDate(endDate);
      WeekFactory weekFactory = (WeekFactory) getBeanFactory().getBean("weekFactory");
	    weekFactory.save(week);
	    assertEquals("Week should exist", week, weekFactory.get(startDate, cyclist.getUsername()));
	    assertEquals("Week should exist", week, weekFactory.get(endDate, cyclist.getUsername()));
	    List expectedWeeks = new ArrayList();
	    expectedWeeks.add(week);
	    
	    // More weeks
	    cal.add(Calendar.DATE, 1);
	    startDate = cal.getTime();
	    cal.add(Calendar.DATE, 6);
	    endDate = cal.getTime();
	    week = new Week();
	    week.setUser(cyclist);
	    week.setStartDate(startDate);
	    week.setEndDate(endDate);
	    weekFactory.save(week);
	    expectedWeeks.add(week);
	    assertEquals("Week should exist", week, weekFactory.get(startDate, cyclist.getUsername()));
	    assertEquals("Week should exist", week, weekFactory.get(endDate, cyclist.getUsername()));

	    cal.add(Calendar.DATE, 1);
	    startDate = cal.getTime();
	    cal.add(Calendar.DATE, 6);
	    endDate = cal.getTime();
	    week = new Week();
	    week.setUser(cyclist);
	    week.setStartDate(startDate);
	    week.setEndDate(endDate);
	    weekFactory.save(week);
	    expectedWeeks.add(week);
	    assertEquals("Week should exist", week, weekFactory.get(startDate, cyclist.getUsername()));
	    assertEquals("Week should exist", week, weekFactory.get(endDate, cyclist.getUsername()));

	    cal.add(Calendar.DATE, 1);
	    startDate = cal.getTime();
	    cal.add(Calendar.DATE, 6);
	    endDate = cal.getTime();
	    week = new Week();
	    week.setUser(cyclist);
	    week.setStartDate(startDate);
	    week.setEndDate(endDate);
	    weekFactory.save(week);
	    expectedWeeks.add(week);
	    assertEquals("Week should exist", week, weekFactory.get(startDate, cyclist.getUsername()));
	    assertEquals("Week should exist", week, weekFactory.get(endDate, cyclist.getUsername()));

	    List weeks = weekFactory.get(rangeStartDate, expectedWeeks.size(), cyclist);
	    assertEquals("get(Date, Date, number of weeks, cyclist)", expectedWeeks, weeks);
	  }
	  
	  public void testAddWeeksUsername() throws Exception {
	    UserFactory userFactory = (UserFactory) getBeanFactory().getBean("userFactory");

	    // Cyclist
	    User cyclist = new User("cyclist", "Test Cyclist");
	    userFactory.save(cyclist);
	    assertNotNull(cyclist.getName() + " should be saved to database (id != null)", cyclist.getId());

		// Add week
	    Calendar cal = Calendar.getInstance();
	    cal.set(2001, 11, 17, 0, 0, 0);
	    Date startDate = cal.getTime();
	    Date rangeStartDate = startDate;
	    cal.add(Calendar.DATE, 6);
	    Date endDate = cal.getTime();
	    Week week = new Week();
	    week.setUser(cyclist);
	    week.setStartDate(startDate);
	    week.setEndDate(endDate);
		WeekFactory weekFactory = (WeekFactory) getBeanFactory().getBean("weekFactory");
	    weekFactory.save(week);
	    assertEquals("Week should exist", week, weekFactory.get(startDate, cyclist.getUsername()));
	    assertEquals("Week should exist", week, weekFactory.get(endDate, cyclist.getUsername()));
	    
	    // Another week
	    cal.add(Calendar.DATE, 1);
	    startDate = cal.getTime();
	    cal.add(Calendar.DATE, 6);
	    endDate = cal.getTime();
	    week = new Week();
	    week.setUser(cyclist);
	    week.setStartDate(startDate);
	    week.setEndDate(endDate);
	    weekFactory.save(week);
	    assertEquals("Week should exist", week, weekFactory.get(startDate, cyclist.getUsername()));
	    assertEquals("Week should exist", week, weekFactory.get(endDate, cyclist.getUsername()));

	    // Username
	    weekFactory.add(4, cyclist.getUsername());
	    List weeks = weekFactory.get(rangeStartDate, 10, cyclist);
	    assertEquals("get(Date, Date, number of weeks, cyclist)", 6, weeks.size());

	    // User object
		weekFactory.add(3, cyclist);
	    weeks = weekFactory.get(rangeStartDate, 10, cyclist);
	    assertEquals("get(Date, Date, number of weeks, cyclist)", 9, weeks.size());
	  }

	  public void testGetByID() throws Exception {
	    Workout workout = createCyclistWorkoutWeek("fausto");
	    
	    List weeks = new ArrayList();
	    weeks.add(workout.getWeek());
	    
	    Week week1 = new Week();
	    User cyclist = workout.getUser();
	    week1.setUser(cyclist);
	    Calendar calendar = new GregorianCalendar();
	    calendar.set(11, 20, 1999);
	    week1.setStartDate(calendar.getTime());
	    calendar.add(Calendar.DATE, 6);
	    week1.setEndDate(calendar.getTime());
		WeekFactory weekFactory = (WeekFactory) getBeanFactory().getBean("weekFactory");
		weekFactory.save(week1);
	    weeks.add(week1);
	    
	    Week week2 = new Week();
	    week2.setUser(cyclist);
	    calendar = new GregorianCalendar();
	    calendar.set(1, 14, 2018);
	    week2.setStartDate(calendar.getTime());
	    calendar.add(Calendar.DATE, 6);
	    week2.setEndDate(calendar.getTime());
		weekFactory.save(week2);
	    weeks.add(week2);
	    
	    for (Iterator iter = weeks.iterator(); iter.hasNext();) {
	      Week expected = (Week) iter.next();
	      Week actual = weekFactory.get(new Long(expected.getId()));
	      assertEquals("Get week by ID", expected, actual);
	    }
	  }
	  
  public void testGetWeekBounds() throws Exception {
    Workout workout = createCyclistWorkoutWeek("chris");
    WeekFactory weekFactory = (WeekFactory) getBeanFactory().getBean("weekFactory");
    Week week = workout.getWeek();
    User cyclist = workout.getUser();
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(week.getStartDate());
    
    calendar.add(Calendar.DATE, -1);
    Week weekFromDB = weekFactory.get(calendar.getTime(), cyclist);
    assertFalse(calendar.getTime() + " should not return " + week, week.equals(weekFromDB));
    
    for (int i = 1; i < 8; i++) {
      calendar.add(Calendar.DATE, 1);
      weekFromDB = weekFactory.get(calendar.getTime(), cyclist);
      assertEquals(calendar.getTime() + " should return " + week, week, weekFromDB);
    }

    calendar.add(Calendar.DATE, 1);
    weekFromDB = weekFactory.get(calendar.getTime(), cyclist);
    assertFalse(calendar.getTime() + " should not return " + week, week.equals(weekFromDB));
  }
}
