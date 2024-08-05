package kr.hamburgersee.domain.review.api;

import kr.hamburgersee.domain.annotation.MemberOnly;
import kr.hamburgersee.domain.file.image.ReviewImageService;
import kr.hamburgersee.domain.review.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewApiController {
    private final ReviewService reviewService;
    private final ReviewImageService reviewImageService;

    /**
     * 사용자가 리뷰 작성 중, 리뷰 이미지를 삽입할 때 요청됩니다.
     * @param file 삽입한 이미지입니다.
     * @return 이미지 스토리지에 저장 후, 실제 저장된 파일의 물리적 경로를 반환합니다.
     */
    @MemberOnly
    @PostMapping("/image")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        return reviewImageService.uploadReviewImages(file);
    }
}
