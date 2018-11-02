package ru.javawebinar.topjava.service.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.MealServiceTest;

@ActiveProfiles(Profiles.JPA)
public class MealServiceJpaTest extends MealServiceTest {
    @Override
    public void delete() throws Exception {
        super.delete();
    }

    @Override
    public void deleteNotFound() throws Exception {
        super.deleteNotFound();
    }

    @Override
    public void create() throws Exception {
        super.create();
    }

    @Override
    public void get() throws Exception {
        super.get();
    }

    @Override
    public void getNotFound() throws Exception {
        super.getNotFound();
    }

    @Override
    public void update() throws Exception {
        super.update();
    }

    @Override
    public void updateNotFound() throws Exception {
        super.updateNotFound();
    }

    @Override
    public void getAll() throws Exception {
        super.getAll();
    }

    @Override
    public void getBetween() throws Exception {
        super.getBetween();
    }
}
