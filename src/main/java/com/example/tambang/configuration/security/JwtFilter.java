package com.example.tambang.configuration.security;

import com.example.tambang.configuration.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorization = jwtProvider.resolveTokenFromRequest(request);

        log.info("authorization: " + authorization);

        if(authorization == null || !authorization.startsWith("Bearer ")){
            log.error("authorization error");
            filterChain.doFilter(request, response);
            return;
        }

        // "Bearer " removing
        String token = jwtProvider.BearerRemove(authorization);
        log.info("token = " + token);
        boolean isExpired = jwtProvider.isExpired(token);
        if(isExpired){
            log.error("token is expired");
            filterChain.doFilter(request, response);
        }
        String username = jwtProvider.getUserEmail(token);
        log.info("username = " + username);
        Authentication authentication = jwtProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("end of jwt authorization filter");
        filterChain.doFilter(request, response);
    }
}