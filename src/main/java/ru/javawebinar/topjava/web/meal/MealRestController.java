package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class MealRestController {
    private static final Logger log = getLogger(MealRestController.class);

    private final MealService mealService;

    public MealRestController(MealService mealService) {
        this.mealService = mealService;
    }

    public Meal create(Meal meal) {
        return mealService.create(meal, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        mealService.delete(id, SecurityUtil.authUserId());
    }

    public void save(Meal meal) {
        mealService.update(meal, SecurityUtil.authUserId());
    }

    public Meal get(int id) {
        return mealService.get(id, SecurityUtil.authUserId());
    }

    public List<MealWithExceed> getAll() {
        List<Meal> resultList = mealService.getAll(SecurityUtil.authUserId());
        return MealsUtil.getWithExceeded(resultList, SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealWithExceed> getAll(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        List<Meal> resultList;

        if ((startDate == null) && (endDate == null)) {
            resultList = mealService.getAll(SecurityUtil.authUserId());
        } else {
            if (startDate == null) {
                startDate = LocalDate.MIN;
            }
            if (endDate == null) {
                endDate = LocalDate.MIN;
            }

            resultList = mealService.findAllWhereDaysBetween(startDate, endDate, SecurityUtil.authUserId());
        }
        if (startTime == null) {
            startTime = LocalTime.MIN;
        }
        if (endTime == null) {
            endTime = LocalTime.MAX;
        }
        return MealsUtil.getFilteredWithExceeded(resultList, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }

}