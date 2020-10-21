package ru.javawebinar.topjava.repository.inmemory;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.subList(0, 7).forEach(meal -> save(meal, MealsUtil.ADMIN_ID));
        MealsUtil.meals.subList(7, 9).forEach(meal -> save(meal, MealsUtil.USER_ID));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew() && get(meal.getId(), userId) == null) {
            return null;
        }

        Map<Integer, Meal> meals = repository.computeIfAbsent(userId, ConcurrentHashMap::new);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        }

        meals.merge(meal.getId(), meal, (old, updated) -> updated);
        return meal;
    }

    @Override
    public boolean delete(int mealId, int userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals != null && meals.remove(mealId) != null;
    }

    @Override
    public Meal get(int mealId, int userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals != null ? meals.get(mealId) : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return filterByPredicate(userId, meal -> true);
    }

    @Override
    public List<Meal> getFiltered(LocalDate startDate, LocalDate endDate, int userId) {
        return filterByPredicate(userId, meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate));
    }

    private List<Meal> filterByPredicate(int userId, Predicate<Meal> filter) {
        Map<Integer, Meal> meals = repository.get(userId);

        return CollectionUtils.isEmpty(meals)
               ? Collections.emptyList()
               : meals.values().stream()
                   .filter(filter)
                   .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                   .collect(Collectors.toList());
    }
}

