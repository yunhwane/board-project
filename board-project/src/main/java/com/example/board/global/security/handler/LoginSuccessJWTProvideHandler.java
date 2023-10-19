package com.example.board.global.security.handler;

import com.example.board.global.Util.JsonUtil;
import com.example.board.global.exception.CommonErrorCode;
import com.example.board.global.exception.ErrorResponse;
import com.example.board.global.jwt.RefreshToken;
import com.example.board.global.jwt.RefreshTokenRepository;
import com.example.board.global.jwt.service.JwtService;
import com.example.board.member.domain.entity.Member;
import com.example.board.member.domain.repository.MemberRepository;
import com.example.board.member.dto.response.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Api(value = "로그인 성공 응답")
@SwaggerDefinition(tags = {@Tag(name = "login API",description = "login API")})
public class LoginSuccessJWTProvideHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @ApiOperation(value ="로그인 성공시 응답")
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = extractUsername(authentication);
        String accessToken = jwtService.createAccessToken(username);
        String refreshToken = jwtService.createRefreshToken();

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

        Optional<Member> findMember = memberRepository.findByAccountId(username);
                if(findMember.get().getIsEnabled().equals("YES")) {
                    RefreshToken rft = RefreshToken.builder()
                            .refreshToken(refreshToken)
                            .accessToken(accessToken)
                            .build();

                    refreshTokenRepository.save(rft);

                    log.info("로그인에 성공합니다. username: {}", username);
                    log.info("AccessToken 을 발급합니다. AccessToken: {}", accessToken);
                    log.info("RefreshToken 을 발급합니다. RefreshToken: {}", refreshToken);

                    if (findMember.get().getPasswordChangeFlag()) {
                        ResponseDto responseDto = new ResponseDto(200, "비밀번호 강제 변경 대상입니다.");
                        response.setStatus(HttpStatus.OK.value());
                        response.setContentType("application/json");
                        response.getWriter().write(JsonUtil.toJson(responseDto)); // JsonUtil은 JSON 변환을 위한 유틸 클래스
                        response.getWriter().flush();
                    } else {
                        // 로그인 성공 로직
                        ResponseDto responseDto = new ResponseDto(200, "Success");
                        response.setStatus(HttpStatus.OK.value());
                        response.setContentType("application/json");
                        response.getWriter().write(JsonUtil.toJson(responseDto)); // JsonUtil은 JSON 변환을 위한 유틸 클래스
                        response.getWriter().flush();
                    }
                } else{
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                    ErrorResponse errorResponse = ErrorResponse.builder()
                            .code(CommonErrorCode.USER_ACCOUNT_DISABLED.getHttpStatus().value())
                            .message(CommonErrorCode.USER_ACCOUNT_DISABLED.getMessage())
                            .build();

                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.writeValue(response.getWriter(), errorResponse);
                }
        }

    private String extractUsername(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
