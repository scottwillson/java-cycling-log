package com.butlerpress.cyclinglog;

import java.util.Collection;
import java.util.Iterator;

public class SelectTool {

  public SelectTool() { }

  public String getSelect(String name, Collection values) {
    return getSelect(name, values, null);
  }

  public String getSelect(String name, Collection values, Object selectedValue) {
    return getSelect(name, values, selectedValue, "", "width: 100px;");
  }

  public String getSelect(String name, Collection values, Object selectedValue, String style) {
    return getSelect(name, values, selectedValue, style, "");
  }
  
  public String getSelect(String name, Collection values, Object selectedValue, String style, String javascript) {
    if (values == null) {
      throw new IllegalArgumentException("Cannot create " + name + ". 'values' cannot be null");
    }
    selectedValue = getFormData(selectedValue);
		StringBuffer buffer = new StringBuffer(256);
		buffer.append("<select name=\"");
		buffer.append(name);
		buffer.append("\" style=\"");
		buffer.append(style);
		buffer.append("\" ");
    buffer.append(javascript);
    buffer.append(">\r");
		Iterator iterator = values.iterator();
		while (iterator.hasNext()) {
			FormData value = getFormData(iterator.next());
      if (value == null) {
        throw new IllegalArgumentException("Cannot create " + name + ". 'values' list cannot contain null values");
      }
			if (value.equals(selectedValue)) {
				buffer.append("\t<option value=\"");
				buffer.append(value.getKey());
				buffer.append("\" ");
				buffer.append("selected>");
				buffer.append(value.getDisplayText());
				buffer.append("</option>\r");
			} else {
				buffer.append("\t<option value=\"");
				buffer.append(value.getKey());
				buffer.append("\">");
				buffer.append(value.getDisplayText());
				buffer.append("</option>\r");
			}
		}
		buffer.append("</select>");
		return buffer.toString();
	}

  public String toString() {
    return "This is the select tool's toString()";
  }
  
  FormData getFormData(Object object) {
    if (object instanceof FormData) {
      return (FormData) object;
    } else {
      return new FormDataBean(object);
    }
    
  }
}