package kr.hamburgersee.domain.review;

import kr.hamburgersee.domain.file.image.ReviewImageService;
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

    @Transactional
    public Long writeProcess(ReviewCreateForm form, Long memberId) {
        // 리뷰 저장과 동시에, 리뷰에 저장되지 못한 이미지들을 모두 제거하는 최적화를 진행합니다.
        Long reviewId = saveReview(form, memberId);
        reviewImageService.attachReview(reviewId, form.getAllImageUrls());
        reviewImageService.deleteUnusedReviewImages(form.getContent(), form.getAllImageUrls());
        return reviewId;
    }

    @Transactional(readOnly = true)
    public ReviewDto getReviewDto(Long reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

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
                review.getAt(),
                review.getGood(),
                "",
                null
        );

        return reviewDto;
    }

    private Long saveReview(ReviewCreateForm form, Long memberId) {
        Review review = Review.create(
                form.getRegion(),
                form.getShopName(),
                form.getTitle(),
                form.getContent(),
                //TODO put member
                null
        );

        review.attachReviewTags(form.getTagTypes());

        Review savedReview = reviewRepository.save(review);

        log.info("savedReview class = {}", savedReview.getClass());

        return savedReview.getId();
    }
}
