package kr.hamburgersee.domain.comment;

import jakarta.persistence.*;
import kr.hamburgersee.domain.common.At;
import kr.hamburgersee.domain.member.Member;
import kr.hamburgersee.domain.review.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
public class Comment {
    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @Lob
    @Column(nullable = false)
    private String content;

    @Embedded
    private At at;

    @Enumerated(EnumType.STRING)
    private CommentStatus status;

    private int good;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 생성자
    private Comment(String content, Review review, Member member, CommentStatus status, int good) {
        this.content = content;
        this.status = status;
        this.review = review;
        this.member = member;
        this.good = good;
    }

    // 팩토리 메소드
    public static Comment create(String content, Review review, Member member) {
        return new Comment(
                content,
                review,
                member,
                CommentStatus.SHOW,
                0
        );
    }
}
