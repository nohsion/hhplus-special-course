package com.hhplus.hhplus_special_course.domain.course.api;

import com.hhplus.hhplus_special_course.domain.course.api.response.CourseEnrollmentResponse;
import com.hhplus.hhplus_special_course.global.common.rest.ApiResponse;
import com.hhplus.hhplus_special_course.domain.course.application.CourseFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseFacade courseFacade;

    public CourseController(CourseFacade courseFacade) {
        this.courseFacade = courseFacade;
    }

    /**
     * 유저의 특강 신청 목록 조회
     */
    @GetMapping("/enrollments")
    public ResponseEntity<ApiResponse<List<CourseEnrollmentResponse>>> getCourseEnrollments(
            @RequestParam(value = "userId") final long userId
    ) {
        List<CourseEnrollmentResponse> courseEnrollments = courseFacade.getCourseEnrollmentsByUserId(userId);

        ApiResponse<List<CourseEnrollmentResponse>> response = ApiResponse.ofSuccess(courseEnrollments);
        return ResponseEntity.ok(response);
    }
}
