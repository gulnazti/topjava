package ru.javawebinar.topjava.repository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import ru.javawebinar.topjava.model.Meal;

/**
 * @author gulnaz
 */
public class InMemoryMealRepository implements MealRepository {
    private AtomicInteger idCounter = new AtomicInteger(0);
    private Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();

    public InMemoryMealRepository() {
        List<Meal> mealList = Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        mealList.forEach(this::save);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealsMap.values());
    }

    @Override
    public Meal get(int id) {
        return mealsMap.get(id);
    }

    @Override
    public Meal save(Meal meal) {
        int mealId = meal.getId();
        if (mealId != 0) {
            if (!mealsMap.containsKey(mealId)) {
                return null;
            }

            mealsMap.put(meal.getId(), meal);
        }
        else {
            mealId = idCounter.incrementAndGet();
            meal.setId(mealId);
            mealsMap.put(mealId, meal);
        }

        return meal;
    }

    @Override
    public void delete(int id) {
        mealsMap.remove(id);
    }
}
