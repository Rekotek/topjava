package ru.javawebinar.topjava.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.javawebinar.topjava.service.UserService;

@RestController
@RequestMapping("/ajax/check_mail/")
public class CheckEmailAjaxController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean checkExistingEmail(@RequestParam("email") String email, @RequestParam("id") Integer id) {
        log.info("Checking email <{}> with user_id: {}", email, id);
        return userService.checkUserWithEmailExists(email, id);
    }
}
