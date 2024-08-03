package kr.hamburgersee.domain.review;

import kr.hamburgersee.domain.file.image.ReviewImageService;
import kr.hamburgersee.domain.file.image.ThumbnailImageException;
import kr.hamburgersee.domain.file.image.ThumbnailImageService;
import kr.hamburgersee.domain.member.Member;
import kr.hamburgersee.domain.member.MemberNotFoundException;
import kr.hamburgersee.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewImageService reviewImageService;
    private final ThumbnailImageService thumbnailImageService;
    private final MemberRepository memberRepository;

    @Transactional
    public Long writeProcess(ReviewCreateForm form, Long memberId) {
        // 리뷰 저장과 동시에, 리뷰에 저장되지 못한 이미지들을 모두 제거하는 최적화를 진행합니다.
        Long reviewId = saveReview(form, memberId);
        reviewImageService.attachReview(reviewId, form.getAllImageUrls());
        reviewImageService.deleteUnusedReviewImages(form.getContent(), form.getAllImageUrls());

        // 썸네일을 저장합니다.
        try {
            thumbnailImageService.uploadThumbnailImage(reviewId);
        } catch (ThumbnailImageException e) {
            // 썸네일 저장 실패 시 로직
        }

        return reviewId;
    }

    @Transactional(readOnly = true)
    public ReviewDto getReviewDto(Long reviewId) {
        Optional<Review> optionalReview = reviewRepository.findByIdWithMember(reviewId);

        if (optionalReview.isEmpty()) {
            throw new ReviewNotFoundException("해당 id의 리뷰를 찾을 수 없습니다.");
        }

        Review review = optionalReview.get();

        List<ReviewTagType> reviewTagTypes = review.getTags().stream()
                .map((tag) -> tag.getTagType())
                .toList();

        ReviewDto reviewDto = new ReviewDto(
                review.getTitle(),
                review.getRegionValue(),
                review.getShopName(),
                review.getContent(),
                reviewTagTypes,
                null,
                review.getGood(),
                review.getMember().getNickname(),
                review.getMember().getId()
        );

        return reviewDto;
    }

    private Long saveReview(ReviewCreateForm form, Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isEmpty()) {
            throw new MemberNotFoundException("해당 회원이 존재하지 않습니다.");
        }
        Review review = Review.create(
                form.getRegion(),
                form.getShopName(),
                form.getTitle(),
                form.getContent(),
                optionalMember.get()
        );

        review.attachReviewTags(form.getTagTypes());

        Review savedReview = reviewRepository.save(review);

        return savedReview.getId();
    }
}
