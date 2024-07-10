package kr.hamburgersee.web.service;

import kr.hamburgersee.domain.Board;
import kr.hamburgersee.domain.Member;
import kr.hamburgersee.domain.base.At;
import kr.hamburgersee.domain.base.Likable;
import kr.hamburgersee.repository.BoardRepository;
import kr.hamburgersee.repository.MemberRepository;
import kr.hamburgersee.web.dto.board.BoardDto;
import kr.hamburgersee.web.dto.board.BoardWriteForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WebBoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Optional<Long> writeBoardByBoardWriteForm(Long memberId, BoardWriteForm boardWriteForm) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isEmpty()) return Optional.empty();

        Board board = getBoardByBoardWriteForm(boardWriteForm, optionalMember.get());

        Board savedBoard = boardRepository.save(board);

        return Optional.of(savedBoard.getId());
    }

    @Transactional(readOnly = true)
    public List<BoardDto> findBoardDtos() {
        List<Board> boards = boardRepository.findAll();
        return boards.stream()
                .map(this::getBoardDtoByBoard)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<BoardDto> findBoardDtoById(Long boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        return optionalBoard.map(this::getBoardDtoByBoard);
    }

    private Board getBoardByBoardWriteForm(BoardWriteForm boardWriteForm, Member member) {
        Board board = new Board(
                boardWriteForm.getTitle(),
                boardWriteForm.getContent(),
                member
        );
        return board;
    }

    private BoardDto getBoardDtoByBoard(Board board) {
            return new BoardDto(
                    board.getId(),
                    board.getTitle(),
                    board.getContent(),
                    new Likable(
                            board.getLikable().getLikes(),
                            board.getLikable().getDislikes()
                    ),
                    new At(
                            board.getAt().getCreatedAt(),
                            board.getAt().getUpdatedAt()
                    ),
                    board.getMember().getId(),
                    board.getMember().getNickname()
            );
    }
}
