package goormton.postappproject.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDto {
    private Long postId;
    private String title;
    @JsonIgnore
    private String content;
    private LocalDateTime createdDate;
    @JsonIgnore
    private boolean isDeleted;
    @JsonIgnore
    private List<CommentDto> commentDtoList;
}
