package com.butlerpress.cyclinglog;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ModelFactory {

  UserFactory userFactory;
  
  public ModelFactory() {
  }
  
  public UserFactory getUserFactory() {
    return userFactory;
  }

  public void setUserFactory(UserFactory userFactory) {
    this.userFactory = userFactory;
  }
  
  public Model getModel(HttpServletRequest request) {
    Model model = new Model();
    model.put("cyclist", getCyclist(request));
    model.put("user", getUser(request));
    model.put(Model.DATE, getDate(request));
    model.put("select", new SelectTool());
    model.put("sort", new SortTool());
    return model;
  }
  
  private User getCyclist(HttpServletRequest request) {
    String cyclistName = request.getParameter("cyclist");
    User cyclist = null;
    if (cyclistName != null) {
      cyclist = getUserFactory().find(cyclistName);
    }
    if (cyclist != null) {
      request.getSession().setAttribute("cyclist", cyclist);
    } else {
      cyclist = (User) request.getSession().getAttribute("cyclist");
    }
    return cyclist;
  }

  /**
   *  Returns current date if there's a problem.
   */
  private Date getDate(HttpServletRequest request) {
    try {
      String dateParam = request.getParameter(Model.DATE);
      if (dateParam != null) {
        return Model.DATE_PARAM_FORMAT.parse(dateParam);
      } else {
          String day = (request.getParameter("day"));
          String month = (request.getParameter("month"));
          String year = (request.getParameter("year"));
          int intDay = Integer.parseInt(day);
          int intMonth = Integer.parseInt(month);
          int intYear = Integer.parseInt(year);
          return Convertor.getDate(intDay, intMonth, intYear);
      }
    } catch (Exception e) {
      return new Date();
    }
  }

  private String get(String date) {
    // TODO Auto-generated method stub
    return null;
  }

  private User getUser(HttpServletRequest request) {
    User user = null;
    HttpSession httpSession = request.getSession(false);
    if (httpSession != null) {
      user = (User) httpSession.getAttribute("user");
    }
    return user;
  }

}
