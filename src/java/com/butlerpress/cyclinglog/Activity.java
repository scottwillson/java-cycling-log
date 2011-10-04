package com.butlerpress.cyclinglog;

import java.util.ArrayList;
import java.util.List;

public class Activity {
  private static final List all = new ArrayList();
  
  public static final Activity NONE = new Activity("");
  public static final Activity ROAD = new Activity("Road");
  public static final Activity CYCLOCROSS = new Activity("CX");
  public static final Activity TRACK = new Activity("Track");
  public static final Activity HIKE = new Activity("Hike");
  public static final Activity MTB = new Activity("MTB");
  public static final Activity OFF = new Activity("Off");
  public static final Activity RACE = new Activity("Race");
  public static final Activity ROCK_CLIMBING = new Activity("Rock Climbing");
  public static final Activity ROLLERS = new Activity("Rollers");
  public static final Activity RUN = new Activity("Run");
  public static final Activity SKI = new Activity("Ski");
  public static final Activity SWIM = new Activity("Swim");
  public static final Activity YOGA = new Activity("Yoga");

  private String name = "Uninitialized";
  
  private Activity(String name) {
    this.name = name;
    all.add(this);
  }
  
  public static List getAll() {
    return all;
  }
  
  public String getName() {
    return name;
  }
  
  public String toString() {
    return name;
  }
} 