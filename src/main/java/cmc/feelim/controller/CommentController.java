package cmc.feelim.controller;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponse;
import cmc.feelim.domain.comment.dto.PatchCommentReq;
import cmc.feelim.domain.comment.dto.PostCommentReq;
import cmc.feelim.service.CommentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/post/{postId}/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @ApiOperation("댓글 달기")
    @PostMapping("/new")
    public BaseResponse<Long> createComment(@PathVariable(name = "postId") Long postId, @RequestBody PostCommentReq postCommentReq) throws BaseException, IOException {
        return new BaseResponse<Long>(commentService.save(postId, postCommentReq));
    }

    @ApiOperation("댓글 수정")
    @PatchMapping("/{commentId}")
    public BaseResponse<Long> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody PatchCommentReq patchCommentReq) throws BaseException {
        return new BaseResponse<Long>(commentService.update(commentId, patchCommentReq));
    }

    @ApiOperation("댓글 삭제")
    @DeleteMapping("/{commentId}")
    public BaseResponse<Long> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) throws BaseException {
        return new BaseResponse<Long>(commentService.delete(commentId));
    }
}
