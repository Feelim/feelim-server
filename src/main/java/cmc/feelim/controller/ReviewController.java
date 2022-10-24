package cmc.feelim.controller;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponse;
import cmc.feelim.domain.review.dto.PostReviewReq;
import cmc.feelim.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/laboratory/{laboratoryId}")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @ApiOperation("후기 작성")
    @PostMapping("/new")
    public BaseResponse<Long> create(@PathVariable Long laboratoryId, @RequestBody PostReviewReq postReviewReq) throws BaseException {
        return new BaseResponse<Long>(reviewService.create(laboratoryId, postReviewReq));
    }
}
