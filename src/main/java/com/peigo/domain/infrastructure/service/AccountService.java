package com.peigo.domain.infrastructure.service;

import com.peigo.domain.infrastructure.persistence.mysql.entity.Account;
import com.peigo.domain.infrastructure.persistence.mysql.repository.AccountRepository;
import com.peigo.domain.infrastructure.persistence.mysql.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public Account create(final String username) {
        var user = userRepository.findByUsername(username);
        var account = Account.builder()
                .balance(BigDecimal.ZERO)
                .user(user)
                .build();

        return accountRepository.save(account);
    }
}
