package kr.hamburgersee.domain.review;

import jakarta.validation.Valid;
import kr.hamburgersee.domain.annotation.MemberOnly;
import kr.hamburgersee.domain.annotation.RedirectStrategy;
import kr.hamburgersee.domain.comment.CommentCreateForm;
import kr.hamburgersee.domain.comment.CommentDto;
import kr.hamburgersee.domain.comment.CommentService;
import kr.hamburgersee.domain.likes.LikeOnReviewService;
import kr.hamburgersee.domain.session.MemberSessionInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final CommentService commentService;
    private final LikeOnReviewService likeOnReviewService;

    @Value("${review.paging.sort.by}")
    private final String SORT_BY = "createdDate";

    @Value("${review.paging.size}")
    private final int PAGE_SIZE = 9;

    private static final String REVIEW_CREATE_FORM = "review-create";
    private static final String REVIEW_PATH = "review";
    private static final String REVIEWS_PATH = "reviews";
    private static final String REVIEW_URI = "review";

    @MemberOnly
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("form", new ReviewCreateForm());
        return REVIEW_CREATE_FORM;
    }

    @MemberOnly
    @PostMapping("/create")
    public String create(
            @Valid @ModelAttribute("form") ReviewCreateForm form,
            BindingResult bindingResult,
            MemberSessionInfo memberSessionInfo
    ) {
        if (bindingResult.hasErrors()) {
            return REVIEW_CREATE_FORM;
        }

        Long memberId = memberSessionInfo.getMemberId();
        Long reviewId = reviewService.writeProcess(form, memberId);

        return "redirect:/" + REVIEW_URI + "/" + reviewId;
    }

    @GetMapping("/{id}")
    public String review(
            @PathVariable("id") Long reviewId,
            Model model,
            MemberSessionInfo memberSessionInfo
    ) {
        ReviewDto reviewDto = reviewService.getReviewDto(reviewId);
        List<CommentDto> commentDtos = commentService.getCommentDtos(reviewId);
        Long likedCount = likeOnReviewService.getLikedCount(reviewId);

        model.addAttribute("review", reviewDto);
        model.addAttribute("form", new CommentCreateForm());
        model.addAttribute("comments", commentDtos);
        model.addAttribute("likedCount", likedCount);

        if (memberSessionInfo != null) {
            // 로그인된 사용자라면 좋아요 여부를 전달합니다.
            boolean isLiked = likeOnReviewService.isLiked(reviewId, memberSessionInfo.getMemberId());
            model.addAttribute("isLiked", isLiked);
        }
        return REVIEW_PATH;
    }

    @MemberOnly
    @PostMapping("/{id}")
    public String comment(
            @PathVariable("id") Long reviewId,
            Model model,
            @Valid @ModelAttribute("form") CommentCreateForm form,
            BindingResult bindingResult,
            MemberSessionInfo memberSessionInfo
    ) {
        if (bindingResult.hasErrors()) {
            // Bean Validation 통과 못할 시, 기존 리뷰와 댓글 폼을 복구시킵니다.
            ReviewDto reviewDto = reviewService.getReviewDto(reviewId);
            List<CommentDto> commentDtos = commentService.getCommentDtos(reviewId);
            model.addAttribute("review", reviewDto);
            model.addAttribute("comments", commentDtos);
            return REVIEW_PATH;
        }

        Long memberId = memberSessionInfo.getMemberId();

        // TODO ReviewException, MemberException 예외 처리하기
        commentService.write(reviewId, memberId, form);

        return "redirect:/" + REVIEW_URI + "/" + reviewId;
    }

    @GetMapping("/list")
    public String reviews(
            Model model,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @PageableDefault(size = PAGE_SIZE, sort = SORT_BY, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        ReviewSearchDto reviewSearchDto = ReviewSearchDto
                .builder()
                .keyword(keyword)
                .isKeywordInTitle(true)
                .isKeywordInContent(true)
                .isKeywordInShopName(true)
                .build();

        Slice<ReviewCardDto> reviewCardDtos = reviewService.getReviewCardDtosByReviewSearchDto(pageable, reviewSearchDto);

        addPagingAttributes(model, reviewCardDtos);
        model.addAttribute("keyword", keyword);
//        model.addAttribute("uri", )

        return REVIEWS_PATH;
    }

    @MemberOnly(redirectStrategy = RedirectStrategy.REFERER)
    @PostMapping("/like")
    public String like(
            @RequestParam("reviewId") Long reviewId,
            MemberSessionInfo memberSessionInfo
    ) {
        likeOnReviewService.toggleReviewLike(reviewId, memberSessionInfo.getMemberId());
        return "redirect:/" + REVIEW_URI + "/" + reviewId;
    }

    private static void addPagingAttributes(Model model, Slice<ReviewCardDto> reviewCardDtos) {
        if (reviewCardDtos.hasNext()) {
            model.addAttribute("nextPageNumber", reviewCardDtos.nextPageable().getPageNumber());
        }
        if (reviewCardDtos.hasPrevious()) {
            model.addAttribute("prevPageNumber", reviewCardDtos.previousPageable().getPageNumber());
        }
        model.addAttribute("hasNext", reviewCardDtos.hasNext());
        model.addAttribute("hasPrevious", reviewCardDtos.hasPrevious());
        model.addAttribute("reviews", reviewCardDtos.getContent());
    }
}

