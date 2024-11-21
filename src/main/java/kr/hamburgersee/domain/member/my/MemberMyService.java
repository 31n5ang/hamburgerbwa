package kr.hamburgersee.domain.member.my;

import kr.hamburgersee.domain.comment.CommentRepository;
import kr.hamburgersee.domain.likes.LikeOnReviewRepository;
import kr.hamburgersee.domain.likes.LikeStatus;
import kr.hamburgersee.domain.member.Member;
import kr.hamburgersee.domain.member.MemberNotFoundException;
import kr.hamburgersee.domain.member.MemberRepository;
import kr.hamburgersee.domain.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberMyService {
    private final MemberRepository memberRepository;
    private final LikeOnReviewRepository likeOnReviewRepository;
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;

    public MemberMyDto getMemberMyDto(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isEmpty()) {
            throw new MemberNotFoundException("해당 회원이 존재하지 않습니다.");
        }
        Member member = optionalMember.get();

        Long likedCount = likeOnReviewRepository.countByMemberId(memberId, LikeStatus.LIKED);
        Long reviewCount = reviewRepository.countByMemberId(memberId);
        Long commentCount = commentRepository.countByMemberId(memberId);

        return new MemberMyDto(
                member.getRegion(),
                member.getGender(),
                member.getBio(),
                likedCount,
                reviewCount,
                commentCount
        );
    }
}
