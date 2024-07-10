package kr.hamburgersee.repository;

import kr.hamburgersee.domain.Board;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class JpaRepoTest {
    @Autowired
    BoardRepository boardRepository;

    @Test
    void saveBoard() {
        Board board = new Board("dd", "aa", null);
        Board savedBoard = boardRepository.save(board);
        Assertions.assertThat(board.getId()).isEqualTo(savedBoard.getId());
    }
}
