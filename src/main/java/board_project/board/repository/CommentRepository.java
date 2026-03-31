package board_project.board.repository;

import board_project.board.domain.Comment;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final EntityManager em;

    public void save(Comment comment) {
        em.persist(comment);
    }

    public Comment findOne(Long commentId) {
        return em.find(Comment.class, commentId);
    }

    public List<Comment> findAll() {
        return em.createQuery("select c from Comment c", Comment.class).getResultList();
    }

    public List<Comment> findByBoard(Long boardId) {
        return em.createQuery("select c from Comment c where c.board.id = :boardId", Comment.class)
                .setParameter("boardId", boardId)
                .getResultList();
    }

    public List<Comment> findByUser(Long userId) {
        return em.createQuery("select c from Comment c where c.user.id = :userId", Comment.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
