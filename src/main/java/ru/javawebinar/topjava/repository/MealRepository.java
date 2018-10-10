package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by taras on 2018-10-08.
 */

public interface MealRepository {
    void insert(Meal meal);

    void update(Meal meal) throws NoSuchElementException;

    void delete(int meal_id);

    void deleteAll();

    List<Meal> findAll();

    Meal findById(int id);

    int count();

}
