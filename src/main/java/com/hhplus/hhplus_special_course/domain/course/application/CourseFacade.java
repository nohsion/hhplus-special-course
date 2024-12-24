package com.hhplus.hhplus_special_course.domain.course.application;

import com.hhplus.hhplus_special_course.domain.course.api.converter.CourseResponseConverter;
import com.hhplus.hhplus_special_course.domain.course.api.response.CourseResponse;
import com.hhplus.hhplus_special_course.domain.course.domain.Course;
import com.hhplus.hhplus_special_course.domain.course.domain.UserCourseEnrollment;
import com.hhplus.hhplus_special_course.domain.course.api.response.CourseEnrollmentResponse;
import com.hhplus.hhplus_special_course.domain.user.application.UserService;
import com.hhplus.hhplus_special_course.domain.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class CourseFacade {

    private final CourseService courseService;
    private final CourseEnrollmentService courseEnrollmentService;
    private final UserService userService;

    public CourseFacade(
            final CourseService courseService,
            final CourseEnrollmentService courseEnrollmentService,
            final UserService userService
    ) {
        this.courseService = courseService;
        this.courseEnrollmentService = courseEnrollmentService;
        this.userService = userService;
    }

    public List<CourseEnrollmentResponse> getCourseEnrollmentsByUserId(final long userId) {
        List<UserCourseEnrollment> courseEnrollments = courseEnrollmentService.getCourseEnrollmentsByUserId(userId);
        if (CollectionUtils.isEmpty(courseEnrollments)) {
            return Collections.emptyList();
        }
        return courseEnrollments.stream()
                .map(enrollment -> {
                    final long courseId = enrollment.getCourseId();

                    Course course = courseService.getCourse(courseId);
                    User instructor = userService.getUser(course.getInstructorId());
                    int enrolledStudentCount = courseEnrollmentService.getEnrolledStudentCount(courseId);

                    return CourseResponseConverter.from(
                            enrollment,
                            course,
                            instructor,
                            enrolledStudentCount
                    );
                })
                .toList();
    }

    public List<CourseResponse> getAvailableCourses() {
        List<Course> allCourses = courseService.getAllCourses();
        if (CollectionUtils.isEmpty(allCourses)) {
            return Collections.emptyList();
        }
        return allCourses.stream()
                .map(course -> {
                    int enrolledStudentCount = courseEnrollmentService.getEnrolledStudentCount(course.getId());
                    if (course.isFullCapacity(enrolledStudentCount)) {
                        return null;
                    }
                    User instructor = userService.getUser(course.getInstructorId());
                    return CourseResponseConverter.from(
                            course,
                            instructor,
                            enrolledStudentCount
                    );
                })
                .filter(Objects::nonNull)
                .toList();
    }

    public void enrollCourse(final long courseId, final long userId) {
        Course course = courseService.getCourse(courseId);
        User user = userService.getUser(userId);
        if (course.isInstructor(user.getId())) {
            throw new IllegalArgumentException("강연자는 자신의 특강에 신청할 수 없습니다.");
        }

        UserCourseEnrollment userCourseEnrollment = courseEnrollmentService.enrollCourse(course, userId);
        log.debug("Enrolled courseId: {}, userId: {}, enrolledDateTime: {}",
                userCourseEnrollment.getCourseId(),
                userCourseEnrollment.getStudentId(),
                userCourseEnrollment.getCreatedAt());
    }
}
