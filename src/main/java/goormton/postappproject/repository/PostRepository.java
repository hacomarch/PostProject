package goormton.postappproject.repository;

import goormton.postappproject.domain.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Post post) {
        if (post.getPostId() == null) {
            em.persist(post);
        }
        em.merge(post);
    }

    public void clear() {
        em.clear();
    }

    public Post findById(Long id) {
        return em.find(Post.class, id);
    }

    public List<Post> findAll() {
        return em.createQuery("select p from Post p" +
                        " where p.isDeleted = false" +
                        " order by p.createdDate desc", Post.class)
                .setMaxResults(10)
                .getResultList();
    }

    public List<Post> findAllFirst(Pageable page) {
        return em.createQuery("select p from Post p" +
                        " where p.isDeleted = false" +
                        " order by p.postId desc", Post.class)
                .setFirstResult(page.getPageNumber() * page.getPageSize())
                .setMaxResults(page.getPageSize())
                .getResultList();
    }

    public List<Post> findAllSecond(Long cursorId, Pageable page) {
        return em.createQuery("select p from Post p" +
                        " where p.postId < :id and p.isDeleted = false" +
                        " order by p.postId desc", Post.class)
                .setParameter("id", cursorId)
                .setFirstResult(page.getPageNumber() * page.getPageSize())
                .setMaxResults(page.getPageSize())
                .getResultList();
    }
}
