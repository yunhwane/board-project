package com.example.board.global.security;

import com.example.board.member.domain.entity.Member;

import com.example.board.member.domain.entity.code.AccountTypeCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Member member;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 사용자의 accountTypeCode를 가져와서 해당 권한을 부여
        AccountTypeCode accountType = member.getAccountTypeCode();

        if (accountType != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + accountType.getCodeName()));
        }

        return authorities;
    }
    @Override
    public String getPassword() {
        return member.getPassword();
    }
    @Override
    public String getUsername() {
        return member.getAccountId();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
