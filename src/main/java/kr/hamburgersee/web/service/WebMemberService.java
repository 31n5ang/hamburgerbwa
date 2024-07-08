package kr.hamburgersee.web.service;

import jakarta.servlet.http.HttpSession;
import kr.hamburgersee.domain.Member;
import kr.hamburgersee.repository.MemberRepository;
import kr.hamburgersee.web.WebConst;
import kr.hamburgersee.web.dto.member.MemberJoinForm;
import kr.hamburgersee.web.dto.member.MemberLoginForm;
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
        return join(new Member(form.getEmail(), encoder.encode(form.getPassword()), form.getNickname())).isPresent();
    }

    @Transactional(readOnly = true)
    public boolean validateLoginByMemberLoginForm(MemberLoginForm form) {
        return validateLogin(form.getEmail(), form.getPassword());
    }

    private Optional<Long> join(Member member) {
        if (validateJoin(member.getEmail(), member.getNickname())) {
            return Optional.of(memberRepository.save(member));
        } else {
            return Optional.empty();
        }
    }

    private boolean validateLogin(String email, String rawPassword) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            return encoder.matches(rawPassword, member.getPassword());
        } else {
            return false;
        }
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
}
