package com.hhplus.hhplus_special_course.domain.user.repository;

import com.hhplus.hhplus_special_course.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
}
