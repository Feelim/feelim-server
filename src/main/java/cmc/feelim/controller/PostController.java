package cmc.feelim.controller;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponse;
import cmc.feelim.domain.post.Category;
import cmc.feelim.domain.post.dto.GetPostRes;
import cmc.feelim.domain.post.dto.GetPostsRes;
import cmc.feelim.domain.post.dto.PatchPostReq;
import cmc.feelim.domain.post.dto.PostPostingReq;
import cmc.feelim.service.PostService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @ApiOperation("새로운 글 작성")
    @PostMapping("/new")
    public BaseResponse<Long> createPosting(@ModelAttribute PostPostingReq postPostingReq) throws BaseException {
        return new BaseResponse<Long>(postService.save(postPostingReq));
    }

    @ApiOperation("모든 게시판")
    @GetMapping("")
    public BaseResponse<List<GetPostsRes>> getAll() throws BaseException {
        return new BaseResponse<>(postService.getAll());
    }

    @ApiOperation("카테고리별 모든 글 불러오기")
    @GetMapping("/category/{category}")
    public BaseResponse<List<GetPostsRes>> getByCategory(@PathVariable(name = "category") Category category) throws BaseException {
        return new BaseResponse<List<GetPostsRes>>(postService.getByCategory(category));
    }

    @ApiOperation("게시물 상세 보기")
    @GetMapping("/{postId}")
    public BaseResponse<GetPostRes> getPost(@PathVariable(name = "postId") Long postId) throws BaseException {
        return new BaseResponse<GetPostRes>(postService.getPost(postId));
    }

    @ApiOperation("게시물 수정")
    @PatchMapping("/{postId}")
    public BaseResponse<Long> updatePost(@PathVariable(name = "postId") Long postId, @ModelAttribute PatchPostReq patchPostReq) throws BaseException {
        return new BaseResponse<Long>(postService.updatePost(postId, patchPostReq));
    }

    @ApiOperation("게시물 삭제")
    @DeleteMapping("/{postId}")
    public BaseResponse<Long> deletePost(@PathVariable(name = "postId") Long postId) throws BaseException {
        return new BaseResponse<Long>(postService.deletePost(postId));
    }

    @ApiOperation("제목 검색")
    @GetMapping("/search/title/{keyword}")
    public BaseResponse<List<GetPostsRes>> searchByTitle(@PathVariable(name = "keyword") String keyword) throws BaseException {
        return new BaseResponse<List<GetPostsRes>>(postService.findByTitle(keyword));
    }

    @ApiOperation("내용 검색")
    @GetMapping("/search/content/{keyword}")
    public BaseResponse<List<GetPostsRes>> searchByContent(@PathVariable(name = "keyword") String keyword) throws BaseException {
        return new BaseResponse<List<GetPostsRes>>(postService.findByContent(keyword));
    }

    @ApiOperation("내가 작성한 게시물 보기")
    @GetMapping("/my-post")
    public BaseResponse<List<GetPostsRes>> getMyPost() throws BaseException {
        return new BaseResponse<List<GetPostsRes>>(postService.getMyPost());
    }
}
