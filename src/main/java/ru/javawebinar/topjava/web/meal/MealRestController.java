package ru.javawebinar.topjava.web.meal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import static ru.javawebinar.topjava.util.DateTimeUtil.getOrDefault;
import static ru.javawebinar.topjava.util.MealsUtil.getFilteredTos;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, authUserId());
    }

    public Meal update(Meal meal, int mealId) {
        log.info("update {}", meal);
        assureIdConsistent(meal, mealId);
        return service.update(meal, authUserId());
    }

    public boolean delete(int mealId) {
        log.info("delete meal with id={}", mealId);
        return service.delete(mealId, authUserId());
    }

    public Meal get(int mealId) {
        log.info("get meal with id={}", mealId);
        return service.get(mealId, authUserId());
    }

    public List<MealTo> getAll() {
        log.info("get all meals");
        return getTos(service.getAll(authUserId()), authUserCaloriesPerDay());
    }

    public List<MealTo> getFiltered(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("get filtered meals");
        List<Meal> filteredByDates = service.getFiltered(getOrDefault(startDate, LocalDate.MIN),
            getOrDefault(endDate, LocalDate.MAX), authUserId());

        return getFilteredTos(filteredByDates, authUserCaloriesPerDay(),
            getOrDefault(startTime, LocalTime.MIN), getOrDefault(endTime, LocalTime.MAX));
    }
}