package com.butlerpress.cyclinglog;

import javax.servlet.http.HttpServletResponse;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class LoginControllerTestCase extends TestCase {
	
	  public void testLogin() throws Exception {
      createCyclistWorkoutWeek("sacha");
      MockHttpServletRequest request = new MockHttpServletRequest(null, "GET", "/login/home");

	    HttpServletResponse response = new MockHttpServletResponse();
	    LoginController controller = (LoginController) getBeanFactory().getBean("loginController");
	    ModelAndView mv = controller.handleRequest(request, response);
	    assertNotNull("Model and view", mv);
	    assertEquals("Login view", "/login/index.html", mv.getViewName());
	  }
}
