package ru.javawebinar.topjava.web.controller;

import java.util.List;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.MealRepositoryInMemoryImpl;

/**
 * @author gulnaz
 */
public class MealControllerInMemoryImpl implements MealController {
    private MealRepository mealRepository = new MealRepositoryInMemoryImpl();

    @Override
    public List<Meal> getAll() {
        return mealRepository.getAll();
    }

    @Override
    public Meal get(int id) {
        return mealRepository.get(id);
    }

    @Override
    public void save(Meal meal) {
        mealRepository.save(meal);
    }

    @Override
    public void delete(int id) {
        mealRepository.delete(id);
    }
}
