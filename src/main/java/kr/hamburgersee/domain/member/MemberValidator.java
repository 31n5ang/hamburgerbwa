package kr.hamburgersee.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberValidator {
    private final MemberRepository memberRepository;

    public void join(Member member) {
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new MemberDuplicateEmailException("이메일이 이미 존재합니다.");
        }

        if (memberRepository.existsByNickname(member.getNickname())) {
            throw new MemberDuplicateNicknameException("닉네임이 이미 존재합니다.");
        }
    }
}
