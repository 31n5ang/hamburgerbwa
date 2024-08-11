package kr.hamburgersee.domain.likes;

import kr.hamburgersee.domain.member.Member;
import kr.hamburgersee.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    @Query("select rl from ReviewLike rl where rl.review = :review and rl.member = :member")
    Optional<Like> findByReviewAndMember(@Param("review") Review review, @Param("member") Member member);

    @Query("select count(rl) from ReviewLike rl where rl.review = :review and rl.status = :status")
    Long countByReview(@Param("review") Review review, @Param("status") LikeStatus status);

    // true=1, false=0 반환
    @Query(value = "select " +
            "exists(select 1 from likes as l where (l.review_id = :reviewId and l.member_id = :memberId) " +
            "and (l.status = 'LIKED'))",
            nativeQuery = true)
    Long existsLikedByReviewIdAndMemberId(@Param("reviewId") Long reviewId, @Param("memberId") Long memberId);

}
