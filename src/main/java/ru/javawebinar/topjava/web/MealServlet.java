package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.repository.MealInMemoryRepoImpl;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.repository.MealInMemoryRepoImpl.setUpTestData;

/**
 * Created by taras on 2018-10-08.
 */

public class MealServlet extends HttpServlet {
    private static final int CALORIES_PER_DAY = 675;
    private static final Logger log = getLogger(MealServlet.class);

    private MealRepository mealRepository;

    @Override
    public void init() throws ServletException {
        super.init();

        log.debug("Servlet initializing...");

        mealRepository = MealInMemoryRepoImpl.getInstance();
        setUpTestData(mealRepository);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("GET request processing...");

        String error = req.getParameter("error");
        if (error != null) {
            req.setAttribute("error", error);
        }

        final String action = req.getParameter("action");
        if ("edit".equals(action)) {
            String idParam = req.getParameter("id");
            int id = Integer.parseInt(idParam);
            Meal meal = mealRepository.findById(id);
            req.setAttribute("meal", meal);
            req.getRequestDispatcher("/update.jsp").forward(req, resp);
            return;
        }

        List<MealWithExceed> mealWithExceedList =
                MealsUtil.getFilteredWithExceededInOnePass(mealRepository.findAll(),
                        LocalTime.MIN,
                        LocalTime.MAX,
                        CALORIES_PER_DAY);
        req.setAttribute("mealList", mealWithExceedList);
        req.setAttribute("countMeals", mealRepository.count());
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String MEAL_SERVLET_PATH = req.getContextPath() + "/meals";
        log.debug("POST request processing");
        req.getParameterMap().entrySet()
                .forEach(m -> log.debug("...Param {}: {}", m.getKey(), m.getValue()));
        final String action = req.getParameter("action");
        final String idParam = req.getParameter("id");
        final int id = Integer.parseInt(idParam);
        if ("delete".equals(action)) {
            log.debug("Deleting meal with id = {}", id);
            mealRepository.delete(id);
        } else if ("edit".equals(action)) {
            log.debug("Updating record with id = {}", id);
            String description = req.getParameter("desc");
            String localDateParam = req.getParameter("date_time");
            LocalDateTime localDateTime = LocalDateTime.parse(localDateParam);
            String caloriesParam = req.getParameter("calories");
            int calories = Integer.parseInt(caloriesParam);
            Meal meal = new Meal(localDateTime, description, calories);
            meal.setId(id);
            mealRepository.update(meal);
        }

        resp.sendRedirect(MEAL_SERVLET_PATH);
    }

}
