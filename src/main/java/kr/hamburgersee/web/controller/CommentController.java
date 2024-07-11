package kr.hamburgersee.web.controller;

import kr.hamburgersee.dto.comment.CommentDto;
import kr.hamburgersee.dto.comment.CommentWriteForm;
import kr.hamburgersee.web.service.WebCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final WebCommentService webCommentService;

    @GetMapping("/comment")
    public String commentForm(Model model) {
        model.addAttribute("form", new CommentWriteForm());
        return "/board/commentForm";
    }

    @PostMapping("/comment")
    public String comment(
            @RequestParam Long memberId,
            @RequestParam Long boardId,
            @ModelAttribute CommentWriteForm commentWriteForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "board/commentForm";
        }

        CommentDto commentDto = new CommentDto(
                memberId, boardId, commentWriteForm.getContent(), commentWriteForm.getMemberNickname()
        );

        Optional<Long> optionalWrittenCommentId = webCommentService.write(commentDto);
        if (optionalWrittenCommentId.isEmpty()) {
            // 정상적으로 저장되어지지 않았다면
            return "board/commentForm";
        } else {
            // 정상적으로 저장되었다면
            return "redirect:/board/list/" + boardId;
        }
    }
}
