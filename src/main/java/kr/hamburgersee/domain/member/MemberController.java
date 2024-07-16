package kr.hamburgersee.domain.member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static kr.hamburgersee.domain.member.MemberConst.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final String JOIN_FORM_PATH = "join";

    @GetMapping("/join")
    public String getJoin(Model model) {
        model.addAttribute("form", MemberJoinForm.createNewDefaultForm());
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
