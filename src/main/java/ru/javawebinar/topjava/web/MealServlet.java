package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.repository.MealInMemoryRepo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by taras on 2018-10-08.
 */

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Meal servlet entered");

        int caloriesPerDay = 675;
        String calories = req.getParameter("calories");
        if (calories != null) {
            int value = 0;
            try {
                value = Integer.parseInt(calories);
            } catch (NumberFormatException e) { }
            if (value != 0) {
                caloriesPerDay = value;
                log.debug("\tparameter 'calories' found with value: {}", caloriesPerDay);
            }
        }

        List<MealWithExceed> mealWithExceedList =
                MealsUtil.getFilteredWithExceededInOnePass(MealInMemoryRepo.getMealList(),
                        LocalTime.MIN,
                        LocalTime.MAX,
                        caloriesPerDay);
        req.setAttribute("mealList", mealWithExceedList);
        req.setAttribute("caloriesPerDay", caloriesPerDay);
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }
}
