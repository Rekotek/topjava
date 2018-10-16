package ru.javawebinar.topjava.repository.mock;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.UserUtil;
import ru.javawebinar.topjava.util.exception.DuplicatedEmailException;

import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.model.Role.ROLE_USER;

/**
 * Created by taras on 2018-10-13.
 */

public class InMemoryUserRepositoryImplTest {
    private UserRepository userRepository;

    @Before
    public void setUp() {
        //Cleaning up IDs within static fields
        UserUtil.USERS.forEach(u -> u.setId(null));
        userRepository = new InMemoryUserRepositoryImpl();
    }

    @Test
    public void getAll() {
        List<User> users = userRepository.getAll();
        users.forEach(System.out::println);
        assertEquals(UserUtil.USERS.size(), users.size());
    }

    @Test
    public void getByEmail() {
        User newUser = new User(null, "ЧудовChudov...", "zxcxz@zxcx.com", "111", ROLE_USER);
        userRepository.save(newUser);
        User user = userRepository.getByEmail(newUser.getEmail());
        assertEquals(newUser.getEmail(), user.getEmail());
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

    @Test(expected = DuplicatedEmailException.class)
    public void noDuplicatedEmailAllowedInNewUser() {
        List<User> users = userRepository.getAll();
        assertNotNull(users);
        System.out.println(users.size());
        String usedEmail = users.get(0).getEmail();
        User newUser = new User(null, "NO Allowing this", usedEmail, "123", ROLE_USER);
        userRepository.save(newUser);
    }

    @Test(expected = DuplicatedEmailException.class)
    public void noDuplicatedEmailAllowedWhenUpdateUser() {
        List<User> users = userRepository.getAll();
        String usedEmail = users.get(0).getEmail();
        User updatedUser = users.get(1);
        updatedUser.setEmail(usedEmail);
        userRepository.save(updatedUser);
    }
}