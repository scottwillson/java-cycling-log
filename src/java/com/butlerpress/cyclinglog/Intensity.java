package com.butlerpress.cyclinglog;

import java.util.ArrayList;
import java.util.List;

public class Intensity {
  private static final List all = new ArrayList();
  
  public static final Intensity NONE = new Intensity("");
  public static final Intensity HARD = new Intensity("Hard");
  public static final Intensity MEDIUM = new Intensity("Medium");
  public static final Intensity EASY = new Intensity("Easy");
  public static final Intensity OFF = new Intensity("Off");

  private String name = "Uninitialized";
  
  private Intensity(String name) {
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