package kr.hamburgersee.domain.session;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class SessionModelControllerAdvice {
    private final SessionService sessionService;

    @ModelAttribute("memberSessionInfo")
    public MemberSessionInfo sessionAttributes() {
        return (MemberSessionInfo) sessionService.find(SessionAttrType.MEMBER_SESSION_INFO);
    }
}
