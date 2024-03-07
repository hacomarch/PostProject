package goormton.postappproject.repository;

import goormton.postappproject.domain.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
        //TODO : offset 페이징 or cursor 페이징
        return em.createQuery("select p from Post p" +
                        " where p.isDeleted = false" +
                        " order by p.createdDate desc", Post.class)
                .setMaxResults(10)
                .getResultList();
    }

}
