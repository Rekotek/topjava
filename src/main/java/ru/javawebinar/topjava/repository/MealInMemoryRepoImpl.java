package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by taras on 2018-10-08.
 */

public class MealInMemoryRepoImpl implements MealRepository {
    private static final Logger log = getLogger(MealInMemoryRepoImpl.class);
    private static final MealRepository INSTANCE = new MealInMemoryRepoImpl();

    private AtomicInteger idGenerator = new AtomicInteger(0);
    private Map<Integer, Meal> mealStore = new ConcurrentHashMap<>();

    public static MealRepository getInstance() {
        return INSTANCE;
    }

    private MealInMemoryRepoImpl() {
    }

    @Override
    public List<Meal> findAll() {
        return new ArrayList<>(mealStore.values());
    }

    @Override
    public Meal findById(int id) {
        Meal mealInDB = mealStore.get(id);
        Meal meal = new Meal(mealInDB.getDateTime(), mealInDB.getDescription(), mealInDB.getCalories());
        meal.setId(mealInDB.getId());
        return meal;
    }

    @Override
    public int count() {
        return mealStore.size();
    }

    @Override
    public void insert(Meal meal) {
        Integer newId = idGenerator.incrementAndGet();
        meal.setId(newId);
        log.debug("New Atomic ID = {}", newId);
        mealStore.put(newId, meal);
    }

    @Override
    public void update(Meal meal) throws NoSuchElementException {
        mealStore.replace(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        log.debug("Delete Meal with ID = {}", id);
        mealStore.remove(id);
    }

    @Override
    public void deleteAll() {
        mealStore.clear();
        idGenerator.set(0);
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

    private static LocalDateTime calculateNewTime(LocalDateTime mealTime, Random random) {
        return mealTime.plusMinutes(8 * 60 + random.nextInt(10) - 5);
    }
}
