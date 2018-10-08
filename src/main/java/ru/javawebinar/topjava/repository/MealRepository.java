package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

/**
 * Created by taras on 2018-10-08.
 */

public interface MealRepository {
    Meal add(Meal meal);

    Meal update(Meal meal);

    void delete(int meal_id);

    List<Meal> findAll();

    int count();

}
