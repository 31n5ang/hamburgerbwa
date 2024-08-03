package kr.hamburgersee.domain.file.image;

import kr.hamburgersee.domain.review.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FirstThumbnailImageSelection implements ThumbnailImageSelection {
    private final ReviewImageRepository reviewImageRepository;

    @Override
    public ReviewImage select(Review review) {
        Optional<ReviewImage> optionalReviewImage = reviewImageRepository.findFirstByReview(review);
        if (optionalReviewImage.isEmpty()) {
            throw new ThumbnailImageSelectionException("썸네일 선택에 실패했습니다.");
        }
        return optionalReviewImage.get();
    }
}
