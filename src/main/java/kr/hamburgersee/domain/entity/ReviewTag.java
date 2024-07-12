package kr.hamburgersee.domain.entity;

import jakarta.persistence.*;
import kr.hamburgersee.domain.ReviewTagType;
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

    private ReviewTagType tagType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Review review;

    // 생성자
    private ReviewTag(ReviewTagType tagType) {
        this.tagType = tagType;
    }

    // 팩토리 메소드
    public static ReviewTag createNewReviewTag(ReviewTagType reviewTagType) {
        return new ReviewTag(reviewTagType);
    }
}
