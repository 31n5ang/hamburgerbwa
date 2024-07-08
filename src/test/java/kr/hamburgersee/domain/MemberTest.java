package kr.hamburgersee.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Encoder;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberTest {
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "testPassword!";
    private static final String NICKNAME = "테스트별명";

    private static final long range = 10; //s

    @PersistenceContext
    EntityManager em;

    PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    public void Member_registrationAt_test() throws InterruptedException {
        //given
        Member member = new Member(EMAIL, encoder.encode(PASSWORD), NICKNAME);

        //when
        LocalDateTime now = LocalDateTime.now();
        em.persist(member);
        em.flush();
        em.clear();

        //then
        Assertions.assertThat(member.getId()).isNotNull();
        Assertions.assertThat(member.getCreatedAt()).isAfter(now.minusSeconds(10));
        Assertions.assertThat(member.getCreatedAt()).isBefore(now.plusSeconds(10));
        Assertions.assertThat(member.getUpdatedAt()).isAfter(now.minusSeconds(10));
        Assertions.assertThat(member.getUpdatedAt()).isBefore(now.plusSeconds(10));
    }
}
