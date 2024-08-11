package kr.hamburgersee.domain.likes;

import jakarta.persistence.*;
import kr.hamburgersee.domain.member.Member;
import kr.hamburgersee.domain.review.Review;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewLike extends Like {
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "like")
    private Review review;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 생성자
    private ReviewLike(Review review, Member member) {
        this.review = review;
        this.member = member;
    }

    // 팩토리 메소드
    public static Like create(Review review, Member member, LikeStatus status) {
        Like like = new ReviewLike(review, member);
        like.updateState(status);
        return like;
    }
}
