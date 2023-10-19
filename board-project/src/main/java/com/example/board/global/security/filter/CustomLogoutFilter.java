package com.example.board.global.security.filter;

import com.example.board.global.jwt.service.BlacklistTokenService;
import com.example.board.global.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CustomLogoutFilter extends OncePerRequestFilter {

    private static final String LOGOUT_URL = "/api/logout";
    private static final String HTTP_METHOD = "POST";

    private final LogoutSuccessHandler logoutSuccessHandler;
    private final JwtService jwtService;
    private final BlacklistTokenService blacklistTokenService;


    public CustomLogoutFilter(LogoutSuccessHandler logoutSuccessHandler, JwtService jwtService, BlacklistTokenService blacklistTokenService) {
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.jwtService = jwtService;
        this.blacklistTokenService = blacklistTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (requiresLogout(request,response)) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            // 로그아웃 로직을 추가하고 로그아웃 처리

            if (auth != null) {
                String token = jwtService.extractAccessToken(request).filter(jwtService::isTokenValid).orElse(null);
                // 로그아웃 처리
                if(token != null){
                    // 블랙리스트 처리
                    blacklistTokenService.addInvalidToken(token);
                }
                logoutSuccessHandler.onLogoutSuccess((HttpServletRequest) request, (HttpServletResponse) response, auth);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
    private boolean requiresLogout(HttpServletRequest request, HttpServletResponse response) {
        return new AntPathRequestMatcher(LOGOUT_URL).matches(request) && HTTP_METHOD.equals(request.getMethod());
    }
}
