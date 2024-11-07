package kr.hamburgersee.domain.member;

import kr.hamburgersee.domain.file.image.ProfileImage;
import kr.hamburgersee.domain.file.image.ProfileImageRepository;
import kr.hamburgersee.domain.file.image.ProfileImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private MemberValidator memberValidator;

    @MockBean
    private ProfileImageService profileImageService;

    @MockBean
    private ProfileImageRepository profileImageRepository;

    @Test
    @DisplayName("회원검증_성공")
    void authenticateSuccess() {
        // Given
        MemberLoginForm memberLoginForm = getSampleMemberLoginForm();
        MemberAuthenticatedInfo memberAuthenticatedInfo = getSampleMemberAuthenticatedInfo();
        when(memberValidator.login(any(), any())).thenReturn(memberAuthenticatedInfo);

        // When
        MemberAuthenticatedInfo successAuthenticatedInfo = memberService.authenticate(memberLoginForm);

        // Then
        assertThat(memberAuthenticatedInfo).isEqualTo(successAuthenticatedInfo);
    }

    @Test
    @DisplayName("회원검증_실패")
    void authenticateFail() {
        // Given
        MemberLoginForm memberLoginForm = getSampleMemberLoginForm();
        when(memberValidator.login(any(), any())).thenThrow(new MemberException());

        // When & Then
        assertThrows(MemberException.class, () -> {
            memberService.authenticate(memberLoginForm);
        });
    }

    @Test
    @DisplayName("회원가입_성공")
    void joinSuccess() throws NoSuchFieldException, IllegalAccessException {
        // Given
        MemberJoinForm memberJoinForm = MemberJoinForm.createDefaultEmpty();

        // 저장될 회원 객체의 id를 조작
        Member savedMember = new Member();
        Field idField = Member.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(savedMember, 1L);

        doNothing().when(memberValidator).join(any(Member.class));
        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        // When
        Long memberId = memberService.join(memberJoinForm);

        // Then
        assertThat(memberId).isEqualTo(1L);
    }

    @Test
    @DisplayName("회원가입_실패")
    void joinFail() {
        // Given
        MemberJoinForm memberJoinForm = MemberJoinForm.createDefaultEmpty();

        doThrow(new MemberException()).when(memberValidator).join(any());

        // When & Then
        assertThrows(MemberException.class, () -> {
            memberService.join(memberJoinForm);
        });
    }

    @Test
    @DisplayName("회원_프로필_이미지_업데이트_성공")
    void updateProfileImageSuccess() {
        // Given
        Long memberId = 1L;

        Member member = new Member();
        MultipartFile multipartFile = mock(MultipartFile.class);
        ProfileImage profileImage = ProfileImage.create("testUri", "testest.png");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(profileImageService.uploadProfileImage(any())).thenReturn(1L);
        when(profileImageRepository.findById(1L)).thenReturn(Optional.of(profileImage));

        // When
        memberService.updateProfileImage(memberId, multipartFile);

        // Then
        assertThat(member.getProfileImage()).isEqualTo(profileImage);
    }

    @Test
    @DisplayName("회원_프로필_이미지_업데이트_예외_회원id_존재X")
    void updateProfileImageFailByInvalidMemberId() {
        // Given
        Long memberId = 1L;

        MultipartFile multipartFile = mock(MultipartFile.class);

        when(memberRepository.findById(any())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(MemberNotFoundException.class, () -> {
            memberService.updateProfileImage(memberId, multipartFile);
        });
    }

    @Test
    @DisplayName("회원_프로필_이미지_업데이트_예외_프로필id_존재X")
    void updateProfileImageFailByInvalidProfileId() {
        // Given
        Long memberId = 1L;
        Long profileId = 1L;

        Member member = new Member();

        MultipartFile multipartFile = mock(MultipartFile.class);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(profileImageService.uploadProfileImage(multipartFile)).thenReturn(profileId);
        when(profileImageRepository.findById(any())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(MemberException.class, () -> {
            memberService.updateProfileImage(memberId, multipartFile);
        });
    }

    private static MemberLoginForm getSampleMemberLoginForm() {
        return new MemberLoginForm("test@test.com", "testPassword");
    }

    private static MemberAuthenticatedInfo getSampleMemberAuthenticatedInfo() {
        return new MemberAuthenticatedInfo(1L, "testNickname", "http" +
                "://test/test.jpg");
    }
}
