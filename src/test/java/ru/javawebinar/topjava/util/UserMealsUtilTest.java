package ru.javawebinar.topjava.util;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by taras on 2018-09-27.
 */

public class UserMealsUtilTest {
    private static final String NEW_YEAR_STR = "Новый Год!";
    private final List<UserMeal> mealList = Arrays.asList(
            new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
            new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
            new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
            new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
            new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
            new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510),
            new UserMeal(LocalDateTime.of(2015, Month.MAY, 1, 11, 10), "Завтрак", 1000),
            new UserMeal(LocalDateTime.of(2015, Month.MAY, 1, 13, 0), "Обед", 500),
            new UserMeal(LocalDateTime.of(2015, Month.MAY, 1, 20, 0), "Ужин", 510),
            new UserMeal(LocalDateTime.of(2015, Month.JUNE, 3, 11, 10), "Завтрак", 100),
            new UserMeal(LocalDateTime.of(2015, Month.JUNE, 3, 13, 0), "Обед", 200),
            new UserMeal(LocalDateTime.of(2015, Month.JUNE, 3, 20, 0), "Ужин", 330)
    );

    private void flashUserMeal() {
        UserMeal.setCaloriesCount(0);
        UserMeal.setDescCount(0);
        UserMeal.setLocalDateCount(0);
    }

    @Before
    public void setUp() {
        System.out.println("----");
        flashUserMeal();
    }

    @Test
    public void shouldReturnEmptyListFromEmptySource() {
        List<UserMealWithExceed> meals = UserMealsUtil.getFilteredWithExceeded(new ArrayList<>(),
                LocalTime.of(0, 0), LocalTime.of(0, 1), 1000);
        assertNotNull(meals);
        assertEquals(0, meals.size());
    }

    @Test
    public void shouldReturnEmptyListWhenDidntMatch() {
        List<UserMealWithExceed> meals = UserMealsUtil.getFilteredWithExceeded(mealList,
                LocalTime.of(0, 0), LocalTime.of(0, 1), 1000);
        assertNotNull(meals);
        assertEquals(0, meals.size());

        printUserMealStaticFields();
    }

    @Test
    public void findExactlyTwoMealsWithNoExceeded() {
        List<UserMealWithExceed> meals = UserMealsUtil.getFilteredWithExceeded(mealList,
                LocalTime.of(9, 59), LocalTime.of(10, 1), 10000);
        assertEquals(2, meals.size());
        assertEquals(30, meals.get(0).getDateTime().getDayOfMonth());
        assertEquals(31, meals.get(1).getDateTime().getDayOfMonth());
        for (UserMealWithExceed meal : meals) {
            assertFalse(meal.isExceed());
            assertEquals(2015, meal.getDateTime().getYear());
            assertEquals(Month.MAY, meal.getDateTime().getMonth());
            assertEquals("Завтрак", meal.getDescription());
            assertEquals(10, meal.getDateTime().getHour());
            assertEquals(0, meal.getDateTime().getMinute());
        }
        meals.forEach(System.out::println);
    }

    @Test
    public void findExactlyTwoMealsWithExceeded() {
        List<UserMealWithExceed> meals = UserMealsUtil.getFilteredWithExceeded(mealList,
                LocalTime.of(9, 59), LocalTime.of(10, 1), 10);
        assertEquals(2, meals.size());
        assertEquals(30, meals.get(0).getDateTime().getDayOfMonth());
        assertEquals(31, meals.get(1).getDateTime().getDayOfMonth());
        for (UserMealWithExceed meal : meals) {
            assertTrue(meal.isExceed());
            assertEquals(2015, meal.getDateTime().getYear());
            assertEquals(Month.MAY, meal.getDateTime().getMonth());
            assertEquals("Завтрак", meal.getDescription());
            assertEquals(10, meal.getDateTime().getHour());
            assertEquals(0, meal.getDateTime().getMinute());
        }

        meals.forEach(System.out::println);
        printUserMealStaticFields();
    }

    @Test
    public void findAllRecords() {
        List<UserMealWithExceed> meals = UserMealsUtil.getFilteredWithExceeded(mealList,
                LocalTime.of(0, 0), LocalTime.of(23, 59), 1000);

        assertEquals(mealList.size(), meals.size());

        printUserMealStaticFields();
    }

    private void printUserMealStaticFields() {
        System.out.println("Calories count: " + UserMeal.getCaloriesCount());
        System.out.println("DateTime count: " + UserMeal.getLocalDateCount());
        System.out.println("Description count: " + UserMeal.getDescCount());
    }

    private List<UserMeal> generateBigData() {
        List<UserMeal> bigData = new ArrayList<>(301);
        UserMeal userMeal = new UserMeal(
                LocalDateTime.of(2010, Month.JANUARY, 1, 0, 1),
                NEW_YEAR_STR,
                2000);
        bigData.add(userMeal);
        LocalDateTime indexDateTime = LocalDateTime.of(2010, Month.JANUARY, 1, 7, 0);
        for (int i = 0; i < 100; i++) {
            userMeal = new UserMeal(indexDateTime, "Завтрак", 200);
            bigData.add(userMeal);
            indexDateTime = indexDateTime.plusHours(8);
            userMeal = new UserMeal(indexDateTime, "Обед", 500);
            bigData.add(userMeal);
            indexDateTime = indexDateTime.plusHours(8);
            userMeal = new UserMeal(indexDateTime, "Ужин", 300);
            bigData.add(userMeal);
            indexDateTime = indexDateTime.plusHours(8);
        }

        return bigData;
    }

    @Test
    public void findAllFromBigData() {
        List<UserMeal> mealList = generateBigData();

        List<UserMealWithExceed> meals = UserMealsUtil.getFilteredWithExceeded(mealList,
                LocalTime.of(0, 0), LocalTime.of(23, 59), 1000);

        assertEquals(mealList.size(), meals.size());

        assertTrue(meals.get(0).isExceed());
        assertTrue(meals.get(1).isExceed());
        assertTrue(meals.get(2).isExceed());
        assertTrue(meals.get(3).isExceed());

        assertFalse(meals.get(4).isExceed());

        assertEquals(mealList.get(1).getDateTime(), meals.get(1).getDateTime());
        assertEquals(mealList.get(1).getDescription(), meals.get(1).getDescription());
        assertEquals(mealList.get(1).getCalories(), meals.get(1).getCalories());
        assertEquals(mealList.get(2).getDateTime(), meals.get(2).getDateTime());
        assertEquals(mealList.get(3).getDateTime(), meals.get(3).getDateTime());

        assertEquals(mealList.get(100).getDateTime(), meals.get(100).getDateTime());
        assertEquals(mealList.get(100).getDescription(), meals.get(100).getDescription());
        assertEquals(mealList.get(100).getCalories(), meals.get(100).getCalories());
        assertFalse(meals.get(100).isExceed());

        printUserMealStaticFields();
    }

    @Test
    public void findOnlyOneFromBigData() {
        List<UserMeal> mealList = generateBigData();

        List<UserMealWithExceed> meals = UserMealsUtil.getFilteredWithExceeded(mealList,
                LocalTime.of(0, 0), LocalTime.of(0, 2), 1000);

        assertEquals(1, meals.size());
        assertEquals(NEW_YEAR_STR, meals.get(0).getDescription());
        assertTrue(meals.get(0).isExceed());

        printUserMealStaticFields();
    }
}