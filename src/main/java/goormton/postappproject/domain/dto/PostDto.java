package goormton.postappproject.domain.dto;

import goormton.postappproject.domain.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDto {
    private Long postId;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private boolean isDeleted;
}
