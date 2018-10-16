package ru.javawebinar.topjava.repository.mock;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.util.MealsUtil.MEALS;

/**
 * Created by taras on 2018-10-15.
 */

public class InMemoryMealRepositoryImplTest {
    private MealRepository mealRepository;

    @Before
    public void setUp() {
        //Cleaning up IDs within static fields
        MealsUtil.MEALS.forEach(m -> m.setId(null));
        mealRepository = new InMemoryMealRepositoryImpl();
    }

    @Test
    public void getAll() {
        Collection<Meal> meals = mealRepository.getAll(1);
        assertEquals(MEALS.size(), meals.size());
    }

    @Test
    public void getAllFromUnknownUser() {
        Collection<Meal> meals = mealRepository.getAll(2);
        assertEquals(0, meals.size());
    }

    @Test
    public void get() {
        Meal meal = mealRepository.get(1, 1);
        assertNotNull(meal);
        assertEquals(MEALS.get(0).getDescription(), meal.getDescription());
        assertEquals(MEALS.get(0).getDateTime(), meal.getDateTime());
        assertEquals(MEALS.get(0).getCalories(), meal.getCalories());
    }

    @Test
    public void getFromUnknownUser() {
        Meal meal = mealRepository.get(1, 1000);
        assertNull(meal);
    }

    @Test
    public void updateRecord() {
        Meal meal = mealRepository.get(2, 1);
        LocalDateTime newDateTime = meal.getDateTime().plusDays(20);
        String newDescription = meal.getDescription() + "NEW";
        int newCalories = meal.getCalories() + 10000;
        Meal newMeal = new Meal(meal.getId(), newDateTime, newDescription, newCalories);
        mealRepository.save(newMeal, 1);

        Meal newestMeal = mealRepository.get(2, 1);

        assertEquals(newDescription, newestMeal.getDescription());
        assertEquals(newCalories, newestMeal.getCalories());
        assertEquals(newDateTime, newestMeal.getDateTime());
    }

    @Test
    public void updateNonExistingRecord() {
        Meal meal = mealRepository.get(2, 1);
        LocalDateTime newDateTime = meal.getDateTime().plusDays(20);
        String newDescription = meal.getDescription() + "NEW";
        int newCalories = meal.getCalories() + 10000;
        Meal newMeal = new Meal(1000, newDateTime, newDescription, newCalories);

        Meal newestMeal = mealRepository.save(newMeal, 1);

        assertNull(newestMeal);
    }

    @Test
    public void delete() {
        boolean deleted = mealRepository.delete(1, 1);
        assertTrue(deleted);
        assertEquals(MEALS.size() - 1, mealRepository.getAll(1).size());
    }

    @Test
    public void deleteFromUnknownUser() {
        boolean deleted = mealRepository.delete(1, 1000);
        assertFalse(deleted);
        assertEquals(MEALS.size(), mealRepository.getAll(1).size());
    }
}