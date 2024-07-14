package kr.hamburgersee.domain.review;

import jakarta.persistence.*;
import kr.hamburgersee.domain.common.At;
import kr.hamburgersee.domain.common.RegionType;
import kr.hamburgersee.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {
    @Id @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private RegionType regionValue;

    @Column(nullable = false)
    private String shopName;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewTag> tags = new ArrayList<>();

    @Embedded
    private At at;

    private int good;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 생성자
    private Review(RegionType regionValue, String shopName, String title, String content,
                   ReviewStatus status,
                   int good, Member member) {
        this.regionValue = regionValue;
        this.shopName = shopName;
        this.title = title;
        this.content = content;
        this.status = status;
        this.good = good;
        this.member = member;
    }

    // 팩토리 메소드
    public static Review createNewReview(RegionType regionValue, String shopName, String title, String content,
                                         Member member) {
        return new Review(
                regionValue,
                shopName,
                title,
                content,
                ReviewStatus.SHOW,
                0,
                member
        );
    }

    // 편의 메소드
    public void attachReviewTags(List<ReviewTagType> reviewTagTypes) {
        List<ReviewTag> tags = new ArrayList<>();
        for (ReviewTagType reviewTagType : reviewTagTypes) {
            tags.add(ReviewTag.createNewReviewTag(this, reviewTagType));
        }
        this.tags = tags;
    }
}