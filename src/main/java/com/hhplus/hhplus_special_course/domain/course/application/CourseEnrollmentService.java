package com.hhplus.hhplus_special_course.domain.course.application;

import com.hhplus.hhplus_special_course.domain.course.domain.UserCourseEnrollment;
import com.hhplus.hhplus_special_course.domain.course.repository.CourseEnrollmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseEnrollmentService {

    private final CourseEnrollmentRepository courseEnrollmentRepository;

    public CourseEnrollmentService(
            final CourseEnrollmentRepository courseEnrollmentRepository
    ) {
        this.courseEnrollmentRepository = courseEnrollmentRepository;
    }

    public List<UserCourseEnrollment> getCourseEnrollmentsByUserId(final long userId) {
        return courseEnrollmentRepository.findByStudentId(userId);
    }

    public int getEnrolledStudentCount(final long courseId) {
        return courseEnrollmentRepository.countByCourseId(courseId);
    }
}
