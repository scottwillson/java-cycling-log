package com.butlerpress.cyclinglog;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class WorkoutTestCase extends TestCase {

  public void testGetDate() {
    User cyclist = new User();
    Workout workout = new Workout(cyclist);
    Calendar calendar = new GregorianCalendar();
    calendar.set(2006, 11, 16);
    workout.setDate(calendar.getTime());
    
    String expected = "Saturday, Dec 16";
    assertEquals("Formated date", expected, workout.getDate("EEEE, MMM d"));
  }
  
  public void testGetSummary() {
    User cyclist = new User();
    Workout workout = new Workout(cyclist);
    String expected = "";
    assertEquals("Blank summary", expected, workout.getSummary());

    workout = new Workout(cyclist);
    workout.setActivity(Activity.ROAD.getName());
    expected = "Road. ";
    assertEquals("Summary", expected, workout.getSummary());

    workout = new Workout(cyclist);
    workout.setDuration(65);
    expected = "1:05. ";
    assertEquals("Summary", expected, workout.getSummary());

    workout = new Workout(cyclist);
    workout.setDistance(80);
    expected = "80 miles. ";
    assertEquals("Summary", expected, workout.getSummary());

    workout = new Workout(cyclist);
    workout.setActivity(Activity.RUN.getName());
    workout.setDuration(65);
    expected = "Run, 1:05. ";
    assertEquals("Summary", expected, workout.getSummary());
    
    workout = new Workout(cyclist);
    workout.setActivity(Activity.RUN.getName());
    workout.setDistance(12);
    expected = "Run, 12 miles. ";
    assertEquals("Summary", expected, workout.getSummary());
    
    workout = new Workout(cyclist);
    workout.setActivity(Activity.RUN.getName());
    workout.setDuration(214);
    workout.setDistance(112);
    expected = "Run, 3:34, 112 miles. ";
    assertEquals("Summary", expected, workout.getSummary());
    
    workout = new Workout(cyclist);
    workout.setDistance(79.9f);
    expected = "80 miles. ";
    assertEquals("Summary", expected, workout.getSummary());

    workout = new Workout(cyclist);
    workout.setDistance(79.4f);
    expected = "79 miles. ";
    assertEquals("Summary", expected, workout.getSummary());

    workout = new Workout(cyclist);
    workout.setDistance(.1f);
    expected = "0 miles. ";
    assertEquals("Summary", expected, workout.getSummary());
  }
}
