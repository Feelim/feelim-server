package cmc.feelim.service;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.domain.comment.Comment;
import cmc.feelim.domain.comment.CommentRepository;
import cmc.feelim.domain.comment.dto.PostCommentReq;
import cmc.feelim.domain.post.PostRepository;
import cmc.feelim.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final AuthService authService;
    private final PostRepository postRepository;

    /** 댓글 달기 **/
    @Transactional
    public Long save(Long postId, PostCommentReq postCommentReq) throws BaseException {
        User user = authService.getUserFromAuth();
        Comment comment = new Comment(user, postRepository.findById(postId).get(), postCommentReq);
        return commentRepository.save(comment).getId();
    }
}
