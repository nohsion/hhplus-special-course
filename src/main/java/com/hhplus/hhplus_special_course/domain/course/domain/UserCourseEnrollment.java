package com.hhplus.hhplus_special_course.domain.course.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "user_course_enrollment")
public class UserCourseEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "student_id", nullable = false)
    private long studentId;

    @Column(name = "course_id", nullable = false)
    private long courseId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected UserCourseEnrollment() {
    }

    private UserCourseEnrollment(final long studentId, final long courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public static UserCourseEnrollment of(final long studentId, final long courseId) {
        return new UserCourseEnrollment(studentId, courseId);
    }
}
