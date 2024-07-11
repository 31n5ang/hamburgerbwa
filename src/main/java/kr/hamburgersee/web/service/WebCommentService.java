package kr.hamburgersee.web.service;

import kr.hamburgersee.domain.Board;
import kr.hamburgersee.domain.Comment;
import kr.hamburgersee.domain.Member;
import kr.hamburgersee.domain.base.Likable;
import kr.hamburgersee.repository.BoardRepository;
import kr.hamburgersee.repository.CommentRepository;
import kr.hamburgersee.repository.MemberRepository;
import kr.hamburgersee.dto.comment.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WebCommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    @Transactional
    public Optional<Long> write(CommentDto commentDto) {
        Optional<Member> optionalMember = memberRepository.findById(commentDto.getMemberId());
        Optional<Board> optionalBoard = boardRepository.findById(commentDto.getBoardId());

        if (optionalMember.isEmpty() || optionalBoard.isEmpty()) {
            // 코멘트를 남길 멤버나 게시판을 조회할 수 없다면 실패합니다.
            return Optional.empty();
        }

        Member member = optionalMember.get();
        Board board = optionalBoard.get();

        Comment comment = new Comment(
                commentDto.getContent(),
                Likable.defaultValue(),
                board,
                member
        );

        Comment savedComment = commentRepository.save(comment);

        return Optional.of(savedComment.getId());
    }

    @Transactional(readOnly = true)
    public List<CommentDto> findCommentDtosByBoardId(Long boardId) {
        List<Comment> comments = commentRepository.findAllByBoardId(boardId);
        return comments.stream()
                .map(Comment::getCommentDto)
                .toList();
    }
}
