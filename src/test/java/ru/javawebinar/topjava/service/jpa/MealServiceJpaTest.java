package ru.javawebinar.topjava.service.jpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.MealServiceTest;

@ActiveProfiles(Profiles.JPA)
public class MealServiceJpaTest extends MealServiceTest {
    @Test
    @Override
    public void delete() throws Exception {
        super.delete();
    }

    @Test
    @Override
    public void deleteNotFound() throws Exception {
        super.deleteNotFound();
    }

    @Test
    @Override
    public void create() throws Exception {
        super.create();
    }

    @Test
    @Override
    public void get() throws Exception {
        super.get();
    }

    @Test
    @Override
    public void getNotFound() throws Exception {
        super.getNotFound();
    }

    @Test
    @Override
    public void update() throws Exception {
        super.update();
    }

    @Test
    @Override
    public void updateNotFound() throws Exception {
        super.updateNotFound();
    }

    @Test
    @Override
    public void getAll() throws Exception {
        super.getAll();
    }

    @Test
    @Override
    public void getBetween() throws Exception {
        super.getBetween();
    }
}
