package kr.hamburgersee.web.controller;

import kr.hamburgersee.web.WebConst;
import kr.hamburgersee.web.dto.MemberSessionInfo;
import kr.hamburgersee.web.dto.board.BoardDto;
import kr.hamburgersee.web.dto.board.BoardWriteForm;
import kr.hamburgersee.web.service.WebBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final WebBoardService webBoardService;

    @GetMapping("/list")
    public String boards(Model model) {
        List<BoardDto> boardDtos = webBoardService.findBoardDtos();
        model.addAttribute("boardDtos", boardDtos);
        return "board/contents";
    }

    @GetMapping("/list/{id}")
    public String board(@PathVariable("id") Long boardId, Model model) {
        Optional<BoardDto> optionalBoardDto = webBoardService.findBoardDtoById(boardId);
        if (optionalBoardDto.isEmpty()) {
            return "redirect:/board/list";
        } else {
            model.addAttribute("boardDto", optionalBoardDto.get());
            return "board/content";
        }
    }

    @GetMapping("/write")
    public String boardWriteForm(Model model) {
        model.addAttribute("form", new BoardWriteForm());
        return "board/writeForm";
    }

    @PostMapping("/write")
    public String boardWrite(
            @SessionAttribute(name = WebConst.SESSION_LOGIN_MEMBER, required = false) MemberSessionInfo memberSessionInfo,
            @ModelAttribute BoardWriteForm boardWriteForm
    ) {
        webBoardService.writeBoardByBoardWriteForm(memberSessionInfo.getMemberId(), boardWriteForm);
        return "redirect:/board/list";
    }
}
