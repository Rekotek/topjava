package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by taras on 2018-10-08.
 */

public class MealInMemoryImpl implements MealRepository {
    private static final Logger log = getLogger(MealInMemoryImpl.class);

    private static AtomicInteger id = new AtomicInteger(0);
    private static Map<Integer, Meal> mealStore = new ConcurrentHashMap<>();

    @Override
    public List<Meal> findAll() {
        return new ArrayList<>(mealStore.values());
    }

    @Override
    public int count() {
        return mealStore.size();
    }

    @Override
    public void insert(Meal meal) {
        Integer newId = id.incrementAndGet();
        meal.setId(newId);
        log.debug("New Atomic ID = {}", newId);
        mealStore.put(newId, meal);
    }

    @Override
    public void update(Meal meal) {
        Integer id = meal.getId();
        mealStore.replace(id, meal);
    }

    @Override
    public void delete(int meal_id) {
        log.debug("Delete Meal with ID = {}", meal_id);
        mealStore.remove(meal_id);
    }

    @Override
    public void deleteAll() {
        mealStore.clear();
        id.set(0);
    }


    private static LocalDateTime calculateNewTime(LocalDateTime mealTime, Random random) {
        return mealTime.plusMinutes(8 * 60 + random.nextInt(10) - 5);
    }

    public static void setUpTestData(MealRepository mealRepository) {
        LocalDateTime mealTime = LocalDateTime.of(2018, 1, 1, 6, 23);
        Meal meal = new Meal(mealTime, "Завтрак", 100);
        mealRepository.insert(meal);
        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            mealTime = calculateNewTime(mealTime, random);
            Meal dinnerMeal = new Meal(mealTime, "Обед", 500);
            mealRepository.insert(dinnerMeal);
            mealTime = calculateNewTime(mealTime, random);
            Meal eveningMeal = new Meal(mealTime, "Ужин", 50 + random.nextInt(50));
            mealRepository.insert(eveningMeal);
            mealTime = calculateNewTime(mealTime, random);
            Meal breakfastMeal = new Meal(mealTime, "Завтрак", 100);
            mealRepository.insert(breakfastMeal);
        }
    }
}
