package com.butlerpress.cyclinglog;

public class Cycle {
  public static final Cycle WEEK = new Cycle("weeks");

  private String table;

  private Cycle(String table) {
    this.table = table;
  }

  public int hashCode() {
    return table.hashCode();
  }

  public boolean equals(Object o) {
    Cycle oCycle = (Cycle)o;
    return table.equals(oCycle.table); 
  }

	public String toString() {
		return "[Week " + table + "]";
	}
}
