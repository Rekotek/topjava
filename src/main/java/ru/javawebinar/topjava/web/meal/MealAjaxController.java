package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = MealAjaxController.REST_URL, produces = APPLICATION_JSON_VALUE)
public class MealAjaxController extends AbstractMealController {
    static final String REST_URL = "/ajax/meals";

    @Override
    @GetMapping
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        super.delete(id);
    }

    @PostMapping
    public void create(@RequestParam("dateTime") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                       @RequestParam("description") String description,
                       @RequestParam("calories") int calories) {
        Meal meal = new Meal(dateTime, description, calories);
        super.create(meal);
    }

    @GetMapping("/defaultcalories")
    public int getDefaultCalories() {
        return SecurityUtil.authUserCaloriesPerDay();
    }
}
