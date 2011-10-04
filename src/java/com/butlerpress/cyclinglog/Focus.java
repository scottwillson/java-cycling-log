package com.butlerpress.cyclinglog;

import java.util.ArrayList;
import java.util.List;

public class Focus {
  private static final List all = new ArrayList();
  
  public static final Focus NONE = new Focus("");
  public static final Focus CLIMB = new Focus("Climbing");
  public static final Focus DISTANCE = new Focus("Distance");
  public static final Focus RECOVERY = new Focus("Recovery");
  public static final Focus SPEED = new Focus("Speed");
  public static final Focus TECHNIQUE = new Focus("Technique");

  private String name = "Uninitialized";
  
  private Focus(String name) {
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