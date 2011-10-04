package com.butlerpress.cyclinglog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class WeekControllerTestCase extends TestCase {

  public void testScroll() throws Exception {
    UserFactory userFactory = (UserFactory) getBeanFactory().getBean("userFactory");
    WeekFactory weekFactory = (WeekFactory) getBeanFactory().getBean("weekFactory");

    // Cyclist
    User cyclist = new User("cyclist", "Test Cyclist");
    userFactory.save(cyclist);
    assertNotNull(cyclist.getName() + " should be saved to database (id != null)", cyclist.getId());
      
    // Pre-populate 10 weeks in past to 20 in future
    Calendar cal = new GregorianCalendar();
    cal.setTime(Week.getStart(new Date()));
    cal.add(Calendar.WEEK_OF_YEAR, -10);
    for (int i = 0; i < 30; i++) {
      Week week = weekFactory.get(cal.getTime(), cyclist);
      weekFactory.save(week);
      cal.add(Calendar.WEEK_OF_YEAR, 1);
    }
    
    // Show page
    MockHttpServletRequest request = new MockHttpServletRequest(null, "GET", "/weeks/home");
    HttpSession session = request.getSession();
    session.setAttribute("cyclist", cyclist);
    session.setAttribute("user", cyclist);
    HttpServletResponse response = new MockHttpServletResponse();
    WeeksController controller = (WeeksController) getBeanFactory().getBean("weeksController");
    ModelAndView mv = controller.handleRequest(request, response);
    Map model = mv.getModel();
    
    DateSelector dateSelector = (DateSelector) model.get("weekDateSelector");
    assertNotNull("DateSelector should be in model", dateSelector);
    SimpleDateFormat format = new SimpleDateFormat("M/d/yyyy");
    String expectedDate = format.format(Week.getStart(new Date()));
    assertEquals("DateSelector date", expectedDate, dateSelector.getFormattedDate());
    
    String weeksCount = (String) model.get("weeksCount");
    assertNotNull("weeksCount should be in model", weeksCount);
    assertEquals("weeksCount", "13", weeksCount);
    List weeks = (List) model.get("weeks");
    assertNotNull("weeks should be in model", weeks);
    assertEquals("Weeks size", 13, weeks.size());
    WeekView firstWeek = (WeekView) weeks.get(0);
    Week expectedFirstWeek = weekFactory.get(new Date(), cyclist);
    expectedDate = format.format(expectedFirstWeek.getStartDate());
    assertEquals("First week ", expectedDate, firstWeek.getStartDate("M/d/yyyy"));
    
    // Pre-populate weeks from 10/1/2008 to 4/1/2009
    cal = new GregorianCalendar();
    cal.set(2008, 9, 1);
    for (int i = 0; i < 40; i++) {
      Week week = weekFactory.get(cal.getTime(), cyclist);
      weekFactory.save(week);
      cal.add(Calendar.WEEK_OF_YEAR, 1);
    }

    // Date selector
    request = new MockHttpServletRequest(null, "GET", "/weeks/home");
    request.addParameter("weeksCount", "6");
    request.addParameter("cyclist", "cyclist");
    request.addParameter("month", "10");
    request.addParameter("day", "17");
    request.addParameter("year", "2008");
    response = new MockHttpServletResponse();
    mv = controller.handleRequest(request, response);
    
    model = mv.getModel();
    
    dateSelector = (DateSelector) model.get("weekDateSelector");
    assertNotNull("DateSelector should be in model", dateSelector);
    assertEquals("DateSelector date", "10/13/2008", dateSelector.getFormattedDate());
    
    weeks = (List) model.get("weeks");
    assertNotNull("weeks should be in model", weeks);
    assertEquals("Weeks size", 6, weeks.size());
    
    firstWeek = (WeekView) weeks.get(0);
    assertEquals("First week start date","10/13/2008", firstWeek.getStartDate("M/d/yyyy"));
    
    WeekView lastWeek = (WeekView) weeks.get(5);
    assertEquals("Last week start date", "11/17/2008", lastWeek.getStartDate("M/d/yyyy"));
    
    // Scroll down 
    request = new MockHttpServletRequest(null, "GET", "/weeks/home");
    request.addParameter("scrollDown.x", "12");
    request.addParameter("weeksCount", "6");
    request.addParameter("cyclist", "cyclist");
    request.addParameter("month", "10");
    request.addParameter("day", "13");
    request.addParameter("year", "2008");
    response = new MockHttpServletResponse();
    mv = controller.handleRequest(request, response);
    
    model = mv.getModel();
    dateSelector = (DateSelector) model.get("weekDateSelector");
    assertNotNull("DateSelector should be in model", dateSelector);
    assertEquals("DateSelector date", "11/17/2008", dateSelector.getFormattedDate());
    
    weeks = (List) model.get("weeks");
    assertNotNull("weeks should be in model", weeks);
    assertEquals("Weeks size", 6, weeks.size());
    
    firstWeek = (WeekView) weeks.get(0);
    assertEquals("First week start date", "11/17/2008", firstWeek.getStartDate("M/d/yyyy"));
    
    lastWeek = (WeekView) weeks.get(5);
    assertEquals("Last week start date", "12/22/2008", lastWeek.getStartDate("M/d/yyyy"));
    
    // Scroll down again
    request = new MockHttpServletRequest(null, "GET", "/weeks/home");
    request.addParameter("scrollDown.x", "12");
    request.addParameter("weeksCount", "6");
    request.addParameter("cyclist", "cyclist");
    request.addParameter("month", "11");
    request.addParameter("day", "17");
    request.addParameter("year", "2008");
    response = new MockHttpServletResponse();
    mv = controller.handleRequest(request, response);
    
    model = mv.getModel();
    dateSelector = (DateSelector) model.get("weekDateSelector");
    assertNotNull("DateSelector should be in model", dateSelector);
    assertEquals("DateSelector date", "12/22/2008", dateSelector.getFormattedDate());
    
    weeks = (List) model.get("weeks");
    assertNotNull("weeks should be in model", weeks);
    assertEquals("Weeks size", 6, weeks.size());
    
    firstWeek = (WeekView) weeks.get(0);
    assertEquals("First week start date", "12/22/2008", firstWeek.getStartDate("M/d/yyyy"));
    
    lastWeek = (WeekView) weeks.get(5);
    assertEquals("Last week start date", "1/26/2009", lastWeek.getStartDate("M/d/yyyy"));
  }
  
  public void testAddWeeks() throws Exception {
    UserFactory userFactory = (UserFactory) getBeanFactory().getBean("userFactory");

    // Cyclist
    User cyclist = new User("cyclist", "Test Cyclist");
    userFactory.save(cyclist);
    assertNotNull(cyclist.getName() + " should be saved to database (id != null)", cyclist.getId());

    // Add week
    Calendar cal = Calendar.getInstance();
    cal.set(2001, 11, 17, 0, 0, 0);
    Date startDate = cal.getTime();
    cal.add(Calendar.DATE, 6);
    Date endDate = cal.getTime();
    Week week = new Week();
    week.setUser(cyclist);
    week.setStartDate(startDate);
    week.setEndDate(endDate);
    WeekFactory weekFactory = (WeekFactory) getBeanFactory().getBean("weekFactory");
    weekFactory.save(week);
    assertEquals("Week should exist", week, weekFactory.get(startDate, cyclist.getUsername()));
    assertEquals("Week should exist", week, weekFactory.get(endDate, cyclist.getUsername()));

    Workout workout = new Workout(cyclist);
    String activity = Activity.MTB.getName();
    workout.setActivity(activity);
    Date date = new Date();
    workout.setDate(date);
    EquipmentFactory equipmentFactory = (EquipmentFactory) getBeanFactory().getBean("equipmentFactory");
    Equipment equipment = new Equipment("Anvil", cyclist);
    equipmentFactory.save(equipment);
    workout.setEquipment(equipment);
    String notes = "These are some notes.";
    workout.setNotes(notes);
    String publicNotes = "Visible to all";
    workout.setPublicNotes(publicNotes);
    workout.setWeek(week);
    WorkoutFactory workoutFactory = (WorkoutFactory) getBeanFactory().getBean("workoutFactory");
    workoutFactory.save(workout);
    
    // Show page
    MockHttpServletRequest request = new MockHttpServletRequest(null, "GET", "/weeks/home");
    HttpSession session = request.getSession();
    session.setAttribute("cyclist", cyclist);
    session.setAttribute("user", cyclist);
    HttpServletResponse response = new MockHttpServletResponse();
    WeeksController controller = (WeeksController) getBeanFactory().getBean("weeksController");
    ModelAndView mv = controller.handleRequest(request, response);

    // Add new weeks
    request = new MockHttpServletRequest(null, "POST", "/weeks/home");
    // Re-use session with logged-in user
    request.setSession(session);
    request.addParameter("add.x", "12");
    request.addParameter("newWeeksQuantity", "7");
    response = new MockHttpServletResponse();
    mv = controller.handleRequest(request, response);

    // Check database
    List weeks = weekFactory.get(startDate, 12, cyclist);
    assertEquals("Weeks in database", 8, weeks.size());
    
    // Should redirect
    assertNotNull("Model and view", mv);
    assertNotNull("View should not be null", mv.getView());
    assertEquals("Success view", RedirectView.class, mv.getView().getClass());
    RedirectView successView = (RedirectView) mv.getView();
    assertTrue("Success view URL", successView.getUrl().indexOf("/weeks/home") > -1);
    // No errors
    assertTrue("Should have no errors", mv.getModel().get("errors") == null);
  }

  public void testHomeNoCyclist() throws Exception {
    // Show form
    MockHttpServletRequest request = new MockHttpServletRequest(null, "GET", "/weeks/home");
    HttpServletResponse response = new MockHttpServletResponse();
    WeeksController controller = (WeeksController) getBeanFactory().getBean("weeksController");

    // No cyclist -- should redirect
    ModelAndView mv = controller.handleRequest(request, response);

    assertNotNull("Should return ModelAndView ", mv);
    assertNotNull("Should be redirecting to a view ", mv.getView());
    assertEquals("Login view", RedirectView.class, mv.getView().getClass());
    RedirectView view = (RedirectView) mv.getView();
    assertTrue("Login view URL", view.getUrl().indexOf("/dynamic/login/home") > -1);
  }

  public void testAddNoCyclist() throws Exception {
    UserFactory userFactory = (UserFactory) getBeanFactory().getBean("userFactory");

    // Cyclist
    User cyclist = new User("cyclist", "Test Cyclist");
    userFactory.save(cyclist);
    assertNotNull(cyclist.getName() + " should be saved to database (id != null)", cyclist.getId());

    // Show form
    MockHttpServletRequest request = new MockHttpServletRequest(null, "GET", "/weeks/home");
    HttpSession session = request.getSession();
    session.setAttribute("user", cyclist);
    request.addParameter("add.x", "12");
    request.addParameter("newWeeksQuantity", "7");
    HttpServletResponse response = new MockHttpServletResponse();
    WeeksController controller = (WeeksController) getBeanFactory().getBean("weeksController");

    // No cyclist -- should redirect
    ModelAndView mv = controller.handleRequest(request, response);

    assertNotNull("Should return ModelAndView ", mv);
    assertNotNull("Should be redirecting to a view ", mv.getView());
    assertEquals("Login view", RedirectView.class, mv.getView().getClass());
    RedirectView view = (RedirectView) mv.getView();
    assertTrue("Login view URL", view.getUrl().indexOf("/dynamic/login/home") > -1);
  }

  public void testAddNoUser() throws Exception {
    UserFactory userFactory = (UserFactory) getBeanFactory().getBean("userFactory");

    // Cyclist
    User cyclist = new User("cyclist", "Test Cyclist");
    userFactory.save(cyclist);
    assertNotNull(cyclist.getName() + " should be saved to database (id != null)", cyclist.getId());

    // Show form
    MockHttpServletRequest request = new MockHttpServletRequest(null, "GET", "/weeks/home");
    HttpSession session = request.getSession();
    session.setAttribute("cyclist", cyclist);
    request.addParameter("add.x", "12");
    request.addParameter("newWeeksQuantity", "7");
    HttpServletResponse response = new MockHttpServletResponse();
    WeeksController controller = (WeeksController) getBeanFactory().getBean("weeksController");

    // No cyclist -- should redirect
    ModelAndView mv = controller.handleRequest(request, response);

    assertNotNull("Should return ModelAndView ", mv);
    assertNotNull("Should be redirecting to a view ", mv.getView());
    assertEquals("Login view", RedirectView.class, mv.getView().getClass());
    RedirectView view = (RedirectView) mv.getView();
    assertTrue("Login view URL", view.getUrl().indexOf("/dynamic/login/home") > -1);
  }
}
