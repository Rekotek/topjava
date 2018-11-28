package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.contentJson;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.web.meal.MealRestController.MEALS_URL;

class MealRestControllerTest extends AbstractControllerTest {
    @Autowired
    private MealService mealService;

    @Test
    void getOne() throws Exception {
        mockMvc.perform(get(MEALS_URL + MEAL1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().encoding("UTF-8"))
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(contentJson(MEAL1));
    }

    @Test
    void deleteOne() throws Exception {
        mockMvc.perform(delete(MEALS_URL + MEAL2.getId()))
                .andExpect(status().isNoContent());
        assertMatch(mealService.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL1);
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(get(MEALS_URL))
                .andExpect(status().isOk())
                .andExpect(content().encoding("UTF-8"))
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(contentJson(MEALS_TO));
    }

    @Test
    void create() throws Exception {
        Meal created = getCreated();
        Meal createdWithId = new Meal(CREATED_MEAL_ID,
                created.getDateTime(),
                created.getDescription(),
                created.getCalories());

        String urlCreatedMeal = MvcUriComponentsBuilder
                .fromMethodCall(on(MealRestController.class).get(CREATED_MEAL_ID))
                .build().toString();

        mockMvc.perform(post(MEALS_URL)
                    .contentType(APPLICATION_JSON)
                    .content(JsonUtil.writeValue(created))
                    )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().encoding("UTF-8"))
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", urlCreatedMeal))
                .andExpect(contentJson(createdWithId));

        assertMatch(mealService.getAll(USER_ID), createdWithId, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    void update() throws Exception {
        Meal updated = getUpdated();
        mockMvc.perform(put(MEALS_URL + updated.getId())
                .contentType(APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        assertMatch(mealService.get(updated.getId(), USER_ID), updated);
    }

    @Test
    void getBetweenDates() throws Exception {
        mockMvc.perform(get(MEALS_URL + "filter/?startDate=2015-05-30&endDate=2015-05-30"))
                .andExpect(status().isOk())
                .andExpect(content().encoding("UTF-8"))
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(contentJson(List.of(MEAL_TO_3, MEAL_TO_2, MEAL_TO_1)));
    }

    @Test
    void getBetweenTime() throws Exception {
        mockMvc.perform(get(MEALS_URL + "filter/?startTime=10:00&endTime=11:00"))
                .andExpect(status().isOk())
                .andExpect(content().encoding("UTF-8"))
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(contentJson(List.of(MEAL_TO_4, MEAL_TO_1)));
    }
}