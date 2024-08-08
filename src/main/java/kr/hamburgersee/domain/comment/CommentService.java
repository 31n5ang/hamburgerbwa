package kr.hamburgersee.domain.comment;

import kr.hamburgersee.domain.common.DateFormatter;
import kr.hamburgersee.domain.member.Member;
import kr.hamburgersee.domain.member.MemberNotFoundException;
import kr.hamburgersee.domain.member.MemberRepository;
import kr.hamburgersee.domain.review.Review;
import kr.hamburgersee.domain.review.ReviewNotFoundException;
import kr.hamburgersee.domain.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void updateStatus(Long commentId, CommentStatus status) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new CommentNotFoundException("해당 댓글의 id가 존재하지 않습니다.");
        }
        Comment comment = optionalComment.get();
        comment.updateStatus(status);
    }

    @Transactional
    public Long write(Long reviewId, Long memberId, CommentCreateForm form) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        Optional<Member> member = memberRepository.findById(memberId);

        if (review.isEmpty()) {
            throw new ReviewNotFoundException("해당 리뷰가 존재하지 않습니다.");
        }

        if (member.isEmpty()) {
            throw new MemberNotFoundException("해당 회원이 존재하지 않습니다.");
        }

        Comment comment = Comment.create(form.getContent(), review.get(), member.get());

        Comment savedComment = commentRepository.save(comment);

        return savedComment.getId();
    }


    @Transactional(readOnly = true)
    public List<CommentDto> getCommentDtos(Long reviewId) {
        List<Comment> comments = commentRepository.findAllByReviewIdWithMemberAndReview(reviewId);
        return comments.stream()
                .filter(comment -> comment.getStatus() == CommentStatus.SHOW)
                .map(comment -> new CommentDto(
                        comment.getId(),
                        comment.getContent(),
                        0,
                        comment.getCreatedDate(),
                        DateFormatter.getAgoFormatted(comment.getCreatedDate(), LocalDateTime.now()),
                        comment.getMember().getNickname(),
                        comment.getMember().getProfileImage() == null ? null :
                        comment.getMember().getProfileImage().getUrl(),
                        reviewId
                    ))
                .toList();
    }
}
