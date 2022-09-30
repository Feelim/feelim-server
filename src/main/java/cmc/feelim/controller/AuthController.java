package cmc.feelim.controller;

import cmc.feelim.config.auth.dto.OAuthResponse;
import cmc.feelim.config.exception.BaseResponse;
import cmc.feelim.service.AuthService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@ComponentScan(basePackages = {"cmc/feelim/controller"})
public class AuthController {

    private final AuthService authService;

    /** 소셜로그인 후 리다이렉트 - ! **/
    @ApiOperation(value = "로그인 완료후 리다이렉트")
    @GetMapping("")
    @ResponseBody
    public BaseResponse<OAuthResponse> jwtResponse(@RequestParam String jwt, @RequestParam Long id) {
        System.out.println("jwt: " + jwt + "!!!!!!!!!!!!!!");
        return new BaseResponse<>(new OAuthResponse(id,jwt));
    }

}
