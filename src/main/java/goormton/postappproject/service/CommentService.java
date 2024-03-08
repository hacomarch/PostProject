package goormton.postappproject.service;

import goormton.postappproject.domain.Comment;
import goormton.postappproject.domain.Post;
import goormton.postappproject.domain.dto.CommentDto;
import goormton.postappproject.repository.CommentRepository;
import goormton.postappproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public void save(CommentDto dto) {
        Comment comment = toEntity(dto);
        commentRepository.save(comment);
        log.info("=== comment insert.id {} ===", comment.getCommentId());
    }

    @Transactional
    public void update(CommentDto dto, Long commentId) {
        if (dto.isDeleted()) {
            throw new IllegalStateException("삭제된 댓글은 수정할 수 없습니다");
        }
        Comment comment = commentRepository.findById(commentId);
        comment.update(dto.getContent());
        commentRepository.save(comment);
        log.info("=== comment update.id {} ===", comment.getCommentId());
    }

    @Transactional
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id);
        comment.delete();
        commentRepository.save(comment);
        log.info("=== comment delete.id {} ===", id);
    }

    @Transactional
    public void deleteAll(Post post) {
        List<Comment> commentList = post.getCommentList();
        commentList.forEach(comment -> {
            comment.delete();
            commentRepository.save(comment);
        });
        log.info("=== comment delete all===");
    }

    @Transactional
    public void clear() {
        commentRepository.clear();
        log.info("=== clear ===");
    }

    public List<CommentDto> findAllComment() {
        log.info("=== findAll ===");
        return commentRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CommentDto findOne(Long id) {
        log.info("=== findOne.id {} ===", id);
        return toDto(commentRepository.findById(id));
    }

    public CommentDto toDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setCommentId(comment.getCommentId());
        dto.setContent(comment.getContent());
        dto.setDeleted(comment.isDeleted());
        dto.setPostId(comment.getPost().getPostId());
        return dto;
    }

    private Comment toEntity(CommentDto dto) {
        Post post = postRepository.findById(dto.getPostId());
        //TODO : 없는 게시글에 댓글을 달려고 할 때 예외 처리
        return new Comment(dto.getContent(), dto.isDeleted(), post);
    }
}
