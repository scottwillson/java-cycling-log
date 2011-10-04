package com.butlerpress.cyclinglog;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.hibernate.Session;

import org.springframework.orm.hibernate.SessionFactoryUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class WeeksController extends CyclingLogRequestHandler {

  public ModelAndView add(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
    int newWeeksQuantity = Integer.parseInt(request.getParameter("newWeeksQuantity"));
    User cyclist = model.getCyclist();
    User user = model.getUser();
	if (cyclist == null || user == null) {
		return new ModelAndView(new RedirectView("/cycling_log/dynamic/login/home"));
	}
    if (!User.canEdit(user, cyclist)) {
      throw new RuntimeException("You do not have rights to add weeks for this cyclist");
    }
    logger.info("Adding " + newWeeksQuantity + " for " + cyclist);
    getWeekFactory().add(newWeeksQuantity, cyclist);
    return new ModelAndView(new RedirectView("/cycling_log/dynamic/weeks/home"));
  }

  ModelAndView update(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {

    if (!User.canEdit(model.getUser(), model.getCyclist())) {
    		logger.info(model.getUser() + " could not update weeks for " + model.getCyclist());
      throw new RuntimeException("You do not have rights to update weeks for this cyclist");
    }
    Session session = null;
    Week firstWeek = null;
    try {
      session = sessionFactory.openSession();
      String[] ids = request.getParameterValues(Model.WEEK + "id");
      for (int i = 0; i < ids.length; i++) {
        Long id = new Long(ids[i]);
        Week week = (Week)session.load(com.butlerpress.cyclinglog.Week.class, id);
        if (i == 0) {
          firstWeek = week;
        }
        String focus = request.getParameterValues(Model.WEEK + "focus")[i];
        week.setFocus(focus);
        String publicNotes = request.getParameterValues(Model.WEEK + "public_notes")[i];
        week.setPublicNotes(publicNotes);
        String notes = request.getParameterValues(Model.WEEK + "notes")[i];
        week.setNotes(notes);
        session.saveOrUpdate(week);
      }
      session.flush();
		logger.info(model.getUser() + " update weeks for " + model.getCyclist());
      return viewHome(request, model, firstWeek.getStartDate());
    } catch (Exception e) {
      throw new ServletException(e);
    } finally {
      SessionFactoryUtils.closeSessionIfNecessary(session, sessionFactory);
    }
  }

  public ModelAndView home(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
			
		Model model = getModelFactory().getModel(request);
		User cyclist = model.getCyclist();
		if (cyclist == null) {
			return new ModelAndView(new RedirectView("/cycling_log/dynamic/login/home"));
		}
		model.put("cyclist", cyclist);
		if (request.getParameter("save") != null) {
			return update(request, response, model);
		} else if (request.getParameter("add.x") != null) {
			return add(request, response, model);
		}

		Week startWeek = getWeekFactory().get(model.getDate(), model.getCyclist());
		int weeksCount = getWeeksCount(request);
		if (request.getParameter("scrollUp.x") != null) {
			startWeek = getWeekFactory().getPrevious(startWeek, weeksCount - 1);
      return viewHome(request, model, startWeek.getStartDate());
		} else if (request.getParameter("scrollDown.x") != null) {
			startWeek = getWeekFactory().getNext(startWeek, weeksCount - 1);
      return viewHome(request, model, startWeek.getStartDate());
		}

		return viewHome(request, model, model.getDate());
	}

  ModelAndView home(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
    return viewHome(request, model, null);
  }

  ModelAndView viewHome(HttpServletRequest request, Model model, Date startDate) throws Exception {
		logger.info(model.getUser() + " view weeks for " + model.getCyclist());
    startDate = Week.getStart(startDate);
    User cyclist = model.getCyclist();
    User user = model.getUser();
    if (startDate == null) {
    		startDate = new Date();
    }
    int weeksCount = getWeeksCount(request);
    List weeks = getWeekFactory().get(startDate, weeksCount, cyclist);
    List weekViews = new ArrayList();
    if (weeks != null) {
      Iterator iter = weeks.iterator();
      while (iter.hasNext()) {
        Week w = (Week)iter.next();
        weekViews.add(new WeekView(w));
      }
    }

    String edit = request.getParameter("edit");
    if (user != null && edit != null) {
      model.put("foci", WeekFocus.getAll());
      model.put("isEditable", Boolean.TRUE);
    }
    String cancel = request.getParameter("cancel");
    if (cancel != null) {
      model.put("isEditable", Boolean.FALSE);
    }
    model.put("weekDateSelector", new DateSelector(startDate));
    model.put("weeksCount", String.valueOf(weeksCount));
    model.put("weeks", weekViews);
    model.put("cyclist", cyclist);
    model.put("cyclists", getAllCyclists());

    if (User.canEdit(user, cyclist)) {
      return new ModelAndView("weeks/index.html", model);
    } else {
      return  new ModelAndView("weeks/public_index.html", model);
    }
  }

  int getWeeksCount(HttpServletRequest request) {
    String weeksCount = (String)request.getSession().getAttribute("weeksCount");
    String param = request.getParameter("weeksCount");
    if (param != null && !"".equals(param)) {
      request.getSession().setAttribute("weeksCount", param);
      weeksCount = param;
    } else if (weeksCount == null || "".equals(weeksCount)) {
      weeksCount = "13";
      request.getSession().setAttribute("weeksCount", weeksCount);
    }
    return Integer.parseInt(weeksCount);
  }

}
