package kr.hamburgersee.domain.member.my;

import kr.hamburgersee.domain.annotation.MemberOnly;
import kr.hamburgersee.domain.session.MemberSessionInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberMyController {
    private final MemberMyService memberMyService;

    private static final String MY_PATH = "my";

    @MemberOnly
    @GetMapping("/my")
    public String getMy(
            Model model,
            MemberSessionInfo memberSessionInfo
    ) {
        MemberMyDto memberMyDto = memberMyService.getMemberMyDto(memberSessionInfo.getMemberId());
        model.addAttribute("info", memberMyDto);
        return MY_PATH;
    }
}
