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
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;

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
    public void getAllFromUnknownUser() {
        List<Meal> meals = mealService.getAll(USER_UNKNOWN);
        assertEquals(0, meals.size());
    }

    @Test
    public void getAllFromRegularUser() {
        List<Meal> meals = mealService.getAll(USER_ID);
        assertEquals(MEAL_TEST_DATA_SIZE, meals.size());
        int lastIndex = meals.size() - 1;
        assertEquals(MEAL_30_1.getDescription(), meals.get(lastIndex).getDescription());
        assertEquals(MEAL_30_1.getDateTime(), meals.get(lastIndex).getDateTime());
        assertEquals(MEAL_30_1.getCalories(), meals.get(lastIndex).getCalories());
        assertEquals(MEAL_30_2.getDescription(), meals.get(lastIndex - 1).getDescription());
        assertEquals(MEAL_30_2.getDateTime(), meals.get(lastIndex - 1).getDateTime());
        assertEquals(MEAL_30_2.getCalories(), meals.get(lastIndex - 1).getCalories());
        assertEquals(MEAL_31_2.getDescription(), meals.get(1).getDescription());
        assertEquals(MEAL_31_2.getDateTime(), meals.get(1).getDateTime());
        assertEquals(MEAL_31_2.getCalories(), meals.get(1).getCalories());
        assertEquals(MEAL_31_3.getDescription(), meals.get(0).getDescription());
        assertEquals(MEAL_31_3.getDateTime(), meals.get(0).getDateTime());
        assertEquals(MEAL_31_3.getCalories(), meals.get(0).getCalories());
    }

    @Test(expected = NotFoundException.class)
    public void getOneMealFromUnknownUser() {
        mealService.get(MEAL_30_1.getId(), USER_UNKNOWN);
    }

    @Test
    public void getOneMealFromRegularUser() {
        Meal meal = mealService.get(MEAL_30_1.getId(), USER_ID);
        assertEquals(MEAL_30_1.getCalories(), meal.getCalories());
        assertEquals(MEAL_30_1.getDateTime(), meal.getDateTime());
        assertEquals(MEAL_30_1.getDescription(), meal.getDescription());
    }

    @Test
    public void getOneMoreMealFromRegularUser() {
        Meal meal = mealService.get(MEAL_31_2.getId(), USER_ID);
        assertEquals(MEAL_31_2.getCalories(), meal.getCalories());
        assertEquals(MEAL_31_2.getDateTime(), meal.getDateTime());
        assertEquals(MEAL_31_2.getDescription(), meal.getDescription());
    }

    @Test(expected = NotFoundException.class)
    public void deleteFromUnknownUser() {
        mealService.delete(MEAL_31_1.getId(), USER_UNKNOWN);
    }

    @Test(expected = NotFoundException.class)
    public void deleteFromRegularUser() {
        mealService.delete(MEAL_30_3.getId(), USER_ID);
        Meal meal = mealService.get(MEAL_30_3.getId(), USER_ID);
        assertNull(meal);
    }

    @Test(expected = NotFoundException.class)
    public void updateFromUnknownUser() {
        final int NEW_CALORIES = 100500;
        final String NEW_DESCRIPTION = "Новое описание";
        final LocalDateTime NEW_DATE_TIME = LocalDateTime.of(2200, 12, 2, 21, 34);

        Meal meal = mealService.get(MEAL_30_3.getId(), USER_ID);
        meal.setCalories(NEW_CALORIES);
        meal.setDescription(NEW_DESCRIPTION);
        meal.setDateTime(NEW_DATE_TIME);
        mealService.update(meal, USER_UNKNOWN);
    }

    @Test
    public void updateFromRegularUser() {
        final int NEW_CALORIES = 100500;
        final String NEW_DESCRIPTION = "Новое описание";
        final LocalDateTime NEW_DATE_TIME = LocalDateTime.of(2200, 12, 2, 21, 34);

        Meal meal = mealService.get(MEAL_30_3.getId(), USER_ID);
        meal.setCalories(NEW_CALORIES);
        meal.setDescription(NEW_DESCRIPTION);
        meal.setDateTime(NEW_DATE_TIME);
        mealService.update(meal, USER_ID);

        Meal mealNew = mealService.get(MEAL_30_3.getId(), USER_ID);
        assertNotNull(mealNew);
        assertEquals(NEW_CALORIES, mealNew.getCalories());
        assertEquals(NEW_DESCRIPTION, mealNew.getDescription());
        assertEquals(NEW_DATE_TIME, mealNew.getDateTime());
    }

    @Test
    public void filterDatesOutOfBoundFromUnknownUser() {
        List<Meal> meals = mealService.getBetweenDates(
                MEAL_31_3.getDate().plusYears(100),
                MEAL_31_3.getDate().plusYears(101),
                USER_UNKNOWN);
        assertNotNull(meals);
        assertEquals(0, meals.size());
    }

    @Test
    public void filterDatesOutOfBoundFromRegularUser() {
        List<Meal> meals = mealService.getBetweenDates(
                MEAL_31_3.getDate().plusYears(100),
                MEAL_31_3.getDate().plusYears(101),
                USER_ID);
        assertNotNull(meals);
        assertEquals(0, meals.size());
    }

    @Test
    public void filterDatesInBoundFromUnknownUser() {
        List<Meal> meals = mealService.getBetweenDates(
                MEAL_30_1.getDate(),
                MEAL_31_3.getDate(),
                USER_UNKNOWN);
        assertNotNull(meals);
        assertEquals(0, meals.size());
    }

    @Test
    public void filterDatesFullInBoundFromRegularUser() {
        List<Meal> meals = mealService.getBetweenDates(
                MEAL_30_1.getDate(),
                MEAL_31_3.getDate(),
                USER_ID);
        assertNotNull(meals);
        assertEquals(MEAL_TEST_DATA_SIZE, meals.size());
        int lastIndex = meals.size() - 1;
        assertEquals(MEAL_30_1.getDescription(), meals.get(lastIndex).getDescription());
        assertEquals(MEAL_30_1.getDateTime(), meals.get(lastIndex).getDateTime());
        assertEquals(MEAL_30_1.getCalories(), meals.get(lastIndex).getCalories());
        assertEquals(MEAL_31_3.getDescription(), meals.get(0).getDescription());
        assertEquals(MEAL_31_3.getDateTime(), meals.get(0).getDateTime());
        assertEquals(MEAL_31_3.getCalories(), meals.get(0).getCalories());
    }

    @Test
    public void filterDatesPartiallyInBoundFromRegularUser() {
        List<Meal> meals = mealService.getBetweenDates(
                MEAL_30_1.getDate(),
                MEAL_30_3.getDate(),
                USER_ID);
        assertNotNull(meals);
        assertEquals(3, meals.size());
        int lastIndex = meals.size() - 1;
        assertEquals(MEAL_30_1.getDescription(), meals.get(lastIndex).getDescription());
        assertEquals(MEAL_30_1.getDateTime(), meals.get(lastIndex).getDateTime());
        assertEquals(MEAL_30_1.getCalories(), meals.get(lastIndex).getCalories());
        assertEquals(MEAL_30_3.getDescription(), meals.get(0).getDescription());
        assertEquals(MEAL_30_3.getDateTime(), meals.get(0).getDateTime());
        assertEquals(MEAL_30_3.getCalories(), meals.get(0).getCalories());
    }

    @Test
    public void filterTimeOutOfBoundFromUnknownUser() {
        List<Meal> meals = mealService.getBetweenDateTimes(
                MEAL_31_3.getDateTime().plusYears(1).plusDays(100),
                MEAL_31_3.getDateTime().plusYears(1).plusDays(100).plusHours(10),
                USER_UNKNOWN);
        assertNotNull(meals);
        assertEquals(0, meals.size());
    }

    @Test
    public void filterTimeOutOfBoundFromRegularUser() {
        List<Meal> meals = mealService.getBetweenDateTimes(
                MEAL_31_3.getDateTime().plusYears(1).plusDays(100),
                MEAL_31_3.getDateTime().plusYears(1).plusDays(100).plusHours(10),
                USER_ID);
        assertNotNull(meals);
        assertEquals(0, meals.size());
    }

    @Test
    public void filterTimeInBoundFromUnknownUser() {
        List<Meal> meals = mealService.getBetweenDateTimes(
                MEAL_30_1.getDateTime().minusMinutes(20),
                MEAL_31_1.getDateTime().plusMinutes(40),
                USER_UNKNOWN);
        assertNotNull(meals);
        assertEquals(0, meals.size());
    }
    
    @Test
    public void filterTimeInBoundOneFromRegularUser() {
        List<Meal> meals = mealService.getBetweenDateTimes(
                MEAL_30_1.getDateTime().minusMinutes(20),
                MEAL_30_1.getDateTime().plusMinutes(20),
                USER_ID);
        assertNotNull(meals);
        assertEquals(3, meals.size());
        assertEquals(MEAL_30_3.getDescription(), meals.get(0).getDescription());
        assertEquals(MEAL_30_3.getDateTime(), meals.get(0).getDateTime());
        assertEquals(MEAL_30_3.getCalories(), meals.get(0).getCalories());
        assertEquals(MEAL_30_1.getDescription(), meals.get(2).getDescription());
        assertEquals(MEAL_30_1.getDateTime(), meals.get(2).getDateTime());
        assertEquals(MEAL_30_1.getCalories(), meals.get(2).getCalories());

    }

    @Test
    public void filterShouldDependsOnDaysNotTime() {
        
    }
}