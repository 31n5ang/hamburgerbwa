package kr.hamburgersee.config.redis;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@RedisHash(value = "redisTestEntity", timeToLive = 300)
public class RedisTestEntity {
    @Id
    public Long id;
    public String name;
}
