package com.peigo.domain.infrastructure.controllers;

import com.peigo.domain.infrastructure.model.TransactionInfo;
import com.peigo.domain.infrastructure.persistence.mysql.entity.Payment;
import com.peigo.domain.infrastructure.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public Optional<Payment> transaction(Authentication authentication,
                                         @Valid TransactionInfo transactionInfo) {
        return paymentService.transaction(authentication.getName(), transactionInfo);
    }
}
