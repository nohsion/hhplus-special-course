package com.hhplus.hhplus_special_course.domain.course.repository;

import com.hhplus.hhplus_special_course.domain.course.domain.Course;
import com.hhplus.hhplus_special_course.test.TestDataCleaner;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

@SpringBootTest
@ActiveProfiles("test")
class CourseCoreRepositoryTest {

    @Autowired
    private CourseRepository sut;

    @Autowired
    private CourseJpaRepository courseJpaRepository;

    @Autowired
    private TestDataCleaner testDataCleaner;

    @BeforeEach
    void setUp() {
        testDataCleaner.cleanUp();
    }

    @DisplayName("없는 특강 ID로 조회하면 빈 Optional을 반환한다.")
    @Test
    void findByIdOptionalEmpty() {
        // given
        long courseId = 1L;

        // when
        Optional<Course> result = sut.findById(courseId);

        // then
        assertThat(result).isEmpty();
    }

    @DisplayName("특강 ID로 조회하면 해당 특강을 반환한다.")
    @Test
    void findById() {
        // given
        Course course = Instancio.of(Course.class)
                .set(field(Course::getId), null)
                .create();
        Course savedCourse = courseJpaRepository.save(course);

        // when
        Optional<Course> result = sut.findById(savedCourse.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).usingRecursiveComparison()
                .isEqualTo(savedCourse);
    }

}