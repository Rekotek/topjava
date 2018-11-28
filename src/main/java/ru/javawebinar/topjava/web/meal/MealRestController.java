package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.javawebinar.topjava.web.meal.MealRestController.MEALS_URL;

@RestController
@RequestMapping(path=MEALS_URL, produces = APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {
    static final String MEALS_URL ="/rest/meals/";

    @Override
    @GetMapping("/{id}")
    public Meal get(@PathVariable("id") int id) {
        return super.get(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        super.delete(id);
    }

    @Override
    @GetMapping("/")
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @Override
    @PostMapping(path = "/", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Meal create(@RequestBody Meal meal) {
        return super.create(meal);
    }

    @Override
    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal, @PathVariable("id") int id) {
        super.update(meal, id);
    }

    @GetMapping("/filter/")
    public List<MealTo> getBetween(@RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
                                   @RequestParam(value = "startTime", required = false) @DateTimeFormat(iso = ISO.TIME) LocalTime startTime,
                                   @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate endDate,
                                   @RequestParam(value = "endTime", required = false) @DateTimeFormat(iso = ISO.TIME) LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}