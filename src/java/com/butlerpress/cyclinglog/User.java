package com.butlerpress.cyclinglog;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class User implements Serializable, FormData {

	public static final User NULL = new User("", "");

	private static final long serialVersionUID = -6211750146476816322L;

private Long id;
  private String password;
  private String username;
  private String name;
  private String firstName;
  private String lastName;
  private String email;
  private Set equipment = new HashSet();
  private Set administrators = new HashSet();

  public User() { }

  User(String username, String name) {
    this.username = username;
    this.name = name;
  }

  public void addEquipment(Equipment newEquipment) {
    equipment.add(newEquipment);
  }

  public void addAdministrator(User user) {
    administrators.add(user);
  }

  public Set getAdministrators() {
    return administrators;
  }

  public void setAdministrators(Set administrators) {
    this.administrators = administrators;
  }

  public String getDisplayText() {
		return name;
	}
	
	public String getKey() {
		return username;
	}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public String getFirstName() {
    return firstName;
  }
  
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  
  public String getLastName() {
    return lastName;
  }
  
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  
  public Set getEquipment() {
    return equipment;
  } 
  
  public void setEquipment(Set equipment) {
    this.equipment = equipment;
  }

  public static boolean canEdit(User user, User cyclist) {
    if (user == null) return false;
    if (user.equals(cyclist)) {
      return true;
    }
    if (cyclist.getAdministrators().contains(user)) {
      return true;
    }
    return false;
  }

  public static boolean canEdit(User user, Week week) {
    if (user == null) return false;
    if (user.getUsername().equals(week.getUser().getUsername())) {
      return true;
    }
    if (user.getUsername().equals("sw")) {
      return true;
    }
    return false;
  }

  public int hashCode() {
    return id.hashCode();
  }

  public boolean equals(Object o) {
    if (!(o instanceof User) || o == null) {
      return false;
    }
    if (id == null) {
      return false;
    }
    User oUser = (User) o;
    Long oId = oUser.getId();
    return id.equals(oId);
  }
  
  public String toString() {
  	return "[User " + id + " " + firstName + " " + lastName + " " + name + " " + username + " " + email + "]";
  }

}
