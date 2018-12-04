package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

class AdminAjaxControllerTest extends AbstractControllerTest {
    private static final String AJAX_URL = AdminAjaxController.AJAX_URL;

    @Autowired
    private UserService userService;

    @Test
    void clearEnableFlag() throws Exception {
        String methodPath = AJAX_URL + USER_ID + "/enabled";
        mockMvc.perform(post(methodPath).param("flag", "false"))
                .andExpect(status().isNoContent());

        User user = userService.get(USER_ID);
        assertFalse(user.isEnabled());
    }

    @Test
    void setEnableFlag() throws Exception {
        String methodPath = AJAX_URL + USER_ID + "/enabled";
        userService.setEnabledFlag(USER_ID, false);

        mockMvc.perform(post(methodPath).param("flag", "true"))
                .andExpect(status().isNoContent());

        User user = userService.get(USER_ID);
        assertTrue(user.isEnabled());
    }
}