package com.example.board.global.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class BlacklistTokenService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String BLACKLIST_KEY = "jwt:blacklist";

    public BlacklistTokenService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addInvalidToken(String token) {
        redisTemplate.opsForSet().add(BLACKLIST_KEY, token);
    }

    public boolean isTokenBlacklisted(String token) {
        return redisTemplate.opsForSet().isMember(BLACKLIST_KEY, token);
    }

}

