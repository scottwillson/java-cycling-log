package com.butlerpress.cyclinglog;

import java.util.Iterator;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.bag.HashBag;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class EquipmentFormValidator implements Validator {

	public EquipmentFormValidator() { }

	public boolean supports(Class clazz) {
		return EquipmentForm.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object obj, Errors errors) {
        EquipmentForm form = (EquipmentForm) obj;
        Iterator iter = form.getEquipment().iterator();
        Bag equipmentNames = new HashBag();
        while (iter.hasNext()) {
            Equipment e = (Equipment) iter.next();
            equipmentNames.add(e.getName());
        }
        Iterator iter2 = equipmentNames.iterator();
        while (iter2.hasNext()) {
            String name = (String) iter2.next();
            if (equipmentNames.getCount(name) > 1) {
                if (name.equals("")) {
                    errors.reject(EquipmentForm.BLANK_ERROR, "Equipment can't have a blank name");
                    break;
                }
                errors.reject(EquipmentForm.DUPLICATE_ERROR, "Duplicate name '" + name + "'");
                break;
            }
            Equipment newEquipment = form.getNewEquipment();
            String newEquipmentName = newEquipment.getName();
            if (!("".equals(newEquipmentName)) && name.equals(newEquipmentName)) {
                errors.reject(EquipmentForm.DUPLICATE_NEW_NAME_ERROR, "Duplicate name '" + newEquipmentName + "'");
                break;
            }
        }
    }
}