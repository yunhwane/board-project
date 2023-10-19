package com.example.board.global.jwt.filter;

import com.example.board.global.jwt.RefreshToken;
import com.example.board.global.jwt.RefreshTokenRepository;
import com.example.board.global.jwt.service.BlacklistTokenService;
import com.example.board.global.jwt.service.JwtService;
import com.example.board.global.jwt.service.redis.RedisService;
import com.example.board.member.domain.entity.Member;
import com.example.board.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisService redisService;
    private final BlacklistTokenService blacklistTokenService;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    private final String NO_CHECK_URL = "/login";

    public static final String REFRESH_TOKEN_COOKIE_NAME = "RefreshToken";

    /*
    1, 리프레시 토큰 -> 유효 Access 재발급 필터진행 x
    2. 리프레시 토큰 없는 경우, 유저 정보 저장후 필터 계속 진행

     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response);
            return;//안해주면 아래로 내려가서 계속 필터를 진행해버림
        }

        // black list
        String accessToken = jwtService.extractAccessToken(request).filter(jwtService::isTokenValid).orElse(null);

        //black list
        if(accessToken != null && blacklistTokenService.isTokenBlacklisted(accessToken)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String refreshToken = jwtService
                .extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null); //2


        // Refresh token이 null이 아닐 때
        if(refreshToken != null && refreshToken.equals(refreshTokenRepository.findRefreshTokenByAccessToken(accessToken).filter(jwtService::isTokenValid).orElse(null))) {
            try {
                checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return;
        }
        checkAccessTokenAndAuthentication(request, response, filterChain);
    }
    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        jwtService.extractAccessToken(request).filter(jwtService::isTokenValid).ifPresent(
                accessToken -> jwtService.extractUsername(accessToken).ifPresent(
                        username -> memberRepository.findByAccountId(username).ifPresent(
                                member -> saveAuthentication(member)
                        )
                )
        );
        filterChain.doFilter(request,response);
    }
    private void saveAuthentication(Member member)  {
        UserDetails user = org.springframework.security.core.userdetails.User.builder()
                .username(member.getAccountId())
                .password(member.getPassword())
                .roles(member.getAccountTypeCode().getCodeName())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,authoritiesMapper.mapAuthorities(user.getAuthorities()));

        SecurityContext context = SecurityContextHolder.createEmptyContext();//5
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) throws Exception{

        Optional<RefreshToken> opt  = refreshTokenRepository.findByAccessToken(refreshToken);
        // refresh token이 값 check -> 있으면 보내기
        opt.ifPresent(member -> jwtService.sendAccessToken(response, jwtService.createAccessToken(member.getAccessToken())));
        opt.orElseThrow(() -> new AuthenticationServiceException("Refresh token 재발급 필요"));
        log.info("재발급함");
    }

}
