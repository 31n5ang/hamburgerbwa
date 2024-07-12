package kr.hamburgersee.domain.entity.image;

import jakarta.persistence.*;
import kr.hamburgersee.domain.entity.Review;
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
}
