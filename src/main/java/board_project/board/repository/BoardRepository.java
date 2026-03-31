package board_project.board.repository;

import board_project.board.domain.Board;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepository {

    private final EntityManager em;

    public void save(Board board) {
        em.persist(board);
    }

    public Board findOne(Long boardId) {
        return em.find(Board.class, boardId);
    }

    public List<Board> findAll() {
        return em.createQuery("select b from Board b", Board.class).getResultList();
    }

    public void remove(Board board) {
        em.remove(board);
    }

    public List<Board> findByUser(Long userId) {
        return em.createQuery("select b from Board b where b.user.id = :userId", Board.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
