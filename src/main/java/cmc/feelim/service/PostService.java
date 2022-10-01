package cmc.feelim.service;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponseStatus;
import cmc.feelim.config.s3.S3FileUploadService;
import cmc.feelim.domain.Status;
import cmc.feelim.domain.post.Category;
import cmc.feelim.domain.post.Post;
import cmc.feelim.domain.post.PostRepository;
import cmc.feelim.domain.post.dto.GetPostRes;
import cmc.feelim.domain.post.dto.GetPostsRes;
import cmc.feelim.domain.post.dto.PatchPostReq;
import cmc.feelim.domain.post.dto.PostPostingReq;
import cmc.feelim.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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

    /** 카테고리별 모든 글 불러오기 **/
    public List<GetPostsRes> getByCategory(Category category) {
        List<Post> posts = postRepository.findByCategory(category);
        List<GetPostsRes> getPostsRes = posts.stream()
                .map(GetPostsRes::new)
                .collect(Collectors.toList());

        return getPostsRes;
    }

    /** 게시물 상세 보기 **/
    public GetPostRes getPost(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        GetPostRes getPostRes = new GetPostRes(post.get());

        return getPostRes;
    }

    /** 게시물 수정 **/
    @Transactional
    public Long updatePost(Long postId, PatchPostReq patchPostReq) {
        Optional<Post> post = postRepository.findById(postId);
        post.get().updatePost(patchPostReq);
        post.get().updateImage(fileUploadService.uploadImageFromPost(patchPostReq.getImages(), post.get()));

        return post.get().getId();
    }

    /** 게시물 삭제 **/
    @Transactional
    public Long deletePost(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        post.get().changeStatus(Status.DELETED);
        return post.get().getId();
    }

    /** 제목 키워드 검색 **/
    public List<GetPostsRes> findByTitle(String keyword) throws BaseException {
        if(keyword.length() < 2) {
            throw new BaseException(BaseResponseStatus.KEYWORD_TOO_SHORT);
        }

        List<Post> posts = postRepository.findByTitleContaining(keyword);
        List<GetPostsRes> getPostsRes = posts.stream()
                .map(GetPostsRes::new)
                .collect(Collectors.toList());
        return getPostsRes;
    }

    /** 내용 검색 **/
    public List<GetPostsRes> findByContent(String keyword) throws BaseException {

        if(keyword.length() < 2) {
            throw new BaseException(BaseResponseStatus.KEYWORD_TOO_SHORT);
        }

        List<Post> posts = postRepository.findByContentContaining(keyword);
        List<GetPostsRes> getPostsRes = posts.stream()
                .map(GetPostsRes::new)
                .collect(Collectors.toList());
        return getPostsRes;
    }
}