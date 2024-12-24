package com.hhplus.hhplus_special_course.domain.course.api.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hhplus.hhplus_special_course.global.utils.DateTimeUtils;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CourseResponse(
        @JsonProperty("course_id")
        long courseId,
        @JsonProperty("course_title")
        String courseTitle,
        @JsonProperty("instructor_id")
        long instructorId,
        @JsonProperty("instructor_name")
        String instructorName,
        @JsonProperty("course_description")
        String courseDescription,
        @JsonProperty("enrolled_students")
        int enrolledStudents,
        @JsonProperty("max_students")
        int maxStudents,
        @JsonFormat(pattern = DateTimeUtils.DATE_FORMAT)
        @JsonProperty("course_date")
        LocalDate courseDate
) {
}
