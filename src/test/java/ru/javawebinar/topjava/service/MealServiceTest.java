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
import static org.assertj.core.api.Assertions.assertThat;
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
    private static final int NEW_CALORIES = 100500;
    private static final String NEW_DESCRIPTION = "Новое описание";
    private static final LocalDateTime NEW_DATE_TIME = LocalDateTime.of(2200, 12, 2, 21, 34);

    @Autowired
    private MealService mealService;

    @Test
    public void getAllFromOtherUser() {
        List<Meal> meals = mealService.getAll(ADMIN_ID);
        assertEquals(0, meals.size());
    }

    @Test
    public void getAllFromRegularUser() {
        List<Meal> meals = mealService.getAll(USER_ID);
        assertEquals(MEAL_TEST_DATA_SIZE, meals.size());
        int lastIndex = meals.size() - 1;
        assertThat(meals.get(lastIndex)).isEqualToComparingFieldByField(MEAL_30_1);
        assertThat(meals.get(lastIndex - 1)).isEqualToComparingFieldByField(MEAL_30_2);
        assertThat(meals.get(1)).isEqualToComparingFieldByField(MEAL_31_2);
        assertThat(meals.get(0)).isEqualToComparingFieldByField(MEAL_31_3);
    }

    @Test(expected = NotFoundException.class)
    public void getOneFromOtherUser() {
        mealService.get(MEAL_30_1.getId(), ADMIN_ID);
    }

    @Test
    public void getOneFromRegularUser() {
        Meal meal = mealService.get(MEAL_30_1.getId(), USER_ID);
        assertThat(meal).isEqualToComparingFieldByField(MEAL_30_1);
    }

    @Test(expected = NotFoundException.class)
    public void deleteFromOtherUser() {
        mealService.delete(MEAL_31_1.getId(), ADMIN_ID);
    }

    @Test
    public void deleteFromRegularUser() {
        int oldSize = mealService.getAll(USER_ID).size();
        mealService.delete(MEAL_30_3.getId(), USER_ID);
        List<Meal> meals = mealService.getAll(USER_ID);
        assertFalse(meals.contains(MEAL_30_3));
        assertEquals(meals.size(), oldSize - 1);
    }

    private Meal getAndUpdateMeal() {
        Meal meal = mealService.get(MEAL_30_3.getId(), USER_ID);
        meal.setCalories(NEW_CALORIES);
        meal.setDescription(NEW_DESCRIPTION);
        meal.setDateTime(NEW_DATE_TIME);
        return meal;
    }

    @Test(expected = NotFoundException.class)
    public void updateFromOtherUser() {
        Meal meal = getAndUpdateMeal();
        mealService.update(meal, ADMIN_ID);
    }

    @Test
    public void updateFromRegularUser() {
        Meal meal = getAndUpdateMeal();
        mealService.update(meal, USER_ID);

        Meal mealNew = mealService.get(MEAL_30_3.getId(), USER_ID);
        assertEquals(NEW_CALORIES, mealNew.getCalories());
        assertEquals(NEW_DESCRIPTION, mealNew.getDescription());
        assertEquals(NEW_DATE_TIME, mealNew.getDateTime());
    }

    @Test
    public void filterDatesOutOfBoundFromOtherUser() {
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
    public void filterDatesInBoundFromOtherUser() {
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
        assertEquals(MEAL_TEST_DATA_SIZE, meals.size());
        int lastIndex = meals.size() - 1;
        assertThat(meals.get(lastIndex)).isEqualToComparingFieldByField(MEAL_30_1);
        assertThat(meals.get(0)).isEqualToComparingFieldByField(MEAL_31_3);
    }

    @Test
    public void filterDatesPartiallyInBoundFromRegularUser() {
        List<Meal> meals = mealService.getBetweenDates(
                MEAL_30_1.getDate(),
                MEAL_30_3.getDate(),
                USER_ID);
        assertEquals(3, meals.size());
        int lastIndex = meals.size() - 1;
        assertThat(meals.get(lastIndex)).isEqualToComparingFieldByField(MEAL_30_1);
        assertThat(meals.get(0)).isEqualToComparingFieldByField(MEAL_30_3);
        assertEquals(MEAL_30_3.getCalories(), meals.get(0).getCalories());
    }

    @Test
    public void filterTimeOutOfBoundFromOtherUser() {
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
    public void filterTimeInBoundFromOtherUser() {
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
        assertEquals(3, meals.size());
        assertThat(meals.get(0)).isEqualToComparingFieldByField(MEAL_30_3);
        assertThat(meals.get(2)).isEqualToComparingFieldByField(MEAL_30_1);
    }

    @Test
    public void filterTimeInBoundTwoDaysFromRegularUser() {
        List<Meal> meals = mealService.getBetweenDateTimes(
                MEAL_30_1.getDateTime().minusMinutes(20),
                MEAL_31_1.getDateTime().minusMinutes(10),
                USER_ID);
        assertEquals(6, meals.size());
        assertThat(meals.get(0)).isEqualToComparingFieldByField(MEAL_31_3);
        assertThat(meals.get(5)).isEqualToComparingFieldByField(MEAL_30_1);
    }
}