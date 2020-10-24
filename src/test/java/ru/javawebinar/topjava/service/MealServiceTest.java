package ru.javawebinar.topjava.service;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.MEAL_ID;
import static ru.javawebinar.topjava.MealTestData.NOT_FOUND;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.MealTestData.getNew;
import static ru.javawebinar.topjava.MealTestData.getUpdated;
import static ru.javawebinar.topjava.MealTestData.meal1;
import static ru.javawebinar.topjava.MealTestData.meal2;
import static ru.javawebinar.topjava.MealTestData.meal3;
import static ru.javawebinar.topjava.MealTestData.userMeals;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

/**
 * @author gulnaz
 */
public class MealServiceTest extends AbstractServiceTest {

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MEAL_ID, USER_ID);
        assertMatch(meal, meal1);
    }

    @Test
    public void getNotBelonging() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, ADMIN_ID));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void delete() {
        service.delete(MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID, USER_ID));
    }

    @Test
    public void deleteNotBelonging() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID, ADMIN_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        LocalDate date = meal1.getDate();
        List<Meal> filteredMeals = service.getBetweenInclusive(date, date, USER_ID);
        assertMatch(filteredMeals, meal3, meal2, meal1);
    }

    @Test
    public void getBetweenNullAndDate() {
        List<Meal> filteredMeals = service.getBetweenInclusive(null, meal1.getDate(), USER_ID);
        assertMatch(filteredMeals, meal3, meal2, meal1);
    }

    @Test
    public void getBetweenNullDates() {
        List<Meal> filteredMeals = service.getBetweenInclusive(null, null, USER_ID);
        assertMatch(filteredMeals, userMeals);
    }

    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(USER_ID);
        assertMatch(meals, userMeals);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL_ID, USER_ID), getUpdated());
    }

    @Test
    public void updateNotFound() {
        assertThrows(NotFoundException.class, () -> service.update(new Meal(meal1), ADMIN_ID));
    }

    @Test
    public void create() {
        Meal newMeal = getNew();
        Meal expected = new Meal(newMeal);
        Meal created = service.create(newMeal, USER_ID);
        Integer newId = created.getId();
        expected.setId(newId);
        assertMatch(created, expected);
        assertMatch(service.get(newId, USER_ID), expected);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class,
            () -> service.create(new Meal(meal1.getDateTime(), "Duplicate", 300), USER_ID));
    }
}