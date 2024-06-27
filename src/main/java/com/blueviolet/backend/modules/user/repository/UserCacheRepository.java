package com.blueviolet.backend.modules.user.repository;

import com.blueviolet.backend.common.util.JsonUtil;
import com.blueviolet.backend.modules.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {

    private final static Duration USER_CACHE_TTL = Duration.ofDays(3);

    private final RedisTemplate<String, User> userRedisTemplate;
    private final JsonUtil jsonUtil;

    public void save(User user) {
        String key = getKey(user.getUserId());
        log.info("Redis에 User 정보 저장 (key: {}, value: {})", key, jsonUtil.toJson(user));
        userRedisTemplate.opsForValue().set(key, user, USER_CACHE_TTL);
    }

    public Optional<User> getUserByUserId(Long userId) {
        User user = userRedisTemplate.opsForValue().get(getKey(userId));
        log.info("Redis에서 User 정보 가져오기 ({})", jsonUtil.toJson(user));
        return Optional.ofNullable(user);
    }

    private String getKey(Long userId) {
        return "users:" + userId;
    }
}
