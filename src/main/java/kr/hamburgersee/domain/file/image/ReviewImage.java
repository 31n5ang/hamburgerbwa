package kr.hamburgersee.domain.file.image;

import jakarta.persistence.*;
import kr.hamburgersee.domain.review.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImage extends Image {
    @Id @GeneratedValue
    @Column(name = "review_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    // 생성자
    private ReviewImage(String uploadedUrl, String originalFilename, Review review) {
        super(uploadedUrl, originalFilename);
        this.review = review;
    }

    // 팩토리 메소드
    public static ReviewImage create(String uploadedUrl, String originalFilename, Review review) {
        return new ReviewImage(uploadedUrl, originalFilename, review);
    }

    public static ReviewImage createWithoutReview(String uploadedUrl, String originalFilename) {
        return new ReviewImage(uploadedUrl, originalFilename, null);
    }

    public void attachReview(Review review) {
        this.review = review;
    }
}
