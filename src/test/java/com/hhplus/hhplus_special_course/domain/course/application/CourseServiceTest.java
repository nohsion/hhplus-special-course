package com.hhplus.hhplus_special_course.domain.course.application;

import com.hhplus.hhplus_special_course.domain.course.domain.Course;
import com.hhplus.hhplus_special_course.domain.course.repository.CourseRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CourseServiceTest {

    private CourseService sut;

    CourseRepository courseRepository = mock(CourseRepository.class);

    @BeforeEach
    void setUp() {
        sut = new CourseService(courseRepository);
    }

    @DisplayName("없는 특강 ID로 조회하면 IllegalArgumentException 예외가 발생한다.")
    @Test
    void getCourseFail() {
        // given
        long courseId = 1L;

        // when
        // then
         assertThatThrownBy(() -> sut.getCourse(courseId))
                 .isInstanceOf(IllegalArgumentException.class)
                 .hasMessage("Course not found");
    }

    @DisplayName("특강 ID로 조회하면 특강이 성공적으로 반환된다.")
    @Test
    void getCourseSuccess() {
        // given
        long courseId = 1L;
        Course course = Instancio.of(Course.class).create();

        when(courseRepository.findById(courseId))
                .thenReturn(Optional.of(course));

        // when
        Course result = sut.getCourse(courseId);

        // then
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(course);
    }

}