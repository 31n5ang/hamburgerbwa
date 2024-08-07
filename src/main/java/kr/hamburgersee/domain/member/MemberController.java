package kr.hamburgersee.domain.member;

import jakarta.validation.Valid;
import kr.hamburgersee.domain.annotation.LoginRedirectUri;
import kr.hamburgersee.domain.annotation.MemberOnly;
import kr.hamburgersee.domain.session.MemberSessionInfo;
import kr.hamburgersee.domain.session.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import static kr.hamburgersee.domain.member.MemberFormConstants.*;
import static kr.hamburgersee.domain.session.SessionAttrType.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final SessionService sessionService;

    private static final String JOIN_FORM_PATH = "join";
    private static final String LOGIN_FORM_PATH = "login";

    @GetMapping("/join")
    public String getJoin(Model model) {
        model.addAttribute("form", MemberJoinForm.createDefaultEmpty());
        return JOIN_FORM_PATH;
    }

    @PostMapping("/join")
    public String postJoin(
            MultipartFile profileImageBase64,
            @Valid @ModelAttribute("form") MemberJoinForm form,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return JOIN_FORM_PATH;
        }

        Long joinedMemberId = -1L;
        try {
            joinedMemberId = memberService.join(form);
        } catch (MemberDuplicateEmailException e) {
            // 이메일이 중복된다면
            bindingResult.rejectValue(EMAIL_FIELD, DUPLICATE_ERROR_CODE);
            return JOIN_FORM_PATH;
        } catch (MemberDuplicateNicknameException e) {
            // 닉네임이 중복된다면
            bindingResult.rejectValue(NICKNAME_FIELD, DUPLICATE_ERROR_CODE);
            return JOIN_FORM_PATH;
        }

        // 프로필 이미지 저장 로직
        if (!profileImageBase64.isEmpty()) {
            try {
                memberService.updateProfileImage(joinedMemberId, profileImageBase64);
            } catch (MemberException e) {
                // 저장에 실패한다면
                bindingResult.rejectValue(PROFILE_IMAGE_FIELD, FAILED_UPLOAD_ERROR_CODE);
                return JOIN_FORM_PATH;
            }
        }

        // 성공하면 리다이렉트
        return "redirect:/";
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("form", new MemberLoginForm());
        return LOGIN_FORM_PATH;
    }

    @PostMapping("/login")
    public String postLogin(
            @LoginRedirectUri String redirectUri,
            @Valid @ModelAttribute("form") MemberLoginForm form,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult.getAllErrors());
            return LOGIN_FORM_PATH;
        }

        try {
            MemberAuthenticatedInfo memberAuthenticatedInfo = memberService.authenticate(form);

            if (sessionService.find(MEMBER_SESSION_INFO) != null) {
                // 이미 로그인 상태라면 현재 로그인된 회원 세션을 제거합니다.
                sessionService.remove(MEMBER_SESSION_INFO);
            }
            sessionService.create(MEMBER_SESSION_INFO, new MemberSessionInfo(
                    memberAuthenticatedInfo.getMemberId(),
                    memberAuthenticatedInfo.getNickname()
            ));
        } catch (MemberException e) {
            bindingResult.rejectValue(EMAIL_FIELD, INCORRECT_LOGIN_ERROR_CODE);
            return LOGIN_FORM_PATH;
        }

        // 리다이렉트 시키기 전, 로그인에 성공했으므로 리다이렉트 세션 값을 삭제시킵니다.
        sessionService.remove(REQUEST_REDIRECT_URI);

        return "redirect:" + redirectUri;
    }

    @MemberOnly
    @GetMapping("/logout")
    public String getLogout() {
        sessionService.clear();
        return "redirect:/";
    }
}
