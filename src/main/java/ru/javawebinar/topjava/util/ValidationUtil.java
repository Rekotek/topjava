package ru.javawebinar.topjava.util;


import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.util.exception.NotFoundException;

public class ValidationUtil {

    public static final String MSG_NOT_FOUND_ENTITY_WITH = "Not found entity with ";
    public static final String MSG_ID = "id=";
    public static final String MSG_MUST_BE_NEW_ID_NULL = " must be new (id=null)";
    public static final String MSG_MUST_BE_WITH_ID = " must be with id=";

    public static <T> T checkNotFoundWithId(T object, int id) {
        return checkNotFound(object, MSG_ID + id);
    }

    private ValidationUtil() {
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, MSG_ID + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException(MSG_NOT_FOUND_ENTITY_WITH + msg);
        }
    }

    public static void checkNew(AbstractBaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + MSG_MUST_BE_NEW_ID_NULL);
        }
    }

    public static void assureIdConsistent(AbstractBaseEntity entity, int id) {
//      http://stackoverflow.com/a/32728226/548473
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.getId() != id) {
            throw new IllegalArgumentException(entity + MSG_MUST_BE_WITH_ID + id);
        }
    }
}