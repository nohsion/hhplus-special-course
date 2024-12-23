package com.hhplus.hhplus_special_course.domain.course.application;

import com.hhplus.hhplus_special_course.domain.course.api.response.CourseEnrollmentResponse;
import com.hhplus.hhplus_special_course.domain.course.domain.Course;
import com.hhplus.hhplus_special_course.domain.course.domain.UserCourseEnrollment;
import com.hhplus.hhplus_special_course.domain.user.application.UserService;
import com.hhplus.hhplus_special_course.domain.user.domain.User;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.mockito.Mockito.*;

class CourseFacadeTest {

    private CourseFacade sut;

    CourseService courseService = mock(CourseService.class);

    CourseEnrollmentService courseEnrollmentService = mock(CourseEnrollmentService.class);

    UserService userService = mock(UserService.class);

    @BeforeEach
    void setUp() {
        sut = new CourseFacade(courseService, courseEnrollmentService, userService);
    }

    @DisplayName("유저의 특강 신청 내역이 없으면 빈 리스트를 반환한다.")
    @Test
    void getCourseEnrollmentsByUserIdEmptyList() {
        // given
        long userId = 1L;

        when(courseEnrollmentService.getCourseEnrollmentsByUserId(userId))
                .thenReturn(Collections.emptyList());

        // when
        List<CourseEnrollmentResponse> result = sut.getCourseEnrollmentsByUserId(userId);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void getCourseEnrollmentsByUserId() {
        // given
        long userId = 1L;
        List<UserCourseEnrollment> userCourseEnrollments =
                Instancio.ofList(UserCourseEnrollment.class).size(3).create();
        List<Course> courses = userCourseEnrollments.stream()
                .map(enrollment -> Instancio.of(Course.class)
                        .set(field(Course::getId), enrollment.getCourseId())
                        .create())
                .toList();
        List<User> instructors = courses.stream()
                .map(course -> Instancio.of(User.class)
                        .set(field(User::getId), course.getInstructorId())
                        .create())
                .toList();

        when(courseEnrollmentService.getCourseEnrollmentsByUserId(userId))
                .thenReturn(userCourseEnrollments);
        for (int i = 0; i < userCourseEnrollments.size(); i++) {
            when(courseService.getCourse(userCourseEnrollments.get(i).getCourseId()))
                    .thenReturn(courses.get(i));
            when(userService.getUser(courses.get(i).getInstructorId()))
                    .thenReturn(instructors.get(i));
            when(courseEnrollmentService.getEnrolledStudentCount(userCourseEnrollments.get(i).getCourseId()))
                    .thenReturn(10);
        }

        // when
        List<CourseEnrollmentResponse> result = sut.getCourseEnrollmentsByUserId(userId);

        // then
        assertThat(result).hasSize(3);

        verify(courseEnrollmentService, times(1)).getCourseEnrollmentsByUserId(userId);
        verify(courseService, times(3)).getCourse(anyLong());
        verify(userService, times(3)).getUser(anyLong());
        verify(courseEnrollmentService, times(3)).getEnrolledStudentCount(anyLong());
    }
}