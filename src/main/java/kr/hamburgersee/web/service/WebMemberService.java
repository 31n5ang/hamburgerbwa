package kr.hamburgersee.web.service;

import kr.hamburgersee.domain.Member;
import kr.hamburgersee.repository.MemberRepository;
import kr.hamburgersee.dto.member.MemberJoinForm;
import kr.hamburgersee.dto.member.MemberLoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebMemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    @Transactional
    public boolean joinByMemberJoinForm(MemberJoinForm form) {
        return join(getMemberByJoinForm(form)).isPresent();
    }

    @Transactional(readOnly = true)
    public Optional<Long> loginByMemberLoginForm(MemberLoginForm form) {
        return validateAndLogin(form.getEmail(), form.getPassword());
    }

    private Optional<Long> join(Member member) {
        if (validateJoin(member.getEmail(), member.getNickname())) {
            Member savedMember = memberRepository.save(member);
            return Optional.of(savedMember.getId());
        } else {
            return Optional.empty();
        }
    }

    private Optional<Long> validateAndLogin(String email, String rawPassword) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            if (encoder.matches(rawPassword, member.getPassword())) {
                return Optional.of(member.getId());
            }
        }
        return Optional.empty();
    }

    private boolean validateJoin(String email, String nickname) {
        return (validateUniqueEmail(email) && validateUniqueNickname(nickname));
    }

    private boolean validateUniqueEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.isEmpty();
    }

    private boolean validateUniqueNickname(String nickname) {
        Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
        return optionalMember.isEmpty();
    }

    // Form은 최종적으로 View에 전달하기 위함이므로, 엔티티(domain)에 변환 로직을 위임하지 않는 것이 좋아보입니다.
    private Member getMemberByJoinForm(MemberJoinForm form) {
        return new Member(
                form.getEmail(),
                encoder.encode(form.getPassword()),
                form.getNickname());
    }
}
