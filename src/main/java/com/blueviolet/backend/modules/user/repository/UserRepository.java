package com.blueviolet.backend.modules.user.repository;

import com.blueviolet.backend.modules.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByNameAndPhone(
            String name,
            String phone
    );

    Long countByEmail(String email);

}
