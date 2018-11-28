package ru.javawebinar.topjava.web.json;

import org.hamcrest.Matcher;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import static org.hamcrest.Matchers.*;

public class JsonMatchers {
    private JsonMatchers() {
    }

    public static Matcher<MealTo> mealMatcher(final Meal meal, boolean excess) {
        return allOf(
                hasProperty("id", is(meal.getId())),
                hasProperty("description", is(meal.getDescription())),
                hasProperty("calories", is(meal.getCalories())),
                hasProperty("dateTime", is(meal.getDateTime())),
                hasProperty("excess", is(excess))
        );
    }
}
