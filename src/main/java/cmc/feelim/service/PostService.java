package cmc.feelim.service;

import antlr.collections.impl.LList;
import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponseStatus;
import cmc.feelim.config.s3.S3FileUploadService;
import cmc.feelim.domain.post.Category;
import cmc.feelim.domain.post.Post;
import cmc.feelim.domain.post.PostRepository;
import cmc.feelim.domain.post.dto.GetPostsRes;
import cmc.feelim.domain.post.dto.PostPostingReq;
import cmc.feelim.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    /** 모든 게시물 불러오기 **/
    public List<GetPostsRes> getAll() {
        List<Post> posts = postRepository.findAll();
        List<GetPostsRes> getPostsRes = posts.stream()
                .map(GetPostsRes::new)
                .collect(Collectors.toList());

        return getPostsRes;
    }

    /** 카메라 게시판 글 불러오기 **/
    public List<GetPostsRes> getCamera() {
        List<Post> posts = postRepository.findByCategory(Category.CAMERA);
        List<GetPostsRes> getPostsRes = posts.stream()
                .map(GetPostsRes::new)
                .collect(Collectors.toList());

        return getPostsRes;
    }

    /** 필름 게시판 글 불러오기 **/
    public List<GetPostsRes> getFilm() {
        List<Post> posts = postRepository.findByCategory(Category.FILM);
        List<GetPostsRes> getPostsRes = posts.stream()
                .map(GetPostsRes::new)
                .collect(Collectors.toList());

        return getPostsRes;
    }
}
