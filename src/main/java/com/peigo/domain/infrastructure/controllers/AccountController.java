package com.peigo.domain.infrastructure.controllers;

import com.peigo.domain.infrastructure.persistence.mysql.entity.Account;
import com.peigo.domain.infrastructure.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> create(Authentication authentication) {
        var account = accountService.create(authentication.getName());
        return ResponseEntity.ok(account);
    }
}
