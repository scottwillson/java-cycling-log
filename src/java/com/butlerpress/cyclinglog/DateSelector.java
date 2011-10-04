package com.butlerpress.cyclinglog;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateSelector {

  static final String[] months = {
    "", "Jan", "Feb", "Mar", "Apr",
    "May", "Jun", "Jul", "Aug", "Sep",
    "Oct", "Nov", "Dec",
  };
  static final String[] years = {
    "",
    "1993", "1994", "1995", "1996",
    "1997", "1998", "1999", "2000",
    "2001", "2002", "2003", "2004",
    "2005", "2006", "2007"
  };

  String html;
  String formattedDate;

  public DateSelector(Date date) {
    this(date, true);
  }

  public DateSelector(String day, String month, String year) {
    int intDay = 0;
    int intMonth = 0;
    int intYear = 0;
    try {
      intDay = Integer.parseInt(day);
      intMonth = Integer.parseInt(month);
      intYear = Integer.parseInt(year);
    } catch (Exception e) {
      // No problem -- default to no date
    }
    init(intDay, intMonth, intYear, true);
  }

  public DateSelector(Date date, boolean javascript) {
    int intDay = 0;
    int intMonth = 0;
    int intYear = 0;

    if (date == null) {
      date = new Date();
    }

    Calendar calendar = new GregorianCalendar();
    calendar.setTime(date);
    intDay = calendar.get(Calendar.DAY_OF_MONTH);
    intMonth = calendar.get(Calendar.MONTH) + 1;
    intYear = calendar.get(Calendar.YEAR);
    init(intDay, intMonth, intYear, javascript);
  }

  public DateSelector(boolean javascript) {
    int intDay = 0;
    int intMonth = 0;
    int intYear = 0;

    Calendar calendar = new GregorianCalendar();
    intDay = calendar.get(Calendar.DAY_OF_MONTH);
    intMonth = calendar.get(Calendar.MONTH) + 1;
    intYear = calendar.get(Calendar.YEAR);
    init(intDay, intMonth, intYear, javascript);
  }

  private void init(int day, int month, int year, boolean javascript) {
    StringBuffer buffer = new StringBuffer(1024);

    buffer.append("<select name=\"");
    buffer.append("month\" ");
    if (javascript) {
      buffer.append("onchange=\"form.submit()\">\r");
    } else {
      buffer.append(">\r");
    }
    for (int i = 0; i < months.length; i++) {
      buffer.append("  <option value=\"");
      buffer.append(new Integer(i));
      buffer.append("\"");
      if (i == month) {
        buffer.append(" selected");
      }
      buffer.append(">");
      buffer.append(months[i]);
      buffer.append("</option>\n");
    }
    buffer.append("</select>\n");

    buffer.append("<select name=\"");
    buffer.append("day\" ");
    if (javascript) {
      buffer.append("onchange=\"form.submit()\">\r");
    } else {
      buffer.append(">\r");
    }
    buffer.append("  <option");
    if (day == 0) {
      buffer.append(" selected");
    }
    buffer.append(">");
    buffer.append("</option>\n");
    for (int j = 1; j < 32; j++) {
      buffer.append("  <option");
      if (j == day) {
        buffer.append(" selected");
      }
      buffer.append(">");
      buffer.append(new Integer(j));
      buffer.append("</option>\n");
    }
    buffer.append("</select>\n");

    buffer.append("<select name=\"");
    buffer.append("year\" class=\"date_selector\" ");
    if (javascript) {
      buffer.append("onchange=\"form.submit()\">\r");
    } else {
      buffer.append(">\r");
    }
    for (int k = 0; k < years.length; k++) {
      buffer.append("  <option");
      if (years[k].equals(Integer.toString(year))) {
        buffer.append(" selected");
      }
      buffer.append(">");
      buffer.append(years[k]);
      buffer.append("</option>\n");
    }
    buffer.append("</select>\n");

    html = buffer.toString();
    
    formattedDate = month + "/" + day + "/" + year;
  }
  
  String getFormattedDate() {
    return formattedDate;
  }

  public String toString() {
    return html;
  }
}
