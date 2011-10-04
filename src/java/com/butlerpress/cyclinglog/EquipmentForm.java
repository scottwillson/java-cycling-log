package com.butlerpress.cyclinglog;

import java.io.Serializable;
import java.util.List;

public class EquipmentForm implements Serializable {

  public static final String BLANK_ERROR = "blank";
  public static final String DUPLICATE_ERROR = "duplicate";
  public static final String DUPLICATE_NEW_NAME_ERROR = "duplicate new name";

	private static final long serialVersionUID = -5111522434130689309L;

	private User cyclist;
	private List equipment;
	private Equipment newEquipment;

  public EquipmentForm() { }

	public User getCyclist() {
		return cyclist;
	}
	
	public void setCyclist(User cyclist) {
		this.cyclist = cyclist;
	}
		
  public List getEquipment() {
		return equipment;
  }
  
  public void setEquipment(List equipment) {
		this.equipment = equipment;
  }
  
  public Equipment getNewEquipment() {
		return newEquipment;
  }
  
  public void setNewEquipment(Equipment newEquipment) {
		this.newEquipment = newEquipment;
  }
  
}
