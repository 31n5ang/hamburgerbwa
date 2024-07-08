package kr.hamburgersee.repository;

import kr.hamburgersee.domain.Member;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Long save(Member member);

    Optional<Member> findById(Long memberId);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    List<Member> findAll();
}
