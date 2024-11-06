package kr.hamburgersee.domain.likes;

import jakarta.persistence.*;
import kr.hamburgersee.domain.member.Member;
import kr.hamburgersee.domain.review.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeOnReview extends Like {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 생성자
    private LikeOnReview(Review review, Member member) {
        this.review = review;
        this.member = member;
    }

    // 팩토리 메소드
    public static Like create(Review review, Member member, LikeStatus status) {
        Like like = new LikeOnReview(review, member);
        like.updateState(status);
        return like;
    }
}
