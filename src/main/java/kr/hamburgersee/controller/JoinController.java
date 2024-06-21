package kr.hamburgersee.controller;

import kr.hamburgersee.member.MemberDto;
import kr.hamburgersee.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class JoinController {
    private final MemberService memberService;
    @GetMapping("/join")
    public String joinForm() {
        return "join";
    }

    @PostMapping("/join")
    public String join(MemberDto memberDto) {
        memberService.join(memberDto);
        return "redirect:/";
    }
}
