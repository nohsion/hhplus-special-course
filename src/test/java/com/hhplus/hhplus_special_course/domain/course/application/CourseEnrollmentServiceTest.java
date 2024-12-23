package com.hhplus.hhplus_special_course.domain.course.application;

import com.hhplus.hhplus_special_course.domain.course.domain.UserCourseEnrollment;
import com.hhplus.hhplus_special_course.domain.course.repository.CourseEnrollmentRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CourseEnrollmentServiceTest {

    private CourseEnrollmentService sut;

    CourseEnrollmentRepository courseEnrollmentRepository = mock(CourseEnrollmentRepository.class);

    @BeforeEach
    void setUp() {
        sut = new CourseEnrollmentService(courseEnrollmentRepository);
    }

    @DisplayName("한번도 신청하지 않은 유저는 빈 특강 신청 목록을 반환한다.")
    @Test
    void getCourseEnrollmentsByUserIdEmptyList() {
        // given
        long userId = 1L;

        when(courseEnrollmentRepository.findByStudentId(userId))
                .thenReturn(List.of());

        // when
        List<UserCourseEnrollment> result = sut.getCourseEnrollmentsByUserId(userId);

        // then
        assertThat(result).isEmpty();
    }

    @DisplayName("신청 이력이 있는 유저는 특강 신청 목록을 반환한다.")
    @Test
    void getCourseEnrollmentsByUserId() {
        // given
        long userId = 1L;
        List<UserCourseEnrollment> courseEnrollments =
                Instancio.ofList(UserCourseEnrollment.class).size(3).create();

        when(courseEnrollmentRepository.findByStudentId(userId))
                .thenReturn(courseEnrollments);

        // when
        List<UserCourseEnrollment> result = sut.getCourseEnrollmentsByUserId(userId);

        // then
        assertThat(result).hasSize(3)
                .usingRecursiveComparison()
                .isEqualTo(courseEnrollments);
    }

    @DisplayName("특강의 신청 인원을 반환한다.")
    @Test
    void getEnrolledStudentCount() {
        // given
        long courseId = 1L;
        int enrolledStudentCount = 15;

        when(courseEnrollmentRepository.countByCourseId(courseId))
                .thenReturn(enrolledStudentCount);

        // when
        int result = sut.getEnrolledStudentCount(courseId);

        // then
        assertThat(result).isEqualTo(enrolledStudentCount);
    }

}