package goormton.postappproject.service;

import goormton.postappproject.domain.dto.CommentDto;
import goormton.postappproject.domain.dto.PostDto;
import goormton.postappproject.repository.CommentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceTest {
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostService postService;

    private CommentDto dto1;
    private CommentDto dto2;
    private CommentDto dto3;
    private PostDto postDto1;
    private PostDto postDto2;

    @BeforeEach
    void setup() {
        System.out.println("=== before Test ===");
        postDto1 = new PostDto();
        postDto1.setTitle("제목");
        postDto1.setContent("내용");
        postDto1.setDeleted(false);
        postDto1.setCreatedDate(LocalDateTime.now());
        postService.save(postDto1);

        postDto2 = new PostDto();
        postDto2.setTitle("제목2");
        postDto2.setContent("내용2");
        postDto2.setDeleted(false);
        postDto2.setCreatedDate(LocalDateTime.now().plusSeconds(10));
        postService.save(postDto2);

        dto1 = new CommentDto();
        dto1.setContent("댓글1");
        dto1.setDeleted(false);
        dto1.setPostId(1L);
        commentService.save(dto1);

        dto2 = new CommentDto();
        dto2.setContent("댓글2");
        dto2.setDeleted(false);
        dto2.setPostId(1L);
        commentService.save(dto2);

        dto3 = new CommentDto();
        dto3.setContent("댓글33");
        dto3.setDeleted(false);
        dto3.setPostId(2L);
        commentService.save(dto3);
        System.out.println("=== ===");
    }

    @AfterEach
    void down() {
        commentService.clear();
    }

    @Test
    @DisplayName("SAVE")
    void save() {
        assertEquals(2, postService.findOne(1L).getCommentDtoList().size());

        CommentDto dto = new CommentDto();
        dto.setContent("댓글글글");
        dto.setDeleted(false);
        dto.setPostId(1L);
        commentService.save(dto);

        assertEquals(3, postService.findOne(1L).getCommentDtoList().size());
    }

    @Test
    @DisplayName("UPDATE")
    void update() {
        CommentDto one = commentService.findOne(3L);
        one.setContent("댓글3");
        commentService.update(one, 3L);

        assertEquals("댓글3", commentService.findOne(3L).getContent());
    }

    @Test
    @DisplayName("DELETE")
    void delete() {
        assertEquals(2, postService.findOne(1L).getCommentDtoList().size());

        Long commentId = commentService.findOne(2L).getCommentId();
        commentService.delete(commentId);

        assertEquals(1, postService.findOne(1L).getCommentDtoList().size());
    }

    @Test
    @DisplayName("SOFT DELETE")
    void softDelete() {
        Long commentId = commentService.findOne(2L).getCommentId();
        commentService.delete(commentId);

        assertTrue(commentService.findOne(2L).isDeleted());
    }
}