package com.hhplus.hhplus_special_course.domain.course.api.converter;

import com.hhplus.hhplus_special_course.domain.course.api.response.CourseEnrollmentResponse;
import com.hhplus.hhplus_special_course.domain.course.api.response.CourseResponse;
import com.hhplus.hhplus_special_course.domain.course.domain.Course;
import com.hhplus.hhplus_special_course.domain.course.domain.UserCourseEnrollment;
import com.hhplus.hhplus_special_course.domain.user.domain.User;

/**
 * Course 관련 도메인을 API Response로 변환하는 유틸 클래스입니다.
 */
public class CourseResponseConverter {

    private CourseResponseConverter() {
    }

    public static CourseEnrollmentResponse from(
            final UserCourseEnrollment courseEnrollment,
            final Course course,
            final User instructor,
            final int enrolledStudents
    ) {
        return CourseEnrollmentResponse.builder()
                .courseEnrollmentId(courseEnrollment.getId())
                .courseId(course.getId())
                .courseTitle(course.getTitle())
                .courseTitle(course.getTitle())
                .instructorId(instructor.getId())
                .instructorName(instructor.getName())
                .maxStudents(course.getMaxStudents())
                .enrolledStudents(enrolledStudents)
                .courseDate(course.getCourseDate())
                .createdAt(courseEnrollment.getCreatedAt())
                .build();

    }

    public static CourseResponse from(
            final Course course,
            final User instructor,
            final int enrolledStudents
    ) {
        return CourseResponse.builder()
                .courseId(course.getId())
                .courseTitle(course.getTitle())
                .instructorId(instructor.getId())
                .instructorName(instructor.getName())
                .courseDescription(course.getDescription())
                .enrolledStudents(enrolledStudents)
                .maxStudents(course.getMaxStudents())
                .courseDate(course.getCourseDate())
                .build();
    }
}
