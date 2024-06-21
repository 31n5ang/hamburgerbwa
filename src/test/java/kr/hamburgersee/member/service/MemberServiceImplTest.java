package kr.hamburgersee.member.service;

import kr.hamburgersee.member.Member;
import kr.hamburgersee.member.MemberDto;
import kr.hamburgersee.member.MemberRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceImplTest {
    @Autowired private MemberService memberService;

    private MemberDto memberDto;

    @BeforeEach
    void beforeEach() {
        memberDto = MemberDto
                .builder()
                .email("31n5ang@gmail.com")
                .region("Daejeon")
                .username("31n5ang")
                .nickname("테스트")
                .build();
    }

    @Test
    void join() {
        Long joinedId = memberService.join(memberDto);
    }
}
