package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealInMemoryImpl;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by taras on 2018-10-10.
 */

public class MealUpdateServlet extends HttpServlet {
    private static final Logger log = getLogger(MealUpdateServlet.class);

    private static final String ERROR_MEAL_ID_DOES_NOT_EXIST = "ID must be as part of forms's parameters!";
    private static final String ERROR_MEAL_ID_MUST_BE_INT = "ID must be Integer!";
    private static final String ERROR_MEAL_ABSENT = "The record you had tried to update does not exist!";
    private static final String ERROR_DESC_NOT_FOUND = "Description should not be empty!";
    private static final String ERROR_DATE_TIME_NOT_FOUND = "Field DateTime should not be empty!";
    private static final String ERROR_CALORIES_NOT_FOUND = "Field calories should not be empty!";

    private MealRepository mealRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        mealRepository = new MealInMemoryImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String MEAL_SERVLET_PATH = req.getContextPath() + "/meals";

        String idParam = req.getParameter("id");
        if (idParam == null) {
            log.error("Field 'id' must present");
            resp.sendRedirect(MEAL_SERVLET_PATH +
                    "?error=" +
                    URLEncoder.encode(ERROR_MEAL_ID_DOES_NOT_EXIST, "UTF-8"));
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            log.error("Field 'id' must be integer");
            resp.sendRedirect(MEAL_SERVLET_PATH +
                    "?error=" +
                    URLEncoder.encode(ERROR_MEAL_ID_MUST_BE_INT, "UTF-8"));
            return;
        }
        Meal meal = mealRepository.findById(id);
        req.setAttribute("meal", meal);
        req.getRequestDispatcher("/update.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String MEAL_SERVLET_PATH = req.getContextPath() + "/meals";
        final int id;
        final String description;
        final LocalDateTime localDateTime;
        final int calories;

        String idParam = req.getParameter("id");
        if (idParam == null) {
            log.error("meal_id must exist");
            resp.sendRedirect(MEAL_SERVLET_PATH +
                    "?error=" +
                    URLEncoder.encode(ERROR_MEAL_ID_DOES_NOT_EXIST, "UTF-8"));
            return;
        }
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            log.error("Field 'meal_id' must be integer");
            resp.sendRedirect(MEAL_SERVLET_PATH +
                    "?error=" +
                    URLEncoder.encode(ERROR_MEAL_ID_MUST_BE_INT, "UTF-8"));
            return;
        }

        description = req.getParameter("desc");
        if (description == null || description.isEmpty()) {
            log.error("Field 'desc' must be not empty");
            resp.sendRedirect(MEAL_SERVLET_PATH +
                    "?error=" +
                    URLEncoder.encode(ERROR_DESC_NOT_FOUND, "UTF-8"));
            return;

        }

        String localDateParam = req.getParameter("date_time");
        if (localDateParam == null) {
            log.error("Field 'date_time' must present");
            resp.sendRedirect(MEAL_SERVLET_PATH +
                    "?error=" +
                    URLEncoder.encode(ERROR_DATE_TIME_NOT_FOUND, "UTF-8"));
            return;
        }

        try {
            localDateTime = LocalDateTime.parse(localDateParam);
        } catch (DateTimeParseException e) {
            log.error("LocalDateTime cannot be parsed: " + e.getParsedString());
            resp.sendRedirect(MEAL_SERVLET_PATH +
                    "?error=" +
                    URLEncoder.encode(ERROR_DATE_TIME_NOT_FOUND, "UTF-8"));
            return;
        }

        String caloriesParam = req.getParameter("calories");
        if (caloriesParam == null) {
            log.error("Field 'calories' should not be empty");
            resp.sendRedirect(MEAL_SERVLET_PATH +
                    "?error=" +
                    URLEncoder.encode(ERROR_CALORIES_NOT_FOUND, "UTF-8"));
            return;
        }
        try {
            calories = Integer.parseInt(caloriesParam);
        } catch (NumberFormatException e) {
            log.error("Field 'calories' must be integer");
            resp.sendRedirect(MEAL_SERVLET_PATH +
                    "?error=" +
                    URLEncoder.encode(ERROR_CALORIES_NOT_FOUND, "UTF-8"));
            return;
        }

        log.debug("Update meal with id = {}", id);
        Meal meal = new Meal(localDateTime, description, calories);
        meal.setId(id);
        try {
            mealRepository.update(meal);
        } catch (NoSuchElementException e) {
            log.error("Record with ID = {} does not exist", id);
            resp.sendRedirect(MEAL_SERVLET_PATH +
                    "?error=" +
                    URLEncoder.encode(ERROR_MEAL_ABSENT, "UTF-8"));
            return;
        }
        resp.sendRedirect(MEAL_SERVLET_PATH);
    }
}
