package kr.hamburgersee.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // TODO : member가 현재는 null이므로 left join 사용
    @Query("select c from Comment c left join fetch c.member m join fetch c.review r where c.review.id = :reviewId")
    List<Comment> findAllByReviewId(@Param("reviewId") Long reviewId);
}
