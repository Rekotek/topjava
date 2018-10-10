package ru.javawebinar.topjava.repository;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static org.junit.Assert.*;


/**
 * Created by taras on 2018-10-09.
 */

public class MealRepositoryTest {
    private static final LocalDateTime DATE_TIME_1 = LocalDateTime.of(2010, 1, 1, 20, 30);
    private static final String DESCRIPTION_1 = "Ужин";
    private static final int CALORIES_1 = 300;
    private static final LocalDateTime DATE_TIME_2 = LocalDateTime.of(2011, 2, 4, 10, 12);
    private static final String DESCRIPTION_2 = "Завтрак";
    private static final int CALORIES_2 = 100;

    private MealRepository mealRepository = MealInMemoryRepoImpl.getInstance();

    @Before
    public void setUp() {
        mealRepository.deleteAll();
    }

    @Test
    public void emptyRepoCountsZero() {
        int count = mealRepository.count();
        assertEquals(0, count);
    }

    @Test
    public void addOneRecordAndItShouldBeCountedAndFind() {
        Meal originalMeal = new Meal(DATE_TIME_1, DESCRIPTION_1, CALORIES_1);
        mealRepository.insert(originalMeal);
        int count = mealRepository.count();
        List<Meal> mealList = mealRepository.findAll();
        assertEquals(1, count);
        Meal foundMeal = mealList.get(0);
        assertEquals(originalMeal, foundMeal);
        assertEquals(originalMeal.getDateTime(), DATE_TIME_1);
        assertEquals(originalMeal.getCalories(), CALORIES_1);
        assertEquals(originalMeal.getDescription(), DESCRIPTION_1);
    }

    @Test
    public void addTwoRecordsAndTheyShouldBeCountedAndFind() {
        Meal originalMeal_1 = new Meal(DATE_TIME_1, DESCRIPTION_1, CALORIES_1);
        Meal originalMeal_2 = new Meal(DATE_TIME_2, DESCRIPTION_2, CALORIES_2);

        mealRepository.insert(originalMeal_1);
        mealRepository.insert(originalMeal_2);

        int count = mealRepository.count();
        List<Meal> mealList = mealRepository.findAll();
        assertEquals(2, count);
        assertTrue(mealList.contains(originalMeal_1));
        assertTrue(mealList.contains(originalMeal_2));
    }

    @Test
    public void addTwoRecordsAndDeleteOne() {
        Meal originalMeal_1 = new Meal(DATE_TIME_1, DESCRIPTION_1, CALORIES_1);
        Meal originalMeal_2 = new Meal(DATE_TIME_2, DESCRIPTION_2, CALORIES_2);
        mealRepository.insert(originalMeal_1);
        mealRepository.insert(originalMeal_2);
        mealRepository.delete(originalMeal_2.getId());

        int count = mealRepository.count();
        List<Meal> mealList = mealRepository.findAll();
        assertEquals(1, count);
        assertTrue(mealList.contains(originalMeal_1));
    }

    @Test
    public void addOneRecordAndUpdate() {
        Meal originalMeal_1 = new Meal(DATE_TIME_1, DESCRIPTION_1, CALORIES_1);
        Meal originalMeal_2 = new Meal(DATE_TIME_2, DESCRIPTION_2, CALORIES_2);
        mealRepository.insert(originalMeal_1);
        mealRepository.insert(originalMeal_2);

        String newDescription = originalMeal_1 + " - NEW";
        originalMeal_1.setDescription(newDescription);
        int newCalories = originalMeal_1.getCalories() + 200;
        originalMeal_1.setCalories(newCalories);
        LocalDateTime newDate = originalMeal_1.getDateTime().plusHours(10);
        originalMeal_1.setDateTime(newDate);

        mealRepository.update(originalMeal_1);
        int count = mealRepository.count();
        assertEquals(2, count);
        Meal foundMeal = mealRepository.findAll().get(0);
        assertNotEquals(originalMeal_1.getCalories(), CALORIES_1);
        assertNotEquals(originalMeal_1.getDescription(), DESCRIPTION_1);
        assertNotEquals(originalMeal_1.getDateTime(), DATE_TIME_1);
        assertEquals(originalMeal_1, foundMeal);
    }

    @Test
    public void deleteWithMultiThreading() throws InterruptedException, ExecutionException, TimeoutException {
        final int AFFECTED_RECORD_COUNT = 20;

        MealInMemoryRepoImpl.setUpTestData(mealRepository);
        int oldCount = mealRepository.count();

        new ForkJoinPool(10).submit(() ->
                IntStream.iterate(1, i -> i + 3).limit(AFFECTED_RECORD_COUNT)
                        .parallel()
                        .forEach(i -> mealRepository.delete(i))
        ).get(10, TimeUnit.SECONDS);

        int newCount = mealRepository.count();
        assertEquals(oldCount - AFFECTED_RECORD_COUNT, newCount);
    }

    @Test
    public void deleteAndInsertWithMultiThreading() throws InterruptedException, ExecutionException, TimeoutException {
        final int AFFECTED_RECORD_COUNT = 80;
        final LocalDateTime INITIAL_DATE_TIME = LocalDateTime.of(2001, 1, 1, 20, 20);
        final int CALORIES = 8000;

        MealInMemoryRepoImpl.setUpTestData(mealRepository);
        int oldCount = mealRepository.count();

        new ForkJoinPool(10).submit(() ->
                IntStream.iterate(1, i -> i + 1).limit(AFFECTED_RECORD_COUNT)
                        .parallel()
                        .forEach(i -> {
                            mealRepository.delete(i);
                            if (i % 2 == 0) {
                                Meal meal = new Meal(
                                        INITIAL_DATE_TIME.plusDays(i),
                                        "Еда №" + i,
                                        CALORIES);
                                mealRepository.insert(meal);
                            }
                        })
        ).get(10, TimeUnit.SECONDS);

        int newCount = mealRepository.count();

        assertEquals(oldCount - AFFECTED_RECORD_COUNT / 2, newCount);
        assertEquals(CALORIES, mealRepository.findAll().get(newCount - 1).getCalories());
    }

}