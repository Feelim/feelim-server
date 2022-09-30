package cmc.feelim.controller;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponse;
import cmc.feelim.domain.post.Category;
import cmc.feelim.domain.post.dto.GetPostRes;
import cmc.feelim.domain.post.dto.GetPostsRes;
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
    public BaseResponse<List<GetPostsRes>> getAll() {
        return new BaseResponse<>(postService.getAll());
    }

    @ApiOperation("카테고리별 모든 글 불러오기")
    @GetMapping("/category/{category}")
    public BaseResponse<List<GetPostsRes>> getByCategory(@PathVariable(name = "category") Category category) {
        return new BaseResponse<List<GetPostsRes>>(postService.getByCategory(category));
    }

    @ApiOperation("게시물 상세 보기")
    @GetMapping("/{postId}")
    public BaseResponse<GetPostRes> getPost(@PathVariable(name = "postId") Long postId) {
        return new BaseResponse<GetPostRes>(postService.getPost(postId));
    }
}
