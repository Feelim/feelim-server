package cmc.feelim.controller;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponse;
import cmc.feelim.config.exception.RefineError;
import cmc.feelim.domain.review.dto.PatchReviewReq;
import cmc.feelim.domain.review.dto.PostReviewReq;
import cmc.feelim.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/laboratory/{laboratoryId}")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @ApiOperation("후기 작성")
    @PostMapping("/new")
    public BaseResponse<Long> create(@PathVariable Long laboratoryId, @ModelAttribute @Validated PostReviewReq postReviewReq, Errors errors) throws BaseException, IOException {

        if(errors.hasErrors()) {
            return new BaseResponse<>(RefineError.refine(errors));
        }

        return new BaseResponse<Long>(reviewService.create(laboratoryId, postReviewReq));
    }

    @ApiOperation("후기 수정")
    @PatchMapping("/{reviewId}/modifying")
    public BaseResponse<Long> modify(@PathVariable Long laboratoryId, @PathVariable Long reviewId, @ModelAttribute @Validated PatchReviewReq patchReviewReq, Errors errors) throws BaseException, IOException {

        if(errors.hasErrors()) {
            return new BaseResponse<>(RefineError.refine(errors));
        }

        return new BaseResponse<Long>(reviewService.modify(laboratoryId, reviewId, patchReviewReq));
    }

    @ApiOperation("후기 삭제")
    @PatchMapping("/{reviewId}/delete")
    public BaseResponse<Long> delete(@PathVariable Long laboratoryId, @PathVariable Long reviewId) throws BaseException {
        return new BaseResponse<Long>(reviewService.delete(laboratoryId, reviewId));
    }
}
