package com.hhplus.hhplus_special_course.domain.course.repository;

import com.hhplus.hhplus_special_course.domain.course.domain.UserCourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseEnrollmentJpaRepository extends JpaRepository<UserCourseEnrollment, Long> {

    List<UserCourseEnrollment> findByStudentId(long userId);

    int countByCourseId(long courseId);
}
