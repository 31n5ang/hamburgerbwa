package kr.hamburgersee.domain.review;

import kr.hamburgersee.domain.argumentresolver.MemberSessionInfoArgumentResolver;
import kr.hamburgersee.domain.comment.CommentCreateForm;
import kr.hamburgersee.domain.comment.CommentDto;
import kr.hamburgersee.domain.comment.CommentService;
import kr.hamburgersee.domain.common.DateFormatter;
import kr.hamburgersee.domain.likes.LikeOnReviewService;
import kr.hamburgersee.domain.session.MemberSessionInfo;
import kr.hamburgersee.domain.session.SessionAttrType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(MemberSessionInfoArgumentResolver.class)
class ReviewControllerTest {

    private static final String REVIEW_CREATE_FORM = "review-create";
    private static final String REVIEW_PATH = "review";
    private static final String REVIEWS_PATH = "reviews";
    private static final String REVIEW_URI = "review";

    @Mock
    private CommentService commentService;

    @Mock
    private LikeOnReviewService likeOnReviewService;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        MemberSessionInfoArgumentResolver argumentResolver = new MemberSessionInfoArgumentResolver();
        PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver = new PageableHandlerMethodArgumentResolver();
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        viewResolver.setCharacterEncoding("UTF-8");

        mockMvc = MockMvcBuilders
                .standaloneSetup(reviewController)
                .setViewResolvers(viewResolver)
                .setCustomArgumentResolvers(argumentResolver, pageableHandlerMethodArgumentResolver)
                .build();
    }

    @Test
    @DisplayName("리뷰_생성_폼_뷰_반환")
    void createForm() throws Exception {
        // Given

        // When
        mockMvc.perform(get("/review/create"))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(REVIEW_CREATE_FORM))
                .andExpect(model().attributeExists("form"));
    }

    @Test
    @DisplayName("리뷰_생성_성공")
    void createSuccess() throws Exception {
        // Given
        Long memberId = 1L;
        Long reviewId = 100L;
        String shopName = "shopName";
        String title = "title";
        String content = "content";

        ReviewCreateForm form = new ReviewCreateForm();
        form.setContent(content);
        form.setTitle(title);
        form.setShopName(shopName);
        MemberSessionInfo memberSessionInfo = getSampleMemberSessionInfo(memberId);

        when(reviewService.writeProcess(eq(form), eq(memberId))).thenReturn(reviewId);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionAttrType.MEMBER_SESSION_INFO.attribute, memberSessionInfo);

        // When
        mockMvc.perform(post("/review/create")
                        .session(session)
                        .flashAttr("form", form))

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/review/" + reviewId));
    }

    @Test
    @DisplayName("리뷰_생성_실패")
    void createFail() throws Exception {
        // Given
        Long memberId = 1L;
        Long reviewId = 100L;
        String shopName = "shopName";
        String title = "title";
        String content = "content";

        ReviewCreateForm form = new ReviewCreateForm();
        form.setContent(content);
        form.setTitle(title);
        form.setShopName(shopName);

        when(reviewService.writeProcess(eq(form), eq(memberId))).thenReturn(reviewId);

        // When
        mockMvc.perform(post("/review/create"))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(REVIEW_CREATE_FORM));
    }

    @Test
    @DisplayName("리뷰_조회_로그인_상태")
    void reviewWithLogin() throws Exception {
        // Given
        Long reviewId = 1L;
        Long memberId = 1L;
        Long likedCount = 10L;
        boolean isLiked = true;

        ReviewDto reviewDto = mock(ReviewDto.class);
        List<CommentDto> commentDtos = mock(List.class);
        MemberSessionInfo memberSessionInfo = getSampleMemberSessionInfo(memberId);

        when(reviewService.getReviewDto(eq(reviewId))).thenReturn(reviewDto);
        when(commentService.getCommentDtos(eq(reviewId))).thenReturn(commentDtos);
        when(likeOnReviewService.getLikedCount(eq(reviewId))).thenReturn(likedCount);
        when(likeOnReviewService.isLiked(eq(reviewId), eq(memberId))).thenReturn(isLiked);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionAttrType.MEMBER_SESSION_INFO.attribute, memberSessionInfo);

        // When
        mockMvc.perform(get("/review/" + reviewId)
                        .session(session))

                // Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name(REVIEW_PATH))
                .andExpect(model().attribute("review", reviewDto))
                .andExpect(model().attribute("form", new CommentCreateForm()))
                .andExpect(model().attribute("comments", commentDtos))
                .andExpect(model().attribute("likedCount", likedCount))
                .andExpect(model().attribute("isLiked", isLiked));

        verify(likeOnReviewService).isLiked(eq(reviewId), eq(memberId));
    }

    @Test
    @DisplayName("리뷰_조회_비로그인_상태")
    void reviewWithNoLogin() throws Exception {
        // Given
        Long reviewId = 1L;
        Long memberId = 1L;
        Long likedCount = 10L;
        Boolean isLiked = true;

        ReviewDto reviewDto = mock(ReviewDto.class);
        List<CommentDto> commentDtos = mock(List.class);

        when(reviewService.getReviewDto(eq(reviewId))).thenReturn(reviewDto);
        when(commentService.getCommentDtos(eq(reviewId))).thenReturn(commentDtos);
        when(likeOnReviewService.getLikedCount(eq(reviewId))).thenReturn(likedCount);
        when(likeOnReviewService.isLiked(eq(reviewId), eq(memberId))).thenReturn(isLiked);

        // When
        mockMvc.perform(get("/review/" + reviewId))

        // Then
                .andExpect(status().isOk())
                .andExpect(view().name(REVIEW_PATH))
                .andExpect(model().attributeDoesNotExist("isLiked"));

        verify(likeOnReviewService, never()).isLiked(any(), any());
    }

    @Test
    @DisplayName("댓글_생성_성공")
    void commentSuccess() throws Exception {
        // Given
        Long reviewId = 100L;
        Long memberId = 1L;

        CommentCreateForm form = new CommentCreateForm();
        form.setContent("hello!");

        MemberSessionInfo memberSessionInfo = getSampleMemberSessionInfo(memberId);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionAttrType.MEMBER_SESSION_INFO.attribute, memberSessionInfo);

        // When
        mockMvc.perform(post("/review/" + reviewId)
                        .session(session)
                        .flashAttr("form", form))

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/" + REVIEW_URI + "/" + reviewId));
    }

    @Test
    @DisplayName("댓글_생성_실패_by_빈_벨리데이션")
    void commentFailByBeanValidation() throws Exception {
        // Given
        Long reviewId = 100L;
        Long memberId = 1L;

        CommentCreateForm form = new CommentCreateForm();

        ReviewDto reviewDto = mock(ReviewDto.class);
        List<CommentDto> commentDtos = mock(List.class);

        MemberSessionInfo memberSessionInfo = getSampleMemberSessionInfo(memberId);

        when(reviewService.getReviewDto(eq(reviewId))).thenReturn(reviewDto);
        when(commentService.getCommentDtos(eq(reviewId))).thenReturn(commentDtos);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionAttrType.MEMBER_SESSION_INFO.attribute, memberSessionInfo);

        // When
        mockMvc.perform(post("/review/" + reviewId)
                        .flashAttr("form", form)
                        .session(session))

                // Then
                .andExpect(status().isOk())
                .andExpect(model().attribute("review", reviewDto))
                .andExpect(model().attribute("comments", commentDtos))
                .andExpect(view().name(REVIEW_PATH));
    }

    @Test
    @DisplayName("리뷰_조회_페이징")
    void findReviewsWithPaging() throws Exception {
        // Given
        int pageNumber = 0;
        int pageSize = 10;
        String sortBy = "createdDate";

        List<ReviewCardDto> reviewCardDtos = List.of(
                getSampleReviewCardDto(1L),
                getSampleReviewCardDto(2L),
                getSampleReviewCardDto(3L),
                getSampleReviewCardDto(4L)
        );

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());

        Slice<ReviewCardDto> slice = new SliceImpl<>(reviewCardDtos, pageable, true);

        when(reviewService.getReviewCardDtos(eq(pageable))).thenReturn(slice);

        // When
        mockMvc.perform(get("/review/list")
                        .param("page", String.valueOf(pageNumber))
                        .param("size", String.valueOf(pageSize))
                        .param("sort", sortBy + ",desc"))

                // Then
                .andExpect(status().isOk())
                .andExpect(view().name(REVIEWS_PATH))
                .andExpect(model().attribute("hasNext", slice.hasNext()))
                .andExpect(model().attribute("hasPrevious", slice.hasPrevious()))
                .andExpect(model().attribute("reviews", slice.getContent()));
    }

    @Test
    @DisplayName("좋아요_토글")
    void like() throws Exception {
        // Given
        Long memberId = 1L;
        Long reviewId = 100L;

        MemberSessionInfo memberSessionInfo = getSampleMemberSessionInfo(memberId);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionAttrType.MEMBER_SESSION_INFO.attribute, memberSessionInfo);

        doNothing().when(likeOnReviewService).toggleReviewLike(eq(reviewId), eq(memberId));

        // When
        mockMvc.perform(post("/review/like")
                        .param("reviewId", String.valueOf(reviewId))
                        .session(session))

                // Then
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/" + REVIEW_URI + "/" + reviewId));
    }

    private static MemberSessionInfo getSampleMemberSessionInfo(Long memberId) {
        return new MemberSessionInfo(memberId, "testName", "test/url");
    }

    private static ReviewCardDto getSampleReviewCardDto(Long id) {
        return new ReviewCardDto(
                id, "test" + id, "title" + id, "content" + id, "name" + id,
                "thumb/url" + id, LocalDateTime.now(),
                new ArrayList<>(), "대전",
                DateFormatter.getAgoFormatted(LocalDateTime.now(), LocalDateTime.now().plusMinutes(2L)),
                3L
        );
    }
}
