package kr.hamburgersee.domain.member;

import kr.hamburgersee.domain.session.MemberSessionInfo;
import kr.hamburgersee.domain.session.SessionAttrType;
import kr.hamburgersee.domain.session.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import static kr.hamburgersee.domain.member.MemberFormConstant.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MemberControllerTest {

    private static final String JOIN_FORM_PATH = "join";
    private static final String LOGIN_FORM_PATH = "login";
    public static final String MODEL_FORM_NAME = "form";
    public static final String PROFILE_IMAGE_BASE_64 = "profileImageBase64";
    public static final String JOIN_URI = "/join";
    public static final String LOGIN_URI = "/login";
    public static final String LOGOUT_URI = "/logout";

    private MockMvc mockMvc;

    @Mock
    MemberService memberService;

    @Mock
    SessionService sessionService;

    @InjectMocks
    MemberController memberController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        viewResolver.setCharacterEncoding("UTF-8");

        mockMvc = MockMvcBuilders
                .standaloneSetup(memberController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    @DisplayName("회원가입_뷰_반환")
    void getJoin() throws Exception {
        // 회원가입 : join
        mockMvc.perform(get(JOIN_URI))
                .andExpect(view().name(JOIN_FORM_PATH));
    }

    @Test
    @DisplayName("회원가입_성공")
    void postJoinSuccess() throws Exception {
        // Given
        MemberJoinForm memberJoinForm = getSampleMemberJoinForm();

        MockMultipartFile profileImage = getMockMultipartFile();

        // 회원 저장 성공
        when(memberService.join(any())).thenReturn(1L);

        // When
        mockMvc.perform(getJoinRequestBuilder(profileImage, memberJoinForm))

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @DisplayName("회원가입_실패_이메일_중복")
    void postJoinFailByDuplicateEmail() throws Exception {
        // Given
        MemberJoinForm memberJoinForm = getSampleMemberJoinForm();

        MockMultipartFile profileImage = getMockMultipartFile();

        // 이메일 중복 예외 발생
        when(memberService.join(any())).thenThrow(new MemberDuplicateEmailException());

        // When
        mockMvc.perform(getJoinRequestBuilder(profileImage, memberJoinForm))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(JOIN_FORM_PATH))
                .andExpect(model().attributeHasFieldErrors(MODEL_FORM_NAME, EMAIL_FIELD));
    }

    @Test
    @DisplayName("회원가입_실패_닉네임_중복")
    void postJoinFailByDuplicateNickname() throws Exception {
        // Given
        MemberJoinForm memberJoinForm = getSampleMemberJoinForm();

        MockMultipartFile profileImage = getMockMultipartFile();

        // 닉네임 중복 예외 발생
        when(memberService.join(any())).thenThrow(new MemberDuplicateNicknameException());

        // When
        mockMvc.perform(getJoinRequestBuilder(profileImage, memberJoinForm))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(JOIN_FORM_PATH))
                .andExpect(model().attributeHasFieldErrors(MODEL_FORM_NAME, NICKNAME_FIELD));
    }

    @Test
    @DisplayName("로그인_뷰_반환")
    void getLogin() throws Exception {
        // 로그인 : login
        mockMvc.perform(get(LOGIN_URI))
                .andExpect(view().name(LOGIN_FORM_PATH));
    }

    @Test
    @DisplayName("로그인_성공")
    void postLoginSuccess() throws Exception{
        // Given
        MemberLoginForm memberLoginForm = getSampleMemberLoginForm();

        MemberAuthenticatedInfo authenticatedInfo = getMockAuthenticatedInfo();
        when(memberService.authenticate(any())).thenReturn(authenticatedInfo);
        when(sessionService.find(SessionAttrType.MEMBER_SESSION_INFO)).thenReturn(null);

        // When
        // 요청 검증
        mockMvc.perform(getLoginRequestBuilder(memberLoginForm).param("redirectUri", "/test"))

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/test"));

        // 세션 생성 메소드 확인
        verify(sessionService).create(eq(SessionAttrType.MEMBER_SESSION_INFO), any(MemberSessionInfo.class));
    }

    @Test
    @DisplayName("로그인_실패")
    void postLoginFailByNotValidEmail() throws Exception{
        // Given
        MemberLoginForm memberLoginForm = getSampleMemberLoginForm();

        when(memberService.authenticate(any())).thenThrow(new MemberException());

        // When
        mockMvc.perform(getLoginRequestBuilder(memberLoginForm))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(LOGIN_FORM_PATH));
    }

    @Test
    @DisplayName("로그아웃")
    void getLogout() throws Exception {
        mockMvc.perform(get(LOGOUT_URI))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        // 세션 무효화 메소드 확인
        verify(sessionService).clear();
    }

    private static MemberAuthenticatedInfo getMockAuthenticatedInfo() {
        return new MemberAuthenticatedInfo(1L, "testNickname", "/profile/image.jpg");
    }

    private static MockMultipartFile getMockMultipartFile() {
        MockMultipartFile profileImage = new MockMultipartFile(PROFILE_IMAGE_BASE_64, "image.jpg", "image/jpeg",
                new byte[0]);
        return profileImage;
    }

    private static MemberLoginForm getSampleMemberLoginForm() {
        MemberLoginForm memberLoginForm = new MemberLoginForm();
        memberLoginForm.setEmail("test@test.com");
        memberLoginForm.setRawPassword("test");
        return memberLoginForm;
    }

    private static MemberJoinForm getSampleMemberJoinForm() {
        MemberJoinForm memberJoinForm = MemberJoinForm.createDefaultEmpty();
        memberJoinForm.setEmail("test@test.com");
        memberJoinForm.setNickname("test");
        memberJoinForm.setRawPassword("rawPassword");
        return memberJoinForm;
    }

    private static MockHttpServletRequestBuilder getLoginRequestBuilder(MemberLoginForm memberLoginForm) {
        return post(LOGIN_URI)
                .param("email", memberLoginForm.getEmail())
                .param("rawPassword", memberLoginForm.getRawPassword());
    }

    private static MockHttpServletRequestBuilder getJoinRequestBuilder(MockMultipartFile profileImage, MemberJoinForm memberJoinForm) {
        return multipart(JOIN_URI)
                .file(profileImage)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param(EMAIL_FIELD, memberJoinForm.getEmail())
                .param(NICKNAME_FIELD, memberJoinForm.getNickname())
                .param(RAW_PASSWORD_FIELD, memberJoinForm.getRawPassword());
    }
}
