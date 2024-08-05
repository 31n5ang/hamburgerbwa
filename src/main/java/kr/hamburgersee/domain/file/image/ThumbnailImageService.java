package kr.hamburgersee.domain.file.image;

import kr.hamburgersee.domain.review.Review;
import kr.hamburgersee.domain.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

import static kr.hamburgersee.domain.aws.storage.StorageUploadFileType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThumbnailImageService {
    private final ThumbnailImageRepository thumbnailImageRepository;
    private final ThumbnailImageSelection thumbnailImageSelection;
    private final ReviewRepository reviewRepository;
    private final ImageManager imageManager;

    @Value("${review.thumbnail.max-width}")
    private int THUMBNAIL_MAX_WIDTH;

    @Value("${review.thumbnail.max-height}")
    private int THUMBNAIL_MAX_HEIGHT;

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
            log.info("썸네일 선택 실패");
            // 썸네일 선택 실패 로직
            return null;
        }

        String originalFilename = thumbnailReviewImage.getFilename();
        String originalUrl = thumbnailReviewImage.getUrl();
        String uploadedUrl = null;

        try (InputStream originalUrlInputStream = new URL(originalUrl).openStream()){
            ByteArrayOutputStream thumbnailImageOutputStream = new ByteArrayOutputStream();

            Thumbnails.of(originalUrlInputStream)
                    .size(THUMBNAIL_MAX_WIDTH, THUMBNAIL_MAX_HEIGHT)
                    .toOutputStream(thumbnailImageOutputStream);

            byte[] thumbnailBytes = thumbnailImageOutputStream.toByteArray();

            uploadedUrl = imageManager.uploadImage(thumbnailBytes, originalFilename, THUMBNAIL_REVIEW_IMAGE);
        } catch (IOException e) {
            throw new ThumbnailImageException("썸네일 생성에 실패했습니다.", e);
        }

        ThumbnailImage thumbnailImage = ThumbnailImage.create(uploadedUrl, originalFilename);
        ThumbnailImage savedThumbnailImage = thumbnailImageRepository.save(thumbnailImage);
        review.attachThumbnailImage(savedThumbnailImage);

        return savedThumbnailImage.getId();
    }
}
