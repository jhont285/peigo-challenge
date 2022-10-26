package com.peigo.domain.infrastructure.persistence.mysql.repository;

import com.peigo.domain.infrastructure.persistence.mysql.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
