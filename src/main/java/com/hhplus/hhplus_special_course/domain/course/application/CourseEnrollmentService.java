package com.hhplus.hhplus_special_course.domain.course.application;

import com.hhplus.hhplus_special_course.domain.course.domain.Course;
import com.hhplus.hhplus_special_course.domain.course.domain.UserCourseEnrollment;
import com.hhplus.hhplus_special_course.domain.course.exception.CourseFullCapacityException;
import com.hhplus.hhplus_special_course.domain.course.repository.CourseEnrollmentRepository;
import com.hhplus.hhplus_special_course.global.common.lock.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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

    @DistributedLock(key = "enroll_course_lock")
    public UserCourseEnrollment enrollCourse(final Course course, final long userId) {
        int enrolledStudents = courseEnrollmentRepository.countByCourseId(course.getId());
        log.debug("student count: {}", enrolledStudents);
        if (course.isFullCapacity(enrolledStudents)) {
            throw new CourseFullCapacityException();
        }

        UserCourseEnrollment userCourseEnrollment = UserCourseEnrollment.of(userId, course.getId());
        return courseEnrollmentRepository.save(userCourseEnrollment);
    }
}
