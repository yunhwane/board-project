package com.example.board.global.security.handler;

import com.example.board.global.Util.JsonUtil;
import com.example.board.global.exception.ErrorResponse;
import com.example.board.member.dto.response.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ResponseDto responseDto = new ResponseDto(401, "인증에 실패하셨습니다.");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write(JsonUtil.toJson(responseDto)); // JsonUtil은 JSON 변환을 위한 유틸 클래스
        response.getWriter().flush();
    }
}
