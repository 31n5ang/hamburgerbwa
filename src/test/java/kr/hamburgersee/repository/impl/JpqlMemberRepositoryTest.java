package kr.hamburgersee.repository.impl;

import kr.hamburgersee.domain.Member;
import kr.hamburgersee.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JpqlMemberRepositoryTest {
    public static final String EMAIL = "test@test.com";
    public static final String PASSWORD = "password";
    public static final String NICKNAME = "테스트";

    @Autowired
    MemberRepository memberRepository;

    @Test
    void save() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByEmail() {
        //given
        Member member = new Member(EMAIL, PASSWORD, NICKNAME);
        memberRepository.save(member);

        //when
        Optional<Member> optionalMember1 = memberRepository.findByEmail(EMAIL);
        Optional<Member> optionalMember2 = memberRepository.findByEmail("test2@test.com");

        //then
        Assertions.assertThat(optionalMember1).isPresent();
        Assertions.assertThat(optionalMember2).isEmpty();
    }

    @Test
    void findByNickname() {
        //given
        Member member = new Member(EMAIL, PASSWORD, NICKNAME);
        memberRepository.save(member);

        //when
        Optional<Member> optionalMember1 = memberRepository.findByNickname(NICKNAME);
        Optional<Member> optionalMember2 = memberRepository.findByNickname("test2");

        //then
        Assertions.assertThat(optionalMember1).isPresent();
        Assertions.assertThat(optionalMember2).isEmpty();
    }

    @Test
    void findAll() {
    }
}
