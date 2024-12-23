package com.hhplus.hhplus_special_course.domain.course.api.converter;

import com.hhplus.hhplus_special_course.domain.course.api.response.CourseEnrollmentResponse;
import com.hhplus.hhplus_special_course.domain.course.domain.Course;
import com.hhplus.hhplus_special_course.domain.course.domain.UserCourseEnrollment;
import com.hhplus.hhplus_special_course.domain.user.domain.User;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CourseResponseConverterTest {

    @Test
    void courseEnrollmentResponseConverterTest() {
        // given
        UserCourseEnrollment userCourseEnrollment = Instancio.of(UserCourseEnrollment.class).create();
        Course course = Instancio.of(Course.class).create();
        User instructor = Instancio.of(User.class).create();
        int enrolledStudentCount = 10;

        // when
        CourseEnrollmentResponse result = CourseResponseConverter.from(
                userCourseEnrollment,
                course,
                instructor,
                enrolledStudentCount
        );

        // then
        assertThat(result.courseEnrollmentId()).isEqualTo(userCourseEnrollment.getId());
        assertThat(result.courseId()).isEqualTo(course.getId());
        assertThat(result.courseTitle()).isEqualTo(course.getTitle());
        assertThat(result.instructorId()).isEqualTo(instructor.getId());
        assertThat(result.instructorName()).isEqualTo(instructor.getName());
        assertThat(result.maxStudents()).isEqualTo(course.getMaxStudents());
        assertThat(result.enrolledStudents()).isEqualTo(enrolledStudentCount);
        assertThat(result.courseDate()).isEqualTo(course.getCourseDate());
        assertThat(result.createdAt()).isEqualTo(userCourseEnrollment.getCreatedAt());
    }

}