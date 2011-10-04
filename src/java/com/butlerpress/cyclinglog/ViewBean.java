package com.butlerpress.cyclinglog;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewBean {
  DecimalFormat decimalFormat = new DecimalFormat("0.0");
  DecimalFormat roundedDecimalFormat = new DecimalFormat("#");

  public String getDate(Date date, String format) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
    return simpleDateFormat.format(date);
  }

}
