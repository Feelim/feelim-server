package cmc.feelim.service;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponseStatus;
import cmc.feelim.domain.comment.Comment;
import cmc.feelim.domain.comment.CommentRepository;
import cmc.feelim.domain.comment.dto.PatchCommentReq;
import cmc.feelim.domain.comment.dto.PostCommentReq;
import cmc.feelim.domain.post.PostRepository;
import cmc.feelim.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    /** 댓글 수정하기 **/
    @Transactional
    public Long update(Long commentId, PatchCommentReq patchCommentReq) throws BaseException {
        User user = authService.getUserFromAuth();
        Optional<Comment> comment = commentRepository.findById(commentId);

        if(user != comment.get().getUser()) {
            throw new BaseException(BaseResponseStatus.NO_EDIT_RIGHTS);
        }

        comment.get().update(patchCommentReq);

        return comment.get().getId();
    }
}
