package com.butlerpress.cyclinglog;

public class FormDataBean implements FormData {
  private String value;
  
  private FormDataBean() { }

  public FormDataBean(String value) {
  	if (value != null) {
  		this.value = value;
  	} else {
  		this.value = "";
  	}
  }

  public FormDataBean(Object value) {
  	if (value != null) {
  		this.value = value.toString();
  	} else {
  		this.value = "";
  	}
  }

  public String getDisplayText() {
    return value;
  }

  public String getKey() {
    return value;
  }

  public int hashCode() {
    return value.hashCode();
  }

  public boolean equals(Object o) {
  	if (o == null) return false;
    FormData oFormData = (FormData) o;
    return value.equals(oFormData.getKey());
  }
}
