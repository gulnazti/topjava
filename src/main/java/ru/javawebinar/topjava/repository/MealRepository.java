package ru.javawebinar.topjava.repository;

import java.util.List;

import ru.javawebinar.topjava.model.Meal;

/**
 * @author gulnaz
 */
public interface MealRepository {
    List<Meal> getAll();

    Meal get(int id);

    void save(Meal meal);

    void delete(int id);
}
