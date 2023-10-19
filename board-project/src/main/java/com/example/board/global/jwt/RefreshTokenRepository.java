package com.example.board.global.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository{

    private final RedisTemplate redisTemplate;

    public void save(final RefreshToken refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(refreshToken.getRefreshToken(), refreshToken.getAccessToken());
        redisTemplate.expire(refreshToken.getRefreshToken(), 2, TimeUnit.DAYS);
    }


    public Optional<RefreshToken> findByAccessToken(final String refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        String accessToken = valueOperations.get(refreshToken);

        if (Objects.isNull(accessToken)) {
            return Optional.empty();
        }
        return Optional.of(new RefreshToken(accessToken,refreshToken));
    }

    // accessToken을 활용하여 refreshToken을 찾는 메서드
    public Optional<String> findRefreshTokenByAccessToken(final String accessToken) {

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        return Optional.ofNullable(valueOperations.get(accessToken));
    }
    //key - value 삭제
    public void delValue(String refreshToken){
        redisTemplate.delete(refreshToken.substring(7));
    }
}
