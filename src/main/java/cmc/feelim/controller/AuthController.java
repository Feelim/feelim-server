package cmc.feelim.controller;

import cmc.feelim.config.auth.dto.*;
import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponse;
import cmc.feelim.config.exception.BaseResponseStatus;
import cmc.feelim.service.AuthService;
import cmc.feelim.utils.AppleLoginUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@ComponentScan(basePackages = {"cmc/feelim/controller"})
public class AuthController {
    @Value("${apple.auth-url}")
    String appleAuthUrl;

    @Value("${apple.auth.redirect-uri}")
    String redirectUri;

    @Value("${apple.auth.client-id}")
    String appleClientId;

    @Value("${apple.auth.team-id}")
    String appleTeamId;

    @Value("${apple.auth.key-id}")
    String appleKeyId;

    @Value("${apple.auth.key-path}")
    String appleKeyPath;

    private final AuthService authService;

    /** 소셜로그인 후 리다이렉트 - ! **/
    @ApiOperation(value = "로그인 완료후 리다이렉트")
    @GetMapping("/auth/success")
    @ResponseBody
    public BaseResponse<OAuthResponse> jwtResponse(@RequestParam Long userId, @RequestParam("grantType") String grantType, @RequestParam("accessToken") String accessToken, @RequestParam("refreshToken") String refreshToken, @RequestParam("accessTokenExpiresIn") Long accessTokenExpiresIn,  @RequestParam("role") String role) {
        return new BaseResponse<OAuthResponse>(new OAuthResponse(userId, accessToken, refreshToken, accessTokenExpiresIn, role));
    }

    @ApiOperation(value = "토큰 갱신")
    @PostMapping("/auth/token")
    public BaseResponse<TokenDto> reissueToken(@RequestParam String accessToken, @RequestParam String refreshToken) throws BaseException {
        return new BaseResponse<>(authService.reissueToken(accessToken, refreshToken));
    }

    @ApiOperation(value = "애플 로그인")
    @PostMapping("/auth/apple-login")
    public BaseResponse<LoginRes> appleLogin(@RequestBody AppleLoginReq appleLoginReq) throws JsonProcessingException {
        return new BaseResponse<>(authService.appleLogin1(appleLoginReq));
    }

    // 애플 로그인 호출
    @RequestMapping(value = "/auth/getAppleAuthUrl")
    public @ResponseBody String getAppleAuthUrl(HttpServletRequest request) throws Exception {

        String reqUrl = appleAuthUrl + "/auth/authorize?client_id=" + appleClientId + "&redirect_uri=" + redirectUri
                + "&response_type=code id_token&response_mode=form_post";

        return reqUrl;
    }

    // 애플 연동정보 조회
//    @RequestMapping(value = "/login/oauth2/apple", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @RequestMapping(value = "/login/oauth2/apple")
    public BaseResponse<LoginRes> oauthApple(String user, HttpServletRequest request, @RequestParam(value = "code") String code, HttpServletResponse response) throws Exception, BaseException {

        String clientId = appleClientId;
        String clientSecret = AppleLoginUtil.createClientSecret(appleTeamId, appleClientId, appleKeyId, appleKeyPath, appleAuthUrl);

        //토큰 검증 및 발급
        String reqUrl = appleAuthUrl + "/auth/token";
        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("client_id", clientId);
        tokenRequest.put("client_secret", clientSecret);
        tokenRequest.put("code", code);
        tokenRequest.put("grant_type", "authorization_code");
        String apiResponse = AppleLoginUtil.doPost(reqUrl, tokenRequest);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JSONObject tokenResponse = objectMapper.readValue(apiResponse, JSONObject.class);

        AppleUserInfo appleUserInfo = new AppleUserInfo();

        if(user != null) {
            System.out.println("유저 있음 !!!!!!!!!!!!!!!!!!!");
            appleUserInfo = objectMapper.readValue(user, AppleUserInfo.class);
            System.out.println("user email " + appleUserInfo.getEmail() + "!!!!!!!!!!!!!!!!!!!!");
            System.out.println(appleUserInfo.getName().getFirstName());
            System.out.println(appleUserInfo.getName().getLastName());
        } else {
            System.out.println("유저 정보 못 읽어옴 !!!!!!!!!!!!!1");
        }

        // 애플 정보조회 성공
        if (tokenResponse.get("error") == null ) {
            JSONObject payload = AppleLoginUtil.decodeFromIdToken(tokenResponse.getString("id_token"));

            System.out.println(payload + "!!!!!!!!!!!!!!!!!!!!!!");

//            String email = payload.getString("email");
//            System.out.println("email + " + email + "!!!!!!!!!!!!!!!!!!!!!!!!!!");

            //  회원 고유 식별자
            String appleUniqueNo = payload.getString("sub");
            System.out.println("appleUniqueNo + " + appleUniqueNo + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!");


            /**
             TO DO : 리턴받은 appleUniqueNo 해당하는 회원정보 조회 후 로그인 처리 후 메인으로 이동
             **/

//            AppleLoginReq appleLoginReq = new AppleLoginReq(appleUniqueNo, email);

            return new BaseResponse<LoginRes>(authService.appleLogin(appleUniqueNo));

            // 애플 정보조회 실패
        } else {
            throw new BaseException(BaseResponseStatus.APPLE_FAIL);
        }

    }

    @ApiOperation("회원 삭제")
    @DeleteMapping("/user/deletion")
    public BaseResponse<Long> deleteUser() throws BaseException {
        return new BaseResponse<Long>(authService.deleteUser());
    }
}
