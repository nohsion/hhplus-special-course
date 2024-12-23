package com.hhplus.hhplus_special_course.domain.user.repository;

import com.hhplus.hhplus_special_course.domain.user.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserCoreRepository implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserCoreRepository(
            final UserJpaRepository userJpaRepository
    ) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> findById(long userId) {
        return userJpaRepository.findById(userId);
    }
}
