package ru.javawebinar.topjava.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.HasId;
import ru.javawebinar.topjava.model.EmailContainer;
import ru.javawebinar.topjava.service.UserService;

public class EmailValidator implements Validator {
    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return EmailContainer.class.isAssignableFrom(clazz) && HasId.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String givenEmail = ((EmailContainer) target).getEmail();
        boolean emailExists = userService.checkUserWithEmailExists(givenEmail, ((HasId) target).getId());
        if (emailExists) {
            errors.rejectValue("email", "", messageSource.getMessage("validator.emailDuplication", null, LocaleContextHolder.getLocale()));
        }
    }
}
