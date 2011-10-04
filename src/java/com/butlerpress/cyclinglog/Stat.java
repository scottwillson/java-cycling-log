package com.butlerpress.cyclinglog;

public class Stat {
  public static final Stat AVERAGE = new Stat("avg");
  public static final Stat STANDARD_DEVIATION = new Stat("std_dev");

  private String name;

  private Stat(String name) {
    this.name = name;
  }

  public int hashCode() {
    return name.hashCode();
  }

  public boolean equals(Object o) {
    Stat oStat = (Stat)o;
    return name.equals(oStat.name);
  }

	public String toString() {
		return "[Stat " + name + "]";
	}
}
