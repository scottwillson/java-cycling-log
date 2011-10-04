package com.butlerpress.cyclinglog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

class Model extends HashMap {

  public static final String DATE = "date";
  public static final String ID = "id";
  public static final String WEEK = "week_";
  public static final String WORKOUT = "workout_";
  public static final SimpleDateFormat DATE_PARAM_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

  public Model() {
    super();
  }

  public User getCyclist() {
    return (User) get("cyclist");
  }

  public User getUser() {
    return (User) get("user");
  }
  
  public Date getDate() {
    return (Date) get(DATE);
  }

}
