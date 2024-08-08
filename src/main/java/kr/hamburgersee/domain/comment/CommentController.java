package kr.hamburgersee.domain.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    private static final String REVIEW_URL = "/review/%d";

    @PostMapping("/delete")
    public String deleteComment(
            @ModelAttribute("commentDelete") CommentDeleteDto commentDeleteDto
    ) {
        commentService.updateStatus(commentDeleteDto.getCommentId(), CommentStatus.DELETED);
        return String.format("redirect:" + REVIEW_URL, commentDeleteDto.getReviewId());
    }
}
