package goormton.postappproject.service;

import goormton.postappproject.domain.dto.PostDto;
import goormton.postappproject.repository.PostRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;

    private PostDto dto1;
    private PostDto dto2;

    @BeforeEach
    void setUp() {
        dto1 = new PostDto();
        dto1.setTitle("제목");
        dto1.setContent("내용");
        dto1.setDeleted(false);
        dto1.setCreatedDate(LocalDateTime.now());

        dto2 = new PostDto();
        dto2.setTitle("제목2");
        dto2.setContent("내용2");
        dto2.setDeleted(false);
        dto2.setCreatedDate(LocalDateTime.now().plusSeconds(10));
    }

    @AfterEach
    void tearDown() {
        postService.clear();
    }

    @Test
    @DisplayName("게시글 저장")
    void save() {
        postService.save(dto1);
        postService.save(dto2);

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
        postService.save(dto1);
        postService.save(dto2);

        PostDto updatePost = postService.findOne(1L);
        updatePost.setTitle("제목 바꿈");
        postService.update(updatePost);

        PostDto findPost = postService.findOne(1L);
        assertEquals("제목 바꿈", findPost.getTitle());
    }

    @Test
    @DisplayName("게시글 삭제")
    void delete() {
        postService.save(dto1);
        postService.save(dto2);

        postService.delete(1L);

        PostDto findPost = postService.findOne(1L);
        assertTrue(findPost.isDeleted());
    }

    @Test
    @DisplayName("삭제된 게시글은 수정할 수 없다.")
    void noUpdateAfterDelete() {
        postService.save(dto1);
        postService.save(dto2);

        postService.delete(1L);

        PostDto findPost = postService.findOne(1L);
        findPost.setTitle("삭제된 게시글 수정하기~");

        assertThrows(IllegalStateException.class,
                () -> postService.update(findPost));
    }


    @Test
    @DisplayName("게시글 리스트 가져올 때 삭제된 것은 안 가져오는지 테스트")
    void findAll_without_deletePost() {
        postService.save(dto1);
        postService.save(dto2);

        assertEquals(2, postService.findAllPost().size());

        postService.delete(1L);

        assertEquals(1, postService.findAllPost().size());
        assertTrue(postService.findOne(1L).isDeleted());
    }

    @Test
    @DisplayName("게시글 리스트 가져올 때 최신 순서로 정렬되는지 테스트")
    void findAllOrderByCreatedAtDESC() {
        postService.save(dto1);
        postService.save(dto2);

        assertEquals(postService.findOne(2L), postService.findAllPost().get(0));
        assertEquals(postService.findOne(1L), postService.findAllPost().get(1));
    }

    @Test
    @DisplayName("게시글 단건 조회")
    void findOne() {
        postService.save(dto1);
        postService.save(dto2);

        PostDto findPost = postService.findOne(1L);
        assertEquals("제목", findPost.getTitle());
        assertEquals("내용", findPost.getContent());
    }
}