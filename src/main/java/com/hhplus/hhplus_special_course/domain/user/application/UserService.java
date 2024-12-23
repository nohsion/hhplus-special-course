package com.hhplus.hhplus_special_course.domain.user.application;

import com.hhplus.hhplus_special_course.domain.user.domain.User;
import com.hhplus.hhplus_special_course.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(
            final UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public User getUser(final long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
