package com.peigo.domain.infrastructure.service;

import com.peigo.domain.infrastructure.model.TransactionInfo;
import com.peigo.domain.infrastructure.persistence.mysql.entity.Account;
import com.peigo.domain.infrastructure.persistence.mysql.entity.Payment;
import com.peigo.domain.infrastructure.persistence.mysql.entity.User;
import com.peigo.domain.infrastructure.persistence.mysql.repository.AccountRepository;
import com.peigo.domain.infrastructure.persistence.mysql.repository.PaymentRepository;
import com.peigo.domain.infrastructure.persistence.mysql.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void transaction() {
        var userName = "userName";
        var user = User.builder()
                .id(1)
                .username("userName")
                .password("password")
                .build();

        var transactionInfo = TransactionInfo.builder()
                .idOriginAccount(1)
                .idDestinyAccount(2)
                .amount(BigDecimal.ONE)
                .build();

        var originAccount = Account.builder()
                .balance(BigDecimal.TEN)
                .user(user)
                .build();

        var destinationAccount = Account.builder()
                .balance(BigDecimal.ZERO)
                .user(user)
                .build();

        when(userRepository.findByUsername(userName)).thenReturn(user);
        when(accountRepository.findById(transactionInfo.idOriginAccount())).thenReturn(Optional.of(originAccount));
        when(accountRepository.findById(transactionInfo.idDestinyAccount())).thenReturn(Optional.of(destinationAccount));
        when(paymentRepository.save(any())).then(returnsFirstArg());

        var paymentExpected = Payment.builder()
                .origin(originAccount)
                .destiny(destinationAccount)
                .user(user)
                .amount(transactionInfo.amount())
                .build();

        var paymentActual = paymentService.transaction(userName, transactionInfo);

        assertEquals(Optional.of(paymentExpected), paymentActual);
    }
}