package kr.hamburgersee.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.hamburgersee.dto.member.MemberJoinForm;
import kr.hamburgersee.web.service.WebMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class JoinController {
    private final WebMemberService webMemberService;
    private static final String JOIN_FORM_PATH = "/joinForm";
    @GetMapping("/join")
    public String joinForm(Model model, HttpServletRequest request) {
        log.info("requestURI={}", request.getRequestURI());
        model.addAttribute("form", new MemberJoinForm());
        return JOIN_FORM_PATH;
    }

    @PostMapping("/join")
    public String join(
            @Valid @ModelAttribute("form") MemberJoinForm form,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors() || !webMemberService.joinByMemberJoinForm(form)) {
            return JOIN_FORM_PATH;
        }
        return "redirect:/";
    }
}
