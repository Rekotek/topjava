package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

@Service
public class MealServiceImpl implements MealService {
    private static final String CANNOT_DELETE = "There is no meal to delete with id = ";
    private static final String CANNOT_UPDATE = "Cannot update meal = ";
    private static final String CANNOT_FIND_MEAL_WITH_ID = "Cannot find meal with id = ";

    private final MealRepository repository;

    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    private static String generateErrorMessage(String startString, Object firstArg, int userId) {
        return startString + firstArg.toString() + " for userId = " + userId;
    }

    @Override
    public Meal create(Meal meal, int userId) {
        meal.setId(null);
        repository.save(meal, userId);
        return meal;
    }

    @Override
    public void delete(int id, int userId) throws NotFoundException {
        if (!repository.delete(id, userId)) {
            throw new NotFoundException(generateErrorMessage(CANNOT_DELETE, id, userId));
        }
    }

    @Override
    public void update(Meal meal, int userId) throws NotFoundException {
        if (null == repository.save(meal, userId)) {
            throw new NotFoundException(generateErrorMessage(CANNOT_UPDATE, meal, userId));
        }
    }

    @Override
    public Meal get(int id, int userId) throws NotFoundException {
        Meal meal = repository.get(id, userId);
        if (null == meal) {
            throw new NotFoundException(generateErrorMessage(CANNOT_FIND_MEAL_WITH_ID, id, userId));
        }
        return meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return (List<Meal>) repository.getAll(userId);
    }

    @Override
    public List<Meal> findAllWhereDaysBetween(LocalDate startDate, LocalDate endDate, int userId) {
        return (List<Meal>) repository.findByDaysBetween(startDate, endDate, userId);
    }
}