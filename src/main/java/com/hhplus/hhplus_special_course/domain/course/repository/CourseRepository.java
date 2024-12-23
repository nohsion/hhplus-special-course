package com.hhplus.hhplus_special_course.domain.course.repository;

import com.hhplus.hhplus_special_course.domain.course.domain.Course;

import java.util.Optional;

public interface CourseRepository {

    Optional<Course> findById(long courseId);
}
