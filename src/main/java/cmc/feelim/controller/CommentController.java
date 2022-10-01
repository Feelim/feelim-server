package cmc.feelim.controller;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponse;
import cmc.feelim.domain.comment.dto.PostCommentReq;
import cmc.feelim.service.CommentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{postId}/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @ApiOperation("댓글 달기")
    @PostMapping("/new")
    public BaseResponse<Long> createComment(@PathVariable(name = "postId") Long postId, @RequestBody PostCommentReq postCommentReq) throws BaseException {
        return new BaseResponse<Long>(commentService.save(postId, postCommentReq));
    }
}
