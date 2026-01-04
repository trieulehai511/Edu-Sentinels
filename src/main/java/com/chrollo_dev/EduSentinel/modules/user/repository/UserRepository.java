package com.chrollo_dev.EduSentinel.modules.user.repository;

import com.chrollo_dev.EduSentinel.modules.user.dto.UserResponse;
import com.chrollo_dev.EduSentinel.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserById(String id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User>  findUserByUsername(String username);
    Optional<User> findUserByEmail(String email);

}
