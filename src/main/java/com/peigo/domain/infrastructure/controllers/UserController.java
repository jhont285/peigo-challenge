package com.peigo.domain.infrastructure.controllers;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.peigo.domain.infrastructure.model.CreateUserRequest;
import com.peigo.domain.infrastructure.persistence.mysql.entity.User;
import com.peigo.domain.infrastructure.persistence.mysql.repository.UserRepository;
import com.peigo.domain.infrastructure.security.SecurityConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.ObjectUtils.notEqual;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManagerUser;

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.of(userRepository.findById(id));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        return isNull(user)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        if (notEqual(createUserRequest.password(), createUserRequest.confirmPassword())) {
            log.error("the pass and conf pass do not match. Unable to create {}",
                    createUserRequest.username());

            return ResponseEntity.badRequest()
                    .build();
        }

        if (nonNull(userRepository.findByUsername(createUserRequest.username()))) {
            log.error("user already exits {}", createUserRequest.username());
            return ResponseEntity.badRequest()
                    .build();
        }

        var user = User.builder()
                .username(createUserRequest.username())
                .password(bCryptPasswordEncoder.encode(createUserRequest.password()))
                .build();

        userRepository.save(user);

        var auth = authenticationManagerUser.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword(),
                        new ArrayList<>())
        );

        var token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

        return ResponseEntity.ok(SecurityConstants.TOKEN_PREFIX + token);
    }

}
