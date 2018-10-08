package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.repository.MealInMemoryImpl;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by taras on 2018-10-08.
 */

public class MealDeleteServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private static final String ERROR_MEAL_ID_DOES_NOT_EXIST = "В запросе должен присутствовать параметр meal_id!";
    private static final String ERROR_MEAL_ID_MUST_BE_INT = "Параметр meal_id должен быть целочисленным!";

    private MealRepository mealRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        mealRepository = new MealInMemoryImpl();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String mealServletPath = req.getContextPath() + "/meals";
        String meal_id = req.getParameter("meal_id");
        if (meal_id == null) {
            log.error("meal_id must exist");
            resp.sendRedirect(mealServletPath +
                    "?error=" +
                    URLEncoder.encode(ERROR_MEAL_ID_DOES_NOT_EXIST, "UTF-8"));
            return;
        }
        try {
            log.debug("Deleting meal with id = {}", meal_id);
            int id = Integer.parseInt(meal_id);
            mealRepository.delete(id);
            resp.sendRedirect(mealServletPath);
        } catch (NumberFormatException e) {
            log.error("meal_id must be integer");
            resp.sendRedirect(mealServletPath +
                    "?error=" +
                    URLEncoder.encode(ERROR_MEAL_ID_MUST_BE_INT, "UTF-8"));
        }
    }
}
