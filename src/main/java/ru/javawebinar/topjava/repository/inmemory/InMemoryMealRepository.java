package ru.javawebinar.topjava.repository.inmemory;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew() && get(meal.getId(), userId) == null) {
            return null;
        }

        meal.setUserId(userId);

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
        } else {
            repository.merge(meal.getId(), meal, (old, updated) -> updated);
        }

        return meal;
    }

    @Override
    public boolean delete(int mealId, int userId) {
        return repository.values().removeIf(meal -> meal.getId() == mealId && meal.getUserId() == userId);
    }

    @Override
    public Meal get(int mealId, int userId) {
        Meal meal = repository.get(mealId);
        return meal != null && meal.getUserId() == userId ? meal : null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.values().stream()
            .filter(meal -> meal.getUserId() == userId)
            .sorted(Comparator.comparing(Meal::getDateTime).reversed())
            .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getFiltered(LocalDate startDate, LocalDate endDate, int userId) {
        return getAll(userId).stream()
            .filter(meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate))
            .collect(Collectors.toList());
    }
}

