package ru.javawebinar.topjava.service.jpa;

import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.UserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

@ActiveProfiles(Profiles.JPA)
public class UserServiceJpaTest extends UserServiceTest {

    @Test
    @Override
    public void create() throws Exception {
        super.create();
    }

    @Test(expected = DataAccessException.class)
    @Override
    public void duplicateMailCreate() throws Exception {
        super.duplicateMailCreate();
    }

    @Test
    @Override
    public void delete() throws Exception {
        super.delete();
    }

    @Test(expected = NotFoundException.class)
    @Override
    public void deletedNotFound() throws Exception {
        super.deletedNotFound();
    }

    @Test
    @Override
    public void get() throws Exception {
        super.get();
    }

    @Test(expected = NotFoundException.class)
    @Override
    public void getNotFound() throws Exception {
        super.getNotFound();
    }

    @Test
    @Override
    public void getByEmail() throws Exception {
        super.getByEmail();
    }

    @Test
    @Override
    public void update() throws Exception {
        super.update();
    }

    @Test
    @Override
    public void getAll() throws Exception {
        super.getAll();
    }
}
