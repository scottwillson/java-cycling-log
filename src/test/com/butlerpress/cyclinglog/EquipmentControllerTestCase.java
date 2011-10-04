package com.butlerpress.cyclinglog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class EquipmentControllerTestCase extends TestCase {

  public void testUpdateEquipment() throws Exception {
    // Tighter test could use mock UserFactory to remove database dependency
    UserFactory userFactory = (UserFactory) getBeanFactory().getBean("userFactory");
    // Create cyclist with equipment
    User user = new User("test", "Test User");
    new Equipment("", user);
    Equipment e2 = new Equipment("Adidas", user);
    new Equipment("Vanilla", user);
    userFactory.save(user);
    assertNotNull(e2.getName() + " should be saved to database (id != null)", e2.getId());
    
    // Show form
    MockHttpServletRequest request = new MockHttpServletRequest(null, "GET", "/dynamic/equipment/home");
    HttpSession session = request.getSession();
    session.setAttribute("cyclist", user);
    session.setAttribute("user", user);
    HttpServletResponse response = new MockHttpServletResponse();
    EquipmentController controller = (EquipmentController) getBeanFactory().getBean("equipmentController");
    ModelAndView mv = controller.handleRequest(request, response);

    request = new MockHttpServletRequest(null, "POST", "/dynamic/equipment/home");
    // Re-use session with logged-in user
    request.setSession(session);
    // Update Vanilla
    String updatedName = "New Balance";
    request.addParameter("cyclist.id", user.getId().toString());
    request.addParameter("equipment[1].name", e2.getName());
    request.addParameter("equipment[2].name", updatedName);
    request.addParameter("save.x", "2");
    response = new MockHttpServletResponse();
    mv = controller.handleRequest(request, response);
    
    // Should redirect 
    assertEquals("Success view", RedirectView.class, mv.getView().getClass());
    RedirectView successView = (RedirectView) mv.getView();
    assertTrue("Success view URL", successView.getUrl().indexOf("/dynamic/equipment/home") > -1);
    // No errors
    assertTrue("Should have no errors", mv.getModel().get("errors") == null);

		// Check database
		user = userFactory.find(user.getUsername());
		List equipment = new ArrayList(user.getEquipment());
		Collections.sort(equipment);
		assertEquals("Equipment count", 3, equipment.size());
		String[] expectedNames = new String[] {"", "Adidas", "New Balance"};
		for (int i = 0; i < equipment.size(); i++) {
			Equipment e = (Equipment) equipment.get(i);
			assertEquals("Equipment name", expectedNames[i], e.getName());
		}
  }

  public void testAddEquipment() throws Exception {
    // Tighter test could use mock UserFactory to remove database dependency
    UserFactory userFactory = (UserFactory) getBeanFactory().getBean("userFactory");
    // Create cyclist with equipment
    User user = new User("test", "Test User");
    new Equipment("", user);
    Equipment e2 = new Equipment("Adidas", user);
    Equipment e3 = new Equipment("Vanilla", user);
    userFactory.save(user);
    assertNotNull(e2.getName() + " should be saved to database (id != null)", e2.getId());

    // Show form
    MockHttpServletRequest request = new MockHttpServletRequest(null, "GET", "/dynamic/equipment/home");
    HttpSession session = request.getSession();
    session.setAttribute("cyclist", user);
    session.setAttribute("user", user);
    HttpServletResponse response = new MockHttpServletResponse();
    EquipmentController controller = (EquipmentController) getBeanFactory().getBean("equipmentController");
    ModelAndView mv = controller.handleRequest(request, response);

    request = new MockHttpServletRequest(null, "POST", "/dynamic/equipment/home");
    // Re-use session with logged-in user
    request.setSession(session);
    // No updates
    request.addParameter("cyclist.id", user.getId().toString());
    request.addParameter("equipment[1].name", e2.getName());
    request.addParameter("equipment[2].name", e3.getName());
    String newName = "New Equipment";
    request.addParameter("newEquipment.name", newName);
    request.addParameter("save.x", "2");
    response = new MockHttpServletResponse();
    mv = controller.handleRequest(request, response);

    // Should redirect
    assertNotNull("Success view should not be null. ModelAndView: " + mv, mv.getView());
    assertEquals("Success view", RedirectView.class, mv.getView().getClass());
    RedirectView successView = (RedirectView) mv.getView();
    assertTrue("Success view URL", successView.getUrl().indexOf("/dynamic/equipment/home") > -1);
    String expected = "/dynamic/equipment/home";
    assertTrue("Success view URL should include '" + expected + "', but was '" + successView.getUrl(),
        successView.getUrl().indexOf(expected) > -1);
    String notExpected = "?workout_date=";
    // Bug fix test: check model not in redirect view
    assertTrue("Success view URL should not include '" + notExpected + "', but was '" + successView.getUrl(),
        successView.getUrl().indexOf(notExpected) <= -1);

	// Check database
	user = userFactory.find(user.getUsername());
	List equipment = new ArrayList(user.getEquipment());
	Collections.sort(equipment);
	assertEquals("Equipment count", 4, equipment.size());
	String[] expectedNames = new String[] {"", "Adidas", newName, "Vanilla"};
	for (int i = 0; i < equipment.size(); i++) {
		Equipment e = (Equipment) equipment.get(i);
		assertEquals("Equipment name", expectedNames[i], e.getName());
	}
  }

  public void testDeleteEquipment() throws Exception {
    // Tighter test could use mock UserFactory to remove database dependency
    UserFactory userFactory = (UserFactory) getBeanFactory().getBean("userFactory");
    // Create cyclist with equipment
    User user = new User("test", "Test User");
    new Equipment("", user);
    Equipment e2 = new Equipment("Adidas", user);
    Equipment e3 = new Equipment("Vanilla", user);
    userFactory.save(user);
    assertNotNull(e2.getName() + " should be saved to database (id != null)", e2.getId());

    // Show form
    MockHttpServletRequest request = new MockHttpServletRequest(null, "GET", "/dynamic/equipment/home");
    HttpSession session = request.getSession();
    session.setAttribute("cyclist", user);
    session.setAttribute("user", user);
    HttpServletResponse response = new MockHttpServletResponse();
    EquipmentController controller = (EquipmentController) getBeanFactory().getBean("equipmentController");
    ModelAndView mv = controller.handleRequest(request, response);

    request = new MockHttpServletRequest(null, "POST", "/dynamic/equipment/home");
    // Re-use session with logged-in user
    request.setSession(session);
    // No updates
    request.addParameter("cyclist.id", user.getId().toString());
    request.addParameter("equipment[1].name", e2.getName());
    request.addParameter("equipment[2].name", e3.getName());
    request.addParameter("newEquipment.name", "");
    request.addParameter("delete_id", e3.getId().toString());
    response = new MockHttpServletResponse();
    mv = controller.handleRequest(request, response);

    // Should redirect
    assertNotNull("Success view should not be null. ModelAndView: " + mv, mv.getView());
    assertEquals("Success view", RedirectView.class, mv.getView().getClass());
    RedirectView successView = (RedirectView) mv.getView();
    assertTrue("Success view URL", successView.getUrl().indexOf("/dynamic/equipment/home") > -1);

		// Check database
		user = userFactory.find(user.getUsername());
		List equipment = new ArrayList(user.getEquipment());
		Collections.sort(equipment);
		assertEquals("Equipment count", 2, equipment.size());
		String[] expectedNames = new String[] {"", "Adidas"};
		for (int i = 0; i < equipment.size(); i++) {
			Equipment e = (Equipment) equipment.get(i);
			assertEquals("Equipment name", expectedNames[i], e.getName());
		}
  }


  public void testDeleteEquipmentCyclistFromRequest() throws Exception {
    // Tighter test could use mock UserFactory to remove database dependency
    UserFactory userFactory = (UserFactory) getBeanFactory().getBean("userFactory");
    // Create cyclist with equipment
    User user = new User("test", "Test User");
    new Equipment("", user);
    Equipment e2 = new Equipment("Adidas", user);
    Equipment e3 = new Equipment("Vanilla", user);
    userFactory.save(user);
    assertNotNull(e2.getName() + " should be saved to database (id != null)", e2.getId());

    // Show form
    MockHttpServletRequest request = new MockHttpServletRequest(null, "GET", "/dynamic/equipment/home");
    HttpSession session = request.getSession();
    session.setAttribute("user", user);
    session.setAttribute("cyclist", user);
    HttpServletResponse response = new MockHttpServletResponse();
    EquipmentController controller = (EquipmentController) getBeanFactory().getBean("equipmentController");
    ModelAndView mv = controller.handleRequest(request, response);

    request = new MockHttpServletRequest(null, "POST", "/dynamic/equipment/home");
    // Re-use session with logged-in user
    request.setSession(session);
    // No updates
    request.addParameter("cyclist.id", user.getId().toString());
    request.addParameter("equipment[1].name", e2.getName());
    request.addParameter("equipment[2].name", e3.getName());
    request.addParameter("newEquipment.name", "");
    request.addParameter("delete_id", e3.getId().toString());
    response = new MockHttpServletResponse();
    mv = controller.handleRequest(request, response);

    // Should redirect
    assertNotNull("Success view should not be null. ModelAndView: " + mv, mv.getView());
    assertEquals("Success view", RedirectView.class, mv.getView().getClass());
    RedirectView successView = (RedirectView) mv.getView();
    assertTrue("Success view URL", successView.getUrl().indexOf("/dynamic/equipment/home") > -1);

	// Check database
	user = userFactory.find(user.getUsername());
	List equipment = new ArrayList(user.getEquipment());
	Collections.sort(equipment);
	assertEquals("Equipment count", 2, equipment.size());
	String[] expectedNames = new String[] {"", "Adidas"};
	for (int i = 0; i < equipment.size(); i++) {
		Equipment e = (Equipment) equipment.get(i);
		assertEquals("Equipment name", expectedNames[i], e.getName());
	}
  }

  public void testDeleteEquipmentThenDone() throws Exception {
    // Tighter test could use mock UserFactory to remove database dependency
    UserFactory userFactory = (UserFactory) getBeanFactory().getBean("userFactory");
    // Create cyclist with equipment
    User user = new User("test", "Test User");
    new Equipment("", user);
    Equipment e2 = new Equipment("Adidas", user);
    Equipment e3 = new Equipment("Vanilla", user);
    userFactory.save(user);
    assertNotNull(e2.getName() + " should be saved to database (id != null)", e2.getId());

    // Show form
    MockHttpServletRequest request = new MockHttpServletRequest(null, "GET", "/dynamic/equipment/home");
    HttpSession session = request.getSession();
    session.setAttribute("cyclist", user);
    session.setAttribute("user", user);
    HttpServletResponse response = new MockHttpServletResponse();
    EquipmentController controller = (EquipmentController) getBeanFactory().getBean("equipmentController");
    ModelAndView mv = controller.handleRequest(request, response);

    request = new MockHttpServletRequest(null, "POST", "/dynamic/equipment/home");
    // Re-use session with logged-in user
    request.setSession(session);
    // No updates
    request.addParameter("cyclist.id", user.getId().toString());
    request.addParameter("equipment[1].name", e2.getName());
    request.addParameter("equipment[2].name", e3.getName());
    request.addParameter("newEquipment.name", "");
    request.addParameter("delete_id", e3.getId().toString());
    response = new MockHttpServletResponse();
    mv = controller.handleRequest(request, response);

    // Should redirect
    assertNotNull("Success view should not be null. ModelAndView: " + mv, mv.getView());
    assertEquals("Success view", RedirectView.class, mv.getView().getClass());
    RedirectView successView = (RedirectView) mv.getView();
    assertTrue("Success view URL", successView.getUrl().indexOf("/dynamic/equipment/home") > -1);

    request = new MockHttpServletRequest(null, "POST", "/dynamic/equipment/home");
    // Re-use session with logged-in user
    request.setSession(session);
    request.addParameter("cyclist.id", user.getId().toString());
    request.addParameter("newEquipment.name", "");
    request.addParameter("done", "Done");
    response = new MockHttpServletResponse();
    mv = controller.handleRequest(request, response);

    // Should redirect to home
    assertNotNull("Success view should not be null. ModelAndView: " + mv, mv.getView());
    assertEquals("Redirect view", RedirectView.class, mv.getView().getClass());
    RedirectView view = (RedirectView) mv.getView();
    String expected = "/dynamic/login/home";
    assertTrue("Redirect view URL should include '" + expected + "', but was '" + view.getUrl(),
        view.getUrl().indexOf(expected) > -1);
  }

public void testDone() throws Exception {
    // Tighter test could use mock UserFactory to remove database dependency
    UserFactory userFactory = (UserFactory) getBeanFactory().getBean("userFactory");
    // Create cyclist with equipment
    User user = new User("test", "Test User");
    new Equipment("", user);
    userFactory.save(user);

    // Show form
    MockHttpServletRequest request = new MockHttpServletRequest(null, "GET", "/dynamic/equipment/home");
    HttpSession session = request.getSession();
    session.setAttribute("cyclist", user);
    session.setAttribute("user", user);
    HttpServletResponse response = new MockHttpServletResponse();
    EquipmentController controller = (EquipmentController) getBeanFactory().getBean("equipmentController");
    ModelAndView mv = controller.handleRequest(request, response);

    request = new MockHttpServletRequest(null, "POST", "/dynamic/equipment/home");
    // Re-use session with logged-in user
    request.setSession(session);
    request.addParameter("cyclist.id", user.getId().toString());
    request.addParameter("newEquipment.name", "");
    request.addParameter("done", "Done");
    response = new MockHttpServletResponse();
    mv = controller.handleRequest(request, response);

    // Should redirect
    assertNotNull("Success view should not be null. ModelAndView: " + mv, mv.getView());
    assertEquals("Redirect view", RedirectView.class, mv.getView().getClass());
    RedirectView view = (RedirectView) mv.getView();
    String expected = "/dynamic/login/home";
    assertTrue("Redirect view URL should inlcude '" + expected + "', but was '" + view.getUrl(),
        view.getUrl().indexOf(expected) > -1);

  }

  public void testNoUser() throws Exception {
    // Show form
    MockHttpServletRequest request = new MockHttpServletRequest(null, "GET", "/dynamic/equipment/home");
    HttpServletResponse response = new MockHttpServletResponse();
    EquipmentController controller = (EquipmentController) getBeanFactory().getBean("equipmentController");

    // No user -- should redirect
	  ModelAndView mv = controller.handleRequest(request, response);

    assertNotNull("Should be redirecting to a view ", mv.getView());
    assertEquals("Login view", RedirectView.class, mv.getView().getClass());
    RedirectView view = (RedirectView) mv.getView();
    assertTrue("Login view URL", view.getUrl().indexOf("/dynamic/login/home") > -1);
  }

  public void testNotAdmin() throws Exception {
    UserFactory userFactory = (UserFactory) getBeanFactory().getBean("userFactory");

    // User
    User user = new User("test", "Test User");
    userFactory.save(user);
    assertNotNull(user.getName() + " should be saved to database (id != null)", user.getId());

    // Create cyclist with equipment
    User cyclist = new User("cyclist", "Test Cyclist");
    new Equipment("", cyclist);
    new Equipment("Adidas", cyclist);
    new Equipment("Vanilla", cyclist);
    userFactory.save(cyclist);
    assertNotNull(cyclist.getName() + " should be saved to database (id != null)", cyclist.getId());

    // Show form
    MockHttpServletRequest request = new MockHttpServletRequest(null, "GET", "/dynamic/equipment/home");
    HttpSession session = request.getSession();
    session.setAttribute("cyclist", cyclist);
    session.setAttribute("user", user);
    HttpServletResponse response = new MockHttpServletResponse();
    EquipmentController controller = (EquipmentController) getBeanFactory().getBean("equipmentController");

    // User is not admin for cyclist -- should redirect. For now, throws exception
    try {
      controller.handleRequest(request, response);
      fail("Should throw exception if user is not admin for cyclist");      
    } catch (CyclingLogException e) {
      // OK
    }
  }

  public void testUpdateDifferentUser() throws Exception {
    UserFactory userFactory = (UserFactory) getBeanFactory().getBean("userFactory");

    // User
    User user = new User("test", "Test User");
    userFactory.save(user);
    assertNotNull(user.getName() + " should be saved to database (id != null)", user.getId());

    // Create cyclist with equipment
    User cyclist = new User("cyclist", "Test Cyclist");
    new Equipment("", cyclist);
    Equipment e2 = new Equipment("Adidas", cyclist);
    new Equipment("Vanilla", cyclist);
    cyclist.addAdministrator(user);
    userFactory.save(cyclist);
    assertNotNull(cyclist.getName() + " should be saved to database (id != null)", cyclist.getId());

    // Show form
    MockHttpServletRequest request = new MockHttpServletRequest(null, "GET", "/dynamic/equipment/home");
    HttpSession session = request.getSession();
    session.setAttribute("cyclist", cyclist);
    session.setAttribute("user", user);
    HttpServletResponse response = new MockHttpServletResponse();
    EquipmentController controller = (EquipmentController) getBeanFactory().getBean("equipmentController");
    ModelAndView mv = controller.handleRequest(request, response);

    request = new MockHttpServletRequest(null, "POST", "/dynamic/equipment/home");
    // Re-use session with logged-in user
    request.setSession(session);
    // Update Vanilla
    String updatedName = "New Balance";
    request.addParameter("cyclist.id", cyclist.getId().toString());
    request.addParameter("equipment[1].name", e2.getName());
    request.addParameter("equipment[2].name", updatedName);
    request.addParameter("save.x", "2");
    response = new MockHttpServletResponse();
    mv = controller.handleRequest(request, response);

    // Should redirect
    assertEquals("Success view", RedirectView.class, mv.getView().getClass());
    RedirectView successView = (RedirectView) mv.getView();
    assertTrue("Success view URL", successView.getUrl().indexOf("/dynamic/equipment/home") > -1);

		// Check database
		cyclist = userFactory.find(cyclist.getUsername());
		List equipment = new ArrayList(cyclist.getEquipment());
		Collections.sort(equipment);
		assertEquals("Equipment count", 3, equipment.size());
		String[] expectedNames = new String[] {"", "Adidas", "New Balance"};
		for (int i = 0; i < equipment.size(); i++) {
			Equipment e = (Equipment) equipment.get(i);
			assertEquals("Equipment name", expectedNames[i], e.getName());
		}
  }
}
