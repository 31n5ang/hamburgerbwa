package kr.hamburgersee.repository.impl;

import jakarta.persistence.EntityManager;
import kr.hamburgersee.domain.Board;
import kr.hamburgersee.domain.Member;
import kr.hamburgersee.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpqlBoardRepository implements BoardRepository {
    private final EntityManager em;

    @Override
    public Long save(Board board) {
        em.persist(board);
        return board.getId();
    }

    @Override
    public Optional<Board> findById(Long boardId) {
        return Optional.ofNullable(em.find(Board.class, boardId));
    }

    @Override
    public List<Board> findAll() {
        return em.createQuery("SELECT b FROM Board b", Board.class).getResultList();
    }
}
