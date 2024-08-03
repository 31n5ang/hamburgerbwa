package kr.hamburgersee.domain.file.image;

import kr.hamburgersee.domain.aws.storage.StorageService;
import kr.hamburgersee.domain.aws.storage.StorageUploadFileType;
import kr.hamburgersee.domain.common.By;
import kr.hamburgersee.domain.review.Review;
import kr.hamburgersee.domain.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static kr.hamburgersee.domain.aws.storage.StorageUploadFileType.*;

@Service
@RequiredArgsConstructor
public class ThumbnailImageService {
    private final ThumbnailImageRepository thumbnailImageRepository;
    private final ThumbnailImageSelection thumbnailImageSelection;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewRepository reviewRepository;
    private final StorageService storageService;

    @Transactional
    public Long uploadThumbnailImage(Long reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (optionalReview.isEmpty()) {
            throw new ThumbnailImageException("썸네일을 생성할 리뷰가 존재하지 않습니다.");
        }
        Review review = optionalReview.get();

        ReviewImage thumbnailReviewImage = null;
        try {
            // 리뷰 이미지 중, 썸네일을 선정합니다.
            thumbnailReviewImage = thumbnailImageSelection.select(review);
        } catch (ThumbnailImageSelectionException e) {
            // 썸네일 선택 실패 로직
            return null;
        }

        String originalFilename = thumbnailReviewImage.getFilename();
        String originalUrl = thumbnailReviewImage.getUrl();
        String uploadedUrl = null;

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Thumbnails.of(originalUrl)
                    .size(200, 200)
                    .toOutputStream(outputStream);

            byte[] thumbnailBytes = outputStream.toByteArray();

            uploadedUrl = storageService.upload(thumbnailBytes, originalFilename, THUMBNAIL_REVIEW_IMAGE);
        } catch (IOException e) {
            throw new ThumbnailImageException("썸네일 생성에 실패했습니다.", e);
        }

        ThumbnailImage thumbnailImage = ThumbnailImage.create(uploadedUrl, originalFilename, review);

        ThumbnailImage savedThumbnailImage = thumbnailImageRepository.save(thumbnailImage);

        return savedThumbnailImage.getId();
    }
}
