package kr.hamburgersee.web.controller;

import jakarta.validation.Valid;
import kr.hamburgersee.web.WebConst;
import kr.hamburgersee.dto.MemberSessionInfo;
import kr.hamburgersee.dto.board.BoardDto;
import kr.hamburgersee.dto.board.BoardWriteForm;
import kr.hamburgersee.dto.comment.CommentDto;
import kr.hamburgersee.dto.comment.CommentWriteForm;
import kr.hamburgersee.web.service.WebBoardService;
import kr.hamburgersee.web.service.WebCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final WebBoardService webBoardService;
    private final WebCommentService webCommentService;

    @GetMapping("/list")
    public String boards(Model model) {
        List<BoardDto> boardDtos = webBoardService.findBoardDtos();
        model.addAttribute("boardDtos", boardDtos);
        return "board/contents";
    }

    @GetMapping("/list/{id}")
    public String board(
            @SessionAttribute(name = WebConst.SESSION_LOGIN_MEMBER, required = false) MemberSessionInfo memberSessionInfo,
            @PathVariable("id") Long boardId, Model model
    ) {
        Optional<BoardDto> optionalBoardDto = webBoardService.findBoardDtoById(boardId);
        if (optionalBoardDto.isEmpty()) {
            return "redirect:/board/list";
        } else {
            List<CommentDto> commentDtos = webCommentService.findCommentDtosByBoardId(boardId);

            model.addAttribute("memberId", memberSessionInfo.getMemberId());
            model.addAttribute("boardId", boardId);

            model.addAttribute("boardDto", optionalBoardDto.get());
            model.addAttribute("commentDtos", commentDtos);

            return "board/content";
        }
    }

    @GetMapping("/write")
    public String boardWriteForm(Model model) {
        model.addAttribute("form", new BoardWriteForm());
        return "board/boardForm";
    }

    @PostMapping("/write")
    public String boardWrite(
            @SessionAttribute(name = WebConst.SESSION_LOGIN_MEMBER, required = false) MemberSessionInfo memberSessionInfo,
            @Valid @ModelAttribute BoardWriteForm boardWriteForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            // 폼 양식을 잘못 입력하였다면 돌아가기
            return "board/boardForm";
        }

        BoardDto boardDto = BoardDto.builder()
                .content(boardWriteForm.getContent())
                .title(boardWriteForm.getTitle())
                .memberId(memberSessionInfo.getMemberId())
                .build();

        Optional<Long> optionalWrittenBoardId = webBoardService.write(boardDto);

        if (optionalWrittenBoardId.isEmpty()) {
            // 정상적으로 저장되어지지 않았다면
            return "board/boardForm";
        } else {
            // 정상적으로 저장되었다면
            return "redirect:/board/list";
        }
    }
}
