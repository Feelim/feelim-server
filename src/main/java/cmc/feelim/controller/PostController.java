package cmc.feelim.controller;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponse;
import cmc.feelim.domain.post.dto.PostPostingReq;
import cmc.feelim.service.PostService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    /** 글 쓰기 **/
    @ApiOperation("새로운 글 작성")
    @PostMapping("/new")
    public BaseResponse<Long> createPosting(@ModelAttribute PostPostingReq postPostingReq) throws BaseException {
        return new BaseResponse<Long>(postService.save(postPostingReq));
    }
}
