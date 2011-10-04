package com.butlerpress.cyclinglog;

import java.util.ArrayList;
import java.util.List;

public class WeekFocus {
  private static final List all = new ArrayList();
  
  public static final WeekFocus NONE = new WeekFocus("");
  public static final WeekFocus BASE = new WeekFocus("Base");
  public static final WeekFocus BUILD = new WeekFocus("Build");
  public static final WeekFocus OFF = new WeekFocus("Off");
  public static final WeekFocus OFF_SEASON = new WeekFocus("Off season");
  public static final WeekFocus PEAK = new WeekFocus("Peak");
  public static final WeekFocus PRE_SEASON = new WeekFocus("Preseason");
  public static final WeekFocus RECOVERY = new WeekFocus("Recovery");
  public static final WeekFocus SHARPEN = new WeekFocus("Sharpen");

  private String name = "Uninitialized";
  
  private WeekFocus(String name) {
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