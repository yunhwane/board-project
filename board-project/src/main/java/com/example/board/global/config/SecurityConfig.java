package com.example.board.global.config;

import com.example.board.global.jwt.RefreshTokenRepository;
import com.example.board.global.jwt.service.BlacklistTokenService;
import com.example.board.global.jwt.service.JwtService;
import com.example.board.global.jwt.service.redis.RedisService;
import com.example.board.global.security.filter.CustomLogoutFilter;
import com.example.board.global.security.filter.JsonUsernamePasswordAuthenticationFilter;
import com.example.board.global.jwt.filter.JwtAuthenticationProcessingFilter;
import com.example.board.global.security.handler.CustomAuthenticationFailureHandler;
import com.example.board.global.security.handler.CustomLogoutSuccessHandler;
import com.example.board.global.security.handler.LoginFailureHandler;
import com.example.board.global.security.handler.LoginSuccessJWTProvideHandler;
import com.example.board.member.domain.repository.MemberRepository;
import com.example.board.global.security.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService loginService;
    private final ObjectMapper objectMapper;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final BlacklistTokenService blacklistTokenService;
    private final RedisService redisService;
    private final JwtService jwtService;

    private static final String[] PERMIT_URL_ARRAY = {
            /* swagger v2 */
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            /* swagger v3 */
            "/v3/api-docs",
            "/swagger-ui/**"
    };

    @Bean
    public SecurityFilterChain springFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()
                //cors 설정
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //권한 설정
                .authenticationManager(authenticationManager())
                .authorizeRequests()
                .antMatchers("/api/admin/**")
                .permitAll()
                .antMatchers(PERMIT_URL_ARRAY)
                .permitAll()
                .antMatchers("/api/user/me", "/api/user/**")
                .permitAll()
               // .authenticated()

                .and()
                .addFilterBefore(characterEncodingFilter(), SecurityContextPersistenceFilter.class)
                .addFilterBefore(customLogoutFilter(),LogoutFilter.class)
                .addFilterBefore(jsonUsernamePasswordLoginFilter(), LogoutFilter.class)
                .addFilterBefore(jwtAuthenticationProcessingFilter(), JsonUsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    //cors 허용 정책 설정 beand
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://192.168.11.113");
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("Authorization-refresh");


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        // 구현 되어 있는 메서드 BCryptPasswordEncoder
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager() {//2 - AuthenticationManager 등록
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();//DaoAuthenticationProvider 사용
        provider.setPasswordEncoder(passwordEncoder());//PasswordEncoder로는 PasswordEncoderFactories.createDelegatingPasswordEncoder() 사용
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    @Bean
    public LoginSuccessJWTProvideHandler loginSuccessJWTProvideHandler(){
        return new LoginSuccessJWTProvideHandler(jwtService,memberRepository,refreshTokenRepository);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler(){
        return new LoginFailureHandler();
    }


    @Bean
    public CustomLogoutSuccessHandler logoutSuccessHandler(){
        return new CustomLogoutSuccessHandler();
    }

    @Bean
    public JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordLoginFilter(){
        JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordLoginFilter = new JsonUsernamePasswordAuthenticationFilter(objectMapper);
        jsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        jsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessJWTProvideHandler());
        jsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return jsonUsernamePasswordLoginFilter;
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter(){
        JwtAuthenticationProcessingFilter jsonUsernamePasswordLoginFilter = new JwtAuthenticationProcessingFilter(jwtService, memberRepository, refreshTokenRepository,redisService,blacklistTokenService);
        return jsonUsernamePasswordLoginFilter;
    }

    @Bean
    public CustomLogoutFilter customLogoutFilter(){
        CustomLogoutFilter customLogoutFilter = new CustomLogoutFilter(logoutSuccessHandler(),jwtService,blacklistTokenService);
        return customLogoutFilter;
    }
    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

}
