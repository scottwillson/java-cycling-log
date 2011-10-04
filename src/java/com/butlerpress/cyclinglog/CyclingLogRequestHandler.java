package com.butlerpress.cyclinglog;

import java.util.List;

import net.sf.hibernate.SessionFactory;

import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class CyclingLogRequestHandler extends MultiActionController {

  ModelFactory modelFactory;
  SessionFactory sessionFactory;
  UserFactory userFactory;
  WeekFactory weekFactory;
  WorkoutFactory workoutFactory;

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
  public ModelFactory getModelFactory() {
    return modelFactory;
  }
  
  public void setModelFactory(ModelFactory modelFactory) {
    this.modelFactory = modelFactory;
  }
  
  public SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  public void setSessionFactory(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public UserFactory getUserFactory() {
  	return userFactory;
  }
  
  public void setUserFactory(UserFactory userFactory) {
  	this.userFactory = userFactory;
  }

  
  List getAllCyclists() {
      return (getUserFactory().findAll());
  }
  
}
