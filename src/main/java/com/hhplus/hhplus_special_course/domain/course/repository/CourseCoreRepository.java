package com.hhplus.hhplus_special_course.domain.course.repository;

import com.hhplus.hhplus_special_course.domain.course.domain.Course;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CourseCoreRepository implements CourseRepository {

    private final CourseJpaRepository courseJpaRepository;

    public CourseCoreRepository(
            final CourseJpaRepository courseJpaRepository
    ) {
        this.courseJpaRepository = courseJpaRepository;
    }

    @Override
    public Optional<Course> findById(final long courseId) {
        return courseJpaRepository.findById(courseId);
    }

    @Override
    public List<Course> findAll() {
        return courseJpaRepository.findAll();
    }
}
