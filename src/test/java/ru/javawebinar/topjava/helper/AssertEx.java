package ru.javawebinar.topjava.helper;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by taras on 2018-10-23.
 */

public class AssertEx {

    public static <T> void assertMatchEx(T actual, T expected, String... ignoredFields) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, ignoredFields);
    }

    public static <T> void assertMatchEx(T actual, T expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static <T> void assertMatchEx(Iterable<T> actual, Iterable<T> expected, String... ignoredFields) {
        assertThat(actual).usingElementComparatorIgnoringFields(ignoredFields).isEqualTo(expected);
    }

    public static <T> void assertMatchEx(Iterable<T> actual, Iterable<T> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}
