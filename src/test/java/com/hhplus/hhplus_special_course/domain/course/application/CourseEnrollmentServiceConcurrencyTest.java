package com.hhplus.hhplus_special_course.domain.course.application;

import com.hhplus.hhplus_special_course.domain.course.domain.Course;
import com.hhplus.hhplus_special_course.domain.course.exception.CourseFullCapacityException;
import com.hhplus.hhplus_special_course.test.TestDataCleaner;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

@SpringBootTest
@ActiveProfiles("test")
class CourseEnrollmentServiceConcurrencyTest {

    @Autowired
    private CourseEnrollmentService courseEnrollmentService;

    @Autowired
    private TestDataCleaner testDataCleaner;

    @BeforeEach
    void setUp() {
        testDataCleaner.cleanUp();
    }

    @DisplayName("동시에 동일한 특강에 대해 40명이 신청하면, 최대 정원인 30명만 성공하고 10명은 실패해야 한다.")
    @Test
    void enrollConcurrently() throws Exception {
        // given
        long courseId = 1L;
        int maxStudents = 30;
        Course course = Instancio.of(Course.class)
                .set(field(Course::getId), courseId)
                .set(field(Course::getMaxStudents), maxStudents)
                .create();

        final int numOfExecute = 40;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numOfExecute);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // when
        for (int i = 0; i < numOfExecute; i++) {
            long userId = i + 1L; // 모두 다른 유저가 특강 신청한다고 가정 (현실 세계 반영)
            executorService.submit(() -> {
                try {
                    courseEnrollmentService.enrollCourse(course, userId);
                    successCount.incrementAndGet();
                } catch (CourseFullCapacityException e) { // 정원 초과시 실패
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        int current = courseEnrollmentService.getEnrolledStudentCount(courseId);
        System.out.println("current = " + current);

        // then
        assertThat(successCount.get()).isEqualTo(30);
        assertThat(failCount.get()).isEqualTo(10);
    }
}
