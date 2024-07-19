package kr.hamburgersee.domain.file.image;

import kr.hamburgersee.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("delete from ReviewImage ri where ri.url in :urls")
    int deleteAllByUrls(@Param("urls") List<String> urls);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("delete from ReviewImage ri where ri.review is null")
    int deleteAllWithoutReviewId();

    @Query("select ri.url from ReviewImage ri where ri.review is null")
    List<String> findAllWithoutReviewId();

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update ReviewImage ri set ri.review = :review where ri.url in :urls")
    int updateReviewByUrls(@Param("review") Review review, @Param("urls") List<String> urls);
}
