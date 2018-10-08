package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by taras on 2018-10-08.
 */

public class MealInMemoryImpl implements MealRepository {
    private static final Logger log = getLogger(MealInMemoryImpl.class);

    private static AtomicInteger id = new AtomicInteger(0);
    private static Map<Integer, Meal> mealStore = new HashMap<>();

    public List<Meal> findAll() {
        return new ArrayList<>(mealStore.values());
    }

    @Override
    public int count() {
        return mealStore.size();
    }

    @Override
    public Meal add(Meal meal) {
        Integer newId = id.incrementAndGet();
        meal.setId(newId);
        log.debug("New Atomic ID = {}", newId);
        mealStore.put(newId, meal);
        return meal;
    }

    @Override
    public Meal update(Meal meal) {
        return null;
    }

    @Override
    public void delete(int meal_id) {
        mealStore.remove(meal_id);
    }
}
