package com.butlerpress.cyclinglog;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/*
 * @TODO Max/avg heart rate
 * @TODO Heart rate zones
 * @TODO Max speed
 * @TODO Workout search
 * @TODO Rotating stats on homepage
 * @TODO Visitor logging
 * @TODO Show several workouts on homepage
 * @TODO Cyclist pictures
 * @TODO Image gallery
 * @TODO Filter out non-aerobic activities
 * @TODO Dynamic creation of week and workouts
 * @TODO RSS
 *
 * @TODO Make all IDs Long/make common domain superclass
 * @TODO Ditch View classes
 * @TODO Move all Hibernate code to Factories/DAO
 * @TODO Make find/get methods consistent
 * @TODO Make user/cyclist consistent
 * @TODO Handle session timeout better
 * @TODO Think about packages
 * @TODO Date -> Calendar
 * @TODO Use HibernateTemplate
 * @TODO Add Week.getMonday()
 * @TODO RSS
 * @TODO Make Activities editable or at least dynamic
 * @TODO Rework speed and auto-calc
 * @TODO Hide inactive users
 * @TODO Move enums to lookup tables
 * @TODO Add DB constraints
 * @TODO Add HTTPUnit system tests
 * @TODO Switch to full Spring MVC
 */
public class App {
  
  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      usage();
    }
    String[] configLocations = new String[] {
        "dataSource.xml", "applicationContext.xml", "frontcontroller-servlet.xml"
      };
    BeanFactory beanFactory = new ClassPathXmlApplicationContext(configLocations);
    App app = (App) beanFactory.getBean("app");
    String cmd = args[0];
    if (cmd.equals("add_weeks")) {
      if (args.length < 2) {
        usage();
      }
      app.addWeeks(Integer.parseInt(args[1]), args[2]);
    } else if (cmd.equals("fill_in_days")) {
      app.fillInDays(args[1]);
    } else if (cmd.equals("stats")) {
      app.stats();
    } else if (cmd.equals("home")) {
      app.home();
    } else if (cmd.equals("add_user")) {
      app.addUser(args[1], args[2], args[3], args[4], args[5]);
    } else {
      usage();
    }
  }
  
  private static void usage() {
    System.out.println("add_weeks number cyclist");
    System.out.println("fill_in_days cyclist");
    System.out.println("add_user username firstName lastName password email");
      System.exit(1);
  }

	EquipmentFactory equipmentFactory;
  UserFactory userFactory;
  WeekFactory weekFactory;
  WorkoutFactory workoutFactory;

	public App() {}
	
  private void home() throws Exception {
    Collection latest = getWorkoutFactory().findLatest();
    System.out.println(latest);
    for (Iterator iter = latest.iterator(); iter.hasNext();) {
      Workout workout = (Workout) iter.next();
      System.out.println(workout.getDate() + " " + workout.getUser().getName() + " " + workout.getPublicNotes());
    }
  }

  private void stats() {
    Week week = getWeekFactory().get(new Date(), "sw");
    System.out.println("duration std dev" + week.getDurationDeviation());
  }

  private void fillInDays(String username) {
    User user = getUserFactory().find(username);
    getWorkoutFactory().fillInDays(user);
  }

  private void addWeeks(int quantity, String username) {
    getWeekFactory().add(quantity, username);
  }

  void addUser(String username, String firstName, String lastName, String password, String email) {
  	User user = new User();
  	user.setUsername(username);
  	user.setFirstName(firstName);
  	user.setLastName(lastName);
  	user.setPassword(password);
  	user.setEmail(email);
		getUserFactory().save(user);
		
		Equipment equipment = new Equipment("", user);
		equipmentFactory.save(equipment);
		
  }

  public EquipmentFactory getEquipmentFactory() {
    return equipmentFactory;
  }
  
  public void setEquipmentFactory(EquipmentFactory equipmentFactory) {
    this.equipmentFactory = equipmentFactory;
  }

  public UserFactory getUserFactory() {
  	  return userFactory;
  }
  
  public void setUserFactory(UserFactory userFactory) {
    	this.userFactory = userFactory;
  }

  public WeekFactory getWeekFactory() {
    return weekFactory;
  }
  
  public void setWeekFactory(WeekFactory weekFactory) {
    this.weekFactory = weekFactory;
  }
  
  public WorkoutFactory getWorkoutFactory() {
    return workoutFactory;
  }

  public void setWorkoutFactory(WorkoutFactory workoutFactory) {
    this.workoutFactory = workoutFactory;
  }
}
