package kr.hamburgersee.web.service;

import kr.hamburgersee.domain.Board;
import kr.hamburgersee.domain.Comment;
import kr.hamburgersee.domain.Member;
import kr.hamburgersee.domain.base.Likable;
import kr.hamburgersee.repository.BoardRepository;
import kr.hamburgersee.repository.CommentRepository;
import kr.hamburgersee.repository.MemberRepository;
import kr.hamburgersee.web.dto.comment.CommentDto;
import kr.hamburgersee.web.dto.comment.CommentWriteForm;
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
    public Optional<Long> writeCommentByCommentWriteForm(CommentDto commentDto) {
        Optional<Member> optionalMember = memberRepository.findById(commentDto.getMemberId());
        Optional<Board> optionalBoard = boardRepository.findById(commentDto.getBoardId());
        if (optionalMember.isEmpty() || optionalBoard.isEmpty()) {
            return Optional.empty();
        }
        Comment comment = new Comment(
                commentDto.getContent(),
                new Likable(0, 0),
                optionalBoard.get(),
                optionalMember.get()
        );

        Comment savedComment = commentRepository.save(comment);

        return Optional.of(savedComment.getId());
    }

    @Transactional(readOnly = true)
    public List<CommentDto> findCommentDtosByBoardId(Long boardId) {
        List<Comment> comments = commentRepository.findAllByBoardId(boardId);
        return comments.stream()
                .map(this::getCommentDtoByComment)
                .toList();
    }

    private CommentDto getCommentDtoByComment(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getMember().getId(),
                comment.getBoard().getId(),
                comment.getContent(),
                comment.getMember().getNickname(),
                comment.getLikable(),
                comment.getAt()
        );
    }
}
