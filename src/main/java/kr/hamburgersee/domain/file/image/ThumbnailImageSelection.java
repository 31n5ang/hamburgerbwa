package kr.hamburgersee.domain.file.image;

import kr.hamburgersee.domain.review.Review;

import java.util.Optional;

public interface ThumbnailImageSelection {
    ReviewImage select(Review review);
}
