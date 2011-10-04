package com.butlerpress.cyclinglog;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class WeekTestCase extends TestCase {

  public void testCreate() {
    User cyclist = new User();
    Calendar startDate = new GregorianCalendar();
    startDate.set(2005, 4, 2);
    Week week = new Week(cyclist, startDate);
    assertSame("Cyclist", cyclist, week.getUser());
    assertEquals("Start Date", startDate.getTime(), week.getStartDate());
    Calendar endDate = new GregorianCalendar();
    endDate.setTime(startDate.getTime());
    endDate.add(Calendar.DATE, 6);
    assertEquals("End Date", endDate.getTime(), week.getEndDate());
  }

  public void testGetStart() {
    Calendar cal = new GregorianCalendar();

    cal.set(2005, 4, 2);
    Date startDate = Week.getStart(cal.getTime());
    cal.set(2005, 4, 2);
    assertEqualsToDay("Start Date", cal.getTime(), startDate);

    cal.set(2005, 4, 3);
    startDate = Week.getStart(cal.getTime());
    cal.set(2005, 4, 2);
    assertEqualsToDay("Start Date", cal.getTime(), startDate);

    cal.set(2005, 4, 1);
    startDate = Week.getStart(cal.getTime());
    cal.set(2005, 3, 25);
    assertEqualsToDay("Start Date", cal.getTime(), startDate);

    cal.set(2005, 4, 9);
    startDate = Week.getStart(cal.getTime());
    cal.set(2005, 4, 9);
    assertEqualsToDay("Start Date", cal.getTime(), startDate);
  }
}
