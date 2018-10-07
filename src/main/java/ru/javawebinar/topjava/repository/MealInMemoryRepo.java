package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by taras on 2018-10-08.
 */

public class MealInMemoryRepo {
    private static List<Meal> mealList = new ArrayList<>();

    static {
        LocalDateTime mealTime = LocalDateTime.of(2018, 1, 1, 6, 23);
        Meal meal = new Meal(
                mealTime,
                "Завтрак",
                100
        );
        add(meal);
        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            mealTime = calculateNewTime(mealTime, random);
            Meal dinnerMeal = new Meal(mealTime, "Обед", 500);
            add(dinnerMeal);
            mealTime = calculateNewTime(mealTime, random);
            Meal eveningMeal = new Meal(mealTime, "Ужин", 50 + random.nextInt(50));
            add(eveningMeal);
            mealTime = calculateNewTime(mealTime, random);
            Meal breakfastMeal = new Meal(mealTime, "Завтрак", 100);
            add(breakfastMeal);
        }
    }

    private static LocalDateTime calculateNewTime(LocalDateTime mealTime, Random random) {
        return mealTime.plusMinutes(8*60 + random.nextInt(10) - 5);
    }

    public static List<Meal> getMealList() {
        return new ArrayList<>(mealList);
    }

    public static void add(Meal meal) {
        mealList.add(meal);
    }




}
