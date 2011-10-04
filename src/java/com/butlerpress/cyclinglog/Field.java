package com.butlerpress.cyclinglog;

public class Field {
  public static final Field DURATION = new Field("duration");
  public static final Field WORK = new Field("work");

  private String name;

  private Field(String name) {
    this.name = name;
  }

  public int hashCode() {
    return name.hashCode();
  }

  public boolean equals(Object o) {
    Field oField = (Field)o;
    return name.equals(oField.name);
  }

	public String toString() {
		return "[Field " + name + "]";
	}
}
