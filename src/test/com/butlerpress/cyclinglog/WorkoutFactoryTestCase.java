package com.butlerpress.cyclinglog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class WorkoutFactoryTestCase extends TestCase {

  public void testSave() throws Exception {
    UserFactory userFactory = (UserFactory) getBeanFactory().getBean("userFactory");

    // Cyclist
    User cyclist = new User("cyclist", "Test Cyclist");
    userFactory.save(cyclist);
    assertNotNull(cyclist.getName() + " should be saved to database (id != null)", cyclist.getId());

    // Workout
    Workout workout = new Workout(cyclist);
    String activity = Activity.MTB.getName();
    workout.setActivity(activity);
    Date date = new Date();
    workout.setDate(date);
    float distance = 22.3f;
    workout.setDistance(distance);
    int duration = 3300;
    workout.setDuration(duration);
    EquipmentFactory equipmentFactory = (EquipmentFactory) getBeanFactory().getBean("equipmentFactory");
    Equipment equipment = new Equipment("Anvil", cyclist);
    equipmentFactory.save(equipment);
    workout.setEquipment(equipment);
    String focus = Focus.DISTANCE.getName();
    workout.setFocus(focus);
    int intensity = 3;
    workout.setIntensity(intensity);
    int life = 2;
    workout.setLife(life);
    int morale = 1;
    workout.setMorale(morale);
    String notes = "These are some notes.";
    workout.setNotes(notes);
    String publicNotes = "Visible to all";
    workout.setPublicNotes(publicNotes);
    float speed = 10.2f;
    workout.setSpeed(speed);
    int weather = 4;
    workout.setWeather(weather);
    Week week = new Week();
    week.setEndDate(new Date());
    week.setStartDate(new Date());
    week.setFocus("");
    week.setIntensity("");
    week.setNotes("");
    week.setPublicNotes("");
    week.setUser(cyclist);
  workout.setWeek(week);
  float weight = 146.5f;
  workout.setWeight(weight);
    WeekFactory weekFactory = (WeekFactory) getBeanFactory().getBean("weekFactory");
    weekFactory.save(week);
    WorkoutFactory workoutFactory = (WorkoutFactory) getBeanFactory().getBean("workoutFactory");
    workoutFactory.save(workout);
    
    Workout workoutFromDB = workoutFactory.find(workout.getId());
  assertNotNull("Workout should be in database", workoutFromDB);
  assertEquals("Workout activity", activity, workoutFromDB.getActivity());
  assertEqualsToDay("Workout date", date, workoutFromDB.getDate());
  assertEquals("Workout distance", distance, workoutFromDB.getDistance(), 0.00001);
  assertEquals("Workout duration", duration, workoutFromDB.getDuration(), 0.00001);
  assertEquals("Workout equipment", equipment, workoutFromDB.getEquipment());
  assertEquals("Workout focus", focus, workoutFromDB.getFocus());
  assertEquals("Workout intensity", intensity, workoutFromDB.getIntensity());
  assertEquals("Workout life", life, workoutFromDB.getLife());
  assertEquals("Workout morale", morale, workoutFromDB.getMorale());
  assertEquals("Workout notes", notes, workoutFromDB.getNotes());
  assertEquals("Workout public notes", publicNotes, workoutFromDB.getPublicNotes());
  assertEquals("Workout speed", speed, workoutFromDB.getSpeed(), 0.00001);
  assertEquals("Workout weather", weather, workoutFromDB.getWeather());
  assertEquals("Workout weight", weight, workoutFromDB.getWeight(), 0.00001);
  assertEquals("Workout week", week, workoutFromDB.getWeek());
  }
  
  public void testDelete() throws Exception {
    Workout workout = createCyclistWorkoutWeek("chris");
    WorkoutFactory workoutFactory = (WorkoutFactory) getBeanFactory().getBean("workoutFactory");
    assertNotNull("Workout should exist", workoutFactory.find(workout.getId()));
    workoutFactory.delete(workout);
    assertNull("Workout should not exist", workoutFactory.find(workout.getId()));
  }
  
  public void testFillInDays() throws Exception {
    Workout workout = createCyclistWorkoutWeek("chris");
    WorkoutFactory workoutFactory = (WorkoutFactory) getBeanFactory().getBean("workoutFactory");
    WeekFactory weekFactory = (WeekFactory) getBeanFactory().getBean("weekFactory");
    
    // Weeks before and after
    Calendar startDate = new GregorianCalendar();
    startDate.setTime(workout.getWeek().getStartDate());
    startDate.add(Calendar.DATE, -7);
    Week weekBefore = new Week(workout.getUser(), startDate);
    weekFactory.save(weekBefore);

    startDate = new GregorianCalendar();
    startDate.setTime(workout.getWeek().getStartDate());
    startDate.add(Calendar.DATE, 7);
    Week weekAfter = new Week(workout.getUser(), startDate);
    weekFactory.save(weekAfter);

    // Get all workouts
    User cyclist = workout.getUser();
    List allWorkouts = workoutFactory.findAll(cyclist);
    assertEquals("Workouts before filling in days", 1, allWorkouts.size());
    
    workoutFactory.fillInDays(cyclist);
    
    allWorkouts = workoutFactory.findAll(cyclist);
    assertEquals("Workouts before filling in days", 21, allWorkouts.size());
    for (int i = 0; i < allWorkouts.size(); i++) {
    	Calendar calendar = new GregorianCalendar();
    	calendar.setTime(weekBefore.getStartDate());
    	calendar.add(Calendar.DATE, i);
    	Date expectedDate = calendar.getTime();
    	Workout newWorkout = (Workout) allWorkouts.get(i);
    	assertEqualsToDay("New workout date", expectedDate, newWorkout.getDate());
    	assertEquals("New workout cyclist", cyclist, newWorkout.getUser());
    }
  }
  
  public void testFindAll() throws Exception {
    // Filler -- we don't want workouts for this cyclist
    createCyclistWorkoutWeek("bob");
    
    Workout workout = createCyclistWorkoutWeek("chris");
    WorkoutFactory workoutFactory = (WorkoutFactory) getBeanFactory().getBean("workoutFactory");
    WeekFactory weekFactory = (WeekFactory) getBeanFactory().getBean("weekFactory");
    
    Calendar startDate = new GregorianCalendar();
    startDate.setTime(workout.getWeek().getStartDate());
    startDate.add(Calendar.DATE, 7);
    User cyclist = workout.getUser();
    Week secondWeek = new Week(cyclist, startDate);
    weekFactory.save(secondWeek);
    List expectedWorkouts = new ArrayList();
    expectedWorkouts.add(workout);
    Calendar workoutDate = new GregorianCalendar();
    workoutDate.setTime(startDate.getTime());
    for (int i = 0; i < 5; i++) {
      Workout newWorkout = new Workout();
      newWorkout.setDate(workoutDate.getTime());
      newWorkout.setUser(cyclist);
      newWorkout.setEquipment(workout.getEquipment());
      newWorkout.setWeek(secondWeek);
      workoutFactory.save(newWorkout);
      expectedWorkouts.add(newWorkout);
      workoutDate.add(Calendar.DATE, 1);
    }

    // Filler -- we don't want workouts for this cyclist
    createCyclistWorkoutWeek("garvin");

    List workoutsFromDB = workoutFactory.findAll(cyclist);
    assertEquals("All workouts", expectedWorkouts.size(), workoutsFromDB.size());
    assertEquals("All workouts", expectedWorkouts, workoutsFromDB);
  }
}
