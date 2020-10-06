package ru.javawebinar.topjava.web;

import java.io.IOException;
import java.time.LocalDate;
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
import ru.javawebinar.topjava.repository.InMemoryMealRepository;
import ru.javawebinar.topjava.repository.MealRepository;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

/**
 * @author gulnaz
 */
public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final int CALORIES_PER_DAY = 2000;
    private MealRepository mealRepository;

    @Override
    public void init() {
        mealRepository =  new InMemoryMealRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String paramValue = request.getParameter("action");
        String action = paramValue != null ? paramValue : "";
        switch (action) {
            case "delete":
                delete(request, response);
                break;
            case "add":
            case "edit":
                getForm(request, response);
                break;
            default:
                list(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        save(request, response);
    }

    private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<MealTo> mealsTo = filteredByStreams(mealRepository.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
        request.setAttribute("meals", mealsTo);

        log.debug("redirect to meals");
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("datetime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        int id = parseId(request.getParameter("id"));
        Meal meal = id != 0
                    ? new Meal(id, dateTime, description, calories)
                    : new Meal(dateTime, description, calories);

        log.debug("save meal");
        mealRepository.save(meal);
        response.sendRedirect("meals");
    }

    private void getForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = parseId(request.getParameter("id"));
        Meal meal = id != 0
                    ? mealRepository.get(id)
                    : new Meal(LocalDateTime.of(LocalDate.now(), LocalTime.NOON), "", 1000);
        request.setAttribute("meal", meal);

        log.debug("redirect to meal form");
        request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        log.debug("delete meal with id={}", id);
        mealRepository.delete(id);
        response.sendRedirect("meals");
    }

    private int parseId(String stringId) {
        if (stringId == null || stringId.isEmpty()) {
            return 0;
        }

        return Integer.parseInt(stringId);
    }
}
