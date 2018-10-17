package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by taras on 2018-10-17.
 */

@RunWith(MockitoJUnitRunner.class)
public class MealServiceImplTest {
    @Mock
    private MealRepository mealRepository;

    private MealService mealService;

    @Before
    public void setUp() {
        mealService = new MealServiceImpl(mealRepository);
    }

    @Test
    public void checkNoMealForOtherDays() {
        when(mealRepository.findByDaysBetween(MealsUtil.MEALS.get(0).getDate(), MealsUtil.MEALS.get(5).getDate(), 1))
                .thenReturn(MealsUtil.MEALS);

        List<Meal> meals = mealService.findAllWhereDaysBetween(LocalDate.of(2100, 1, 1),
                LocalDate.of(2200, 1, 1),
                1);
        assertNotNull(meals);
        assertEquals(0, meals.size());
    }
}