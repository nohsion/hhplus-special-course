package com.hhplus.hhplus_special_course.domain.course.api.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hhplus.hhplus_special_course.global.utils.DateTimeUtils;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record CourseEnrollmentResponse(
        @JsonProperty("course_enrollment_id")
        long courseEnrollmentId,
        @JsonProperty("course_id")
        long courseId,
        @JsonProperty("course_title")
        String courseTitle,
        @JsonProperty("instructor_id")
        long instructorId,
        @JsonProperty("instructor_name")
        String instructorName,
        @JsonProperty("max_students")
        int maxStudents,
        @JsonProperty("enrolled_students")
        int enrolledStudents,
        @JsonFormat(pattern = DateTimeUtils.DATE_FORMAT)
        @JsonProperty("course_date")
        LocalDate courseDate,
        @JsonFormat(pattern = DateTimeUtils.DATE_TIME_FORMAT)
        @JsonProperty("created_at")
        LocalDateTime createdAt
) {
}
