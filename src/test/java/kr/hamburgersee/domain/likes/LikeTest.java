package kr.hamburgersee.domain.likes;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class LikeTest {
    @Autowired
    EntityManager em;

    private final static int LIKE_COUNT = 10;
    private final static int UNLIKE_COUNT = 10;

    @BeforeEach
    public void beforeEach() {
        for (int i = 0; i < LIKE_COUNT; i++) {
            Like like = ReviewLike.create(null, null, LikeStatus.LIKED);
            like.updateState(LikeStatus.LIKED);
            em.persist(like);
        }

        for (int i = 0; i < UNLIKE_COUNT; i++) {
            Like like = ReviewLike.create(null, null, LikeStatus.UNLIKED);
            like.updateState(LikeStatus.UNLIKED);
            em.persist(like);
        }

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("좋아요_개수_체크")
    public void likes_count() {
        String query = "SELECT count(l) FROM Like l WHERE l.status = :status";
        Long result = em.createQuery(query, Long.class)
                .setParameter("status", LikeStatus.LIKED)
                .getSingleResult();

        assertThat(result).isEqualTo(LIKE_COUNT);
    }

    @Test
    @DisplayName("좋아요_싫어요_차이_체크")
    public void likes_and_unlikes_count() {
        String query = "SELECT count(l) FROM Like l WHERE l.status = :status";
        Long likes = em.createQuery(query, Long.class)
                        .setParameter("status", LikeStatus.LIKED)
                        .getSingleResult();

        Long unlikes = em.createQuery(query, Long.class)
                        .setParameter("status", LikeStatus.UNLIKED)
                        .getSingleResult();

        assertThat(likes - unlikes).isEqualTo(LIKE_COUNT - UNLIKE_COUNT);
    }
}
