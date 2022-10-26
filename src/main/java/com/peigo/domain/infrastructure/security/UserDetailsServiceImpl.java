package com.peigo.domain.infrastructure.security;

import com.peigo.domain.infrastructure.persistence.mysql.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        if (isNull(user)) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.emptyList());
    }
}
