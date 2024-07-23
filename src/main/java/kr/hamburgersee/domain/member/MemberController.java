package kr.hamburgersee.domain.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

import static kr.hamburgersee.domain.member.MemberConst.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private static final String JOIN_FORM_PATH = "join";

    @ResponseBody
    @GetMapping("/login")
    public String test(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        session.setAttribute("hello", "world");
        session.setAttribute("hellooo", "worldddd");
        return "";
    }

    @ResponseBody
    @GetMapping("/session")
    public ResponseEntity<Map<String, Object>> session(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        HttpSession session = request.getSession(false);
        result.put("hello", session.getAttribute("hello"));
        result.put("helloo", session.getAttribute("hellooo"));
        return ResponseEntity.ok(result);
    }

    @ResponseBody
    @GetMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        HttpSession session = request.getSession(false);
        session.invalidate();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/join")
    public String getJoin(Model model) {
        model.addAttribute("form", MemberJoinForm.createDefaultEmpty());
        return JOIN_FORM_PATH;
    }

    @PostMapping("/join")
    public String postJoin(
            @Valid @ModelAttribute("form") MemberJoinForm form,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return JOIN_FORM_PATH;
        }

        try {
            memberService.join(form);
        } catch (MemberDuplicateEmailException e) {
            // 이메일이 중복된다면
            bindingResult.rejectValue(EMAIL_FIELD, DUPLICATE_ERROR_CODE);
            return JOIN_FORM_PATH;
        } catch (MemberDuplicateNicknameException e) {
            // 닉네임이 중복된다면
            bindingResult.rejectValue(NICKNAME_FIELD, DUPLICATE_ERROR_CODE);
            return JOIN_FORM_PATH;
        }

        // 성공하면 리다이렉트
        return "redirect:/";
    }
}
