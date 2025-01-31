package com.hhplus.hhplus_special_course.domain.course.api;

import com.hhplus.hhplus_special_course.domain.course.api.response.CourseEnrollmentResponse;
import com.hhplus.hhplus_special_course.domain.course.api.response.CourseResponse;
import com.hhplus.hhplus_special_course.global.common.rest.ApiResponse;
import com.hhplus.hhplus_special_course.domain.course.application.CourseFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 특강 신청 가능 목록 조회
     */
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getAvailableCourses() {
        List<CourseResponse> availableCourses = courseFacade.getAvailableCourses();

        ApiResponse<List<CourseResponse>> response = ApiResponse.ofSuccess(availableCourses);
        return ResponseEntity.ok(response);
    }

    /**
     * 특강 신청
     */
    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<ApiResponse<Void>> enrollCourse(
            @PathVariable("courseId") final long courseId,
            @RequestParam(value = "userId") final long userId
    ) {
        // todo: 강의별로 동시성 제어 필요 (선착순)
        courseFacade.enrollCourse(courseId, userId);

        ApiResponse<Void> response = ApiResponse.ofSuccess(null);
        return ResponseEntity.ok(response);
    }
}
