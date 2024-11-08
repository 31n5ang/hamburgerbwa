package kr.hamburgersee.domain.member;

import kr.hamburgersee.domain.common.RegionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    TestEntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    private final String EMAIL = "test@test.com";
    private final String ENC_PASSWORD = "encPassword";
    private final String NICKNAME = "test";
    private final RegionType REGION_TYPE = RegionType.ETC;
    private final GenderType GENDER_TYPE = GenderType.NONE;
    private final String BIO = "hello test!";

    @BeforeEach
    void setup() {
        saveSampleMember();
    }

    @Test
    @DisplayName("회원_존재_by_이메일")
    void existsByEmail() {
        // Given
        String INVALID_EMAIL = "invalid@test.com";

        // When
        Boolean existsResult = memberRepository.existsByEmail(EMAIL);
        Boolean noExistsResult = memberRepository.existsByEmail(INVALID_EMAIL);

        // Then
        assertThat(existsResult).isEqualTo(true);
        assertThat(noExistsResult).isEqualTo(false);
    }

    @Test
    @DisplayName("회원_존재_by_닉네임")
    void existsByNickname() {
        // Given
        String INVALID_NICKNAME = "invalidName";

        // When
        Boolean existsResult = memberRepository.existsByNickname(NICKNAME);
        Boolean noExistsResult = memberRepository.existsByNickname(INVALID_NICKNAME);

        // Then
        assertThat(existsResult).isEqualTo(true);
        assertThat(noExistsResult).isEqualTo(false);
    }

    @Test
    @DisplayName("회원_조회_by_이메일_with_프로필_이미지")
    void findByEmailWithProfileImage() {
        // Given
        String SAVED_EMAIL = EMAIL;

        // When
        Optional<Member> findMember = memberRepository.findByEmailWithProfileImage(SAVED_EMAIL);

        // Then
        assertThat(findMember).isPresent();
        assertThat(findMember.get().getEmail()).isEqualTo(SAVED_EMAIL);
    }

    private void saveSampleMember() {
        member = Member.create(EMAIL, ENC_PASSWORD, NICKNAME, REGION_TYPE, GENDER_TYPE, BIO);
        em.persist(member);
        em.flush();
        em.clear();
    }
}
