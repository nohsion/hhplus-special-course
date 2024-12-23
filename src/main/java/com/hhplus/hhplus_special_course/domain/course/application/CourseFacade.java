package com.hhplus.hhplus_special_course.domain.course.application;

import com.hhplus.hhplus_special_course.domain.course.api.converter.CourseResponseConverter;
import com.hhplus.hhplus_special_course.domain.course.domain.Course;
import com.hhplus.hhplus_special_course.domain.course.domain.UserCourseEnrollment;
import com.hhplus.hhplus_special_course.domain.course.api.response.CourseEnrollmentResponse;
import com.hhplus.hhplus_special_course.domain.user.application.UserService;
import com.hhplus.hhplus_special_course.domain.user.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

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
}
