package com.example.board.global.Util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtils {
    //context holder에서 인증된 로그인 상태의 자기 자신 가져오기.
    public static String getLoginUsername(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && ((Authentication) authentication).getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            return user.getUsername();
        } else {
            throw new IllegalStateException("User not authenticated.");
        }
    }
}
