package com.peigo.domain.infrastructure.persistence.mysql.repository;

import com.peigo.domain.infrastructure.persistence.mysql.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
