package com.peigo.domain.infrastructure.security;

import com.auth0.jwt.JWT;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
public class JUTAAuthenticationVerificationFilter extends BasicAuthenticationFilter {
	
	public JUTAAuthenticationVerificationFilter(AuthenticationManager authManager) {
        super(authManager);
    }
	
	@Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) 
    		throws IOException, ServletException {
        var header = req.getHeader(SecurityConstants.HEADER_STRING);

        if (isNull(header) || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        var authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
		var token = req.getHeader(SecurityConstants.HEADER_STRING);
        if (nonNull(token)) {
            var user = JWT.require(HMAC512(SecurityConstants.SECRET.getBytes())).build()
                    .verify(token.replace(SecurityConstants.TOKEN_PREFIX, StringUtils.EMPTY))
                    .getSubject();
            if (nonNull(user)) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
        }
        return null;
	}

}
