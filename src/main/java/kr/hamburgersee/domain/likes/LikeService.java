package kr.hamburgersee.domain.likes;

import kr.hamburgersee.domain.member.Member;
import kr.hamburgersee.domain.member.MemberNotFoundException;
import kr.hamburgersee.domain.member.MemberRepository;
import kr.hamburgersee.domain.review.Review;
import kr.hamburgersee.domain.review.ReviewNotFoundException;
import kr.hamburgersee.domain.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void  toggleReviewLike(Long reviewId, Long memberId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        if (optionalReview.isEmpty()) {
            throw new ReviewNotFoundException("해당 리뷰의 id가 존재하지 않습니다.");
        }

        if (optionalMember.isEmpty()) {
            throw new MemberNotFoundException("해당 회원의 id가 존재하지 않습니다.");
        }

        Review review = optionalReview.get();
        Member member = optionalMember.get();

        Optional<Like> optionalLike = likeRepository.findByReviewAndMember(review, member);

        if (optionalLike.isEmpty()) {
            // 처음 좋아요를 누른다면 저장합니다.
            Like like = ReviewLike.create(review, member, LikeStatus.LIKED);
            likeRepository.save(like);
        } else {
            // 이전에 해당 리뷰의 좋아요를 눌렀다면 토글 시킵니다.
            Like beforeLiked = optionalLike.get();
            LikeStatus beforeStatus = beforeLiked.getStatus();
            switch (beforeStatus) {
                case LIKED -> beforeLiked.updateState(LikeStatus.NONE);
                case NONE -> beforeLiked.updateState(LikeStatus.LIKED);
            }
        }
    }

    @Transactional(readOnly = true)
    public boolean isLiked(Long reviewId, Long memberId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        if (optionalReview.isEmpty()) {
            throw new ReviewNotFoundException("해당 리뷰의 id가 존재하지 않습니다.");
        }

        if (optionalMember.isEmpty()) {
            throw new MemberNotFoundException("해당 회원의 id가 존재하지 않습니다.");
        }

        return likeRepository.existsLikedByReviewIdAndMemberId(reviewId, memberId) == 1;
    }

    public Long getLikedCount(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new ReviewNotFoundException("해당 리뷰의 id가 존재하지 않습니다."));

        return likeRepository.countByReview(review, LikeStatus.LIKED);
    }
}
