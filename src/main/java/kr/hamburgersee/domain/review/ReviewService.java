package kr.hamburgersee.domain.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Transactional
    public Long write(ReviewCreateForm form, Long memberId) {
        Review review = Review.create(
                form.getRegion(),
                form.getShopName(),
                form.getTitle(),
                form.getContent(),
                null
        );
        review.attachReviewTags(form.getTagTypes());

        Review savedReview = reviewRepository.save(review);

        return savedReview.getId();
    }

    @Transactional(readOnly = true)
    public ReviewDto getReviewDto(Long reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (optionalReview.isEmpty()) {
            throw new ReviewNotFoundException("해당 id의 리뷰를 찾을 수 없습니다.");
        } else {
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
    }
}
