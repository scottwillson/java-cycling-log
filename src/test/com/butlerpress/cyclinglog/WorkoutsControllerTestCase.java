package com.butlerpress.cyclinglog;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.type.Type;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class WorkoutsControllerTestCase extends TestCase {

  public void testDelete() throws Exception {
    Workout workout = createCyclistWorkoutWeek("test");

    User user = workout.getUser();
    
    // Show form
    MockHttpServletRequest request = new MockHttpServletRequest(null, "GET", "/workouts/edit");
    request.addParameter("workout_id", workout.getId().toString());
    HttpSession session = request.getSession();
    session.setAttribute("cyclist", user);
    session.setAttribute("user", user);
    HttpServletResponse response = new MockHttpServletResponse();
    WorkoutsController controller = (WorkoutsController) getBeanFactory().getBean("workoutsController");
    ModelAndView mv = controller.handleRequest(request, response);

    // Delete
    request = new MockHttpServletRequest(null, "POST", "/workouts/update");
    // Re-use session with logged-in user
    request.setSession(session);
    request.addParameter("delete", "Delete");
    request.addParameter("workout_id", workout.getId().toString());
    request.addParameter("month", "2");
    request.addParameter("day", "14");
    request.addParameter("year", "2005");
    request.addParameter("workout_activity", "");
    request.addParameter("workout_intensity", "");
    request.addParameter("workout_focus", "");
    request.addParameter("workout_morale", "");
    request.addParameter("workout_equipment", workout.getEquipment().getId().toString());
    request.addParameter("workout_hours", "");
    request.addParameter("workout_minutes", "00");
    request.addParameter("workout_life", "");
    request.addParameter("workout_speed", "0.0");    
    request.addParameter("workout_weather" , "");
    request.addParameter("workout_distance", "0.0");    
    request.addParameter("workout_weight", "0.0");  
    request.addParameter("workout_publicNotes", "");
    request.addParameter("workout_notes", "");
    response = new MockHttpServletResponse();
    mv = controller.handleRequest(request, response);
    
    // Should redirect 
    assertEquals("Success view", RedirectView.class, mv.getView().getClass());
    RedirectView successView = (RedirectView) mv.getView();
    assertTrue("Success view URL", successView.getUrl().indexOf("/workouts/week") > -1);
    // No errors
    assertTrue("Should have no errors", mv.getModel().get("errors") == null);
    String weekDate = (String) mv.getModel().get(Model.DATE);
    assertNotNull("weekDate", weekDate);
    assertEquals("Week date", "2005-02-15", weekDate);
    Long id = (Long) mv.getModel().get(Model.ID);
    assertEquals("Cyclist ID", user.getId(), id);

    // Check database
    WorkoutFactory workoutFactory = (WorkoutFactory) getBeanFactory().getBean("workoutFactory");
    Workout workoutFromDB = workoutFactory.find(workout.getId());
    assertNull("Workout should not be database after deletion", workoutFromDB);
  }

  public void testCancel() throws Exception {
    Workout workout = createCyclistWorkoutWeek("test");
    User user = workout.getUser();
    
    // Show form
    MockHttpServletRequest request = new MockHttpServletRequest(null, "GET", "/workouts/edit");
    request.addParameter("workout_id", workout.getId().toString());
    HttpSession session = request.getSession();
    session.setAttribute("cyclist", user);
    session.setAttribute("user", user);
    HttpServletResponse response = new MockHttpServletResponse();
    WorkoutsController controller = (WorkoutsController) getBeanFactory().getBean("workoutsController");
    ModelAndView mv = controller.handleRequest(request, response);

    // Done
    request = new MockHttpServletRequest(null, "POST", "/workouts/update");
    // Re-use session with logged-in user
    request.setSession(session);
    request.addParameter("cancel", "Cancel");
    request.addParameter("workout_id", workout.getId().toString());
    request.addParameter("month", "7");
    request.addParameter("day", "6");
    request.addParameter("year", "2004");
    request.addParameter("workout_activity", "");
    request.addParameter("workout_intensity", "");
    request.addParameter("workout_focus", "");
    request.addParameter("workout_morale", "");
    request.addParameter("workout_equipment", workout.getEquipment().getId().toString());
    request.addParameter("workout_hours", "");
    request.addParameter("workout_minutes", "00");
    request.addParameter("workout_life", "");
    request.addParameter("workout_speed", "0.0");    
    request.addParameter("workout_weather" , "");
    request.addParameter("workout_distance", "0.0");    
    request.addParameter("workout_weight", "0.0");  
    request.addParameter("workout_publicNotes", "");
    request.addParameter("workout_notes", "Don't save these notes!");
    response = new MockHttpServletResponse();
    mv = controller.handleRequest(request, response);
    
    // Should redirect 
    assertNotNull("Success ModelAndView should not be null", mv);
    assertNotNull("Success ModelAndView.view should not be null", mv.getView());
    assertEquals("Success view", RedirectView.class, mv.getView().getClass());
    RedirectView successView = (RedirectView) mv.getView();
    assertTrue("Success view URL", successView.getUrl().indexOf("/workouts/week") > -1);
    // No errors
    assertTrue("Should have no errors", mv.getModel().get("errors") == null);
    String weekDate = (String) (mv.getModel().get(Model.DATE));
    assertNotNull(Model.DATE + "date should not be null", weekDate);
    assertEquals("Week date", "2004-07-06", weekDate);
    Long id = (Long) mv.getModel().get(Model.ID);
    assertEquals("Cyclist ID", user.getId(), id);

    // Check database
    WorkoutFactory workoutFactory = (WorkoutFactory) getBeanFactory().getBean("workoutFactory");
    Workout workoutFromDB = workoutFactory.find(workout.getId());
    assertNotNull("Workout should be database after deletion", workoutFromDB);
    assertEquals("Workout notes", "", workoutFromDB.getNotes());
  }
  
  public void testDifferentEdit() throws Exception {
    UserFactory userFactory = (UserFactory) getBeanFactory().getBean("userFactory");
    // Create 2 cyclists: Cheryl and Scout
    User cheryl = new User("cheryl", "Cheryl");
    String cherylPassword = "foo";
    cheryl.setPassword(cherylPassword);
    userFactory.save(cheryl);
    EquipmentFactory equipmentFactory = (EquipmentFactory) getBeanFactory().getBean("equipmentFactory");
    Equipment cherylsEquipment = new Equipment("", cheryl);
    equipmentFactory.save(cherylsEquipment);
    Week week = new Week();
    week.setEndDate(new Date());
    week.setStartDate(new Date());
    week.setUser(cheryl);
    Workout cherylsWorkout = new Workout(cheryl);
    cherylsWorkout.setWeek(week);
    cherylsWorkout.setDate(new Date());
    cherylsWorkout.setEquipment(cherylsEquipment);
    WorkoutFactory workoutFactory = (WorkoutFactory) getBeanFactory().getBean("workoutFactory");
    WeekFactory weekFactory = (WeekFactory) getBeanFactory().getBean("weekFactory");
    weekFactory.save(week);
    workoutFactory.save(cherylsWorkout);

    User scout = new User("scout", "Scout");
    String scoutPassword = "bar";
    scout.setPassword(scoutPassword);
    userFactory.save(scout);
    Equipment scoutsEquipment = new Equipment("", scout);
    equipmentFactory.save(scoutsEquipment);
    week = new Week();
    week.setEndDate(new Date());
    week.setStartDate(new Date());
    week.setUser(scout);
    Workout scoutWorkout = new Workout(scout);
    scoutWorkout.setWeek(week);
    scoutWorkout.setDate(new Date());
    scoutWorkout.setEquipment(cherylsEquipment);
    weekFactory.save(week);
    workoutFactory.save(scoutWorkout);

    // log-in
    MockHttpServletRequest request = new MockHttpServletRequest(null, "POST", "/login/login");
    request.addParameter("username", "cheryl");
    request.addParameter("password", cherylPassword);
    HttpSession session = request.getSession();
    HttpServletResponse response = new MockHttpServletResponse();
    LoginController loginController = (LoginController) getBeanFactory().getBean("loginController");
    ModelAndView mv = loginController.handleRequest(request, response);
    
    assertNotNull("Cheryl should be logged-in", mv);
    assertNotNull("Redirect to Cheryl's workouts view", mv.getView());
    // Redirect
    RedirectView redirectView = (RedirectView) mv.getView();
    String urlWithoutRoot = redirectView.getUrl().replaceFirst("/cycling_log/dynamic", "");
    request = new MockHttpServletRequest(null, "GET", urlWithoutRoot);
    request.setSession(session);
    WorkoutsController workoutsController = (WorkoutsController) getBeanFactory().getBean("workoutsController");
    response = new MockHttpServletResponse();
    mv = workoutsController.handleRequest(request, response);
    assertEquals("Cheryl is user in session", cheryl, session.getAttribute("user"));
    assertEquals("Cheryl is cyclist in session", cheryl, session.getAttribute("cyclist"));
    assertEquals("Cheryl should be cyclist in view model", cheryl, mv.getModel().get("cyclist"));
    
    // edit blank workout
    request = new MockHttpServletRequest(null, "GET", "/workouts/edit");
    request.setSession(session);
    request.addParameter("workout_id", cherylsWorkout.getId().toString());
    response = new MockHttpServletResponse();
    mv = workoutsController.handleRequest(request, response);

    assertNotNull("Cheryl's workout page", mv);
    assertEquals("Cheryl's workout page", "workouts/edit.html", mv.getViewName());
    WorkoutView actualWorkout = (WorkoutView) mv.getModel().get("workout");
    assertEquals("Cheryl's workout should be cyclist in view model", cherylsWorkout.getId(), actualWorkout.getId());
    DateSelector workoutDateSelector = (DateSelector) mv.getModel().get("workoutDateSelector");
    assertNotNull("Workout date selector", workoutDateSelector);

    // new window with Scout
    request = new MockHttpServletRequest(null, "GET", "/login/home");
    request.setSession(session);
    response = new MockHttpServletResponse();
    mv = loginController.handleRequest(request, response);

    // Scout workouts
    request = new MockHttpServletRequest(null, "GET", "/workouts/home");
    request.addParameter("cyclist", "scout");
    request.setSession(session);
    response = new MockHttpServletResponse();
    mv = workoutsController.handleRequest(request, response);
    assertEquals("Scout should be cyclist in view model", scout, mv.getModel().get("cyclist"));

    // Look at scout workout
    request = new MockHttpServletRequest(null, "GET", "/workouts/edit");
    request.setSession(session);
    request.addParameter("workout_id", scoutWorkout.getId().toString());
    response = new MockHttpServletResponse();
    mv = workoutsController.handleRequest(request, response);

    assertNotNull("Scout's workout page", mv);
    assertEquals("Scout's workout page", "workouts/edit.html", mv.getViewName());
    actualWorkout = (WorkoutView) mv.getModel().get("workout");
    assertEquals("Scout's workout should be cyclist in view model", scoutWorkout.getId(), actualWorkout.getId());

    // original window -- Cheryl's workout
    String notes = "Cheryl's notes about her hike";
    request = new MockHttpServletRequest(null, "POST", "/workouts/update");
    request.setSession(session);
    request.addParameter("Save.x", "2");
    request.addParameter("workout_id", cherylsWorkout.getId().toString());
    request.addParameter("month", "7");
    request.addParameter("day", "6");
    request.addParameter("year", "2004");
    request.addParameter("workout_activity", "");
    request.addParameter("workout_intensity", "");
    request.addParameter("workout_focus", "");
    request.addParameter("workout_morale", "");
    request.addParameter("workout_equipment", cherylsEquipment.getId().toString());
    request.addParameter("workout_hours", "");
    request.addParameter("workout_minutes", "00");
    request.addParameter("workout_life", "");
    request.addParameter("workout_speed", "0.0");    
    request.addParameter("workout_weather" , "");
    request.addParameter("workout_distance", "0.0");    
    request.addParameter("workout_weight", "0.0");  
    request.addParameter("workout_publicNotes", "");
    request.addParameter("workout_notes", notes);
    response = new MockHttpServletResponse();
    // Save
    mv = workoutsController.handleRequest(request, response);
    
    // Should redirect 
    assertNotNull("Success ModelAndView should not be null", mv);
    assertNotNull("Success ModelAndView.view should not be null", mv.getView());
    assertEquals("Success view", RedirectView.class, mv.getView().getClass());
    RedirectView successView = (RedirectView) mv.getView();
    assertTrue("Success view URL", successView.getUrl().indexOf("/workouts/week") > -1);
    // No errors
    assertTrue("Should have no errors", mv.getModel().get("errors") == null);
    Date weekDate = (Date) mv.getModel().get(Model.DATE);
    assertNotNull("date param should not be null", weekDate);
    GregorianCalendar cal = new GregorianCalendar();
    cal.set(2004, 6, 5);
    Date expectedDate = cal.getTime();
    assertEqualsToDay("Week date", expectedDate, weekDate);
    Long id = (Long) mv.getModel().get(Model.ID);
    assertEquals("Cyclist ID", cheryl.getId(), id);

    // Check database
    Workout workoutFromDB = workoutFactory.find(cherylsWorkout.getId());
    assertNotNull("Workout should be database", workoutFromDB);
    assertEquals("Workout notes", notes, workoutFromDB.getNotes());
    assertEquals("Workout owner", cheryl, workoutFromDB.getUser());
  }
  
  public void testEditOutsideCurrentDate() throws Exception {
    UserFactory userFactory = (UserFactory) getBeanFactory().getBean("userFactory");
    User cheryl = new User("cheryl", "Cheryl");
    String cherylPassword = "foo";
    cheryl.setPassword(cherylPassword);
    userFactory.save(cheryl);
    EquipmentFactory equipmentFactory = (EquipmentFactory) getBeanFactory().getBean("equipmentFactory");
    Equipment cherylsEquipment = new Equipment("", cheryl);
    equipmentFactory.save(cherylsEquipment);
    Week week = new Week();
    week.setEndDate(new Date());
    week.setStartDate(new Date());
    week.setUser(cheryl);
    Workout cherylsWorkout = new Workout(cheryl);
    cherylsWorkout.setWeek(week);
    cherylsWorkout.setDate(new Date());
    cherylsWorkout.setEquipment(cherylsEquipment);
    WorkoutFactory workoutFactory = (WorkoutFactory) getBeanFactory().getBean("workoutFactory");
    WeekFactory weekFactory = (WeekFactory) getBeanFactory().getBean("weekFactory");
    weekFactory.save(week);
    workoutFactory.save(cherylsWorkout);

    // log-in
    MockHttpServletRequest request = new MockHttpServletRequest(null, "POST", "/login/login");
    request.addParameter("username", "cheryl");
    request.addParameter("password", cherylPassword);
    HttpSession session = request.getSession();
    HttpServletResponse response = new MockHttpServletResponse();
    LoginController loginController = (LoginController) getBeanFactory().getBean("loginController");
    ModelAndView mv = loginController.handleRequest(request, response);
    
    assertNotNull("Cheryl should be logged-in", mv);
    assertNotNull("Redirect to Cheryl's workouts view", mv.getView());
    // Redirect
    RedirectView redirectView = (RedirectView) mv.getView();
    String urlWithoutRoot = redirectView.getUrl().replaceFirst("/cycling_log/dynamic", "");
    request = new MockHttpServletRequest(null, "GET", urlWithoutRoot);
    request.setSession(session);
    WorkoutsController workoutsController = (WorkoutsController) getBeanFactory().getBean("workoutsController");
    response = new MockHttpServletResponse();
    mv = workoutsController.handleRequest(request, response);
    
    // edit blank workout
    request = new MockHttpServletRequest(null, "GET", "/workouts/edit");
    request.setSession(session);
    request.addParameter("workout_id", cherylsWorkout.getId().toString());
    response = new MockHttpServletResponse();
    mv = workoutsController.handleRequest(request, response);

    assertNotNull("Cheryl's workout page", mv);
    assertEquals("Cheryl's workout page", "workouts/edit.html", mv.getViewName());

    String notes = "Cheryl's notes about her hike";
    request = new MockHttpServletRequest(null, "POST", "/workouts/update");
    request.setSession(session);
    request.addParameter("Save.x", "2");
    request.addParameter("workout_id", cherylsWorkout.getId().toString());
    request.addParameter("month", "7");
    request.addParameter("day", "6");
    request.addParameter("year", "2004");
    request.addParameter("workout_activity", "");
    request.addParameter("workout_intensity", "");
    request.addParameter("workout_focus", "");
    request.addParameter("workout_morale", "");
    request.addParameter("workout_equipment", cherylsEquipment.getId().toString());
    request.addParameter("workout_hours", "");
    request.addParameter("workout_minutes", "00");
    request.addParameter("workout_life", "");
    request.addParameter("workout_speed", "0.0");    
    request.addParameter("workout_weather" , "");
    request.addParameter("workout_distance", "0.0");    
    request.addParameter("workout_weight", "0.0");  
    request.addParameter("workout_publicNotes", "");
    request.addParameter("workout_notes", notes);
    response = new MockHttpServletResponse();
    // Save
    mv = workoutsController.handleRequest(request, response);
    
    // Should redirect 
    assertNotNull("Success ModelAndView should not be null", mv);
    assertNotNull("Success ModelAndView.view should not be null", mv.getView());
    assertEquals("Success view", RedirectView.class, mv.getView().getClass());
    RedirectView successView = (RedirectView) mv.getView();
    assertTrue("Success view URL", successView.getUrl().indexOf("/workouts/week") > -1);
    // No errors
    assertTrue("Should have no errors", mv.getModel().get("errors") == null);
    Date weekDate = (Date) mv.getModel().get(Model.DATE);
    assertNotNull("date param should not be null", weekDate);
    GregorianCalendar cal = new GregorianCalendar();
    cal.set(2004, 6, 5);
    Date expectedDate = cal.getTime();
    assertEqualsToDay("Week date", expectedDate, weekDate);
    Long id = (Long) mv.getModel().get(Model.ID);
    assertEquals("Cyclist ID", cheryl.getId(), id);
  }
  
  public void testAdd() throws Exception {
    UserFactory userFactory = (UserFactory) getBeanFactory().getBean("userFactory");
    User cheryl = new User("cheryl", "Cheryl");
    String cherylPassword = "foo";
    cheryl.setPassword(cherylPassword);
    userFactory.save(cheryl);
    EquipmentFactory equipmentFactory = (EquipmentFactory) getBeanFactory().getBean("equipmentFactory");
    Equipment cherylsEquipment = new Equipment("", cheryl);
    equipmentFactory.save(cherylsEquipment);
    Week cherylsWeek = new Week();
    Calendar cherylsWeekStartDate = Calendar.getInstance();
    cherylsWeekStartDate.set(2004, 10, 12);
    cherylsWeek.setStartDate(cherylsWeekStartDate.getTime());
    Calendar cherylsWeekEndDate = Calendar.getInstance();
    cherylsWeekEndDate.add(Calendar.DATE, 6);
    cherylsWeek.setEndDate(cherylsWeekEndDate.getTime());
    cherylsWeek.setUser(cheryl);
    Workout cherylsWorkout = new Workout(cheryl);
    cherylsWorkout.setWeek(cherylsWeek);
    cherylsWorkout.setDate(cherylsWeekStartDate.getTime());
    cherylsWorkout.setEquipment(cherylsEquipment);
    WorkoutFactory workoutFactory = (WorkoutFactory) getBeanFactory().getBean("workoutFactory");
    WeekFactory weekFactory = (WeekFactory) getBeanFactory().getBean("weekFactory");
    weekFactory.save(cherylsWeek);
    workoutFactory.save(cherylsWorkout);
    assertEquals("Cheryl's workout count", 1, totalWorkouts(cheryl));

    // log-in
    MockHttpServletRequest request = new MockHttpServletRequest(null, "POST", "/login/login");
    request.addParameter("username", "cheryl");
    request.addParameter("password", cherylPassword);
    HttpSession session = request.getSession();
    HttpServletResponse response = new MockHttpServletResponse();
    LoginController loginController = (LoginController) getBeanFactory().getBean("loginController");
    ModelAndView mv = loginController.handleRequest(request, response);
    
    assertNotNull("Cheryl should be logged-in", mv);
    assertNotNull("Redirect to Cheryl's workouts view", mv.getView());
    // Redirect
    RedirectView redirectView = (RedirectView) mv.getView();
    String urlWithoutRoot = redirectView.getUrl().replaceFirst("/cycling_log/dynamic", "");
    request = new MockHttpServletRequest(null, "GET", urlWithoutRoot);
    request.setSession(session);
    WorkoutsController workoutsController = (WorkoutsController) getBeanFactory().getBean("workoutsController");
    response = new MockHttpServletResponse();
    mv = workoutsController.handleRequest(request, response);
    assertEquals("Cheryl is user in session", cheryl, session.getAttribute("user"));
    assertEquals("Cheryl is cyclist in session", cheryl, session.getAttribute("cyclist"));
    assertEquals("Cheryl should be cyclist in view model", cheryl, mv.getModel().get("cyclist"));
    
    // original window -- Add workout for Cheryl
    request = new MockHttpServletRequest(null, "POST", "/workouts/home");
    request.addParameter("new", "New Workout");
    request.addParameter("cyclist", "cheryl");
    request.setSession(session);
    response = new MockHttpServletResponse();
    mv = workoutsController.handleRequest(request, response);

    assertNotNull("Cheryl's new workout page", mv);
    assertEquals("Cheryl's new workout page", "workouts/edit.html", mv.getViewName());
    WorkoutView cherylsNewWorkout = (WorkoutView) mv.getModel().get("workout");
    assertEquals("Cheryl should be cyclist in view model", cheryl, cherylsNewWorkout.getUser());
    DateSelector workoutDateSelector = (DateSelector) mv.getModel().get("workoutDateSelector");
    assertNotNull("Workout date selector", workoutDateSelector);

    // original window -- Cheryl's workout
    String notes = "Cheryl's notes about her hike";
    request = new MockHttpServletRequest(null, "POST", "/workouts/update");
    request.setSession(session);
    request.addParameter("Save.x", "2");
    request.addParameter("workout_id", "");
    cherylsWeekStartDate.setTime(cherylsWeek.getStartDate());
    request.addParameter("month", new Long(cherylsWeekStartDate.get(Calendar.MONTH) + 1).toString());
    request.addParameter("day", new Long(cherylsWeekStartDate.get(Calendar.DAY_OF_MONTH)  + 2).toString());
    request.addParameter("year", "" + cherylsWeekStartDate.get(Calendar.YEAR));
    request.addParameter("workout_activity", "");
    request.addParameter("workout_intensity", "");
    request.addParameter("workout_focus", "");
    request.addParameter("workout_morale", "");
    request.addParameter("workout_equipment", cherylsEquipment.getId().toString());
    request.addParameter("workout_hours", "");
    request.addParameter("workout_minutes", "00");
    request.addParameter("workout_life", "");
    request.addParameter("workout_speed", "0.0");    
    request.addParameter("workout_weather" , "");
    request.addParameter("workout_distance", "0.0");    
    request.addParameter("workout_weight", "0.0");  
    request.addParameter("workout_publicNotes", "");
    request.addParameter("workout_notes", notes);
    response = new MockHttpServletResponse();
    // Save
    mv = workoutsController.handleRequest(request, response);
    
    // Should redirect 
    assertNotNull("Success ModelAndView should not be null", mv);
    assertNotNull("Success ModelAndView.view should not be null", mv.getView());
    assertEquals("Success view", RedirectView.class, mv.getView().getClass());
    RedirectView successView = (RedirectView) mv.getView();
    assertTrue("Success view URL", successView.getUrl().indexOf("/workouts/week") > -1);
    // No errors
    assertTrue("Should have no errors", mv.getModel().get("errors") == null);
    Date weekDate = (Date) mv.getModel().get(Model.DATE);
    assertNotNull("weekDate", weekDate);
    GregorianCalendar cal = new GregorianCalendar();
    cal.set(cherylsWeekStartDate.get(Calendar.YEAR), cherylsWeekStartDate.get(Calendar.MONTH), cherylsWeekStartDate.get(Calendar.DATE));
    assertNotNull("date param should not be null", weekDate);
    Long id = (Long) mv.getModel().get(Model.ID);
    assertEquals("Cyclist ID", cheryl.getId(), id);

    // Check database
    Session hibernateSession = null;
    List workouts = null;
    try {
        SessionFactory sessionFactory = (SessionFactory) getBeanFactory().getBean("sessionFactory");
        hibernateSession = sessionFactory.openSession();
        workouts = hibernateSession.find(
              "from workout in class com.butlerpress.cyclinglog.Workout " +
              "where date >= ? and date <= ? " +
              "  and workout.user = ? " +
              "order by date asc",
              new Object[] { cherylsWeekStartDate, cherylsWeekEndDate, cheryl },
              new Type[] { Hibernate.CALENDAR, Hibernate.CALENDAR, Hibernate.entity(User.class) }
            );
    } finally {
        if (hibernateSession != null) hibernateSession.close();
    }

    for (Iterator iter = workouts.iterator(); iter.hasNext();) {
      Workout workoutFromDB = (Workout) iter.next();
      assertEquals("Workout owner", cheryl, workoutFromDB.getUser());
    }
    assertEquals("Cheryl's workouts in date range", 2, workouts.size());
    assertEquals("Cheryl's workout count", 2, totalWorkouts(cheryl));
  }

  public void testDifferentAdd() throws Exception {
    UserFactory userFactory = (UserFactory) getBeanFactory().getBean("userFactory");
    // Create 2 cyclists: Cheryl and Scout
    User cheryl = new User("cheryl", "Cheryl");
    String cherylPassword = "foo";
    cheryl.setPassword(cherylPassword);
    userFactory.save(cheryl);
    EquipmentFactory equipmentFactory = (EquipmentFactory) getBeanFactory().getBean("equipmentFactory");
    Equipment cherylsEquipment = new Equipment("", cheryl);
    equipmentFactory.save(cherylsEquipment);
    Week cherylsWeek = new Week();
    Calendar cherylsWeekStartDate = Calendar.getInstance();
    cherylsWeekStartDate.set(2004, 10, 12);
    cherylsWeek.setStartDate(cherylsWeekStartDate.getTime());
    Calendar cherylsWeekEndDate = Calendar.getInstance();
    cherylsWeekEndDate.add(Calendar.DATE, 6);
    cherylsWeek.setEndDate(cherylsWeekEndDate.getTime());
    cherylsWeek.setUser(cheryl);
    Workout cherylsWorkout = new Workout(cheryl);
    cherylsWorkout.setWeek(cherylsWeek);
    cherylsWorkout.setDate(cherylsWeekStartDate.getTime());
    cherylsWorkout.setEquipment(cherylsEquipment);
    WorkoutFactory workoutFactory = (WorkoutFactory) getBeanFactory().getBean("workoutFactory");
    WeekFactory weekFactory = (WeekFactory) getBeanFactory().getBean("weekFactory");
    weekFactory.save(cherylsWeek);
    workoutFactory.save(cherylsWorkout);

    User scout = new User("scout", "Scout");
    String scoutPassword = "bar";
    scout.setPassword(scoutPassword);
    userFactory.save(scout);
    Equipment scoutsEquipment = new Equipment("", scout);
    equipmentFactory.save(scoutsEquipment);
    Week scoutsWeek = new Week();
    scoutsWeek.setEndDate(cherylsWeekStartDate.getTime());
    scoutsWeek.setStartDate(cherylsWeekEndDate.getTime());
    scoutsWeek.setUser(scout);
    Workout scoutWorkout = new Workout(scout);
    scoutWorkout.setWeek(scoutsWeek);
    scoutWorkout.setDate(cherylsWeekStartDate.getTime());
    scoutWorkout.setEquipment(scoutsEquipment);
    weekFactory.save(scoutsWeek);
    workoutFactory.save(scoutWorkout);

    // log-in
    MockHttpServletRequest request = new MockHttpServletRequest(null, "POST", "/login/login");
    request.addParameter("username", "cheryl");
    request.addParameter("password", cherylPassword);
    HttpSession session = request.getSession();
    HttpServletResponse response = new MockHttpServletResponse();
    LoginController loginController = (LoginController) getBeanFactory().getBean("loginController");
    ModelAndView mv = loginController.handleRequest(request, response);
    
    assertNotNull("Cheryl should be logged-in", mv);
    assertNotNull("Redirect to Cheryl's workouts view", mv.getView());
    // Redirect
    RedirectView redirectView = (RedirectView) mv.getView();
    String urlWithoutRoot = redirectView.getUrl().replaceFirst("/cycling_log/dynamic", "");
    request = new MockHttpServletRequest(null, "GET", urlWithoutRoot);
    request.setSession(session);
    WorkoutsController workoutsController = (WorkoutsController) getBeanFactory().getBean("workoutsController");
    response = new MockHttpServletResponse();
    mv = workoutsController.handleRequest(request, response);
    assertEquals("Cheryl is user in session", cheryl, session.getAttribute("user"));
    assertEquals("Cheryl is cyclist in session", cheryl, session.getAttribute("cyclist"));
    assertEquals("Cheryl should be cyclist in view model", cheryl, mv.getModel().get("cyclist"));
    
    // new window with Scout
    request = new MockHttpServletRequest(null, "GET", "/login/home");
    request.setSession(session);
    response = new MockHttpServletResponse();
    mv = loginController.handleRequest(request, response);

    // Scout workouts
    request = new MockHttpServletRequest(null, "GET", "/workouts/home");
    request.addParameter("cyclist", "scout");
    request.setSession(session);
    response = new MockHttpServletResponse();
    mv = workoutsController.handleRequest(request, response);
    assertEquals("Scout should be cyclist in view model", scout, mv.getModel().get("cyclist"));

   // Look at scout workout
    request = new MockHttpServletRequest(null, "GET", "/workouts/edit");
    request.setSession(session);
    request.addParameter("workout_id", scoutWorkout.getId().toString());
    response = new MockHttpServletResponse();
    mv = workoutsController.handleRequest(request, response);

    assertNotNull("Scout's workout page", mv);
    assertEquals("Scout's workout page", "workouts/edit.html", mv.getViewName());
    WorkoutView actualWorkout = (WorkoutView) mv.getModel().get("workout");
    assertEquals("Scout's workout should be cyclist in view model", scoutWorkout.getId(), actualWorkout.getId());
    DateSelector workoutDateSelector = (DateSelector) mv.getModel().get("workoutDateSelector");
    assertNotNull("Workout date selector prefix", workoutDateSelector);

    // original window -- Add workout for Cheryl
    request = new MockHttpServletRequest(null, "POST", "/workouts/home");
    request.addParameter("new", "New Workout");
    request.addParameter("cyclist", "cheryl");
    request.setSession(session);
    response = new MockHttpServletResponse();
    mv = workoutsController.handleRequest(request, response);

    assertNotNull("Cheryl's new workout page", mv);
    assertEquals("Cheryl's new workout page", "workouts/edit.html", mv.getViewName());
    WorkoutView cherylsNewWorkout = (WorkoutView) mv.getModel().get("workout");
    assertEquals("Cheryl should be cyclist in view model", cheryl, cherylsNewWorkout.getUser());
    workoutDateSelector = (DateSelector) mv.getModel().get("workoutDateSelector");
    assertNotNull("Workout date selector", workoutDateSelector);

    // original window -- Cheryl's workout
    String notes = "Cheryl's notes about her hike";
    request = new MockHttpServletRequest(null, "POST", "/workouts/update");
    request.setSession(session);
    request.addParameter("Save.x", "2");
    request.addParameter("workout_id", "");
    cherylsWeekStartDate.setTime(cherylsWeek.getStartDate());
    request.addParameter("month", new Long(cherylsWeekStartDate.get(Calendar.MONTH) + 1).toString());
    request.addParameter("day", new Long(cherylsWeekStartDate.get(Calendar.DAY_OF_MONTH)  + 2).toString());
    request.addParameter("year", "" + cherylsWeekStartDate.get(Calendar.YEAR));
    request.addParameter("workout_activity", "");
    request.addParameter("workout_intensity", "");
    request.addParameter("workout_focus", "");
    request.addParameter("workout_morale", "");
    request.addParameter("workout_equipment", cherylsEquipment.getId().toString());
    request.addParameter("workout_hours", "");
    request.addParameter("workout_minutes", "00");
    request.addParameter("workout_life", "");
    request.addParameter("workout_speed", "0.0");    
    request.addParameter("workout_weather" , "");
    request.addParameter("workout_distance", "0.0");    
    request.addParameter("workout_weight", "0.0");  
    request.addParameter("workout_publicNotes", "");
    request.addParameter("workout_notes", notes);
    response = new MockHttpServletResponse();
    // Save
    mv = workoutsController.handleRequest(request, response);
    
    // Should redirect 
    assertNotNull("Success ModelAndView should not be null", mv);
    assertNotNull("Success ModelAndView.view should not be null", mv.getView());
    assertEquals("Success view", RedirectView.class, mv.getView().getClass());
    RedirectView successView = (RedirectView) mv.getView();
    assertTrue("Success view URL", successView.getUrl().indexOf("/workouts/week") > -1);
    // No errors
    assertTrue("Should have no errors", mv.getModel().get("errors") == null);
    Date weekDate = (Date) mv.getModel().get(Model.DATE);
    assertNotNull("date param should not be null", weekDate);
    GregorianCalendar cal = new GregorianCalendar();
    cal.set(cherylsWeekStartDate.get(Calendar.YEAR), cherylsWeekStartDate.get(Calendar.MONTH), cherylsWeekStartDate.get(Calendar.DATE));
    Date expectedDate = cal.getTime();
    assertEqualsToDay("Week date", expectedDate, weekDate);
    Long id = (Long) mv.getModel().get(Model.ID);
    assertEquals("Cyclist ID", cheryl.getId(), id);

    // Check database
    Session hibernateSession = null;
    List workouts = null;
    try {
        SessionFactory sessionFactory = (SessionFactory) getBeanFactory().getBean("sessionFactory");
        hibernateSession = sessionFactory.openSession();
        workouts = hibernateSession.find(
              "from workout in class com.butlerpress.cyclinglog.Workout " +
              "where date >= ? and date <= ? " +
              "  and workout.user = ? " +
              "order by date asc",
              new Object[] { cherylsWeekStartDate, cherylsWeekEndDate, cheryl },
              new Type[] { Hibernate.CALENDAR, Hibernate.CALENDAR, Hibernate.entity(User.class) }
            );
    } finally {
        if (hibernateSession != null) hibernateSession.close();
    }

    for (Iterator iter = workouts.iterator(); iter.hasNext();) {
      Workout workoutFromDB = (Workout) iter.next();
      assertEquals("Workout owner", cheryl, workoutFromDB.getUser());
    }
    assertEquals("Cheryl's workouts in date range", 2, workouts.size());
  
    try {
        SessionFactory sessionFactory = (SessionFactory) getBeanFactory().getBean("sessionFactory");
        hibernateSession = sessionFactory.openSession();
        workouts = hibernateSession.find(
              "from workout in class com.butlerpress.cyclinglog.Workout " +
              "where date >= ? and date <= ? " +
              "  and workout.user = ? " +
              "order by date asc",
              new Object[] { cherylsWeekStartDate, cherylsWeekEndDate, scout },
              new Type[] { Hibernate.CALENDAR, Hibernate.CALENDAR, Hibernate.entity(User.class) }
            );
    } finally {
        if (hibernateSession != null) hibernateSession.close();
    }

    for (Iterator iter = workouts.iterator(); iter.hasNext();) {
      Workout workoutFromDB = (Workout) iter.next();
      assertEquals("Workout owner", scout, workoutFromDB.getUser());
    }
    assertEquals("Scout's workouts in date range", 1, workouts.size());

    assertEquals("Cheryl's workout count", 2, totalWorkouts(cheryl));
    assertEquals("Scout's workout count", 1, totalWorkouts(scout));
  }

  public void testWeek() throws Exception {
    createCyclistWorkoutWeek("fausto");
    String username = "eddy";
    Workout workout = createCyclistWorkoutWeek(username);
    User user = workout.getUser();
    createCyclistWorkoutWeek("gino");

    // log-in
    MockHttpServletRequest request = new MockHttpServletRequest(null, "POST", "/login/login");
    request.addParameter("username", username);
    request.addParameter("password", workout.getUser().getPassword());
    HttpSession session = request.getSession();
    HttpServletResponse response = new MockHttpServletResponse();
    LoginController loginController = (LoginController) getBeanFactory().getBean("loginController");
    ModelAndView mv = loginController.handleRequest(request, response);
    
    assertNotNull(username + " should be logged-in", mv);
    assertNotNull("Redirect to " + username + "'s workouts view", mv.getView());
    
    request = new MockHttpServletRequest(null, "GET", "/workouts/week");
    request.setSession(session);
    Week week = workout.getWeek();
    request.addParameter(Model.WEEK + "id", new Long(week.getId()).toString());
    response = new MockHttpServletResponse();
    WorkoutsController workoutsController = (WorkoutsController) getBeanFactory().getBean("workoutsController");
    mv = workoutsController.handleRequest(request, response);
    assertNotNull("Workouts page", mv);
    assertEquals("Workouts page", "workouts/index.html", mv.getViewName());
    WeekView actualWeek = (WeekView) mv.getModel().get("week");
    assertEquals("Week should be cyclist in view model", week.getId(), actualWeek.getId());
    DateSelector weekDateSelector = (DateSelector) mv.getModel().get("weekDateSelector");
    assertNotNull("Week date selector", weekDateSelector);
    
    // Go to another date
    request = new MockHttpServletRequest(null, "GET", "/workouts/home");
    request.setSession(session);
    request.addParameter("day", "5");
    request.addParameter("month", "4");
    request.addParameter("year", "2000");
    request.addParameter("cyclist", workout.getUser().getName());
    response = new MockHttpServletResponse();
    workoutsController = (WorkoutsController) getBeanFactory().getBean("workoutsController");
    mv = workoutsController.handleRequest(request, response);
    assertNotNull("Workouts page", mv);
    assertEquals("Workouts page", "workouts/index.html", mv.getViewName());
    WeekView weekView = (WeekView) mv.getModel().get("week");
    String expectedStartDate = "Apr 3, 2000";
    assertEquals("Week start date", expectedStartDate, weekView.getStartDate("MMM d, yyyy"));
    
    // Year bounday
    request = new MockHttpServletRequest(null, "GET", "/workouts/home");
    request.setSession(session);
    request.addParameter("day", "31");
    request.addParameter("month", "12");
    request.addParameter("year", "1999");
    request.addParameter("cyclist", workout.getUser().getName());
    response = new MockHttpServletResponse();
    workoutsController = (WorkoutsController) getBeanFactory().getBean("workoutsController");
    mv = workoutsController.handleRequest(request, response);
    assertNotNull("Workouts page", mv);
    assertEquals("Workouts page", "workouts/index.html", mv.getViewName());
    weekView = (WeekView) mv.getModel().get("week");
    expectedStartDate = "Dec 27, 1999";
    assertEquals("Week start date", expectedStartDate, weekView.getStartDate("MMM d, yyyy"));
    
    // Year bounday
    request = new MockHttpServletRequest(null, "GET", "/workouts/home");
    request.setSession(session);
    request.addParameter("day", "1");
    request.addParameter("month", "1");
    request.addParameter("year", "2000");
    request.addParameter("cyclist", workout.getUser().getName());
    response = new MockHttpServletResponse();
    workoutsController = (WorkoutsController) getBeanFactory().getBean("workoutsController");
    mv = workoutsController.handleRequest(request, response);
    assertNotNull("Workouts page", mv);
    assertEquals("Workouts page", "workouts/index.html", mv.getViewName());
    weekView = (WeekView) mv.getModel().get("week");
    expectedStartDate = "Dec 27, 1999";
    assertEquals("Week start date", expectedStartDate, weekView.getStartDate("MMM d, yyyy"));
    
    // Year bounday
    request = new MockHttpServletRequest(null, "GET", "/workouts/home");
    request.setSession(session);
    request.addParameter("day", "3");
    request.addParameter("month", "1");
    request.addParameter("year", "1999");
    request.addParameter("cyclist", workout.getUser().getName());
    response = new MockHttpServletResponse();
    workoutsController = (WorkoutsController) getBeanFactory().getBean("workoutsController");
    mv = workoutsController.handleRequest(request, response);
    assertNotNull("Workouts page", mv);
    assertEquals("Workouts page", "workouts/index.html", mv.getViewName());
    weekView = (WeekView) mv.getModel().get("week");
    expectedStartDate = "Dec 28, 1998";
    assertEquals("Week start date", expectedStartDate, weekView.getStartDate("MMM d, yyyy"));
    
    // Year bounday
    request = new MockHttpServletRequest(null, "GET", "/workouts/home");
    request.setSession(session);
    request.addParameter("day", "4");
    request.addParameter("month", "1");
    request.addParameter("year", "1999");
    request.addParameter("cyclist", workout.getUser().getName());
    response = new MockHttpServletResponse();
    workoutsController = (WorkoutsController) getBeanFactory().getBean("workoutsController");
    mv = workoutsController.handleRequest(request, response);
    assertNotNull("Workouts page", mv);
    assertEquals("Workouts page", "workouts/index.html", mv.getViewName());
    weekView = (WeekView) mv.getModel().get("week");
    expectedStartDate = "Jan 4, 1999";
    assertEquals("Week start date", expectedStartDate, weekView.getStartDate("MMM d, yyyy"));
  }

  public void testWeekByDate() throws Exception {
    createCyclistWorkoutWeek("fausto");
    String username = "eddy";
    Workout workout = createCyclistWorkoutWeek(username);
    User user = workout.getUser();
    createCyclistWorkoutWeek("gino");

    // log-in
    MockHttpServletRequest request = new MockHttpServletRequest(null, "POST", "/login/login");
    request.addParameter("username", username);
    request.addParameter("password", workout.getUser().getPassword());
    HttpSession session = request.getSession();
    HttpServletResponse response = new MockHttpServletResponse();
    LoginController loginController = (LoginController) getBeanFactory().getBean("loginController");
    ModelAndView mv = loginController.handleRequest(request, response);
    
    assertNotNull(username + " should be logged-in", mv);
    assertNotNull("Redirect to " + username + "'s workouts view", mv.getView());
    
    request = new MockHttpServletRequest(null, "GET", "/workouts/week");
    request.setSession(session);
    Week week = workout.getWeek();
    request.addParameter(Model.WEEK + "id", new Long(week.getId()).toString());
    response = new MockHttpServletResponse();
    WorkoutsController workoutsController = (WorkoutsController) getBeanFactory().getBean("workoutsController");
    mv = workoutsController.handleRequest(request, response);
    assertNotNull("Workouts page", mv);
    assertEquals("Workouts page", "workouts/index.html", mv.getViewName());
    WeekView actualWeek = (WeekView) mv.getModel().get("week");
    assertEquals("Week should be cyclist in view model", week.getId(), actualWeek.getId());
    DateSelector weekDateSelector = (DateSelector) mv.getModel().get("weekDateSelector");
    assertNotNull("Week date selector", weekDateSelector);
    
    // Go to another date
    request = new MockHttpServletRequest(null, "GET", "/workouts/week");
    request.setSession(session);
    request.addParameter("date", "2000-04-05");
    request.addParameter("cyclist", workout.getUser().getId().toString());
    response = new MockHttpServletResponse();
    workoutsController = (WorkoutsController) getBeanFactory().getBean("workoutsController");
    mv = workoutsController.handleRequest(request, response);
    assertNotNull("Workouts page", mv);
    assertEquals("Workouts page", "workouts/index.html", mv.getViewName());
    WeekView weekView = (WeekView) mv.getModel().get("week");
    String expectedStartDate = "Apr 3, 2000";
    assertEquals("Week start date", expectedStartDate, weekView.getStartDate("MMM d, yyyy"));
    assertEquals("Cyclist ID", user, mv.getModel().get("cyclist"));
    
    // Year bounday
    request = new MockHttpServletRequest(null, "GET", "/workouts/home");
    request.setSession(session);
    request.addParameter("date", "1999-12-31");
    request.addParameter("cyclist", workout.getUser().getName());
    response = new MockHttpServletResponse();
    workoutsController = (WorkoutsController) getBeanFactory().getBean("workoutsController");
    mv = workoutsController.handleRequest(request, response);
    assertNotNull("Workouts page", mv);
    assertEquals("Workouts page", "workouts/index.html", mv.getViewName());
    weekView = (WeekView) mv.getModel().get("week");
    expectedStartDate = "Dec 27, 1999";
    assertEquals("Week start date", expectedStartDate, weekView.getStartDate("MMM d, yyyy"));
    assertEquals("Cyclist ID", user, mv.getModel().get("cyclist"));
    
    // Year bounday
    request = new MockHttpServletRequest(null, "GET", "/workouts/home");
    request.setSession(session);
    request.addParameter("date", "2000-01-01");
    request.addParameter("cyclist", workout.getUser().getName());
    response = new MockHttpServletResponse();
    workoutsController = (WorkoutsController) getBeanFactory().getBean("workoutsController");
    mv = workoutsController.handleRequest(request, response);
    assertNotNull("Workouts page", mv);
    assertEquals("Workouts page", "workouts/index.html", mv.getViewName());
    weekView = (WeekView) mv.getModel().get("week");
    expectedStartDate = "Dec 27, 1999";
    assertEquals("Week start date", expectedStartDate, weekView.getStartDate("MMM d, yyyy"));
    assertEquals("Cyclist ID", user, mv.getModel().get("cyclist"));
    
    // Year bounday
    request = new MockHttpServletRequest(null, "GET", "/workouts/home");
    request.setSession(session);
    request.addParameter("date", "1999-01-03");
    request.addParameter("cyclist", workout.getUser().getName());
    response = new MockHttpServletResponse();
    workoutsController = (WorkoutsController) getBeanFactory().getBean("workoutsController");
    mv = workoutsController.handleRequest(request, response);
    assertNotNull("Workouts page", mv);
    assertEquals("Workouts page", "workouts/index.html", mv.getViewName());
    weekView = (WeekView) mv.getModel().get("week");
    expectedStartDate = "Dec 28, 1998";
    assertEquals("Week start date", expectedStartDate, weekView.getStartDate("MMM d, yyyy"));
    assertEquals("Cyclist ID", user, mv.getModel().get("cyclist"));
    
    // Year bounday
    request = new MockHttpServletRequest(null, "GET", "/workouts/home");
    request.setSession(session);
    request.addParameter("date", "1999-01-04");
    request.addParameter("cyclist", workout.getUser().getName());
    response = new MockHttpServletResponse();
    workoutsController = (WorkoutsController) getBeanFactory().getBean("workoutsController");
    mv = workoutsController.handleRequest(request, response);
    assertNotNull("Workouts page", mv);
    assertEquals("Workouts page", "workouts/index.html", mv.getViewName());
    weekView = (WeekView) mv.getModel().get("week");
    expectedStartDate = "Jan 4, 1999";
    assertEquals("Week start date", expectedStartDate, weekView.getStartDate("MMM d, yyyy"));
    assertEquals("Cyclist ID", user, mv.getModel().get("cyclist"));
  }
  
  int totalWorkouts(User user) throws Exception {
    Session hibernateSession = null;
    try {
        SessionFactory sessionFactory = (SessionFactory) getBeanFactory().getBean("sessionFactory");
        hibernateSession = sessionFactory.openSession();
        List workouts = hibernateSession.find(
              "from workout in class com.butlerpress.cyclinglog.Workout " +
              "where workout.user = ?",
              user,
              Hibernate.entity(User.class)
            );
        return workouts.size();
    } finally {
        if (hibernateSession != null) hibernateSession.close();
    }
  }
}
