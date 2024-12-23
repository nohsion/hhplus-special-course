package com.hhplus.hhplus_special_course.test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
public class IntegrationTestSupport {

    @Container
    static final MariaDBContainer MARIADB_CONTAINER = new MariaDBContainer<>("mariadb:10.11")
            .withReuse(true);
}
