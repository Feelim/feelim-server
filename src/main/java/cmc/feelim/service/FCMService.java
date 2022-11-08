package cmc.feelim.service;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.domain.notification.dto.PostFCMReq;
import cmc.feelim.domain.user.User;
import cmc.feelim.domain.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FCMService {

    @Value("${firebase.project-id}")
    String fireBaseId;

    @Value("${firebase.key-path}")
    String fcmKeyPath;

    private final ObjectMapper objectMapper; //jackson을 사용해 dto 객체를 String으로 변환

    private final UserRepository userRepository;

    private final UserService userService;
    private final AuthService authService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void sendMessageToUser(String infoMessage, String bodyMessage, User user) throws IOException {

        String info = user.getNickname() + "님, " + infoMessage;

        String message = makeMessage(user.getFcmToken(), info, bodyMessage);

        System.out.println(message);

        setBuild(message);

    }

    public void setBuild(String message) throws IOException {
        //okhttp 객체 생성
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message,
                MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/v1/projects/"+ fireBaseId +"/messages:send")
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer "+getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .build();

        Response response = okHttpClient.newCall(request)
                .execute();

        logger.error(response.toString());
    }

    @Transactional
    public String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
        //빌더 패턴을 이용해 fcm Request 객체 생성
        PostFCMReq fcm = PostFCMReq.builder()
                .message(PostFCMReq.Message.builder()
                        .token(targetToken)
                        .notification(PostFCMReq.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()
                )
                .validate_only(false)
                .build();

        // dto를 string으로 반환
        return objectMapper.writeValueAsString(fcm);
    }


    /** FCM 서버에 push 요청시 필요한 서버측 토큰 발급 **/
    public String getAccessToken() throws IOException {

        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource(fcmKeyPath).getInputStream())
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();

        /**
         * 서버 키 확인
         * String token = googleCredentials.getAccessToken().getTokenValue();
         * logger.error(token);
         * return token;
         **/

        //google credentials 객체의 토큰값을 가져옴
        return googleCredentials.getAccessToken().getTokenValue();
    }

    /** fcm 토큰 정보 업데이트 **/
    public User addFcmToken(String fcmToken) throws BaseException {
        User user = authService.getUserFromAuth();
        user.addFcmToken(fcmToken);
        userRepository.save(user);

        return user;
    }

}
