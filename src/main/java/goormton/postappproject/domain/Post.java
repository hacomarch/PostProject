package goormton.postappproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    private String title;
    private String content;
    private boolean isDeleted;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList;

    public Post() {

    }

    public Post(String title, String content, boolean isDeleted, LocalDateTime createdDate) {
        this.title = title;
        this.content = content;
        this.isDeleted = isDeleted;
        this.createdDate = createdDate;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
