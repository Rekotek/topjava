package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

/**
 * Created by taras on 2018-10-21.
 */

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int USER_UNKNOWN = START_SEQ + 1000;

    public static final Meal MEAL_30_1 = new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
    public static final Meal MEAL_30_2 = new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
    public static final Meal MEAL_30_3 = new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500);
    public static final Meal MEAL_31_1 = new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal MEAL_31_2 = new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500);
    public static final Meal MEAL_31_3 = new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510);

    public static final int MEAL_TEST_DATA_SIZE = 6;

    static {
        MEAL_30_1.setId(100002);
        MEAL_30_2.setId(100003);
        MEAL_30_3.setId(100004);
        MEAL_31_1.setId(100005);
        MEAL_31_2.setId(100006);
        MEAL_31_3.setId(100007);
    }
}
