package ru.javawebinar.topjava.web.controller;

import java.util.List;

import ru.javawebinar.topjava.model.Meal;

/**
 * @author gulnaz
 */
public interface MealController {
    List<Meal> getAll();

    Meal get(int id);

    void save(Meal meal);

    void delete(int id);
}
