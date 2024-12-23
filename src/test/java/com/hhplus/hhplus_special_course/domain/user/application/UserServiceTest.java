package com.hhplus.hhplus_special_course.domain.user.application;

import com.hhplus.hhplus_special_course.domain.user.domain.User;
import com.hhplus.hhplus_special_course.domain.user.repository.UserRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private UserService sut;

    UserRepository userRepository = mock(UserRepository.class);

    @BeforeEach
    void setUp() {
        sut = new UserService(userRepository);
    }

    @DisplayName("없는 사용자 ID로 조회하면 IllegalArgumentException 예외가 발생한다.")
    @Test
    void getUserFail() {
        // given
        long userId = 1L;

        // when
        // then
        assertThatThrownBy(() -> sut.getUser(userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found");
    }

    @DisplayName("사용자 ID로 조회하면 사용자가 성공적으로 반환된다.")
    @Test
    void getUserSuccess() {
        // given
        long userId = 1L;
        User user = Instancio.of(User.class).create();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        // when
        User result = sut.getUser(userId);

        // then
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(user);
    }

}