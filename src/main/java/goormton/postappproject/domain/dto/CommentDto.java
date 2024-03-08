package goormton.postappproject.domain.dto;

import lombok.Data;

@Data
public class CommentDto {
    private Long commentId;
    private String content;
    private boolean isDeleted;
    private Long postId;
}
