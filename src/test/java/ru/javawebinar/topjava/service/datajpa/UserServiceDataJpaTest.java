package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.MEALS;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class UserServiceDataJpaTest extends UserServiceTest {
    @Test
    public void getWithMeals() {
        User user = userService.getWithMeals(USER_ID);

        assertMatch(user, USER);
        List<Meal> meals = user.getMeals();
        MealTestData.assertMatch(meals, MEALS);

        //More elegant way
        USER.setMeals(MEALS);
        assertMatchWithMeals(user, USER);
    }

    @Test(expected = NotFoundException.class)
    public void getWithMealsNotFound() {
        userService.getWithMeals(1);
    }

    @Test
    public void getWithEmptyMeal() {
        User storedUser = userService.create(
                new User(null,
                        "Vasisualiy Pupkins",
                        "pupkin@yandex.ru",
                        "12345",
                        Role.ROLE_USER));
        storedUser.setMeals(new ArrayList<>());
        User newUser = userService.getWithMeals(storedUser.getId());
        assertMatchWithMeals(newUser, storedUser);
    }
}
