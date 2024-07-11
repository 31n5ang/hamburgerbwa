package kr.hamburgersee.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.hamburgersee.web.WebConst;
import kr.hamburgersee.web.dto.MemberSessionInfo;
import kr.hamburgersee.web.dto.member.MemberLoginForm;
import kr.hamburgersee.web.service.WebMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private static final String LOGIN_FORM_PATH = "/loginForm";
    private final WebMemberService webMemberService;
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("form", new MemberLoginForm());
        return LOGIN_FORM_PATH;
    }

    @PostMapping("/login")
    public String login(
            HttpServletRequest request,
            @RequestParam(defaultValue = "/", required = false) String redirect,
            @Valid @ModelAttribute("form") MemberLoginForm form,
            BindingResult bindingResult
    ) {
        Optional<Long> optionalMemberId = webMemberService.loginByMemberLoginForm(form);

        if (bindingResult.hasErrors() || optionalMemberId.isEmpty()) {
            return LOGIN_FORM_PATH;
        } else {
            HttpSession session = request.getSession(true);
            session.setAttribute(
                    WebConst.SESSION_LOGIN_MEMBER,
                    new MemberSessionInfo(optionalMemberId.get(), form.getEmail())
            );
            return "redirect:" + redirect;
        }
    }
}
