package goormton.postappproject.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostListDto {
    private Long postId;
    private String title;
    private LocalDateTime createdDate;
}
