package ru.javawebinar.topjava.repository.mock;

import org.junit.Test;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.model.Role.ROLE_USER;

/**
 * Created by taras on 2018-10-13.
 */

public class InMemoryUserRepositoryImplTest {
    private static final UserRepository userRepository = new InMemoryUserRepositoryImpl();

    @Test
    public void getAll() {
        List<User> users = userRepository.getAll();
        users.forEach(System.out::println);
    }

    @Test
    public void getByEmail() {
        User newUser = new User(null, "ЧудовChudov...", "zxcxz@zxcx.com", "111", ROLE_USER);
        userRepository.save(newUser);
        User user = userRepository.getByEmail(newUser.getEmail());
        assertEquals(newUser.getEmail(), user.getName());
    }

    @Test
    public void deleteOneExistedRecord() {
        List<User> usersBefore = userRepository.getAll();
        boolean deletedFlag = userRepository.delete(usersBefore.get(0).getId());
        assertTrue(deletedFlag);
        List<User> usersAfter = userRepository.getAll();
        assertEquals(usersBefore.size(), usersAfter.size() + 1);
    }

    @Test
    public void deleteNonExistedRecord() {
        List<User> usersBefore = userRepository.getAll();
        boolean deletedFlag = userRepository.delete(usersBefore.size() + 10000);
        assertFalse(deletedFlag);
    }
}