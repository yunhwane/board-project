package com.example.board.global.jwt.service.redis;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate redisTemplate;

    // key - value
    public void setValue(String refreshToken, String username){

        ValueOperations<String,String> values = redisTemplate.opsForValue();
        //values.set(token, username);
        values.set(username,refreshToken, Duration.ofMinutes(3));
    }

    // key로 value 가져오는 함수
    public String getValues(String refreshToken){
        ValueOperations<String,String> values = redisTemplate.opsForValue();
        return values.get(refreshToken);
    }

    // 키-벨류 삭제
    public void delValues(String token) {
        redisTemplate.delete(token.substring(7));
    }

}
