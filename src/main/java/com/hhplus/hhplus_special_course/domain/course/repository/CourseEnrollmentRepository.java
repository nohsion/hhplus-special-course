package com.hhplus.hhplus_special_course.domain.course.repository;

import com.hhplus.hhplus_special_course.domain.course.domain.UserCourseEnrollment;

import java.util.List;

public interface CourseEnrollmentRepository {

    List<UserCourseEnrollment> findByStudentId(long userId);

    int countByCourseId(long courseId);

    UserCourseEnrollment save(UserCourseEnrollment userCourseEnrollment);
}
