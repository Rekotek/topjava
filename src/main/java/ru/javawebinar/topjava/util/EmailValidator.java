package ru.javawebinar.topjava.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.HasId;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;

public class EmailValidator implements Validator {
    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz) || UserTo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String givenEmail;
        int id = ((HasId) target).isNew() ? 0 : ((HasId) target).getId();
        if (target instanceof User) {
            User u = (User) target;
            givenEmail = u.getEmail();
        } else {
            UserTo u = (UserTo) target;
            givenEmail = u.getEmail();
        }
        boolean emailExists = userService.checkUserWithEmailExists(givenEmail, id);
        if (emailExists) {
            errors.rejectValue("email", "", messageSource.getMessage("validator.emailDuplication", null, LocaleContextHolder.getLocale()));
        }
    }
}
