package com.butlerpress.cyclinglog;

import java.util.Date;

public class WorkoutView extends ViewBean {

  Workout workout;

  public WorkoutView(Workout workout) {
    this.workout = workout;
  }

  public String getDate(String format) {
    return getDate(workout.getDate(), format);
  }

  public String getDurationHHMM() {
    StringBuffer durationHHMM = new StringBuffer(6);
    int remainder = workout.getDuration();
    if (remainder == 0) {
      return "";
    }
    if (remainder > 59) {
      int hours = (remainder / 60);
      durationHHMM.append(hours);
      remainder = remainder - (hours * 60);
    }
    durationHHMM.append(":");
    if (remainder < 10) {
      durationHHMM.append("0");
    }
    durationHHMM.append(remainder);
    return durationHHMM.toString();
  }

  public String getHours() {
    int duration = workout.getDuration();
    if (duration > 59) {
      return Integer.toString((duration / 60));
    } else {
      return "";
    }
  }

  public String getMinutes() {
    StringBuffer durationMM = new StringBuffer(6);
    int remainder = workout.getDuration();
    if (remainder > 59) {
      int hours = (remainder / 60);
      remainder = remainder - (hours * 60);
    }
    if (remainder < 10) {
      durationMM.append("0");
    }
    durationMM.append(remainder);
    return durationMM.toString();
  }
  
  public String getMinutesPerMile() {
  	float speed = workout.getSpeed();
  	if (speed > 0) {
	  return new Integer((int)Math.floor(60 / speed)).toString();
  	} else {
  	  return "";
  	}
  }

  public String getSecondsPerMile() {
  	float speed = workout.getSpeed();
	  long secondsPerMile = Math.round(((60 / speed) - Math.floor(60 / speed)) * 60);
	  if (secondsPerMile < 10) {
		return "0" + secondsPerMile;
	  } else {
		return "" + secondsPerMile;
	  }
  }

    public Long getId() {
        return workout.getId();
    }

    public void setId(Long id) {
        workout.setId(id);
    }

    public Equipment getEquipment() {
      return workout.getEquipment();
    }

    public void setEquipment(Equipment equipment) {
      workout.setEquipment(equipment);
    }

    public String getFocus() {
      return workout.getFocus();
    }

    public void setFocus(String focus) {
      workout.setFocus(focus);
    }

    public String getNotes() {
      return workout.getNotes();
    }

    public void setNotes(String notes) {
      workout.setNotes(notes);
    }

    public String getPublicNotes() {
      return workout.getPublicNotes();
    }

    public void setPublicNotes(String publicNotes) {
      workout.setPublicNotes(publicNotes);
    }

    public Date getDate() {
        return workout.getDate();
    }

    public void setDate(Date date) {
        workout.setDate(date);
    }
    public int getDuration() {
        return workout.getDuration();
    }

    public void setDuration(int duration) {
      workout.setDuration(duration);
    }

    public float getDistance() {
      return workout.getDistance();
    }

    public String getDistanceM() {
      if (workout.getDistance() == 0) {
        return "";
      }
      Math.round(workout.getDistance());
      return Integer.toString(Math.round(workout.getDistance()));
    }

    public void setDistance(float distance) {
      workout.setDistance(distance);
    }

    public float getSpeed() {
      return workout.getSpeed();
    }

    public String getSpeedString() {
    if (workout.getSpeed() == 0) return "";
    return decimalFormat.format(workout.getSpeed());
    }

    public void setSpeed(float speed) {
      workout.setSpeed(speed);
    }

    public int getIntensity() {
      return workout.getIntensity();
    }

    public String getIntensityString() {
      if (workout.getIntensity() == 0) {
        return "";
      }
      return new Integer(workout.getIntensity()).toString();
    }

    public void setIntensity(int intensity) {
        workout.setIntensity(intensity);
    }

    public int getMorale() {
      return workout.getMorale();
    }

    public String getMoraleString() {
      if (workout.getMorale() == 0) {
        return "";
      }
      return new Integer(workout.getMorale()).toString();
    }

    public void setMorale(int morale) {
        workout.setMorale(morale);
    }
    public int getLife() {
        return workout.getLife();
    }

    public String getLifeString() {
      if (workout.getLife() == 0) {
        return "";
      }
      return new Integer(workout.getLife()).toString();
    }

    public void setLife(int life) {
        workout.setLife(life);
    }

    public int getWeather() {
      return workout.getWeather();
    }

    public String getWeatherString() {
      if (workout.getWeather() == 0) {
        return "";
      }
      return new Integer(workout.getWeather()).toString();
    }

    public void setWeather(int weather) {
      workout.setWeather(weather);
    }

    public String getActivity() {
        return workout.getActivity();
    }

    public void setActivity(String activity) {
        workout.setActivity(activity);
    }

    public float getWeight() {
      return workout.getWeight();
    }

    public String getWeightString() {
      if (workout.getWeight() == 0) {
        return "";
      }
      return new Float(workout.getWeight()).toString();
    }

    public void setWeight(float weight) {
        workout.setWeight(weight);
    }

    public float getWork() {
        return workout.getWork();
    }

    public String getWorkString() {
      if (workout.getWork() == 0) {
        return "";
      }
      return new Float(workout.getWork()).toString();
    }


    public void setWork(float work) {
        workout.setWork(work);
    }
    
    public User getUser() {
      return workout.getUser();
    }

}