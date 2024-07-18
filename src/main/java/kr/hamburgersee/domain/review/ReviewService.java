package kr.hamburgersee.domain.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
