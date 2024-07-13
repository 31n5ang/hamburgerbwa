package kr.hamburgersee.domain.entity;

import jakarta.persistence.EntityManager;
import kr.hamburgersee.domain.common.RegionType;
import kr.hamburgersee.domain.review.ReviewTagType;
import kr.hamburgersee.domain.review.Review;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@SpringBootTest
class ReviewTest {
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    @Autowired
    EntityManager em;
    @Test
    @Transactional
    void saveReviewWithTags() {
        Review review = Review.createNewReview(
                RegionType.CHEONAN,
                "맛집이름",
                "맛있습니다. 추천",
                "맛나요~",
                null
        );

        review.attachReviewTags(Arrays.asList(ReviewTagType.BEEF, ReviewTagType.PORK));

        em.persist(review);
        em.flush();
        em.clear();

        Review findReview = em.find(Review.class, 1L);

        Assertions.assertThat(findReview.getTags().size()).isEqualTo(2);
        Assertions.assertThat(findReview.getId()).isEqualTo(review.getId());
    }
}
