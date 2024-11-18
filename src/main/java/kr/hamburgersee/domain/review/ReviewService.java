package kr.hamburgersee.domain.review;

import kr.hamburgersee.domain.common.DateFormatter;
import kr.hamburgersee.domain.file.image.*;
import kr.hamburgersee.domain.likes.LikeOnReviewService;
import kr.hamburgersee.domain.member.Member;
import kr.hamburgersee.domain.member.MemberNotFoundException;
import kr.hamburgersee.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static kr.hamburgersee.domain.review.ContentUtils.omitContent;
import static kr.hamburgersee.domain.review.ContentUtils.purifyHtmlTagContent;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ThumbnailImageService thumbnailImageService;
    private final ReviewImageService reviewImageService;
    private final LikeOnReviewService likeOnReviewService;

    @Value("${review.content.omitted-length}")
    private int CONTENT_OMITTED_LENGTH;

    @Value("${review.content.ellipsis}")
    private String CONTENT_ELLIPSIS;

    @Value("${review.content.replace.img-tag}")
    private String CONTENT_REPLACE_IMG_TAG;

    @Transactional
    public Long writeProcess(ReviewCreateForm form, Long memberId) {
        // 리뷰 저장과 동시에, 리뷰에 저장되지 못한 이미지들을 모두 제거하는 최적화를 진행합니다.
        Long reviewId = saveReview(form, memberId);
        reviewImageService.attachReview(reviewId, form.getAllImageUrls());
        reviewImageService.deleteUnusedReviewImages(form.getContent(), form.getAllImageUrls());

        // 썸네일을 저장합니다.
        try {
            thumbnailImageService.uploadThumbnailImage(reviewId);
        } catch (ThumbnailImageException e) {
            // 썸네일 저장 실패 시 로직
            log.error("썸네일 저장 실패", e);
        }

        return reviewId;
    }

    @Transactional(readOnly = true)
    public ReviewDto getReviewDto(Long reviewId) {
        Optional<Review> optionalReview = reviewRepository.findByIdWithMember(reviewId);

        if (optionalReview.isEmpty()) {
            throw new ReviewNotFoundException("해당 id의 리뷰를 찾을 수 없습니다.");
        }

        Review review = optionalReview.get();

        List<ReviewTagType> reviewTagTypes = review.getTags().stream()
                .map((tag) -> tag.getTagType())
                .toList();

        ReviewDto reviewDto = new ReviewDto(
                review.getId(),
                review.getTitle(),
                review.getRegionValue(),
                review.getShopName(),
                review.getContent(),
                reviewTagTypes,
                review.getCreatedDate(),
                review.getLastModifiedDate(),
                review.getGood(),
                review.getMember().getNickname(),
                review.getMember().getId(),
                (review.getMember().getProfileImage() == null ? null : review.getMember().getProfileImage().getUrl())
        );

        return reviewDto;
    }

    private Long saveReview(ReviewCreateForm form, Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isEmpty()) {
            throw new MemberNotFoundException("해당 회원이 존재하지 않습니다.");
        }
        Review review = Review.create(
                form.getRegion(),
                form.getShopName(),
                form.getTitle(),
                form.getContent(),
                optionalMember.get()
        );

        review.attachReviewTags(form.getTagTypes());

        Review savedReview = reviewRepository.save(review);

        return savedReview.getId();
    }

    @Transactional(readOnly = true)
    public Slice<ReviewCardDto> getReviewCardDtos(Pageable pageable) {
        Slice<Review> reviews = reviewRepository.findSliceWithRelated(pageable);
        return convertReviewsToReviewCardDtos(reviews);
    }

    @Transactional(readOnly = true)
    public Slice<ReviewCardDto> getReviewCardDtosByReviewSearchDto(Pageable pageable, ReviewSearchDto reviewSearchDto) {
        String keyword = reviewSearchDto.getKeyword();

        String titleKeyword = reviewSearchDto.isKeywordInTitle() ? keyword : "";
        String contentKeyword = reviewSearchDto.isKeywordInTitle() ? keyword : "";
        String shopNameKeyword = reviewSearchDto.isKeywordInTitle() ? keyword : "";

        Slice<Review> reviews = reviewRepository.findSliceWithRelatedByKeyword(pageable, titleKeyword, contentKeyword, shopNameKeyword);
        return convertReviewsToReviewCardDtos(reviews);
    }

    private Slice<ReviewCardDto> convertReviewsToReviewCardDtos(Slice<Review> reviews) {
        return reviews.map(review -> new ReviewCardDto(
                review.getId(),
                review.getShopName(),
                review.getTitle(),
                omitContent(purifyHtmlTagContent(review.getContent(), CONTENT_REPLACE_IMG_TAG),
                        CONTENT_ELLIPSIS, CONTENT_OMITTED_LENGTH),
                review.getMember().getNickname(),
                review.getThumbnailImage() == null ? null : review.getThumbnailImage().getUrl(),
                review.getCreatedDate(),
                review.getTags().stream()
                        .map(reviewTag -> reviewTag.getTagType())
                        .toList(),
                review.getRegionValue().displayName,
                DateFormatter.getAgoFormatted(review.getCreatedDate(), LocalDateTime.now()),
                likeOnReviewService.getLikedCount(review.getId())
        ));
    }
}
