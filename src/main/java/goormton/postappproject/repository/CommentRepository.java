package goormton.postappproject.repository;

import goormton.postappproject.domain.Comment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepository{

    @PersistenceContext
    private EntityManager em;

    public void save(Comment comment) {
        if (comment.getCommentId() == null) {
            em.persist(comment);
        }
        em.merge(comment);
    }

    public void clear() {
        em.clear();
    }

    public Comment findById(Long id) {
        return em.find(Comment.class, id);
    }

    public List<Comment> findAll() {
        return em.createQuery("select c from Comment c where c.isDeleted = false", Comment.class)
                .getResultList();
    }
}
