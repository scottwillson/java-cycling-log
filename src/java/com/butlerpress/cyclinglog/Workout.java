package com.butlerpress.cyclinglog;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @TODO initialize all non-null fields.
 */
public class Workout extends ViewBean implements Serializable {

	private Long id;
  private String activity = "";
  private Equipment equipment;
  private String focus = "";
  private String notes = "";
  private String publicNotes = "";
  private Date date;
  private int duration;
  private float distance;
  private float speed;
  private int intensity;
  private int morale;
  private int life;
  private User user;
  private int weather;
  private Week week;
  private float weight;
  private float work;

  public Workout(Map map) {
    // Use BeanUtils!
    update(map);
  }

  public Workout() {
  }

  // TODO Make equipment mandatory
  public Workout(User cyclist) {
  	this.user = cyclist;
  	date = new Date();
  }

  public Long getId() {
      return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Equipment getEquipment() {
    return this.equipment;
  }

  public void setEquipment(Equipment equipment) {
    this.equipment = equipment;
  }

  public String getFocus() {
    return this.focus;
  }

  public void setFocus(String focus) {
    this.focus = focus;
  }

  public String getNotes() {
    return this.notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getPublicNotes() {
    return this.publicNotes;
  }

  public void setPublicNotes(String publicNotes) {
    this.publicNotes = publicNotes;
  }

  public Date getDate() {
      return this.date;
  }

  public String getDate(String format) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
    return simpleDateFormat.format(this.date);
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public void setDate(int day, int month, int year) {
    setDate(Convertor.getDate(day, month, year));
  }

  public int getDuration() {
      return this.duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public float getDistance() {
      return this.distance;
  }

  public void setDistance(float distance) {
    this.distance = distance;
  }

  public float getSpeed() {
      return this.speed;
  }

  public void setSpeed(float speed) {
    this.speed = speed;
  }

  public int getIntensity() {
      return this.intensity;
  }

  public void setIntensity(int intensity) {
    this.intensity = intensity;
  }

  public int getMorale() {
    return this.morale;
  }

  public void setMorale(int morale) {
    this.morale = morale;
  }
  public int getLife() {
      return this.life;
  }

  public void setLife(int life) {
    this.life = life;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public int getWeather() {
      return this.weather;
  }

  public void setWeather(int weather) {
    this.weather = weather;
  }

  public String getActivity() {
      return this.activity;
  }

  public void setActivity(String activity) {
    this.activity = activity;
  }

  public Week getWeek() {
    return this.week;
  }

  public void setWeek(Week week) {
    this.week = week;
  }

  public float getWeight() {
      return this.weight;
  }

  public void setWeight(float weight) {
    this.weight = weight;
  }

  public float getWork() {
      return this.work;
  }

  public void setWork(float work) {
    this.work = work;
  }

  /*
   * Formatting getters
   */
  public String getDurationHHMM() {
    StringBuffer durationHHMM = new StringBuffer(6);
    int remainder = getDuration();
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
    int duration = getDuration();
    if (duration > 59) {
      return Integer.toString((duration / 60));
    } else {
      return "";
    }
  }

  public String getMinutes() {
    StringBuffer durationMM = new StringBuffer(6);
    int remainder = getDuration();
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
  	float speed = getSpeed();
  	if (speed > 0) {
	  return new Integer((int)Math.floor(60 / speed)).toString();
  	} else {
  	  return "";
  	}
  }

  public String getSecondsPerMile() {
  	float speed = getSpeed();
	  long secondsPerMile = Math.round(((60 / speed) - Math.floor(60 / speed)) * 60);
	  if (secondsPerMile < 10) {
		return "0" + secondsPerMile;
	  } else {
		return "" + secondsPerMile;
	  }
  }

    public String getDistanceM() {
      if (getDistance() == 0) {
        return "";
      }
      Math.round(getDistance());
      return Integer.toString(Math.round(getDistance()));
    }

   public String getSpeedString() {
    if (getSpeed() == 0) return "";
    return decimalFormat.format(getSpeed());
   }
   
  public String getSummary() {
    String prefix = "";
    
    String summary = "" + activity;
    if (!summary.equals("")) {
      prefix = ", ";
    }
    
    if (duration > 0) {
      summary = summary + prefix + getDurationHHMM();
      prefix = ", ";
    }

    if (distance > 0) {
      summary = summary + prefix + getDistanceM() + " miles";
    }

    if (!summary.equals("")) {
      summary = summary + ". ";
    }
    return summary;  
  }
 
  public String toString() {
      return "[Workout " + this.id + " " + getDate() + "]";
  }

  public boolean equals(Object other) {
      if ( !(other instanceof Workout) ) return false;
      Workout castOther = (Workout) other;
      return new EqualsBuilder()
          .append(this.id, castOther.id)
          .isEquals();
  }

  public int hashCode() {
      return new HashCodeBuilder()
          .append(id)
          .toHashCode();
  }

  void calculate() {
    work = duration * intensity / 60;
  }

  public void update(Map map) {
    setFocus(Convertor.getStringNotNull("workout_focus", map));
    setNotes(Convertor.getStringNotNull("workout_notes", map));
    notes = notes.replaceAll("\"", "");
    setPublicNotes(Convertor.getStringNotNull("workout_publicNotes", map));
    publicNotes = publicNotes.replaceAll("\"", "");

    String dateString = Convertor.getString("date", map);
    Calendar dateObject = (Calendar) map.get("workout_date_object");
    if (dateString != null) {
      String[] dateParts = dateString.split("-");
      setDate(Integer.parseInt(dateParts[2]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[0]));
    } else if (dateObject != null) {
      setDate(dateObject.getTime());
    } else {
      setDate(Convertor.getInt("day", map), Convertor.getInt("month", map), Convertor.getInt("year", map));
    }

    String durationString = Convertor.getString("workout_duration", map);
    if (durationString != null) {
      setDuration(Convertor.getInt("workout_duration", map));
    } else {
      setDuration(Convertor.getInt("workout_hours", map) * 60 + Convertor.getInt("workout_minutes", map));
    }

    setIntensity(Convertor.getInt("workout_intensity", map));
    setMorale(Convertor.getInt("workout_morale", map));
    setLife(Convertor.getInt("workout_life", map));
    setWeather(Convertor.getInt("workout_weather", map));
    String activity = (Convertor.getStringNotNull("workout_activity", map));
    setActivity(activity);
    setDistance(Convertor.getFloat("workout_distance", map));
    setSpeed(Convertor.getFloat("workout_speed", map));
    setWeight(Convertor.getFloat("workout_weight", map));
    User user = (User) map.get("workout_user");
    if (user != null) {
        setUser(user);
    }
    setEquipment((Equipment) map.get("workout_equipment"));
    calculate();
  }

}
