package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.Util.orElse;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private final MealHelper mealHelper;

    public MealRestController(MealHelper mealHelper) {
        this.mealHelper = mealHelper;
    }

    public Meal get(int id) {
        return mealHelper.get(id);
    }

    public Meal create(Meal meal) {
        return mealHelper.create(meal);
    }

    public List<MealTo> getAll() {
        return mealHelper.getAll();
    }

    public void update(Meal meal, int id) {
        mealHelper.update(meal, id);
    }

    public void delete(int id) {
        mealHelper.delete(id);
    }

    /**
     * <ol>Filter separately
     * <li>by date</li>
     * <li>by time for every date</li>
     * </ol>
     */
    public List<MealTo> getBetween(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        return mealHelper.getBetween(startDate, startTime, endDate, endTime);
    }
}