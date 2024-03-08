package goormton.postappproject.service;

import goormton.postappproject.domain.dto.CommentDto;
import goormton.postappproject.domain.dto.PostDto;
import goormton.postappproject.repository.PostRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentService commentService;

    private PostDto dto1;
    private PostDto dto2;
    private CommentDto commentDto1;
    private CommentDto commentDto2;

    @BeforeEach
    void setUp() {
        dto1 = new PostDto();
        dto1.setTitle("제목");
        dto1.setContent("내용");
        dto1.setDeleted(false);
        dto1.setCreatedDate(LocalDateTime.now());
        postService.save(dto1);

        dto2 = new PostDto();
        dto2.setTitle("제목2");
        dto2.setContent("내용2");
        dto2.setDeleted(false);
        dto2.setCreatedDate(LocalDateTime.now().plusSeconds(10));
        postService.save(dto2);

        commentDto1 = new CommentDto();
        commentDto1.setContent("댓글1");
        commentDto1.setDeleted(false);
        commentDto1.setPostId(1L);
        commentService.save(commentDto1);

        commentDto2 = new CommentDto();
        commentDto2.setContent("댓글2");
        commentDto2.setDeleted(false);
        commentDto2.setPostId(1L);
        commentService.save(commentDto2);
    }

    @AfterEach
    void tearDown() {
        postService.clear();
    }

    @Test
    @DisplayName("게시글 저장")
    void save() {
        PostDto dto = new PostDto();
        dto.setTitle("제목");
        dto.setContent("내용");
        dto.setDeleted(false);
        dto.setCreatedDate(LocalDateTime.now());

        postService.save(dto);

        assertEquals(3, postService.findAllPost().size());
    }

    @Test
    @DisplayName("게시글 수정")
    void update() {
        PostDto updatePost = postService.findOne(1L);
        updatePost.setTitle("제목 바꿈");
        postService.update(updatePost);

        PostDto findPost = postService.findOne(1L);
        assertEquals("제목 바꿈", findPost.getTitle());
    }

    @Test
    @DisplayName("게시글 삭제")
    void delete() {
        postService.delete(1L);

        PostDto findPost = postService.findOne(1L);
        assertTrue(findPost.isDeleted());
    }

    @Test
    @DisplayName("게시글 삭제 시 댓글도 삭제되는지 테스트")
    void deleteCommentDelete() {
        assertEquals(2, postService.findOne(1L).getCommentDtoList().size());

        postService.delete(1L);

        assertEquals(0, postService.findOne(1L).getCommentDtoList().size());
    }

    @Test
    @DisplayName("삭제된 게시글은 수정할 수 없다.")
    void noUpdateAfterDelete() {
        postService.delete(1L);

        PostDto findPost = postService.findOne(1L);
        findPost.setTitle("삭제된 게시글 수정하기~");

        assertThrows(IllegalStateException.class,
                () -> postService.update(findPost));
    }

    @Test
    @DisplayName("게시글 리스트 가져올 때 삭제된 것은 안 가져오는지 테스트")
    void findAll_without_deletePost() {
        assertEquals(2, postService.findAllPost().size());

        postService.delete(1L);

        assertEquals(1, postService.findAllPost().size());
        assertTrue(postService.findOne(1L).isDeleted());
    }

    @Test
    @DisplayName("게시글 리스트 가져올 때 최신 순서로 정렬되는지 테스트")
    void findAllOrderByCreatedAtDESC() {
        assertEquals(postService.findOne(2L), postService.findAllPost().get(0));
        assertEquals(postService.findOne(1L), postService.findAllPost().get(1));
    }

    @Test
    @DisplayName("게시글 단건 조회")
    void findOne() {
        PostDto findPost = postService.findOne(1L);

        assertEquals("제목", findPost.getTitle());
        assertEquals("내용", findPost.getContent());
        assertEquals(2, findPost.getCommentDtoList().size());

        commentService.delete(2L);
        postService.update(findPost);

        assertEquals(1, postService.findOne(1L).getCommentDtoList().size());
    }
}