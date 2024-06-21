package kr.hamburgersee.member.service;

import kr.hamburgersee.member.Member;
import kr.hamburgersee.member.MemberDto;
import kr.hamburgersee.member.MemberRole;
import kr.hamburgersee.member.repo.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final PasswordEncoder encoder;
    private final MemberRepository memberRepository;
    @Override
    @Transactional
    public Long join(MemberDto memberDto) {
        return memberRepository.save(convertDtoToMember(memberDto));
    }

    @Override
    public MemberDto findById(Long id) {
        return null;
    }

    @Override
    public Member convertDtoToMember(MemberDto memberDto) {
        return Member
                .builder()
                .nickname(memberDto.getNickname())
                .email(memberDto.getEmail())
                .region(memberDto.getRegion())
                .password(encoder.encode(memberDto.getRawPassword()))
                .username(memberDto.getUsername())
                .role(MemberRole.ROLE_MEMBER)
                .build();
    }

    @Override
    public MemberDto convertMemberToDto(Member member) {
        return null;
    }
}
