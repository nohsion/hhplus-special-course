package com.hhplus.hhplus_special_course.domain.course.repository;

import com.hhplus.hhplus_special_course.domain.course.domain.UserCourseEnrollment;
import com.hhplus.hhplus_special_course.test.TestDataCleaner;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

@SpringBootTest
@ActiveProfiles("test")
class CourseEnrollmentCoreRepositoryTest {

    @Autowired
    private CourseEnrollmentRepository sut;

    @Autowired
    private CourseEnrollmentJpaRepository courseEnrollmentJpaRepository;

    @Autowired
    private TestDataCleaner testDataCleaner;

    @BeforeEach
    void setUp() {
        testDataCleaner.cleanUp();
    }

    @DisplayName("한번도 특강을 신청하지 않은 수강생 ID로 특강 신청 목록을 조회하면 빈 목록을 반환한다.")
    @Test
    void findByStudentIdEmptyList() {
        // given
        long userId = 1L;

        // when
        List<UserCourseEnrollment> result = sut.findByStudentId(userId);

        // then
        assertThat(result).isEmpty();
    }

    @DisplayName("수강생 ID로 특강 신청 목록을 조회한다.")
    @Test
    void findByStudentId() {
        // given
        long studentId = 1L;
        List<UserCourseEnrollment> userCourseEnrollments =
                Instancio.ofList(UserCourseEnrollment.class).size(3)
                        .set(field(UserCourseEnrollment::getId), null)
                        .set(field(UserCourseEnrollment::getStudentId), studentId)
                        .create();
        List<UserCourseEnrollment> savedUserCourseEnrollments = courseEnrollmentJpaRepository.saveAll(userCourseEnrollments);

        // when
        List<UserCourseEnrollment> result = sut.findByStudentId(studentId);

        // then
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(savedUserCourseEnrollments);
    }

    @DisplayName("아무도 신청하지 않은 특강은 수강생 수는 0이다.")
    @Test
    void countByCourseIdZero() {
        // given
        long courseId = 1L;

        // when
        int result = sut.countByCourseId(courseId);

        // then
        assertThat(result).isZero();
    }

    @DisplayName("특강에 신청한 수강생 수는 UserCourseEnrollment 엔티티의 수와 같다.")
    @Test
    void countByCourseId() {
        // given
        long courseId = 1L;
        List<UserCourseEnrollment> userCourseEnrollments =
                Instancio.ofList(UserCourseEnrollment.class).size(3)
                        .set(field(UserCourseEnrollment::getId), null)
                        .set(field(UserCourseEnrollment::getCourseId), courseId)
                        .create();
        List<UserCourseEnrollment> userCourseEnrollmentsDifferentCourseId =
                Instancio.ofList(UserCourseEnrollment.class).size(5)
                        .set(field(UserCourseEnrollment::getId), null)
                        .set(field(UserCourseEnrollment::getCourseId), 9L)
                        .create();

        List<UserCourseEnrollment> courseEnrollmentsToSave = new ArrayList<>();
        courseEnrollmentsToSave.addAll(userCourseEnrollments);
        courseEnrollmentsToSave.addAll(userCourseEnrollmentsDifferentCourseId);

        courseEnrollmentJpaRepository.saveAll(courseEnrollmentsToSave);

        // when
        int result = sut.countByCourseId(courseId);

        // then
        assertThat(result)
                .as("총 8개의 특강 신청 중 3개만 해당 특강에 신청했으므로 3이 반환되어야 한다.")
                .isEqualTo(3);
    }

}