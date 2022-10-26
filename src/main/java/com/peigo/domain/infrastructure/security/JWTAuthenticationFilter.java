package com.peigo.domain.infrastructure.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peigo.domain.infrastructure.persistence.mysql.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@RequiredArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) {
        var credentials = new ObjectMapper()
                .readValue(req.getInputStream(), User.class);

        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getUsername(),
                        credentials.getPassword(),
                        Collections.emptyList()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) {

        String token = JWT.create()
                .withSubject(((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(HMAC512(SecurityConstants.SECRET.getBytes()));
        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
    }
}
