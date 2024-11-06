package kr.hamburgersee.domain.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.hamburgersee.domain.common.RegionType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {
    private static final String RAW_PASSWORD = "rawPassword";
    private static final String NICKNAME_1 = "nickname1";
    private static final String NICKNAME_2 = "nickname2";
    public static final String EMAIL_1 = "email1";
    public static final String EMAIL_2 = "email2";

    @PersistenceContext EntityManager em;
    @Autowired MemberService memberService;

    @Test
    @Transactional
    @DisplayName("회원가입_이메일_중복_예외")
    void duplicateEmail() {
        MemberJoinForm form1 = MemberJoinForm.createDefaultEmpty();
        form1.setEmail(EMAIL_1);
        form1.setRawPassword(RAW_PASSWORD);
        form1.setNickname(NICKNAME_1);

        MemberJoinForm form2 = MemberJoinForm.createDefaultEmpty();
        form2.setEmail(EMAIL_1);
        form2.setRawPassword(RAW_PASSWORD);
        form2.setNickname(NICKNAME_2);

        memberService.join(form1);

        Assertions.assertThatThrownBy(() -> {
            memberService.join(form2);
        }).isInstanceOf(MemberDuplicateEmailException.class);
    }

    @Test
    @Transactional
    @DisplayName("회원가입_닉네임_중복_예외")
    void duplicateNickname() {
        MemberJoinForm form1 = MemberJoinForm.createDefaultEmpty();
        form1.setEmail(EMAIL_1);
        form1.setRawPassword(RAW_PASSWORD);
        form1.setNickname(NICKNAME_1);

        MemberJoinForm form2 = MemberJoinForm.createDefaultEmpty();
        form2.setEmail(EMAIL_2);
        form2.setRawPassword(RAW_PASSWORD);
        form2.setNickname(NICKNAME_1);

        memberService.join(form1);

        Assertions.assertThatThrownBy(() -> {
            memberService.join(form2);
        }).isInstanceOf(MemberDuplicateNicknameException.class);
    }
}
