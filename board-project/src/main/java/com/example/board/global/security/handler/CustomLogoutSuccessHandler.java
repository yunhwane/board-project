package com.example.board.global.security.handler;

import com.example.board.global.Util.JsonUtil;
import com.example.board.member.dto.response.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ResponseDto responseDto = new ResponseDto(204, "로그아웃이 완료되었습니다.");
        response.setStatus(HttpStatus.NO_CONTENT.value());
        response.setContentType("application/json");
        response.getWriter().write(JsonUtil.toJson(responseDto)); // JsonUtil은 JSON 변환을 위한 유틸 클래스
        response.getWriter().flush();
    }
}
