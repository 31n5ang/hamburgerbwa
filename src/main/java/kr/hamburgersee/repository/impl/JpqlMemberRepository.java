package kr.hamburgersee.repository.impl;

import jakarta.persistence.EntityManager;
import kr.hamburgersee.domain.Member;
import kr.hamburgersee.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpqlMemberRepository implements MemberRepository {
    private final EntityManager em;

    @Override
    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return Optional.ofNullable(em.find(Member.class, memberId));
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        List<Member> members = em.createQuery("SELECT m FROM Member m WHERE m.email=:email", Member.class)
                .setParameter("email", email)
                .getResultList();

        return members.size() == 1 ? Optional.of(members.get(0)) : Optional.empty();
    }

    @Override
    public Optional<Member> findByNickname(String nickname) {
        List<Member> members = em.createQuery("SELECT m FROM Member m WHERE m.nickname=:nickname", Member.class)
                .setParameter("nickname", nickname)
                .getResultList();

        return members.size() == 1 ? Optional.of(members.get(0)) : Optional.empty();
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("SELECT m FROM Member m", Member.class).getResultList();
    }
}
