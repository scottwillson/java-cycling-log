package com.butlerpress.cyclinglog;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

public abstract class Convertor {

  static float getFloat(String key, Map map) {
    try {
      Object value = map.get(key);
      String stringValue = null;
      if (value instanceof String[]) {
        stringValue = ((String[])(value))[0];
      } else {
        stringValue = (String)value;
      }
      return Float.parseFloat(stringValue);
    } catch (Exception e) {
      return 0;
    }
  }

  static int getInt(String key, Map map) {
    try {
      Object value = map.get(key);
      String stringValue = null;
      if (value instanceof String[]) {
        stringValue = ((String[])(value))[0];
      } else {
        stringValue = (String)value;
      }
      return Integer.parseInt(stringValue);
    } catch (Exception e) {
      return 0;
    }
  }

  static String getStringNotNull(String key, Map map) {
    String value = getString(key, map);
    if (value == null) {
      return "";
    }
    return value;
  }

  static String getString(String key, Map map) {
    try {
      Object value = map.get(key);
      if (value instanceof String[]) {
        return ((String[])(value))[0];
      }
			return (String)value;
    } catch (Exception e) {
      return "";
    }
  }

  static Date getDate(int day, int month, int year) {
    Date date = new Date();
    try {
      Calendar calendar = new GregorianCalendar();
      calendar.set(year, month - 1, day, 0, 0);
      date = calendar.getTime();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return date;
  }

  static Date getDate(Map map) {
    Date date = new Date();
    try {
      String day = (String)(map.get("day"));
      String month = (String)(map.get("month"));
      String year = (String)(map.get("year"));
      int intDay = Integer.parseInt(day);
      int intMonth = Integer.parseInt(month);
      int intYear = Integer.parseInt(year);
      date = getDate(intDay, intMonth, intYear);
    } catch (Exception e) {

    }
    return date;
  }

}