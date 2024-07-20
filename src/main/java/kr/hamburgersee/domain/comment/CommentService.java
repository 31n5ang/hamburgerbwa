package kr.hamburgersee.domain.comment;

import kr.hamburgersee.domain.member.Member;
import kr.hamburgersee.domain.member.MemberNotFoundException;
import kr.hamburgersee.domain.member.MemberRepository;
import kr.hamburgersee.domain.review.Review;
import kr.hamburgersee.domain.review.ReviewDto;
import kr.hamburgersee.domain.review.ReviewNotFoundException;
import kr.hamburgersee.domain.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long write(Long reviewId, Long memberId, CommentCreateForm form) {
        Optional<Review> review = reviewRepository.findById(reviewId);
//        Optional<Member> member = reviewRepository.findById(memberId);

        if (review.isEmpty()) {
            throw new ReviewNotFoundException("해당 리뷰가 존재하지 않습니다.");
        }

//        if (member.isEmpty()) {
//            throw new MemberNotFoundException("해당 회원이 존재하지 않습니다.");
//        }

        Comment comment = Comment.create(form.getContent(), review.get(), null);

        Comment savedComment = commentRepository.save(comment);

        return savedComment.getId();
    }


    @Transactional(readOnly = true)
    public List<CommentDto> getCommentDtos(Long reviewId) {
        List<Comment> comments = commentRepository.findAllByReviewId(reviewId);
        return comments.stream()
                .map((comment) -> new CommentDto(
                            comment.getContent(),
                            0,
                            comment.getAt(),
                            // TODO : member's property
                            null,
                            null
                    ))
                .collect(Collectors.toList());
    }
}
