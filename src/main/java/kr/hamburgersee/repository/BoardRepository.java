package kr.hamburgersee.repository;

import kr.hamburgersee.domain.Board;
import kr.hamburgersee.domain.Member;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    Long save(Board board);

    Optional<Board> findById(Long boardId);

    List<Board> findAll();
}
