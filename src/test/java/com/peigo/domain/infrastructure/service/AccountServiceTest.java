package com.peigo.domain.infrastructure.service;

import com.peigo.domain.infrastructure.persistence.mysql.entity.Account;
import com.peigo.domain.infrastructure.persistence.mysql.entity.User;
import com.peigo.domain.infrastructure.persistence.mysql.repository.AccountRepository;
import com.peigo.domain.infrastructure.persistence.mysql.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void create() {
        var userName = "userName";

        var user = User.builder()
                .id(1)
                .username("username")
                .password("password")
                .build();

        when(userRepository.findByUsername(userName)).thenReturn(user);
        when(accountRepository.save(any())).then(returnsFirstArg());

        var accountExpected = Account.builder()
                .balance(BigDecimal.ZERO)
                .user(user)
                .build();
        var accountActual = accountService.create(userName);

        assertEquals(accountExpected.getBalance(), accountActual.getBalance());
        assertEquals(accountExpected.getUser(), accountActual.getUser());
    }
}