package kr.hamburgersee.domain.file.image;

import kr.hamburgersee.domain.aws.StorageService;
import kr.hamburgersee.domain.file.FileUtils;
import kr.hamburgersee.domain.review.Review;
import kr.hamburgersee.domain.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewImageService {
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewRepository reviewRepository;
    private final StorageService storageService;

    @Transactional
    public void attachReview(Long reviewId, List<String> uploadedUrls) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        if (optionalReview.isEmpty()) {
            // 만약 부착할 리뷰가 없다면 이미지를 모두 삭제합니다.
            deleteUselessReviewImage("", uploadedUrls);
        } else {
            Review review = optionalReview.get();
            reviewImageRepository.updateReviewByUrls(review, uploadedUrls);
        }
    }

    @Transactional
    public void deleteUselessReviewImage(String content, List<String> allImageUrls) {
        // 리뷰에 저장되지 못한 image들의 url을 삭제합니다.
        List<String> uselessReviewImagesUrls = getUselessReviewImages(content, allImageUrls);
        uselessReviewImagesUrls.forEach((url) -> {
            storageService.delete(url);
        });
        reviewImageRepository.deleteAllByUrls(uselessReviewImagesUrls);
    }

    @Transactional
    public String uploadReviewImages(MultipartFile file) {
        try {
            String uploadedUrl = uploadImage(file);
            saveReviewImage(uploadedUrl, file.getOriginalFilename());
            return uploadedUrl;
        } catch (IOException e) {
            throw new ReviewImageUploadFailException("이미지 업로드를 실패했습니다.", e);
        }
    }

    private List<String> getUselessReviewImages(String content, List<String> uploadedUrls) {
        return uploadedUrls.stream()
                .filter((url) -> !content.contains(url))
                .toList();
    }

    private String uploadImage(MultipartFile imageFile) throws IOException {
        return uploadImage(imageFile.getBytes(), imageFile.getOriginalFilename());
    }

    private String uploadImage(byte[] file, String originalFilename) {
        String ext = FileUtils.parseExt(originalFilename);
        String uploadFilename = FileUtils.generateUploadFilename(ext);
        return storageService.upload(file, uploadFilename);
    }

    private Long saveReviewImage(String uploadedUrl, String originalFilename) {
        ReviewImage reviewImage = ReviewImage.createWithoutReview(uploadedUrl, originalFilename);
        ReviewImage savedReviewImage = reviewImageRepository.save(reviewImage);
        return savedReviewImage.getId();
    }
}
