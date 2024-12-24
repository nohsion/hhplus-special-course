package com.hhplus.hhplus_special_course.domain.course.application;

import com.hhplus.hhplus_special_course.domain.course.domain.Course;
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

    public UserCourseEnrollment enrollCourse(final Course course, final long userId) {
        int enrolledStudents = courseEnrollmentRepository.countByCourseId(course.getId());
        if (course.isFullCapacity(enrolledStudents)) {
            throw new IllegalStateException("정원이 초과되었습니다.");
        }

        UserCourseEnrollment userCourseEnrollment = UserCourseEnrollment.of(userId, course.getId());
        return courseEnrollmentRepository.save(userCourseEnrollment);
    }
}
