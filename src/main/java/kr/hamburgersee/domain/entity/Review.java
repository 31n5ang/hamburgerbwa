package kr.hamburgersee.domain.entity;

import jakarta.persistence.*;
import kr.hamburgersee.domain.At;
import kr.hamburgersee.domain.RegionType;
import kr.hamburgersee.domain.ReviewStatus;
import kr.hamburgersee.domain.ReviewTagType;
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
                   ReviewStatus status, List<ReviewTag> tags,
                   int good, Member member) {
        this.regionValue = regionValue;
        this.shopName = shopName;
        this.title = title;
        this.content = content;
        this.status = status;
        this.tags = tags;
        this.good = good;
        this.member = member;
    }

    // 팩토리 메소드
    public static Review createNewReview(RegionType regionValue, String shopName, String title, String content,
                                         List<ReviewTag> tags, Member member) {
        return new Review(
                regionValue,
                shopName,
                title,
                content,
                ReviewStatus.SHOW,
                tags,
                0,
                member
        );
    }

    // 편의 메소드
    public void attachReviewTags(List<ReviewTagType> reviewTagTypes) {
        for (ReviewTagType reviewTagType : reviewTagTypes) {
            this.getTags().add(ReviewTag.createNewReviewTag(reviewTagType));
        }
    }
}
