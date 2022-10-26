package com.peigo.domain.infrastructure.persistence.mysql.repository;

import com.peigo.domain.infrastructure.persistence.mysql.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
