package kr.hamburgersee.domain.review;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewTag {
    @Id @GeneratedValue
    @Column(name = "review_tag_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReviewTagType tagType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    // 생성자
    private ReviewTag(Review review, ReviewTagType tagType) {
        this.review = review;
        this.tagType = tagType;
    }

    // 팩토리 메소드
    public static ReviewTag create(Review review, ReviewTagType reviewTagType) {
        return new ReviewTag(review, reviewTagType);
    }
}
