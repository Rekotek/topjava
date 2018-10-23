package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

/**
 * Created by taras on 2018-10-21.
 */

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final Meal MEAL_30_1 = new Meal(START_SEQ + 2, LocalDateTime.of(2015, Month.MAY, 30, 10, 0),
            "Завтрак", 500);
    public static final Meal MEAL_30_2 = new Meal(START_SEQ + 3, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед"
            , 1000);
    public static final Meal MEAL_30_3 = new Meal(START_SEQ + 4, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин"
            , 500);
    public static final Meal MEAL_31_1 = new Meal(START_SEQ + 5, LocalDateTime.of(2015, Month.MAY, 31, 10, 0),
            "Завтрак", 1000);
    public static final Meal MEAL_31_2 = new Meal(START_SEQ + 6, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед"
            , 500);
    public static final Meal MEAL_31_3 = new Meal(START_SEQ + 7, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин"
            , 510);

    public static final List<Meal> MEAL_LIST_REVERSED = Arrays.asList(MEAL_31_3, MEAL_31_2, MEAL_31_1, MEAL_30_3,MEAL_30_2, MEAL_30_1);

    public static final Meal MEAL_30_2_UPDATED = new Meal(MEAL_30_2.getId(), LocalDateTime.of(2200, 12, 2, 21, 34), "Новое описание", 100500);



}
