package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;

public class UserMeal {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private static int localDateCount;
    private static int caloriesCount;
    private static int descCount;

    public static int getLocalDateCount() {
        return localDateCount;
    }

    public static int getCaloriesCount() {
        return caloriesCount;
    }

    public static int getDescCount() {
        return descCount;
    }

    public static void setLocalDateCount(int localDateCount) {
        UserMeal.localDateCount = localDateCount;
    }

    public static void setCaloriesCount(int caloriesCount) {
        UserMeal.caloriesCount = caloriesCount;
    }

    public static void setDescCount(int descCount) {
        UserMeal.descCount = descCount;
    }

    public UserMeal(LocalDateTime dateTime, String description, int calories) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        localDateCount++;
        return dateTime;
    }

    public String getDescription() {
        descCount++;
        return description;
    }

    public int getCalories() {
        caloriesCount++;
        return calories;
    }
}
