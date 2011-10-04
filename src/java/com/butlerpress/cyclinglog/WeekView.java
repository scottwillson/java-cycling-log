package com.butlerpress.cyclinglog;


public class WeekView extends ViewBean {

  Week week;

  public WeekView(Week week) {
    this.week = week;
  }

  public String getEndDate(String format) {
    return getDate(week.getEndDate(), format);
  }

  public String getStartDate(String format) {
    return getDate(week.getStartDate(), format);
  }

  public long getId() {
    return week.getId();
  }

  public void setId(long id) {
    week.setId(id);
  }

  public String getFocus() {
    return week.getFocus();
  }

  public void setFocus(String focus) {
    week.setFocus(focus);
  }

  public String getNotes() {
    return week.getNotes();
  }

  public void setNotes(String notes) {
    week.setNotes(notes);
  }

  public String getPublicNotes() {
    return week.getPublicNotes();
  }

  public int getDuration() {
    return week.getDuration();
  }

  public String getDurationH() {
    if (getDuration() == 0) return "";
    return decimalFormat.format(getDuration() / 60.0);
  }

  public String getDurationDeviation() {
    if (week != null) {
      float deviation = week.getDurationDeviation();
      if (deviation <= -1.5) {
        return "--";
      } else if (deviation <= -1) {
        return "-";
      } else if (deviation >= 1) {
        return "+";
      } else if (deviation >= 1.5) {
        return "++";
      }
    }
    return "";
 }

  public String getWorkDeviation() {
    if (week != null) {
      float deviation = week.getWorkDeviation();
      if (deviation <= -1.5) {
        return "--";
      } else if (deviation <= -1) {
        return "-";
      } else if (deviation >= 1) {
        return "+";
      } else if (deviation >= 1.5) {
        return "++";
      }
    }
    return "";
 }

  public void setDuration(int duration) {
    week.setDuration(duration);
  }

  public float getDistance() {
    return week.getDistance();
  }

  public String getDistanceM() {
    if (week.getDistance() == 0) return "";
    return roundedDecimalFormat.format(week.getDistance());
  }

  public void setDistance(float distance) {
    week.setDistance(distance);
  }

  public String getIntensity() {
    return week.getIntensity();
  }

  public void setIntensity(String intensity) {
    week.setIntensity(intensity);
  }

  public float getMorale() {
    return week.getMorale();
  }

  public String getMoraleString() {
    if (week.getMorale() == 0) return "";
    return decimalFormat.format(week.getMorale());
  }

  public void setMorale(float morale) {
    week.setMorale(morale);
  }
  public float getLife() {
    return week.getLife();
  }

  public String getLifeString() {
    if (week.getLife() == 0) return "";
    return decimalFormat.format(week.getLife());
  }

  public void setLife(float life) {
    week.setLife(life);
  }

  public float getWeather() {
    return week.getWeather();
  }

  public String getWeatherString() {
    if (week.getWeather() == 0) return "";
    return decimalFormat.format(week.getWeather());
  }

  public void setWeather(float weather) {
    week.setWeather(weather);
  }

  public float getWeight() {
    return week.getWeight();
  }

  public String getWeightString() {
    if (week.getWeight() == 0) return "";
    return decimalFormat.format(week.getWeight());
  }

  public void setWeight(float weight) {
    week.setWeight(weight);
  }

  public float getWork() {
    return week.getWork();
  }

  public String getWorkString() {
    if (week.getWork() == 0) return "";
    return roundedDecimalFormat.format(week.getWork());
  }

  public void setWork(float work) {
    week.setWork(work);
  }
  
  public String toString() {
    return "[WeekView " + getStartDate("M/d/yyyy") + " " + getEndDate("M/d/yyyy") + "]";
  }
}