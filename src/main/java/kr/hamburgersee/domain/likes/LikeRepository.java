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
    Optional<Like> findReviewLikeByReviewAndMember(@Param("review") Review review, @Param("member") Member member);
}
