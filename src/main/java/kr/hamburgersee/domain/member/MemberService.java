package kr.hamburgersee.domain.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final PasswordEncoder encoder;
    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;

    @Transactional
    public void join(MemberJoinForm form) {
        Member createdMember = Member.create(
                form.getEmail(),
                encoder.encode(form.getRawPassword()),
                form.getNickname(),
                form.getRegion(),
                form.getGender(),
                form.getBio());

        join(createdMember);
    }

    private void join(Member member) {
        memberValidator.join(member);
        memberRepository.save(member);
    }
}
