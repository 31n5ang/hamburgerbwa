package kr.hamburgersee.domain.likes;

import kr.hamburgersee.domain.common.RegionType;
import kr.hamburgersee.domain.member.GenderType;
import kr.hamburgersee.domain.member.Member;
import kr.hamburgersee.domain.review.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LikeOnReviewRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private LikeOnReviewRepository likeOnReviewRepository;

    private Member member;
    private Review review;
    private Like like;

    @BeforeEach
    void setup() {
        member = Member.create(
                "test@test.com",
                "encPassword",
                "testNick",
                RegionType.ETC,
                GenderType.NONE,
                "testestestest."
        );

        review = Review.create(
                RegionType.SEOUL,
                "my_burger",
                "intro_good_burger",
                "<b>hello</b> here is good burger shop",
                member
        );

        like = LikeOnReview.create(
                review,
                member,
                LikeStatus.UNLIKED
        );

        em.persist(member);
        em.persist(review);
        em.persist(like);
    }

    @Test
    @DisplayName("조회_by_리뷰_and_회원")
    void findByReviewAndMember() {
        Optional<Like> findLike = likeOnReviewRepository.findByReviewAndMember(review, member);
        assertThat(findLike).isPresent();

        LikeOnReview likeOnReview = (LikeOnReview) findLike.get();

        assertThat(likeOnReview.getMember()).isEqualTo(member);
        assertThat(likeOnReview.getReview()).isEqualTo(review);
    }

    @Test
    @DisplayName("좋아요_개수_by_리뷰")
    void countByReview() {
        int iterLikeCount = 100;
        int iterUnlikeCount = 50;

        emPersistLike(iterLikeCount, LikeStatus.LIKED);
        emPersistLike(iterUnlikeCount, LikeStatus.UNLIKED);

        Long likeCount = likeOnReviewRepository.countByReview(review, LikeStatus.LIKED);
        Long unlikeCount = likeOnReviewRepository.countByReview(review, LikeStatus.UNLIKED);

        assertThat(likeCount).isEqualTo(iterLikeCount);
        assertThat(unlikeCount).isEqualTo(iterUnlikeCount + 1);
    }

    @Test
    @DisplayName("좋아요_존재_by_리뷰id_and_회원id")
    void existsLikedByReviewIdAndMemberId() {
        // 좋아요로 토글
        like.updateState(LikeStatus.LIKED);

        Boolean invalidMemberIdResult = likeOnReviewRepository.existsLikedByReviewIdAndMemberId(review.getId(), -1L);
        Boolean invalidReviewIdResult = likeOnReviewRepository.existsLikedByReviewIdAndMemberId(-1L, member.getId());
        Boolean validResult = likeOnReviewRepository.existsLikedByReviewIdAndMemberId(review.getId(), member.getId());

        assertThat(invalidMemberIdResult).isEqualTo(false);
        assertThat(invalidReviewIdResult).isEqualTo(false);
        assertThat(validResult).isEqualTo(true);
    }

    private void emPersistLike(int iterLikeCount, LikeStatus likeStatus) {
        for (int i = 0; i < iterLikeCount; i++) {
            em.persist(LikeOnReview.create(review, member, likeStatus));
        }
    }
}
