package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = getLogger(InMemoryMealRepositoryImpl.class);
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, SecurityUtil.authUserId()));
    }

    private boolean checkUserIdNotPresent(int userId) {
        if (!repository.containsKey(userId)) {
            log.error("There is no such user in meal repository. UserID = {}", userId);
            return true;
        }
        return false;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew() && !repository.containsKey(userId)) {
            log.error("Trying to update meal but no corresponding user found! Meal = {}; userId = {}", meal, userId);
            return null;
        }

        Map<Integer, Meal> mealMap;
        if (!repository.containsKey(userId)) {
            mealMap = new HashMap<>();
        } else {
            mealMap = repository.get(userId);
        }
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        } else {
            if (!mealMap.containsKey(meal.getId())) {
                log.error("Trying to update non-existed record, meal = {}", meal);
                return null;
            }
        }
        mealMap.put(meal.getId(), meal);
        repository.put(userId, mealMap);
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("Trying to delete meal with id = {}; userId = {}", id, userId);
        if (checkUserIdNotPresent(userId)) return false;
        Map<Integer, Meal> mealMap = repository.get(userId);
        boolean wasExisted = mealMap.remove(id) != null;
        if (wasExisted) {
            repository.put(userId, mealMap);
        }
        return wasExisted;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("Get with id = {}; userId = {}", id, userId);
        if (checkUserIdNotPresent(userId)) return null;
        return repository.get(userId).get(id);
    }


    @Override
    public Collection<Meal> getAll(int userId) {
        log.info("Method getAll() with userId = {}", userId);
        if (checkUserIdNotPresent(userId)) return Collections.emptyList();
        Map<Integer, Meal> mealMap = repository.get(userId);
        return mealMap.values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

