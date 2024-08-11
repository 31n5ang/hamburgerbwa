package kr.hamburgersee.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    @Query("select m from Member m left join fetch m.profileImage pi where m.email = :email")
    Optional<Member> findByEmailWithProfileImage(@Param("email") String email);
}
