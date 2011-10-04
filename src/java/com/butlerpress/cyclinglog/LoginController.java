package com.butlerpress.cyclinglog;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.hibernate.HibernateException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class LoginController extends CyclingLogRequestHandler {

  public static final Log log = LogFactory.getLog(LoginController.class);

  public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
    request.getSession().setAttribute("user", null);
    response.sendRedirect("/cycling_log/dynamic/login/home");
    return null;
  }

  public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {

    Model model = getModelFactory().getModel(request);
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    User user = getUserFactory().find(username, password);
    if (user != null) {
      log.info(user.getUsername() + " logged-in");
      model.put("user", user);
      model.put("cyclists", getAllCyclists());
      request.getSession().setAttribute("user", user);
      request.getSession().setAttribute("cyclist", user);
      return new ModelAndView(new RedirectView("/cycling_log/dynamic/workouts/home"));
    } else {
      model.put("message", "Could not log in with that username and password");
      model.put("username", username);
      model.put("password", password);
      model.put("cyclists", getAllCyclists());
      model.put("workouts", getLatestWorkouts());
      return new ModelAndView("/login/index.html", model);
    }
  }

  public ModelAndView home(HttpServletRequest request, HttpServletResponse response) throws Exception {

    Model model = getModelFactory().getModel(request);
    model.put("cyclists", getAllCyclists());
    model.put("workouts", getLatestWorkouts());
    return new ModelAndView("/login/index.html", model);
  }

  List getAllCyclists() {
    List cyclists = new ArrayList();
    cyclists.add(User.NULL);
    cyclists.addAll(getUserFactory().findAll());
    return cyclists;
  }
  
  List getLatestWorkouts() throws HibernateException {
		return  getWorkoutFactory().findLatest();
  }

}
