package ru.javawebinar.topjava.web;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.web.controller.MealController;
import ru.javawebinar.topjava.web.controller.MealControllerInMemoryImpl;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

/**
 * @author gulnaz
 */
public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealController mealController = new MealControllerInMemoryImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath().substring(1);
        switch (action) {
            case "delete":
                delete(request, response);
                break;
            case "add":
            case "edit":
                getForm(request, response);
                break;
            case "save":
                save(request, response);
                break;
            default:
                list(request, response);
                break;
        }
    }

    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<MealTo> mealsTo = filteredByStreams(mealController.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
        request.setAttribute("meals", mealsTo);

        log.debug("redirect to meals");
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("datetime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        String stringId = request.getParameter("id");
        Meal meal = !stringId.isEmpty()
                    ? new Meal(Integer.parseInt(stringId), dateTime, description, calories)
                    : new Meal(dateTime, description, calories);

        log.debug("save meal");
        mealController.save(meal);
        response.sendRedirect("meals");
    }

    private void getForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String stringId = request.getParameter("id");
        Meal meal = stringId != null
                    ? mealController.get(Integer.parseInt(stringId))
                    : new Meal(LocalDateTime.now(), "", 1000);
        request.setAttribute("meal", meal);

        log.debug("redirect to meal form");
        request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        log.debug("delete meal with id={}", id);
        mealController.delete(id);
        response.sendRedirect("meals");
    }
}
