package com.butlerpress.cyclinglog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class WorkoutsController extends CyclingLogRequestHandler {

  static final SimpleDateFormat DATE_PARAM_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
  private static final Logger log = Logger.getLogger(WorkoutsController.class);

  private EquipmentFactory equipmentFactory;

  public ModelAndView home(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Enumeration enumeration = request.getParameterNames();
    if (log.isDebugEnabled()) {
      while (enumeration.hasMoreElements()) {
        String name = (String) enumeration.nextElement();
        log.debug(name + " = " + request.getParameter(name));
      }
    }
    Model model = getModelFactory().getModel(request);
    User cyclist = model.getCyclist();
    if (cyclist == null) {
      return new ModelAndView(new RedirectView("/cycling_log/dynamic/login/home"));
    }
    Date weekDate = model.getDate();
    Week week = getWeekFactory().get(weekDate, cyclist);
    if (request.getParameter("scrollUp.x") != null) {
      week = getWeekFactory().getPrevious(week);
    } else if (request.getParameter("scrollDown.x") != null) {
      week = getWeekFactory().getNext(week);
    } else if (request.getParameter("new") != null) {
      return add(request, response);
    }

    return viewHome(week, request);
  }

  public ModelAndView week(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String idParam = request.getParameter(Model.WEEK + "id");
    if (idParam != null) {
      log.debug("View week " + idParam);
      Long id = new Long(idParam);
      Week week = getWeekFactory().get(id);
      return viewHome(week, request);
    }
    
    String dateParam = request.getParameter(Model.DATE);
    if (dateParam != null) {
      log.debug("View week " + dateParam);
      Model model = getModelFactory().getModel(request);
      Week week = getWeekFactory().get(model.getDate(), model.getCyclist());
      return viewHome(week, request);
    }
    
    throw new CyclingLogException(Model.WEEK + "id or " + Model.DATE + " is mandatory");
  }

  public ModelAndView delete(HttpServletRequest request) throws Exception {

    Model model = getModelFactory().getModel(request);
    User cyclist = model.getCyclist();
    if (!User.canEdit(model.getUser(), cyclist)) {
      throw new CyclingLogException("You do not have rights to delete workouts for this cyclist");
    }
    log.debug(model.getUser() + " delete workout for " + cyclist);

    Date workoutDate = new Date();
    String longParam = request.getParameter(Model.WORKOUT + "id");
    Long id = Long.valueOf(longParam);
    if (id.longValue() != 0) {
      Workout workout = getWorkoutFactory().find(id);
      workoutDate = workout.getDate();
      getWorkoutFactory().delete(workout);
    }
    RedirectView view = new RedirectView("/cycling_log/dynamic/workouts/week");
    Map simpleRedirectModel = new HashMap();
    simpleRedirectModel.put(Model.DATE, DATE_PARAM_FORMAT.format(workoutDate));
    simpleRedirectModel.put(Model.ID, cyclist.getId());
    return new ModelAndView(view, simpleRedirectModel);
  }

  public ModelAndView cancel(HttpServletRequest request) throws Exception {
    RedirectView view = new RedirectView("/cycling_log/dynamic/workouts/week");
    Map simpleRedirectModel = new HashMap();
    Model model = getModelFactory().getModel(request);
    Date date = model.getDate();
    simpleRedirectModel.put(Model.DATE, DATE_PARAM_FORMAT.format(date));
    simpleRedirectModel.put(Model.ID, model.getCyclist().getId());
    return new ModelAndView(view, simpleRedirectModel);
  }

  public ModelAndView edit(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Model model = getModelFactory().getModel(request);
    if (!User.canEdit(model.getUser(), model.getCyclist())) {
      throw new RuntimeException("You do not have rights to edit workouts for this cyclist");
    }
    log.info(model.getUser() + " edit workout for " + model.getCyclist());

    Long id = new Long((request.getParameter(Model.WORKOUT + "id")));

    Session session = sessionFactory.openSession();
    try {
      Workout workout = (Workout)session.load(Workout.class, id);
      Hibernate.initialize(workout.getUser().getEquipment());
      WorkoutView workoutView = new WorkoutView(workout);
      model.put("activities", Activity.getAll());
      model.put("foci", Focus.getAll());
      model.put("workout", workoutView);
      model.put("workoutDateSelector", new DateSelector(workout.getDate()));
      return new ModelAndView("workouts/edit.html", model);
    } finally {
      if (session != null) session.close();
    }
  }

  public ModelAndView add(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Model model = getModelFactory().getModel(request);
    User cyclist = model.getCyclist();
    log.info(model.getUser() + " add workout for " + cyclist);
    if (!User.canEdit(model.getUser(), cyclist)) {
      throw new RuntimeException("You do not have rights to create new workouts for this cyclist");
    }
    model.put("workoutDateSelector", new DateSelector(false));
    model.put("activities", Activity.getAll());
    model.put("equipment", getEquipmentFactory().find(cyclist));
    model.put("foci", Focus.getAll());
    WorkoutView workoutView = new WorkoutView(new Workout(cyclist));
    model.put("workout", workoutView);
    return new ModelAndView("workouts/edit.html", model);
  }

  public ModelAndView update(HttpServletRequest request, HttpServletResponse response) throws Exception {
    
    if (request.getParameter("delete") != null) {
      return delete(request);
    } else if (request.getParameter("cancel") != null) {
      return cancel(request);
    }

    Session session = sessionFactory.openSession();
    Week week = null;
    Workout workout = null;
    Model model = getModelFactory().getModel(request);
    User cyclist = model.getCyclist();
    if (!User.canEdit(model.getUser(), cyclist)) {
      throw new RuntimeException("You do not have rights to edit workouts for this cyclist");
    }
    log.info(model.getUser() + " update workout for " + cyclist);
    try {
      String idParam = request.getParameter(Model.WORKOUT + "id");
      Map requestMap = new HashMap(request.getParameterMap());
      Equipment equipment = getEquipmentFactory().find(new Long(request.getParameter("workout_equipment")));
      requestMap.put("workout_equipment", equipment);
      long id = 0;
      if (idParam != null && !idParam.equals("")) {
        id = Long.parseLong(idParam);
      }
      log.debug("id = " + id);
      if (id != 0) {
        log.debug("updating existing workout");
        workout = (Workout) session.load(Workout.class, new Long(id));
        Week existingWorkoutWeek = workout.getWeek();
        workout.update(requestMap);
        Week updatedWorkoutWeek = getWeekFactory().get(workout.getDate(), cyclist.getUsername());
        if (!existingWorkoutWeek.equals(updatedWorkoutWeek)) {
          existingWorkoutWeek.removeWorkout(workout);
          updatedWorkoutWeek.addWorkout(workout);
          session.saveOrUpdate(existingWorkoutWeek);
          session.saveOrUpdate(updatedWorkoutWeek);
          week = updatedWorkoutWeek;
        } else {
          existingWorkoutWeek.calculate();
          week = existingWorkoutWeek;
        }
      } else {
        log.debug("adding workout");
        Date date = model.getDate();
        week = getWeekFactory().get(date, cyclist.getUsername());
        requestMap.put("workout_user", cyclist);
        workout = new Workout(requestMap);
        week.addWorkout(workout);
        session.saveOrUpdate(week);
      }
      session.saveOrUpdate(workout);
      session.flush();

    } finally {
      if (session != null) {
        session.close();
      }
    }
    RedirectView view = new RedirectView("/cycling_log/dynamic/workouts/week");
    Map simpleRedirectModel = new HashMap();
    simpleRedirectModel.put(Model.DATE, week.getStartDate());
    simpleRedirectModel.put(Model.ID, workout.getUser().getId());
    return new ModelAndView(view, simpleRedirectModel);
  }

  ModelAndView viewHome(Week week, HttpServletRequest request) throws Exception {
    Model model = getModelFactory().getModel(request);
    User user = model.getUser();
    User cyclist = model.getCyclist();
    log.debug(model.getUser() + " view workouts for " + cyclist);

    List workouts = null;
    Session session = null;
    try {
      session = sessionFactory.openSession();
      workouts = session.find(
        "from workout in class com.butlerpress.cyclinglog.Workout " +
        "where date >= ? and date <= ? " +
        "  and workout.user.username = ? " +
        "order by date asc",
        new Object[] { week.getStartDate(), week.getEndDate(), cyclist.getUsername() },
        new Type[] { Hibernate.DATE, Hibernate.DATE, Hibernate.STRING }
      );
  
      List workoutViews = new ArrayList();
      if (workouts != null) {
        Iterator iter = workouts.iterator();
        while (iter.hasNext()) {
          Workout workout = (Workout)iter.next();
          workoutViews.add(new WorkoutView(workout));
        }
      }
      
      model.put("weekDateSelector", new DateSelector(week.getStartDate()));
      model.put("workouts", workoutViews);
      model.put("week", new WeekView(week));
      model.put("cyclists", getAllCyclists());
      if (User.canEdit(user, cyclist)) {
        return new ModelAndView("workouts/index.html", model);
      } else {
        return new ModelAndView("workouts/public_index.html", model);    
      }
    } finally {
      if (session != null) session.close();
    }
  }
  
  public EquipmentFactory getEquipmentFactory() {
    return equipmentFactory;
  }
  
  public void setEquipmentFactory(EquipmentFactory equipmentFactory) {
    this.equipmentFactory = equipmentFactory;
  }
}
