package com.hhplus.hhplus_special_course.domain.course.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.hhplus_special_course.domain.course.api.response.CourseEnrollmentResponse;
import com.hhplus.hhplus_special_course.domain.course.application.CourseFacade;
import com.hhplus.hhplus_special_course.global.common.rest.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseFacade courseFacade;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getCourseEnrollments() throws Exception {
        // given
        long userId = 1L;
        List<CourseEnrollmentResponse> courseEnrollments = List.of(
                getCourseEnrollmentResponse(1L),
                getCourseEnrollmentResponse(2L),
                getCourseEnrollmentResponse(3L)
        );

        when(courseFacade.getCourseEnrollmentsByUserId(userId))
                .thenReturn(courseEnrollments);

        // when
        // then
        String jsonResponse = mockMvc.perform(get("/api/v1/courses/enrollments")
                        .param("userId", String.valueOf(userId)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value(ApiResponse.ResultCode.SUCCESS.name()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        TypeReference<ApiResponse<List<CourseEnrollmentResponse>>> typeReference = new TypeReference<>() {
        };
        ApiResponse<List<CourseEnrollmentResponse>> actualApiResponse = objectMapper.readValue(jsonResponse, typeReference);

        assertThat(actualApiResponse.getData())
                .hasSize(3)
                .usingRecursiveComparison().isEqualTo(courseEnrollments);

    }

    private CourseEnrollmentResponse getCourseEnrollmentResponse(long courseEnrollmentId) {
        LocalDateTime mockEnrolledDate = LocalDateTime.of(2024, 12, 24, 12, 0, 0);
        return CourseEnrollmentResponse.builder()
                .courseEnrollmentId(courseEnrollmentId)
                .courseId(1L)
                .courseTitle("특강 제목")
                .instructorId(1L)
                .instructorName("강연자")
                .maxStudents(30)
                .enrolledStudents(5)
                .courseDate(LocalDate.of(2024, 12, 25))
                .createdAt(mockEnrolledDate)
                .build();
    }
}