package com.hhplus.hhplus_special_course.domain.course.application;

import com.hhplus.hhplus_special_course.domain.course.api.response.CourseEnrollmentResponse;
import com.hhplus.hhplus_special_course.domain.course.api.response.CourseResponse;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @DisplayName("특강 신청 가능 목록이 없으면 빈 리스트를 반환한다.")
    @Test
    void getAvailableCoursesEmptyList() {
        // given
        when(courseService.getAllCourses())
                .thenReturn(Collections.emptyList());

        // when
        List<CourseEnrollmentResponse> result = sut.getCourseEnrollmentsByUserId(1L);

        // then
        assertThat(result).isEmpty();
    }

    @DisplayName("정원이 가득찬 특강 신청 가능 목록은 제외하고 반환한다.")
    @Test
    void getAvailableCoursesFullCapacity() {
        // given
        int maxStudents = 30;
        int enrolledStudents = 30;
        List<Course> courses = Instancio.ofList(Course.class)
                .size(3)
                .set(field(Course::getMaxStudents), maxStudents)
                .create();
        List<User> instructors = courses.stream()
                .map(course -> Instancio.of(User.class)
                        .set(field(User::getId), course.getInstructorId())
                        .create())
                .toList();

        when(courseService.getAllCourses())
                .thenReturn(courses);
        for (int i = 0; i < courses.size(); i++) {
            when(userService.getUser(courses.get(i).getInstructorId()))
                    .thenReturn(instructors.get(i));
            when(courseEnrollmentService.getEnrolledStudentCount(courses.get(i).getId()))
                    .thenReturn(enrolledStudents);
        }

        // when
        List<CourseResponse> result = sut.getAvailableCourses();

        // then
        assertThat(result).isEmpty();
    }

    @DisplayName("정원이 가득차지 않은 특강 신청 가능 목록을 반환한다.")
    @Test
    void getAvailableCourses() {
        // given
        int maxStudents = 30;
        int enrolledStudents = 10;
        List<Course> courses = Instancio.ofList(Course.class)
                .size(3)
                .set(field(Course::getMaxStudents), maxStudents)
                .create();
        List<User> instructors = courses.stream()
                .map(course -> Instancio.of(User.class)
                        .set(field(User::getId), course.getInstructorId())
                        .create())
                .toList();

        when(courseService.getAllCourses())
                .thenReturn(courses);
        for (int i = 0; i < courses.size(); i++) {
            when(userService.getUser(courses.get(i).getInstructorId()))
                    .thenReturn(instructors.get(i));
            when(courseEnrollmentService.getEnrolledStudentCount(courses.get(i).getId()))
                    .thenReturn(enrolledStudents);
        }

        // when
        List<CourseResponse> result = sut.getAvailableCourses();

        // then
        assertThat(result).hasSize(3);

        verify(courseService, times(1)).getAllCourses();
        verify(userService, times(3)).getUser(anyLong());
        verify(courseEnrollmentService, times(3)).getEnrolledStudentCount(anyLong());
    }

    @DisplayName("강연자는 자신의 특강을 신청하면 IllegalArgumentException 예외가 발생한다.")
    @Test
    void enrollCourseMyCourse() {
        // given
        long courseId = 1L;
        long userId = 1L;
        Course course = Instancio.of(Course.class)
                .set(field(Course::getInstructorId), userId)
                .create();
        User user = Instancio.of(User.class)
                .set(field(User::getId), userId)
                .create();

        when(courseService.getCourse(courseId))
                .thenReturn(course);
        when(userService.getUser(userId))
                .thenReturn(user);

        // when
        // then
        assertThatThrownBy(() -> sut.enrollCourse(courseId, userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("강연자는 자신의 특강에 신청할 수 없습니다.");
    }

    @DisplayName("특강 신청 성공")
    @Test
    void enrollCourseSuccess() {
        // given
        long courseId = 1L;
        long studentId = 2L;
        Course course = Instancio.of(Course.class)
                .set(field(Course::getInstructorId), 1L)
                .create();
        User user = Instancio.of(User.class)
                .set(field(User::getId), studentId)
                .create();
        UserCourseEnrollment userCourseEnrollment = Instancio.of(UserCourseEnrollment.class)
                .set(field(UserCourseEnrollment::getCourseId), courseId)
                .set(field(UserCourseEnrollment::getStudentId), studentId)
                .create();

        when(courseService.getCourse(courseId))
                .thenReturn(course);
        when(userService.getUser(studentId))
                .thenReturn(user);
        when(courseEnrollmentService.enrollCourse(course, studentId))
                .thenReturn(userCourseEnrollment);

        // when
        sut.enrollCourse(courseId, studentId);

        // then
        verify(courseService, times(1)).getCourse(courseId);
        verify(userService, times(1)).getUser(studentId);
        verify(courseEnrollmentService, times(1)).enrollCourse(course, studentId);
    }
}