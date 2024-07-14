package kr.hamburgersee.domain.member;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {
    private final String JOIN_FORM_PATH = "layout/fragments/member/join";

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
        return "/";
    }
}
