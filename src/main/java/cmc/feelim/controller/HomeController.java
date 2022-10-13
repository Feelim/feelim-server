package cmc.feelim.controller;


import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponse;
import cmc.feelim.domain.user.dto.GetProfileRes;
import cmc.feelim.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
