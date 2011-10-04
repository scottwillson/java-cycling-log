package com.butlerpress.cyclinglog;

import java.io.Serializable;

public class Equipment implements Comparable, FormData, Serializable {

	private static final long serialVersionUID = -6941190043199692771L;

  private Long id;
  private String name;
  private User user;

  public Equipment() {
  }

  public Equipment(User cyclist, Long id) {
    this.id = id;
    this.user = cyclist;
    user.addEquipment(this);
  }

  public Equipment(String name, User user) {
    if (user == null) {
      throw new IllegalArgumentException("User cannot be null");
    }
    this.name = name;
    this.user = user;
    user.addEquipment(this);
  }

  public String getName() {
    return name;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public boolean equals(Object o) {
    if (!(o instanceof Equipment) || o == null) {
      return false;
    }
    final Equipment oEquipment = (Equipment) o;
    if (id == null) return false;
    return id.equals(oEquipment.getId());
  }
  
  public int hashCode() {
    return super.hashCode();
  }

  public String getDisplayText() {
    return name;
  }

  public String getKey() {
    return String.valueOf(id);
  }
  
  public int compareTo(Object o) {
  	final Equipment oEquipment = (Equipment) o;
  	return getName().compareTo(oEquipment.getName());
  }
  
  public String toString() {
    return "[Equipment " + id + " " + name + " " + user + "]";
  }
  
}
