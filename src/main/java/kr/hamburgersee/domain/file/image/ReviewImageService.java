package kr.hamburgersee.domain.file.image;

import kr.hamburgersee.domain.aws.storage.StorageService;
import kr.hamburgersee.domain.file.FileUtils;
import kr.hamburgersee.domain.review.Review;
import kr.hamburgersee.domain.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static kr.hamburgersee.domain.aws.storage.StorageUploadFileType.*;

@Slf4j
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
            deleteUnusedReviewImages("", uploadedUrls);
        } else {
            // 리뷰를 부착(참조)합니다.
            Review review = optionalReview.get();

            // 이미지의 개수가 많을 수 있으므로 벌크 연산을 사용합니다.
            reviewImageRepository.updateReviewByUrls(review, uploadedUrls);
        }
    }

    @Transactional
    public void deleteUnusedReviewImages(String content, List<String> allImageUrls) {
        // 리뷰의 본문과 비교하여 저장되지 못한 이미지들의 url을 삭제합니다.
        List<String> uselessReviewImagesUrls = getUselessReviewImages(content, allImageUrls);
        uselessReviewImagesUrls.forEach((url) -> storageService.delete(url, REVIEW_IMAGE));
        reviewImageRepository.deleteAllByUrls(uselessReviewImagesUrls);
    }

    @Transactional
    public void deleteOrphanedReviewImages() {
        // TODO : 이미지 생성으로부터 7일이 지나도 review_id가 부착이 안되어있다면 삭제 고려
        // Review id가 없는 리뷰 이미지들을 모든 저장소로부터 제거합니다.
        List<String> uploadedUrls = reviewImageRepository.findAllWithoutReviewId();
        uploadedUrls.forEach((uploadedUrl) -> storageService.delete(uploadedUrl, REVIEW_IMAGE));
        reviewImageRepository.deleteAllWithoutReviewId();
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
        return storageService.upload(file, uploadFilename, REVIEW_IMAGE);
    }

    private Long saveReviewImage(String uploadedUrl, String originalFilename) {
        ReviewImage reviewImage = ReviewImage.createWithoutReview(uploadedUrl, originalFilename);
        ReviewImage savedReviewImage = reviewImageRepository.save(reviewImage);
        return savedReviewImage.getId();
    }
}
