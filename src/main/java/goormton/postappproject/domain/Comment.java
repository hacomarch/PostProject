package goormton.postappproject.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    private String content;
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment() {
    }

    public Comment(String content, boolean isDeleted, Post post) {
        this.content = content;
        this.isDeleted = isDeleted;
        this.post = post;
        post.getCommentList().add(this);
    }

    public void update(String content) {
        this.content = content;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
