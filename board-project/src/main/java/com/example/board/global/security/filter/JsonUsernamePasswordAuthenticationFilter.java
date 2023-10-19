package com.example.board.global.security.filter;

import com.example.board.global.exception.CommonErrorCode;
import com.example.board.global.exception.ErrorResponse;
import com.example.board.global.exception.GlobalException;
import com.example.board.global.security.CustomUserDetails;
import com.example.board.global.security.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.models.Operation;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final String DEFAULT_LOGIN_REQUEST_URL = "/api/login";
    //login 요청에서만 작동하도록 설정하는 변수
    private static final String HTTP_METHOD = "POST";
    //HTTP 메서드의 방식은 POST 이다.
    private static final String CONTENT_TYPE = "application/json";
    //json 타입의 데이터로만 로그인을 진행한다.
    private final ObjectMapper objectMapper;
    private static final String USERNAME_KEY="accountId";
    private static final String PASSWORD_KEY="password";



    private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD); //=>   /login 의 요청에, POST로 온 요청에 매칭된다.

    public JsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
        super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER);   // 위에서 설정한  /oauth2/login/* 의 요청에, GET으로 온 요청을 처리하기 위해 설정한다.
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        if(request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)) {
            throw new AuthenticationServiceException("Authentication Content-Type not supported: " + request.getContentType());
        }

        String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

        Map<String, String> usernamePasswordMap = objectMapper.readValue(messageBody, Map.class);

        String username = usernamePasswordMap.get(USERNAME_KEY);
        String password = usernamePasswordMap.get(PASSWORD_KEY);

        if (username == null || username.isEmpty()) {
            throw new GlobalException(CommonErrorCode.NOT_FOUND_ID);
        }


        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);//principal 과 credentials 전달

        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
