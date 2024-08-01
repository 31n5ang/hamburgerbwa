package kr.hamburgersee.domain.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberValidator {
    private final PasswordEncoder encoder;
    private final MemberRepository memberRepository;

    public void join(Member member) {
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new MemberDuplicateEmailException("이메일이 이미 존재합니다.");
        }

        if (memberRepository.existsByNickname(member.getNickname())) {
            throw new MemberDuplicateNicknameException("닉네임이 이미 존재합니다.");
        }
    }

    public MemberAuthenticatedInfo login(String email, String rawPassword) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isEmpty()) {
            throw new MemberNotFoundException("가입된 이메일이 아닙니다.");
        }

        Member member = optionalMember.get();
        if (!validatePassword(rawPassword, member.getEncPassword())) {
            throw new MemberIncorrectPasswordException("비밀번호가 틀립니다.");
        }

        return new MemberAuthenticatedInfo(member.getId(), member.getNickname());
    }

    private boolean validatePassword(String rawPassword, String encPassword) {
        return encoder.matches(rawPassword, encPassword);
    }
}
