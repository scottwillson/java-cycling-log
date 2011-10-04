package com.butlerpress.cyclinglog;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 
 * @TODO initialize all non-null fields.
 */
public class Week implements Comparable, Serializable {

	private long id;
  private int duration;
  private float distance;
  Date endDate;
  private String focus = "";
  private String intensity = "";
  private float life;
  private float morale;
  private String notes = "";
  private String publicNotes = "";
  Date startDate;
  private User user;
  private float weather;
  private float weight;
  private float work;
  Set workouts = new HashSet();

  public static Date getStart(Date date) {
      Calendar calendar = new GregorianCalendar();
      calendar.setTime(date);
      calendar.set(Calendar.HOUR, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      switch (calendar.get(Calendar.DAY_OF_WEEK)) {
        case Calendar.SUNDAY:
          calendar.add(Calendar.DAY_OF_YEAR, -6);
          break;
        case Calendar.MONDAY:
          break;
        case Calendar.TUESDAY:
          calendar.add(Calendar.DAY_OF_YEAR, -1);
          break;
        case Calendar.WEDNESDAY:
          calendar.add(Calendar.DAY_OF_YEAR, -2);
          break;
        case Calendar.THURSDAY:
          calendar.add(Calendar.DAY_OF_YEAR, -3);
          break;
        case Calendar.FRIDAY:
          calendar.add(Calendar.DAY_OF_YEAR, -4);
          break;
        case Calendar.SATURDAY:
          calendar.add(Calendar.DAY_OF_YEAR, -5);
          break;
      }
      return calendar.getTime();
  }

  public Week() {
  }
  
  /**
   * Set end date to start date + 6 days
   */
  public Week(User cyclist, Calendar startDate) {
    this.user = cyclist;
    if (startDate != null) {
      this.startDate = startDate.getTime();
      Calendar endDateCal = new GregorianCalendar();
      endDateCal.setTime(this.startDate);
      endDateCal.add(Calendar.DATE, 6);
      this.endDate = endDateCal.getTime();
    }
  }

  public long getId() {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
  }
  
  public void calculate() {
    distance = 0;
    duration = 0;
    life = 0;
    morale = 0;
    weather = 0;
    weight = 0;
    work = 0;
    int lifeCount = 0;
    int moraleCount = 0;
    int weatherCount = 0;
    int weightCount = 0;
    Iterator iter = getWorkouts().iterator();
    while (iter.hasNext()) {
      Workout workout = (Workout)iter.next();
      if (workout.getDistance() > 0) {
        distance = distance + workout.getDistance();
      }
      if (workout.getDuration() > 0) {
        duration = duration + workout.getDuration();
      }
      if (workout.getLife() > 0) {
        life = life + workout.getLife();
        lifeCount++;
      }
      if (workout.getMorale() > 0) {
        morale = morale + workout.getMorale();
        moraleCount++;
      }
      if (workout.getWeather() > 0) {
        weather = weather + workout.getWeather();
        weatherCount++;
      }
      if (workout.getWeight() > 0) {
        weight = weight + workout.getWeight();
        weightCount++;
      }
      if (workout.getWork() > 0) {
        work = work + workout.getWork();
      }
    }
    if (lifeCount > 0) {
      life = life / lifeCount;
    }
    if (moraleCount > 0) {
      morale = morale / moraleCount;
    }
    if (weatherCount > 0) {
      weather = weather / weatherCount;
    }
    if (weightCount > 0) {
      weight = weight / weightCount;
    }
  }
    
    public float getDistance() {
        return this.distance;
    }

    public void setDistance(float distance) {
      this.distance = distance;
    }
    
    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
      this.duration = duration;
    }
    
  public Date getEndDate() {
    return endDate;
  }
  
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }
  
    public String getFocus() {
      return this.focus;
    }

    public void setFocus(String focus) {
      this.focus = focus;
    }

    public String getIntensity() {
        return this.intensity;
    }

    public void setIntensity(String intensity) {
      this.intensity = intensity;
    }

    public float getLife() {
        return this.life;
    }

    public void setLife(float life) {
      this.life = life;
    }
    
    public float getMorale() {
      return this.morale;
    }

    public void setMorale(float morale) {
      this.morale = morale;
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

  public Date getStartDate() {
    return startDate;
  }
  
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
  
  public void update(Map map) {
    setFocus(Convertor.getString("week_focus", map));
    setIntensity(Convertor.getString("week_intensity", map));
    setLife(Convertor.getInt("week_life", map));
    setNotes(Convertor.getStringNotNull("week_notes", map));
    setPublicNotes(Convertor.getStringNotNull("week_public_notes", map));
  }
    
    public float getWeather() {
        return this.weather;
    }

    public void setWeather(float weather) {
      this.weather = weather;
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

  public void addWorkout(Workout workout) {
    workout.setWeek(this);
    getWorkouts().add(workout);
    calculate();
  }
  
  public void removeWorkout(Workout workout) {
    workout.setWeek(null);
    getWorkouts().remove(workout);
    calculate();
  }
  
  public Set getWorkouts() {
    return workouts;
  }
  
  public void setWorkouts(Set workouts) {
    this.workouts = workouts;
  }
  
  public String toString() {
    return "Week (" + id + " " + startDate + " " + endDate + " " + this.user + ")";
  }

    public boolean equals(Object other) {
        if ( !(other instanceof Week) ) return false;
        Week castOther = (Week) other;
        return new EqualsBuilder()
            .append(this.id, castOther.id)
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(id)
            .toHashCode();
    }
    
  public int compareTo(Object o) {
    if ( !(o instanceof Week) ) return Integer.MIN_VALUE;
    Week otherWeek = (Week) o;
    return (int) (this.id - otherWeek.getId());
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public float getDurationDeviation() {
    float avg =  Statistics.getAverage(user.getId(), Cycle.WEEK, Field.DURATION, focus);
    float stddev =  Statistics.getStandardDeviation(user.getId(), Cycle.WEEK, Field.DURATION, focus);
    float difference = duration - avg;
    return difference / stddev;
  }

  public float getWorkDeviation() {
    float avg =  Statistics.getAverage(user.getId(), Cycle.WEEK, Field.WORK, focus);
    float stddev =  Statistics.getStandardDeviation(user.getId(), Cycle.WEEK, Field.WORK, focus);
    float difference = duration - avg;
    return difference / stddev;
  }

}
