package ru.javawebinar.topjava.repository;

import java.time.LocalDate;
import java.util.List;

import ru.javawebinar.topjava.model.Meal;

public interface MealRepository {
    // null if not found, when updated
    Meal save(Meal meal, int userId);

    // false if not found
    boolean delete(int mealId, int userId);

    // null if not found
    Meal get(int mealId, int userId);

    List<Meal> getAll(int userId);

    List<Meal> getFiltered(LocalDate startDate, LocalDate endDate, int userId);
}
