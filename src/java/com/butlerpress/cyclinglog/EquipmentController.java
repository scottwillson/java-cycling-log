package com.butlerpress.cyclinglog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

public class EquipmentController extends SimpleFormController {
  
  protected static final Log log = LogFactory.getLog(EquipmentController.class);
  
  ModelFactory modelFactory;
  EquipmentFactory equipmentFactory;
  UserFactory userFactory;

  public EquipmentController() {
	  setSessionForm(true);
  }
  
  public EquipmentFactory getEquipmentFactory() {
    return equipmentFactory;
  }
  
  public void setEquipmentFactory(EquipmentFactory equipmentFactory) {
    this.equipmentFactory = equipmentFactory;
  }
  
  public ModelFactory getModelFactory() {
    return modelFactory;
  }
  
  public void setModelFactory(ModelFactory modelFactory) {
    this.modelFactory = modelFactory;
  }
  
  public UserFactory getUserFactory() {
  	return userFactory;
  }
  
  public void setUserFactory(UserFactory userFactory) {
  	this.userFactory = userFactory;
  }

  protected Object formBackingObject(HttpServletRequest request) throws Exception {
  	log.debug("Get form backing object");
    Model model = getModelFactory().getModel(request);
    EquipmentForm equipmentForm = new EquipmentForm();
    User cyclist = model.getCyclist();
    User user = model.getUser();
    // No user = no form. Will redirect to login anyway
    if (cyclist == null || user == null || !User.canEdit(user, cyclist)) {
      return equipmentForm;
    }
    // Refresh cyclist from database
    cyclist = userFactory.find(cyclist.getUsername());
    equipmentForm.setCyclist(cyclist);
    // Put equipment in List to sort for display and populate from request
    List equipment = new ArrayList(cyclist.getEquipment());
    Collections.sort(equipment);
    equipmentForm.setEquipment(equipment);
    equipmentForm.setNewEquipment(new Equipment("", cyclist));
    return equipmentForm;
  }
  
  protected ModelAndView showForm(HttpServletRequest request,
            HttpServletResponse response, BindException errors)
            throws Exception {
        
    Model model = getModelFactory().getModel(request);
    User cyclist = model.getCyclist();
    User user = model.getUser();
    // No user = no form. Session expired or never logged-in
    if (cyclist == null || user == null) {
  		return new ModelAndView(new RedirectView("/cycling_log/dynamic/login/home"));
    }
    if (!User.canEdit(user, cyclist)) {
      throw new CyclingLogException(user.getName() + " cannot edit equipment for " + cyclist.getName());
    }
     
    return super.showForm(request, response, errors);
  }
    
  protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
    Model model = getModelFactory().getModel(request);
    if (errors.getErrorCount() > 0) {
      model.put("errors", errors.getGlobalError().getDefaultMessage());
    }
    return model;
  }

  protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, 
  	Object command, BindException errors) throws Exception {
  	
    EquipmentForm form = (EquipmentForm) command;
    Model model = getModelFactory().getModel(request);
		if (WebUtils.hasSubmitParameter(request, "save")) {
			return save(form, model);
		} else if (WebUtils.hasSubmitParameter(request, "done")) {
			return new ModelAndView(new RedirectView("/cycling_log/dynamic/login/home"));
		} else {
			return delete(request, form, model);
		}
  }
  
  ModelAndView delete(HttpServletRequest request, EquipmentForm form, Model model) throws Exception {
  	log.debug("delete()");
  	String deleteID = request.getParameter("delete_id");
  	if (deleteID != null) {
			Equipment equipment = new Equipment(form.getCyclist(), new Long(deleteID));
			equipmentFactory.delete(equipment);
  	}
    return new ModelAndView(new RedirectView("/cycling_log/dynamic/equipment/home"));
  }
  
  ModelAndView save(EquipmentForm form, Model model) {
  	log.debug("save()");
    Equipment newEquipment = form.getNewEquipment();
    if (!newEquipment.getName().equals("")) {
	    equipmentFactory.save(form.getNewEquipment());
    }
    equipmentFactory.update(form.getEquipment());
    return new ModelAndView(new RedirectView("/cycling_log/dynamic/equipment/home"));
  }
}
