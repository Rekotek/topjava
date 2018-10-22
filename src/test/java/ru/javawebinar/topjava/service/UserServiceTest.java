package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static ru.javawebinar.topjava.UserTestData.*;

@RunWith(SpringRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml",
        "classpath:spring/spring-jdbc.xml"
})
@Sql(scripts = {"classpath:db/populateDB.sql"}, config = @SqlConfig(encoding = "UTF-8"))
public class UserServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private UserService userService;

    @Test
    public void create() {
        User newUser = new User(null, "New", "new@gmail.com", "newPass", 1555, false, new Date(), Collections.singleton(Role.ROLE_USER));
        User created = userService.create(newUser);
        newUser.setId(created.getId());
        assertMatch(userService.getAll(), ADMIN, newUser, USER);
    }

    @Test(expected = DataAccessException.class)
    public void duplicateMailCreate() {
        userService.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.ROLE_USER));
    }

    @Test
    public void delete() {
        userService.delete(USER_ID);
        assertMatch(userService.getAll(), ADMIN);
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotFound() {
        userService.delete(1);
    }

    @Test
    public void get() {
        User user = userService.get(USER_ID);
        assertMatch(user, USER);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        userService.get(1);
    }

    @Test
    public void getByEmail() {
        User user = userService.getByEmail("user@yandex.ru");
        assertMatch(user, USER);
    }

    @Test
    public void update() {
        User updated = new User(USER);
        updated.setName("UpdatedName");
        updated.setCaloriesPerDay(330);
        userService.update(updated);
        assertMatch(userService.get(USER_ID), updated);
    }

    @Test
    public void getAll() {
        List<User> all = userService.getAll();
        assertMatch(all, ADMIN, USER);
    }
}