package cmc.feelim.controller;


import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponse;
import cmc.feelim.config.exception.RefineError;
import cmc.feelim.domain.order.dto.GetOrderRes;
import cmc.feelim.domain.order.dto.GetOrdersRes;
import cmc.feelim.domain.post.dto.GetRecommendationsRes;
import cmc.feelim.domain.user.dto.GetProfileRes;
import cmc.feelim.domain.user.dto.PatchProfileReq;
import cmc.feelim.service.OrderService;
import cmc.feelim.service.PostService;
import cmc.feelim.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final UserService userService;
    private final OrderService orderService;
    private final PostService postService;

    @ApiOperation("기본 홈")
    @GetMapping("/chalkak")
    public BaseResponse<List<GetRecommendationsRes>> getArticles() throws BaseException {
        return new BaseResponse<List<GetRecommendationsRes>>(postService.getArticles());
    }

    @ApiOperation("사용자 프로필")
    @GetMapping("/my-page")
    public BaseResponse<GetProfileRes> getProfile() throws BaseException {
        return new BaseResponse<GetProfileRes>(userService.getProfile());
    }

    @ApiOperation("프로필 수정")
    @PatchMapping("/my-page")
    public BaseResponse<Long> updateProfile(@Validated @ModelAttribute PatchProfileReq patchProfileReq, Errors errors) throws BaseException, ConstraintViolationException {

        if(errors.hasErrors()) {
            return new BaseResponse<>(RefineError.refine(errors));
        }

        return new BaseResponse<Long>(userService.updateProfile(patchProfileReq));
    }

    @ApiOperation("현상 신청 내역 리스트")
    @GetMapping("/order-sheets")
    public BaseResponse<List<GetOrdersRes>> getOrders() throws BaseException {
        return new BaseResponse<List<GetOrdersRes>>(orderService.getAll());
    }

    @ApiOperation("신청 상세 내역")
    @GetMapping("/order-sheets/{orderId}")
    public BaseResponse<GetOrderRes> getOrder(@PathVariable Long orderId) throws BaseException {
        return new BaseResponse<GetOrderRes>(orderService.getOrder(orderId));
    }
}
