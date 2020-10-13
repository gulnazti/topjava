package ru.javawebinar.topjava.web.meal;

import java.util.Collection;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseTime;
import static ru.javawebinar.topjava.util.MealsUtil.getFilteredTos;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal save(Meal meal) {
        return service.save(meal, authUserId());
    }

    public boolean delete(int mealId) {
        return service.delete(mealId, authUserId());
    }

    public Meal get(int mealId) {
        return service.get(mealId, authUserId());
    }

    public Collection<MealTo> getAll() {
        return getTos(service.getAll(authUserId()), authUserCaloriesPerDay());
    }

    public Collection<MealTo> getFiltered(String startDate, String endDate, String startTime, String endTime) {
        return getFilteredTos(service.getFiltered(parseDate(startDate), parseDate(endDate), authUserId()),
            authUserCaloriesPerDay(), parseTime(startTime), parseTime(endTime));
    }
}