package com.hhplus.hhplus_special_course.domain.course.domain;

import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

class CourseTest {

    @DisplayName("본인의 강의라면 isInstructor()는 true를 반환한다.")
    @Test
    void isInstructorTrue() {
        // given
        long userId = 1L;
        Course course = Instancio.of(Course.class)
                .set(field(Course::getInstructorId), userId)
                .create();

        // when
        boolean result = course.isInstructor(userId);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("본인의 강의가 아니라면 isInstructor()는 false를 반환한다.")
    @Test
    void isInstructorFalse() {
        // given
        long userId = 1L;
        Course course = Instancio.of(Course.class)
                .set(field(Course::getInstructorId), 2L)
                .create();

        // when
        boolean result = course.isInstructor(userId);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("정원보다 적은 학생 수라면 isFullCapacity()는 false를 반환한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 10, 20})
    void isFullCapacityFalse(int currentStudents) {
        // given
        Course course = Instancio.of(Course.class)
                .set(field(Course::getMaxStudents), 30)
                .create();

        // when
        boolean result = course.isFullCapacity(currentStudents);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("정원과 같거나 많은 학생 수라면 isFullCapacity()는 true를 반환한다.")
    @ParameterizedTest
    @ValueSource(ints = {30, 40, 50})
    void isFullCapacityTrue(int currentStudents) {
        // given
        Course course = Instancio.of(Course.class)
                .set(field(Course::getMaxStudents), 30)
                .create();

        // when
        boolean result = course.isFullCapacity(currentStudents);

        // then
        assertThat(result).isTrue();
    }

}