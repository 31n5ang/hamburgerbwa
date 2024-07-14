package kr.hamburgersee.redis;

import groovy.util.logging.Slf4j;
import kr.hamburgersee.config.redis.RedisTestEntity;
import kr.hamburgersee.config.redis.RedisTestRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class redisSimpleTest {
    @Autowired
    RedisTestRepository redisTestRepository;

    @Test
    @DisplayName("레디스 저장")
    void redisSave() {
        RedisTestEntity entity = RedisTestEntity.builder().id(10L).name("만두쿵야요").build();

        redisTestRepository.save(entity);

        Optional<RedisTestEntity> findEntity = redisTestRepository.findById(10L);

        RedisTestEntity entity1 = findEntity.get();

        System.out.println("entity1 = " + entity1.name);
    }
}
