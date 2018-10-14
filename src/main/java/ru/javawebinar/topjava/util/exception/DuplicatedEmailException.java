package ru.javawebinar.topjava.util.exception;

/**
 * Created by taras on 2018-10-14.
 */

public class DuplicatedEmailException extends RuntimeException {
    public DuplicatedEmailException(String message) {
        super(message);
    }
}
