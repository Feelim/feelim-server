package cmc.feelim.service;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponseStatus;
import cmc.feelim.config.s3.S3FileUploadService;
import cmc.feelim.domain.post.Post;
import cmc.feelim.domain.post.PostRepository;
import cmc.feelim.domain.post.dto.PostPostingReq;
import cmc.feelim.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AuthService authService;
    private final S3FileUploadService fileUploadService;

    /** 글 작성 **/
    @Transactional
    public Long save(PostPostingReq postPostingReq) throws BaseException {

        User user = authService.getUserFromAuth();

        Post post = new Post(postPostingReq, user);
        post.updateImage(fileUploadService.uploadImageFromPost(postPostingReq.getImages(), post));

        return postRepository.save(post).getId();
    }
}
