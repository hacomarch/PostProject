package goormton.postappproject.domain.dto;

import lombok.Data;

@Data
public class CommentDto {
    private String content;
    private boolean isDeleted;
    private Long postId;
}
