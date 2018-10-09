package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.repository.MealInMemoryImpl;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.repository.MealInMemoryImpl.setUpTestData;

/**
 * Created by taras on 2018-10-08.
 */

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private MealRepository mealRepository;

    @Override
    public void init() throws ServletException {
        super.init();

        log.debug("Servlet initializing...");

        mealRepository = new MealInMemoryImpl();
        setUpTestData(mealRepository);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("GET request processing...");

        String error = req.getParameter("error");
        if (error != null) {
            req.setAttribute("error", error);
        }

        int caloriesPerDay = 675;
        String calories = req.getParameter("calories");
        if (calories != null) {
            int value = 0;
            try {
                value = Integer.parseInt(calories);
            } catch (NumberFormatException ignored) { }
            if (value != 0) {
                caloriesPerDay = value;
                log.debug("\tparameter 'calories' found with value: {}", caloriesPerDay);
            }
        }

        List<MealWithExceed> mealWithExceedList =
                MealsUtil.getFilteredWithExceededInOnePass(mealRepository.findAll(),
                        LocalTime.MIN,
                        LocalTime.MAX,
                        caloriesPerDay);
        req.setAttribute("mealList", mealWithExceedList);
        req.setAttribute("caloriesPerDay", caloriesPerDay);
        req.setAttribute("countMeals", mealRepository.count());
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }
}
