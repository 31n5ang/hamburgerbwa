package kr.hamburgersee.domain.review;

import jakarta.validation.Valid;
import kr.hamburgersee.domain.annotation.MemberOnly;
import kr.hamburgersee.domain.comment.CommentCreateForm;
import kr.hamburgersee.domain.comment.CommentDto;
import kr.hamburgersee.domain.comment.CommentService;
import kr.hamburgersee.domain.file.image.ReviewImageService;
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

    @Value("${review.paging.sort.by}")
    private final String SORT_BY = "createdDate";

    @Value("${review.paging.size}")
    private final int PAGE_SIZE = 9;

    private static final String REVIEW_CREATE_FORM = "review-create";
    private static final String REVIEW = "review";

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

        return "redirect:/review/" + reviewId;
    }

    @GetMapping("/{id}")
    public String review(
            @PathVariable("id") Long reviewId,
            Model model
    ) {
        ReviewDto reviewDto = reviewService.getReviewDto(reviewId);
        List<CommentDto> commentDtos = commentService.getCommentDtos(reviewId);
        model.addAttribute("review", reviewDto);
        model.addAttribute("form", new CommentCreateForm());
        model.addAttribute("comments", commentDtos);
        return REVIEW;
    }

    @MemberOnly
    @PostMapping("/{id}")
    public String comment(
            @PathVariable("id") Long reviewId,
            Model model,
            @ModelAttribute("form") CommentCreateForm form,
            BindingResult bindingResult,
            MemberSessionInfo memberSessionInfo
    ) {
        if (bindingResult.hasErrors()) {
            // Bean Validation 통과 못할 시, 기존 리뷰와 댓글 폼을 복구시킵니다.
            ReviewDto reviewDto = reviewService.getReviewDto(reviewId);
            List<CommentDto> commentDtos = commentService.getCommentDtos(reviewId);
            model.addAttribute("review", reviewDto);
            model.addAttribute("comments", commentDtos);
            return REVIEW;
        }

        Long memberId = memberSessionInfo.getMemberId();

        // TODO ReviewException, MemberException 예외 처리하기
        commentService.write(reviewId, memberId, form);

        return "redirect:/review/" + reviewId;
    }

    @GetMapping("/list")
    public String reviews(
            Model model,
            @PageableDefault(size = 9, sort = SORT_BY, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Slice<ReviewCardDto> reviewCardDtos = reviewService.getReviewCardDtos(pageable);
        if (reviewCardDtos.hasNext()) {
            model.addAttribute("nextPageNumber", reviewCardDtos.nextPageable().getPageNumber());
        }
        if (reviewCardDtos.hasPrevious()) {
            model.addAttribute("prevPageNumber", reviewCardDtos.previousPageable().getPageNumber());
        }
        model.addAttribute("hasNext", reviewCardDtos.hasNext());
        model.addAttribute("hasPrevious", reviewCardDtos.hasPrevious());
        model.addAttribute("reviews", reviewCardDtos.getContent());

        return "reviews";
    }
}

