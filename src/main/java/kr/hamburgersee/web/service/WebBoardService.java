package kr.hamburgersee.web.service;

import kr.hamburgersee.domain.Board;
import kr.hamburgersee.domain.Member;
import kr.hamburgersee.repository.BoardRepository;
import kr.hamburgersee.repository.MemberRepository;
import kr.hamburgersee.dto.board.BoardDto;
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
    public Optional<Long> write(BoardDto boardDto) {
        Optional<Member> optionalMember = memberRepository.findById(boardDto.getMemberId());

        // 작성자가 비어있다면 실패합니다.
        if (optionalMember.isEmpty()) return Optional.empty();

        Board board = Board.getBoard(boardDto, optionalMember.get());
        Board savedBoard = boardRepository.save(board);
        return Optional.of(savedBoard.getId());
    }

    @Transactional(readOnly = true)
    public List<BoardDto> findBoardDtos() {
        List<Board> boards = boardRepository.findAll();
        return boards.stream()
                .map(Board::getBoardDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<BoardDto> findBoardDtoById(Long boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        return optionalBoard.map(Board::getBoardDto);
    }
}
