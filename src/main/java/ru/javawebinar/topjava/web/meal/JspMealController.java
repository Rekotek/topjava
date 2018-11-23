package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController {
    private static final int DEFAULT_CALORIES_FOR_NEW = 1000;

    private final MealHelper mealHelper;

    public JspMealController(MealHelper mealHelper) {
        this.mealHelper = mealHelper;
    }

    @GetMapping("")
    public String processGetAll(Model model) {
        List<MealTo> meals = mealHelper.getAll();
        model.addAttribute("meals", meals);
        return "meals";
    }

    @PostMapping("/filter")
    public String processFilter(HttpServletRequest request, Model model) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        List<MealTo> meals = mealHelper.getBetween(startDate, startTime, endDate, endTime);
        model.addAttribute("meals", meals);
        return "meals";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Meal meal = mealHelper.get(id);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping("/update")
    public String processUpdate(@ModelAttribute("meal") Meal meal) {
        Objects.requireNonNull(meal.getId());
        mealHelper.update(meal, meal.getId());
        return "redirect:/meals";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Meal meal = new Meal();
        // Needs to correct passing datetime to view control on JSP
        meal.setDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        meal.setCalories(DEFAULT_CALORIES_FOR_NEW);
        meal.setDescription("");
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping("/create")
    public String processCreate(@ModelAttribute("meal") Meal meal) {
        mealHelper.create(meal);
        return "redirect:/meals";
    }

    @GetMapping("/delete/{id}")
    public String processDelete(@PathVariable("id") int id) {
        mealHelper.delete(id);
        return "redirect:/meals";
    }
}
