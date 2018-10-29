package ru.javawebinar.topjava.service;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.util.ValidationUtil.MSG_ID;
import static ru.javawebinar.topjava.util.ValidationUtil.MSG_NOT_FOUND_ENTITY_WITH;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    private static final Logger log = getLogger(MealServiceTest.class);
    private static final Map<String, Long> executionTimeMap = new LinkedHashMap<>();

    static {
        SLF4JBridgeHandler.install();
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Rule
    public TestWatcher watcher = new TestWatcher() {
        private long beginningTime;

        @Override
        protected void starting(Description description) {
            beginningTime = System.currentTimeMillis();
        }

        @Override
        protected void finished(Description description) {
            long executionTime = System.currentTimeMillis() - beginningTime;
            log.info("Running time for method '" + description.getMethodName() + "()': " + executionTime + " ms.");
            executionTimeMap.put(description.getMethodName(), executionTime);
        }
    };

    @AfterClass
    public static void tearDownAll() {
        executionTimeMap.forEach((k, v) ->
                log.info("Running time for method '" + k + "'(): " + v + "ms.")
        );
    }

    @Autowired
    private MealService service;

    @Test
    public void delete() {
        service.delete(MEAL1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    public void deleteNotFound() {
        exception.expect(NotFoundException.class);
        exception.expectMessage(MSG_NOT_FOUND_ENTITY_WITH + MSG_ID + MEAL1_ID);
        service.delete(MEAL1_ID, 1);
    }

    @Test
    public void create() {
        Meal created = getCreated();
        service.create(created, USER_ID);
        assertMatch(service.getAll(USER_ID), created, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void get() {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    public void getNotFound() {
        exception.expect(NotFoundException.class);
        exception.expectMessage(MSG_NOT_FOUND_ENTITY_WITH + MSG_ID + MEAL1_ID);
        service.get(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void updateNotFound() {
        exception.expect(NotFoundException.class);
        exception.expectMessage(MSG_NOT_FOUND_ENTITY_WITH + MSG_ID + MEAL1_ID);
        service.update(MEAL1, ADMIN_ID);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetween() {
        assertMatch(service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30), USER_ID), MEAL3, MEAL2, MEAL1);
    }
}