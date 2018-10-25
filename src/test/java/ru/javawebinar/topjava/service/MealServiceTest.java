package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.helper.AssertEx.assertMatchEx;
import static ru.javawebinar.topjava.util.DateTimeUtil.MAX_POSTGRESQL_TIME;

/**
 * Created by taras on 2018-10-21.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml",
        "classpath:spring/spring-jdbc.xml"
})
@Sql(scripts = {"classpath:db/populateDB.sql"}, config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    @Autowired
    private MealService mealService;

    @Test
    public void getEmptyListFromUserWhoHasNoMeal() {
        List<Meal> meals = mealService.getAll(ADMIN_ID);
        assertEquals(0, meals.size());
    }

    @Test
    public void getAllFromRegularUser() {
        List<Meal> meals = mealService.getAll(USER_ID);
        assertMatchEx(MEAL_LIST_REVERSED, meals);
    }

    @Test(expected = NotFoundException.class)
    public void tryingToGetOneSomebodyElseRecord() {
        mealService.get(MEAL_30_1.getId(), ADMIN_ID);
    }

    @Test
    public void getOneFromRegularUser() {
        Meal meal = mealService.get(MEAL_30_1.getId(), USER_ID);
        assertMatchEx(meal, MEAL_30_1);
    }

    @Test(expected = NotFoundException.class)
    public void tryingToDeleteSomebodyElseRecord() {
        mealService.delete(MEAL_31_1.getId(), ADMIN_ID);
    }

    @Test
    public void deleteFromRegularUser() {
        mealService.delete(MEAL_30_3.getId(), USER_ID);
        List<Meal> meals = mealService.getAll(USER_ID);
        assertFalse(meals.contains(MEAL_30_3));
        assertEquals(MEAL_LIST_REVERSED.size() - 1, meals.size());
    }

    @Test(expected = NotFoundException.class)
    public void tryingToUpdateSomebodyElseRecord() {
        mealService.update(MEAL_30_2_UPDATED, ADMIN_ID);
    }

    @Test
    public void updateFromRegularUser() {
        mealService.update(MEAL_30_2_UPDATED, USER_ID);
        Meal mealNew = mealService.get(MEAL_30_2_UPDATED.getId(), USER_ID);
        assertMatchEx(mealNew, MEAL_30_2_UPDATED);
    }

    @Test
    public void filterDatesOutOfBoundFromUserWithoutMeal() {
        List<Meal> meals = mealService.getBetweenDates(
                MEAL_31_3.getDate().plusYears(100),
                MEAL_31_3.getDate().plusYears(101),
                ADMIN_ID);
        assertEquals(0, meals.size());
    }

    @Test
    public void filterDatesOutOfBoundFromRegularUser() {
        List<Meal> meals = mealService.getBetweenDates(
                MEAL_31_3.getDate().plusYears(100),
                MEAL_31_3.getDate().plusYears(101),
                USER_ID);
        assertEquals(0, meals.size());
    }

    @Test
    public void filterDatesInBoundFromUserWithoutMeal() {
        List<Meal> meals = mealService.getBetweenDates(
                MEAL_30_1.getDate(),
                MEAL_31_3.getDate(),
                ADMIN_ID);
        assertEquals(0, meals.size());
    }

    @Test
    public void filterDatesFullInBoundFromRegularUser() {
        List<Meal> meals = mealService.getBetweenDates(
                MEAL_30_1.getDate(),
                MEAL_31_3.getDate(),
                USER_ID);
        assertMatchEx(meals, MEAL_LIST_REVERSED);
    }

    @Test
    public void filterDatesPartiallyInBoundFromRegularUser() {
        List<Meal> meals = mealService.getBetweenDates(
                MEAL_30_1.getDate(),
                MEAL_30_3.getDate(),
                USER_ID);
        assertMatchEx(meals, asList(MEAL_30_3, MEAL_30_2, MEAL_30_1));
    }

    @Test
    public void filterTimeOutOfBoundFromUserWithoutMeal() {
        List<Meal> meals = mealService.getBetweenDateTimes(
                MEAL_31_3.getDateTime().plusYears(1).plusDays(100),
                MEAL_31_3.getDateTime().plusYears(1).plusDays(100).plusHours(10),
                ADMIN_ID);
        assertEquals(0, meals.size());
    }

    @Test
    public void filterTimeOutOfBoundFromRegularUser() {
        List<Meal> meals = mealService.getBetweenDateTimes(
                MEAL_31_3.getDateTime().plusYears(1).plusDays(100),
                MEAL_31_3.getDateTime().plusYears(1).plusDays(100).plusHours(10),
                USER_ID);
        assertEquals(0, meals.size());
    }

    @Test
    public void filterTimeInBoundFromUserWithoutMeal() {
        List<Meal> meals = mealService.getBetweenDateTimes(
                MEAL_30_1.getDateTime().minusMinutes(20),
                MEAL_31_1.getDateTime().plusMinutes(40),
                ADMIN_ID);
        assertEquals(0, meals.size());
    }
    
    @Test
    public void filterTimeInBoundOneBreakfastFromRegularUser() {
        List<Meal> meals = mealService.getBetweenDateTimes(
                MEAL_30_1.getDateTime().minusMinutes(20),
                MEAL_30_1.getDateTime().plusMinutes(20),
                USER_ID);
        assertMatchEx(meals, asList(MEAL_30_1));
    }

    @Test
    public void filterTimeInBoundTwoDaysFromRegularUser() {
        List<Meal> meals = mealService.getBetweenDateTimes(
                MEAL_30_1.getDateTime().minusMinutes(20),
                MEAL_31_1.getDateTime().minusMinutes(10),
                USER_ID);
        assertMatchEx(meals, asList(MEAL_30_3, MEAL_30_2, MEAL_30_1));
    }

    @Test
    public void filterLocalTimeMax() {
        final LocalDateTime END_OF_THE_DAY = LocalDateTime.of(MEAL_30_3.getDate(), MAX_POSTGRESQL_TIME);
        Meal updatedMeal = new Meal(MEAL_30_3.getId(), END_OF_THE_DAY, MEAL_30_3.getDescription(), MEAL_30_3.getCalories());
        mealService.update(updatedMeal, USER_ID);
        List<Meal> meals = mealService.getBetweenDateTimes(
                MEAL_30_1.getDateTime(),
                updatedMeal.getDateTime(),
                USER_ID);
        assertMatchEx(meals, asList(updatedMeal, MEAL_30_2, MEAL_30_1));

    }
}