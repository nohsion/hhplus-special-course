package com.hhplus.hhplus_special_course.global.common.lock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * MariaDB 호환성 체크를 위해 @SpringBootTest 통합 테스트로 확인합니다.
 */
@SpringBootTest
@ActiveProfiles("test")
class MariaDBJpaNamedLockManagerTest {

    @Autowired
    private MariaDBJpaNamedLockManager namedLockManager;

    @DisplayName("lockName을 이용하여 lock을 획득하고 해제한다.")
    @Test
    void executeWithLockSuccess() {
        // given
        String lockName = "testLock";
        int timeoutSeconds = 1;

        // when
        String result = namedLockManager.executeWithLock(lockName, timeoutSeconds, () -> "success");

        // then
        assertThat(result).isEqualTo("success");
    }

    @DisplayName("타임아웃이 음수이면 lock 획득에 실패한다.")
    @Test
    void executeWithLockFailWhenTimeoutisZero() {
        // given
        String lockName = "testLock";
        int timeoutSeconds = -1;

        // when
        // then
        assertThatThrownBy(() -> namedLockManager.executeWithLock(lockName, timeoutSeconds, () -> "success"))
                .isInstanceOf(NamedLockException.class)
                .hasMessage("NamedLock을 수행하는 도중 오류가 발생했습니다. - RELEASE");
    }

    @DisplayName("lockName이 null이면 lock 획득에 실패한다.")
    @Test
    void executeWithLockFailWhenLockNameIsNull() {
        // given
        String lockName = null;
        int timeoutSeconds = 1;

        // when
        // then
        assertThatThrownBy(() -> namedLockManager.executeWithLock(lockName, timeoutSeconds, () -> "success"))
                .isInstanceOf(NamedLockException.class)
                .hasMessage("NamedLock을 수행하는 도중 오류가 발생했습니다. - RELEASE");
    }

    @DisplayName("lockName이 빈 문자열이면 lock 획득에 실패한다.")
    @Test
    void executeWithLockFailWhenLockNameIsEmpty() {
        // given
        String lockName = "";
        int timeoutSeconds = 1;

        // when
        // then
        assertThatThrownBy(() -> namedLockManager.executeWithLock(lockName, timeoutSeconds, () -> "success"))
                .isInstanceOf(NamedLockException.class)
                .hasMessage("NamedLock을 수행하는 도중 오류가 발생했습니다. - RELEASE");
    }

}