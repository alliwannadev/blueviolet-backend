package com.blueviolet.backend.modules.user.repository;

import com.blueviolet.backend.modules.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByEmail(String email);

    Long countByEmail(String email);

    boolean existsByEmail(String email);
}
