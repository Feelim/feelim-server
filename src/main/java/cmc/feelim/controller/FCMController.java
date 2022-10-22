package cmc.feelim.controller;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponse;
import cmc.feelim.domain.user.User;
import cmc.feelim.service.FCMService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/push")
public class FCMController {

    private final FCMService fcmService;

    @Autowired
    public FCMController(FCMService fcmService) {
        this.fcmService = fcmService;
    }

    @ApiOperation("fcm 토큰 전송")
    @PatchMapping
    public BaseResponse<Long> addFcmToken(@RequestParam("fcmToken") String fcmToken) throws BaseException {
        User user = fcmService.addFcmToken(fcmToken);
        return new BaseResponse<Long>(user.getId());
    }

//    @ApiOperation("첫번째 유저에게 테스트")
//    @GetMapping("/test")
//    public String tokenTest() throws IOException {
        // String testToken = "fpO9B9HkQs-5nUrb0t-2de:APA91bHSK_JgiUJDL4re0dSfEyVAgNwp7C6Cugc05vUoXxg3WiHv6fubih7_czuJggVq6Yl4DUwwQIlzvz2qkVHh7IGPwQnQprd6ZN-6MXmo1jfogRX1t4WQK5ABmT-XaKOkVw_Wa39q";
//        fcmService.sendMessageToUser("첫번째유저에게가는테스트입니다","테스트메시지",1L);
//        return "itistest";
//    }

}
