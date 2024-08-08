package kr.hamburgersee.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c " +
            "join fetch c.member m " +
            "left join fetch m.profileImage pi " +
            "join fetch c.review r " +
            "where c.review.id = :reviewId"
    )
    List<Comment> findAllByReviewIdWithMemberAndReview(@Param("reviewId") Long reviewId);
}
