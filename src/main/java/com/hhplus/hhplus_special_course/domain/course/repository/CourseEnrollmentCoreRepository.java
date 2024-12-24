package com.hhplus.hhplus_special_course.domain.course.repository;

import com.hhplus.hhplus_special_course.domain.course.domain.UserCourseEnrollment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseEnrollmentCoreRepository implements CourseEnrollmentRepository {

    private final CourseEnrollmentJpaRepository courseEnrollmentJpaRepository;

    public CourseEnrollmentCoreRepository(
            final CourseEnrollmentJpaRepository courseEnrollmentJpaRepository
    ) {
        this.courseEnrollmentJpaRepository = courseEnrollmentJpaRepository;
    }

    @Override
    public List<UserCourseEnrollment> findByStudentId(final long userId) {
        return courseEnrollmentJpaRepository.findByStudentId(userId);
    }

    @Override
    public int countByCourseId(final long courseId) {
        return courseEnrollmentJpaRepository.countByCourseId(courseId);
    }

    @Override
    public UserCourseEnrollment save(final UserCourseEnrollment userCourseEnrollment) {
        return courseEnrollmentJpaRepository.save(userCourseEnrollment);
    }

}
