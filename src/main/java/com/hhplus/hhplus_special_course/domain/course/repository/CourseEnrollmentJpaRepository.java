package com.hhplus.hhplus_special_course.domain.course.repository;

import com.hhplus.hhplus_special_course.domain.course.domain.UserCourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseEnrollmentJpaRepository extends JpaRepository<UserCourseEnrollment, Long> {

    List<UserCourseEnrollment> findByStudentId(long userId);

    int countByCourseId(long courseId);
}
