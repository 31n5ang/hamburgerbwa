package kr.hamburgersee.domain.file.image;

import jakarta.persistence.*;
import kr.hamburgersee.domain.review.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ThumbnailImage extends Image {
    @Id @GeneratedValue
    @Column(name = "thumbnail_image_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "thumbnailImage")
    private Review review;

    // 생성 메소드
    private ThumbnailImage(String uploadedUrl, String originalFilename) {
        super(uploadedUrl, originalFilename);
    }

    // 팩토리 메소드
    public static ThumbnailImage create(String uploadedUrl, String originalFilename) {
        return new ThumbnailImage(uploadedUrl, originalFilename);
    }

    public static ThumbnailImage createWithoutReview(String uploadedUrl, String originalFilename) {
        return new ThumbnailImage(uploadedUrl, originalFilename);
    }

    public void attachReview(Review review) {
        this.review = review;
    }
}
