package kr.hamburgersee.member.repo;

import kr.hamburgersee.exception.MemberNotFoundException;
import kr.hamburgersee.member.Member;
import kr.hamburgersee.member.MemberRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void beforeEach() {
        member = Member
                .builder()
                .email("31n5ang@gmail.com")
                .region("Daejeon")
                .role(MemberRole.ROLE_MEMBER)
                .username("31n5ang")
                .password("")
                .nickname("테스트")
                .build();
    }

    @Test
    @Transactional
    void find_correct() {
        memberRepository.save(member);
        Optional<Member> maybeMemberById = memberRepository.findById(member.getId());
        Optional<Member> maybeMemberByUsername = memberRepository.findByUsername("31n5ang");
        if (maybeMemberById.isEmpty() || maybeMemberByUsername.isEmpty()) {
            throw new MemberNotFoundException();
        } else {
            assertThat(maybeMemberById.get()).isSameAs(member);
            assertThat(maybeMemberByUsername.get()).isSameAs(member);
        }
    }

    @Test
    @Transactional
    void find_incorrect() {
        Optional<Member> maybeMemberById = memberRepository.findById(-1L);
        Optional<Member> maybeMemberByUsername = memberRepository.findByUsername("");
        assertThat(maybeMemberById.isPresent()).isFalse();
        assertThat(maybeMemberByUsername.isPresent()).isFalse();
    }

    @Test
    @Transactional
    void save() {
        Long savedId = memberRepository.save(member);
        Optional<Member> findMemberById = memberRepository.findById(savedId);
        assertThat(findMemberById.isPresent()).isTrue();
        assertThat(findMemberById.get().getId()).isEqualTo(savedId);
    }
}
