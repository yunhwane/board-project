package com.example.board.global.jwt;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;


//redis 2주

@Getter
@NoArgsConstructor
//2주
@RedisHash(value = "refreshToken",timeToLive = 1209600)
public class RefreshToken {

    @Id
    private String accessToken;
    private String refreshToken;

    @Builder
    public RefreshToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
