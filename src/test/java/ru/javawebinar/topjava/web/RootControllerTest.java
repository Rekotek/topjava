package ru.javawebinar.topjava.web;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

class RootControllerTest extends AbstractControllerTest {

    @Test
    void testUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"))
                .andExpect(model().attribute("users", hasSize(2)))
                .andExpect(model().attribute("users", hasItem(
                        allOf(
                                hasProperty("id", is(START_SEQ)),
                                hasProperty("name", is(USER.getName()))
                        )
                )));
    }

    @Test
    void testMeals() throws Exception {
        mockMvc.perform(get("/meals"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals", hasSize(6)))
                .andExpect(model().attribute("meals", contains(
                                mealMatcher(MEAL6, true),
                                mealMatcher(MEAL5, true),
                                mealMatcher(MEAL4, true),
                                mealMatcher(MEAL3, false),
                                mealMatcher(MEAL2, false),
                                mealMatcher(MEAL1, false)
                )));
    }

    private Matcher<MealTo> mealMatcher(final Meal meal, boolean excess) {
        return allOf(
                hasProperty("id", is(meal.getId())),
                hasProperty("description", is(meal.getDescription())),
                hasProperty("calories", is(meal.getCalories())),
                hasProperty("dateTime", is(meal.getDateTime())),
                hasProperty("excess", is(excess))
        );
    }
}