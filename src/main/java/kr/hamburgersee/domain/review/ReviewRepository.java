package kr.hamburgersee.domain.review;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query(value =
            "select r from Review r " +
            "join fetch r.member m " +
            "left join fetch m.profileImage pi " +
            "where r.id = :reviewId"
    )
    Optional<Review> findByIdWithMember(@Param("reviewId") Long reviewId);

    @Query(
            value = "select r " +
                    "from Review r " +
                    "left join fetch r.thumbnailImage ti " +
                    "join fetch r.member m ",
            countQuery = "select r from Review r"
    )
    Slice<Review> findSliceWithRelated(Pageable pageable);

    @Query(
            value =
                    "select r " +
                    "from Review r " +
                    "left join fetch r.thumbnailImage ti " +
                    "join fetch r.member m " +
                    "where r.title like " +
                    "concat('%', :titleKeyword, '%') " +
                    "or r.content like " +
                    "concat('%', :contentKeyword, '%') " +
                    "or r.shopName like " +
                    "concat('%', :shopNameKeyword, '%') ",
            countQuery =
                    "select r from Review r " +
                    "where r.title like " +
                    "concat('%', :titleKeyword, '%') " +
                    "or r.content like " +
                    "concat('%', :contentKeyword, '%') " +
                    "or r.shopName like " +
                    "concat('%', :shopNameKeyword, '%') "
    )
    Slice<Review> findSliceWithRelatedByKeyword(Pageable pageable, String titleKeyword, String contentKeyword,
                                                String shopNameKeyword);
}
