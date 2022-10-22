package cmc.feelim.service;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponseStatus;
import cmc.feelim.domain.Status;
import cmc.feelim.domain.comment.Comment;
import cmc.feelim.domain.comment.CommentRepository;
import cmc.feelim.domain.comment.dto.PatchCommentReq;
import cmc.feelim.domain.comment.dto.PostCommentReq;
import cmc.feelim.domain.notification.NotificationType;
import cmc.feelim.domain.post.Post;
import cmc.feelim.domain.post.PostRepository;
import cmc.feelim.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final AuthService authService;
    private final PostRepository postRepository;
    private final FCMService fcmService;

    /** 댓글 달기 **/
    @Transactional
    public Long save(Long postId, PostCommentReq postCommentReq) throws BaseException, IOException {
        User user = authService.getUserFromAuth();

        Optional<Post> post = postRepository.findById(postId);

        if(!post.isPresent()) {
            throw new BaseException(BaseResponseStatus.CHECK_POST_ID);
        }

        Comment comment = new Comment(user, post.get(), postCommentReq);

        String title = post.get().getTitle().length() > 6 ? post.get().getTitle().substring(0, 7) : post.get().getTitle();
        String content = (post.get().getContent().length() > 14 ) ? post.get().getContent().substring(0,15) : post.get().getContent();

        fcmService.sendMessageToUser(title + "..." + NotificationType.COMMENT.getMessage(),
                content + "...",
                post.get().getUser());

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

    /** 댓글 삭제하기 **/
    @Transactional
    public Long delete(Long commentId) throws BaseException {
        User user = authService.getUserFromAuth();
        Optional<Comment> comment = commentRepository.findById(commentId);

        if(user != comment.get().getUser()) {
            throw new BaseException(BaseResponseStatus.NO_EDIT_RIGHTS);
        }

        comment.get().changeStatus(Status.DELETED);
        return comment.get().getId();
    }
}
