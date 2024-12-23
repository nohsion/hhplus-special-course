package com.hhplus.hhplus_special_course.domain.user.repository;

import com.hhplus.hhplus_special_course.domain.user.domain.User;
import com.hhplus.hhplus_special_course.test.TestDataCleaner;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

@SpringBootTest
@ActiveProfiles("test")
class UserCoreRepositoryTest {

    @Autowired
    private UserCoreRepository sut;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private TestDataCleaner testDataCleaner;

    @BeforeEach
    void setUp() {
        testDataCleaner.cleanUp();
    }

    @DisplayName("없는 사용자 ID로 조회하면 빈 Optional을 반환한다.")
    @Test
    void findByIdOptionalEmpty() {
        // given
        long userId = 1L;

        // when
        Optional<User> result = sut.findById(userId);

        // then
        assertThat(result).isEmpty();
    }

    @DisplayName("사용자 ID로 조회하면 해당 사용자를 반환한다.")
    @Test
    void findById() {
        // given
        User user = Instancio.of(User.class)
                .set(field(User::getId), null)
                .create();
        User savedUser = userJpaRepository.save(user);

        // when
        Optional<User> result = sut.findById(savedUser.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).usingRecursiveComparison()
                .isEqualTo(savedUser);
    }

}