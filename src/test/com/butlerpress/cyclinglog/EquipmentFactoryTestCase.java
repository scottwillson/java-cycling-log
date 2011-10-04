package com.butlerpress.cyclinglog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class EquipmentFactoryTestCase extends TestCase {

  public void testFindByUser() throws Exception {
    EquipmentFactory factory = (EquipmentFactory) getBeanFactory().getBean("equipmentFactory");
    User user = new User();
    user.setId(new Long(1));
    List equipment = factory.find(user);
    assertNotNull("Equipment list", equipment);
  }
  
  public void testDeleteUsedEquipment() throws Exception {
    UserFactory userFactory = (UserFactory) getBeanFactory().getBean("userFactory");
    // Create cyclist with equipment
    User user = new User("test", "Test User");
    userFactory.save(user);
    EquipmentFactory equipmentFactory = (EquipmentFactory) getBeanFactory().getBean("equipmentFactory");
    Equipment e1 = new Equipment("", user);
    equipmentFactory.save(e1);
    Equipment e2 = new Equipment("Adidas", user);
    equipmentFactory.save(e2);
    Equipment e3 = new Equipment("Vanilla", user);
    equipmentFactory.save(e3);
    assertNotNull(e2.getName() + " should be saved to database (id != null)", e2.getId());
    
    equipmentFactory = (EquipmentFactory) getBeanFactory().getBean("equipmentFactory");
    Week week = new Week();
    week.setEndDate(new Date());
    week.setStartDate(new Date());
    week.setFocus("");
    week.setIntensity("");
    week.setNotes("");
    week.setPublicNotes("");
    week.setUser(user);
    Workout workout = new Workout(user);
    workout.setWeek(week);
    workout.setActivity("");
    workout.setDate(new Date());
    workout.setFocus("");
    workout.setNotes("");
    workout.setPublicNotes("");
    workout.setEquipment(e2);
    WorkoutFactory workoutFactory = (WorkoutFactory) getBeanFactory().getBean("workoutFactory");
    WeekFactory weekFactory = (WeekFactory) getBeanFactory().getBean("weekFactory");
    weekFactory.save(week);
    workoutFactory.save(workout);
    
    // Delete
    try {
	  equipmentFactory.delete(e2);
      fail("Attempt to delete referenced equipment hould throw exception");
    } catch (Exception e) {
    		// OK
    }
    
	// Check database
	user = userFactory.find(user.getUsername());
	List equipment = new ArrayList(user.getEquipment());
	Collections.sort(equipment);
	assertEquals("Equipment count", 3, equipment.size());
	String[] expectedNames = new String[] {"", "Adidas", "Vanilla"};
	for (int i = 0; i < equipment.size(); i++) {
		Equipment e = (Equipment) equipment.get(i);
		assertEquals("Equipment name", expectedNames[i], e.getName());
	}
  	
  }
}
