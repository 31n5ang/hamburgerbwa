package kr.hamburgersee.member.repo;

import kr.hamburgersee.member.Member;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    List<Member> findAll();
    Optional<Member> findById(Long id);
    Optional<Member> findByUsername(String username);
    Long save(Member member);
}
