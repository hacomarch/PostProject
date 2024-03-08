package goormton.postappproject.controller;

import goormton.postappproject.domain.dto.CommentDto;
import goormton.postappproject.domain.dto.PostDto;
import goormton.postappproject.domain.dto.PostListDto;
import goormton.postappproject.service.CommentService;
import goormton.postappproject.service.PostService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
@Slf4j
public class MainController {
    private static final int DEFAULT_SIZE = 5;

    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("/post/cursor/{cursorId}")
    @ResponseBody
    public List<PostListDto> getPosts(@PathVariable Long cursorId) {
        return postService.getPagingPosts(cursorId, PageRequest.of(0, DEFAULT_SIZE));
    }

    @GetMapping("/post/{postId}")
    @ResponseBody
    public PostDto findPostOne(@PathVariable Long postId) {
        return postService.findOne(postId);
    }

    @PostMapping("/post/add")
    public void savePost(@RequestBody PostDto dto) {
        postService.save(dto);
        log.info("게시글이 저장되었습니다.");
    }

    @PostMapping("/post/update/{postId}")
    public String updatePost(@RequestBody PostDto dto, @PathVariable Long postId) {

        try {
            postService.update(dto, postId);
            log.info("게시글이 수정되었습니다.");
            return "{\"message\":\"update Post\"}";
        } catch (IllegalStateException e) {
            log.info(e.getMessage());
            return "{\"message\":\"" + e.getMessage() + "\"}";
        }
    }

    @GetMapping("/post/delete/{postId}")
    public void deletePost(@PathVariable Long postId) {
        postService.delete(postId);
        log.info("게시글이 삭제되었습니다.");
    }

    @PostMapping("/comment/add")
    public void saveComment(@RequestBody CommentDto dto) {
        commentService.save(dto);
        log.info("댓글이 저장되었습니다.");
    }

    @PostMapping("/comment/update/{commentId}")
    public void updateComment(@RequestBody CommentDto dto, @PathVariable Long commentId) {
        commentService.update(dto, commentId);
        log.info("댓글이 수정되었습니다.");
    }

    @GetMapping("/comment/delete/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.delete(commentId);
        log.info("댓글이 삭제되었습니다.");
    }


    @PostConstruct
    public void init() {
        PostDto dto = new PostDto();
        dto.setTitle("제목");
        dto.setContent("내용");
        dto.setDeleted(false);
        for (int i = 0; i < 9; i++) {
            postService.save(dto);
        }

        CommentDto commentDto = new CommentDto();
        commentDto.setContent("댓글");
        commentDto.setDeleted(false);
        commentDto.setPostId(1L);
        commentService.save(commentDto);

        CommentDto commentDto2 = new CommentDto();
        commentDto2.setContent("댓글");
        commentDto2.setDeleted(false);
        commentDto2.setPostId(1L);
        commentService.save(commentDto2);
    }

}
