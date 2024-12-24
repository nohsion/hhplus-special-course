package com.hhplus.hhplus_special_course.domain.course.exception;

public class CourseFullCapacityException extends RuntimeException {
    public CourseFullCapacityException() {
        super("정원이 초과되었습니다.");
    }
}
