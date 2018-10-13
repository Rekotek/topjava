package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.model.Role.ROLE_ADMIN;
import static ru.javawebinar.topjava.model.Role.ROLE_USER;

/**
 * Created by taras on 2018-10-13.
 */

public class UserUtil {
    public static final List<User> USERS = Arrays.asList(
            new User(null, "Петров", "xx@yy.com", "123", ROLE_USER),
            new User(null, "Васечкин", "vasya@yy.com", "321", ROLE_ADMIN, ROLE_USER),
            new User(null, "Иванов", "ivanov@xx.com", "111", ROLE_USER),
            new User(null, "Васечкин", "busurman@yy.com", "321x", ROLE_ADMIN)
    );

}
