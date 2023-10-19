package com.example.board.global.security.handler;

import com.example.board.global.Util.JsonUtil;
import com.example.board.global.exception.CommonErrorCode;
import com.example.board.global.exception.ErrorResponse;
import com.example.board.global.exception.GlobalException;
import com.example.board.member.dto.response.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(value = "로그인 실패 응답 url")
@SwaggerDefinition(tags = {@Tag(name = "로그인 실패 API 응답",description = "로그인 실패 API 응답")})
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    @ApiOperation(value = "로그인 실패 응답")
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ResponseDto responseDto = new ResponseDto(401, "아이디와 비밀번호가 유효하지 않습니다.");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write(JsonUtil.toJson(responseDto)); // JsonUtil은 JSON 변환을 위한 유틸 클래스
        response.getWriter().flush();
    }
}
