package cmc.feelim.controller;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponse;
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

    @ApiOperation("카메라 게시판")
    @GetMapping("/camera")
    public BaseResponse<List<GetPostsRes>> getCamera() {
        return new BaseResponse<>(postService.getCamera());
    }

    @ApiOperation("필름 게시판")
    @GetMapping("/film")
    public BaseResponse<List<GetPostsRes>> getFilm() {
        return new BaseResponse<List<GetPostsRes>>(postService.getFilm());
    }
}
