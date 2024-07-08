package kr.hamburgersee.web.service;

import kr.hamburgersee.domain.Member;
import kr.hamburgersee.repository.MemberRepository;
import kr.hamburgersee.web.dto.member.MemberJoinForm;
import kr.hamburgersee.web.dto.member.MemberLoginForm;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class WebMemberServiceTest {
    public static final String EMAIL = "test@test.com";
    public static final String PASSWORD = "password";
    public static final String NICKNAME = "테스트";

    @Autowired
    private WebMemberService webMemberService;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void validateLoginByMemberLoginForm() {
        MemberJoinForm joinForm = new MemberJoinForm();
        joinForm.setEmail(EMAIL);
        joinForm.setPassword(PASSWORD);
        joinForm.setNickname(NICKNAME);

        MemberLoginForm loginForm = new MemberLoginForm();
        loginForm.setEmail(EMAIL);
        loginForm.setPassword(PASSWORD);

        webMemberService.joinByMemberJoinForm(joinForm);
        Assertions.assertThat(webMemberService.validateLoginByMemberLoginForm(loginForm)).isTrue();
    }
}
