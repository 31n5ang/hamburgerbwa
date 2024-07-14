package kr.hamburgersee.config.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisTestRepository extends CrudRepository<RedisTestEntity, Long> {
}
