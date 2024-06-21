package kr.hamburgersee.member.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.hamburgersee.member.Member;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MySqlMemberRepository implements MemberRepository{
    @PersistenceContext EntityManager em;
    @Override
    public List<Member> findAll() {
        String query = "SELECT m FROM Member m";
        return em.createQuery(query, Member.class).getResultList();
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByUsername(String username) {
        String query = "SELECT m FROM Member m WHERE username=:username";
        List<Member> result = em
                .createQuery(query, Member.class)
                .setParameter("username", username)
                .setFirstResult(0)
                .setMaxResults(1)
                .getResultList();
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.get(0));
        }
    }

    @Override
    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }
}
