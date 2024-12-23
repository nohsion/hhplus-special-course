package com.hhplus.hhplus_special_course.domain.user.repository;

import com.hhplus.hhplus_special_course.domain.user.domain.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(long userId);
}
