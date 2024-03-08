package goormton.postappproject.service;

import goormton.postappproject.domain.Post;
import goormton.postappproject.domain.dto.CommentDto;
import goormton.postappproject.domain.dto.PostDto;
import goormton.postappproject.domain.dto.PostListDto;
import goormton.postappproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final CommentService commentService;

    @Transactional
    public void save(PostDto dto) {
        Post post = toEntity(dto);
        postRepository.save(post);
        log.info("=== insert.id {} ===", post.getPostId());
    }

    @Transactional
    public void update(PostDto dto, Long postId) {
        Post post = postRepository.findById(postId);
        if (post.isDeleted()) {
            throw new IllegalStateException("Deleted posts cannot be modified.");
        }

        post.update(dto.getTitle(), dto.getContent());
        postRepository.save(post);
        log.info("=== update.id {} ===", post.getPostId());
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id);
        post.delete();
        commentService.deleteAll(post);

        postRepository.save(post);
        log.info("=== delete.id {} ===", id);
    }

    @Transactional
    public void clear() {
        postRepository.clear();
        log.info("=== clear ===");
    }

    public List<PostListDto> getPagingPosts(Long cursorId, Pageable page) {
        if (cursorId == 1L) {
            return postRepository.findAllFirst(page)
                    .stream().map(this::postListDto).collect(Collectors.toList());
        }
        return postRepository.findAllSecond(cursorId, page)
                .stream().map(this::postListDto).collect(Collectors.toList());
    }

    public List<PostDto> findAllPost() {
        log.info("=== findAll ===");
        return postRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public PostDto findOne(Long id) {
        log.info("=== findOne.id {} ===", id);
        return toDto(postRepository.findById(id));
    }

    private PostListDto postListDto(Post post) {
        PostListDto dto = new PostListDto();
        dto.setPostId(post.getPostId());
        dto.setTitle(post.getTitle());
        dto.setCreatedDate(post.getCreatedDate());
        return dto;
    }

    private PostDto toDto(Post post) {
        PostDto dto = new PostDto();
        dto.setPostId(post.getPostId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setCreatedDate(post.getCreatedDate());
        dto.setDeleted(post.isDeleted());
        List<CommentDto> commentDtoList = post.getCommentList()
                .stream()
                .filter(comment -> !comment.isDeleted())
                .map(commentService::toDto)
                .toList();
        dto.setCommentDtoList(commentDtoList);
        return dto;
    }

    private Post toEntity(PostDto dto) {
        return new Post(
                dto.getTitle(),
                dto.getContent(),
                dto.isDeleted(),
                LocalDateTime.now()
        );
    }

}
