package com.butlerpress.cyclinglog;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

public class EquipmentFormValidatorTestCase extends junit.framework.TestCase {

  public EquipmentFormValidatorTestCase(String name) {
    super(name);
  }

  public void testValidateNoErrors() throws Exception {
    // Create cyclist with equipment
    User user = new User("test", "Test User");
    Equipment e1 = new Equipment("", user);
    Equipment e2 = new Equipment("Adidas", user);
    Equipment e3 = new Equipment("Vanilla", user);
    EquipmentForm form = new EquipmentForm();
    List equipment = new ArrayList();
    equipment.add(e1);
    equipment.add(e2);
    equipment.add(e3);
    form.setCyclist(user);
    form.setEquipment(equipment);
    Equipment newEquipment = new Equipment("", user);
    form.setNewEquipment(newEquipment);

    Errors errors = new BindException(form, "form");
    EquipmentFormValidator validator = new EquipmentFormValidator();
    validator.validate(form, errors);
    assertEquals("No errors", 0, errors.getErrorCount());
  }

  public void testValidateValidNew() throws Exception {
    // Create cyclist with equipment
    User user = new User("test", "Test User");
    Equipment e1 = new Equipment("", user);
    Equipment e2 = new Equipment("Adidas", user);
    Equipment e3 = new Equipment("Vanilla", user);
    EquipmentForm form = new EquipmentForm();
    List equipment = new ArrayList();
    equipment.add(e1);
    equipment.add(e2);
    equipment.add(e3);
    form.setCyclist(user);
    form.setEquipment(equipment);
    Equipment newEquipment = new Equipment("Foo Bar", user);
    form.setNewEquipment(newEquipment);

    Errors errors = new BindException(form, "form");
    EquipmentFormValidator validator = new EquipmentFormValidator();
    validator.validate(form, errors);
    assertEquals("No errors", 0, errors.getErrorCount());
  }

  public void testValidateValidUpdate() throws Exception {
    // Create cyclist with equipment
    User user = new User("test", "Test User");
    Equipment e1 = new Equipment("", user);
    Equipment e2 = new Equipment("Adidas 2", user);
    Equipment e3 = new Equipment("Vanilla 3", user);
    EquipmentForm form = new EquipmentForm();
    List equipment = new ArrayList();
    equipment.add(e1);
    equipment.add(e2);
    equipment.add(e3);
    form.setCyclist(user);
    form.setEquipment(equipment);
    Equipment newEquipment = new Equipment("", user);
    form.setNewEquipment(newEquipment);

    Errors errors = new BindException(form, "form");
    EquipmentFormValidator validator = new EquipmentFormValidator();
    validator.validate(form, errors);
    assertEquals("No errors", 0, errors.getErrorCount());
  }

  public void testValidateNewDuplicate() throws Exception {
    // Create cyclist with equipment
    User user = new User("test", "Test User");
    Equipment e1 = new Equipment("", user);
    Equipment e2 = new Equipment("Adidas", user);
    Equipment e3 = new Equipment("Vanilla", user);
    EquipmentForm form = new EquipmentForm();
    List equipment = new ArrayList();
    equipment.add(e1);
    equipment.add(e2);
    equipment.add(e3);
    form.setCyclist(user);
    form.setEquipment(equipment);
    Equipment newEquipment = new Equipment("Adidas", user);
    form.setNewEquipment(newEquipment);

    Errors errors = new BindException(form, "form");
    EquipmentFormValidator validator = new EquipmentFormValidator();
    validator.validate(form, errors);
    assertEquals("Should have error", 1, errors.getErrorCount());
    assertNotNull("Global error", errors.getGlobalError());
    assertNotNull("Global error message", errors.getGlobalError().getDefaultMessage());
    assertEquals("Global error code", EquipmentForm.DUPLICATE_NEW_NAME_ERROR, errors.getGlobalError().getCode());
  }

  public void testValidateUpdatedDuplicate() throws Exception {
    // Create cyclist with equipment
    User user = new User("test", "Test User");
    Equipment e1 = new Equipment("", user);
    Equipment e2 = new Equipment("Vanilla", user);
    Equipment e3 = new Equipment("Vanilla", user);
    EquipmentForm form = new EquipmentForm();
    List equipment = new ArrayList();
    equipment.add(e1);
    equipment.add(e2);
    equipment.add(e3);
    form.setCyclist(user);
    form.setEquipment(equipment);
    Equipment newEquipment = new Equipment("Steelman", user);
    form.setNewEquipment(newEquipment);

    Errors errors = new BindException(form, "form");
    EquipmentFormValidator validator = new EquipmentFormValidator();
    validator.validate(form, errors);
    assertEquals("Should have error", 1, errors.getErrorCount());
    assertNotNull("Global error", errors.getGlobalError());
    assertNotNull("Global error message", errors.getGlobalError().getDefaultMessage());
    assertEquals("Global error code", EquipmentForm.DUPLICATE_ERROR, errors.getGlobalError().getCode());
  }

  public void testValidateUpdatedDuplicateBlankNames() throws Exception {
    // Create cyclist with equipment
    User user = new User("test", "Test User");
    Equipment e1 = new Equipment("", user);
    Equipment e2 = new Equipment("", user);
    Equipment e3 = new Equipment("Vanilla", user);
    EquipmentForm form = new EquipmentForm();
    List equipment = new ArrayList();
    equipment.add(e1);
    equipment.add(e2);
    equipment.add(e3);
    form.setCyclist(user);
    form.setEquipment(equipment);
    Equipment newEquipment = new Equipment("Steelman", user);
    form.setNewEquipment(newEquipment);

    Errors errors = new BindException(form, "form");
    EquipmentFormValidator validator = new EquipmentFormValidator();
    validator.validate(form, errors);
    assertEquals("Should have error", 1, errors.getErrorCount());
    assertNotNull("Global error", errors.getGlobalError());
    assertNotNull("Global error message", errors.getGlobalError().getDefaultMessage());
    assertEquals("Global error code", EquipmentForm.BLANK_ERROR, errors.getGlobalError().getCode());
  }
}
