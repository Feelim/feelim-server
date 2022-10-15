package cmc.feelim.controller;


import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponse;
import cmc.feelim.config.exception.RefineError;
import cmc.feelim.domain.user.dto.GetProfileRes;
import cmc.feelim.domain.user.dto.PatchProfileReq;
import cmc.feelim.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final UserService userService;

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
}
