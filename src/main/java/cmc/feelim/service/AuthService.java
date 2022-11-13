package cmc.feelim.service;

import cmc.feelim.config.auth.dto.AppleLoginReq;
import cmc.feelim.config.auth.dto.LoginRes;
import cmc.feelim.config.auth.dto.TokenDto;
import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponseStatus;
import cmc.feelim.config.security.JwtTokenProvider;
import cmc.feelim.domain.token.RefreshToken;
import cmc.feelim.domain.token.RefreshTokenRepository;
import cmc.feelim.domain.user.User;
import cmc.feelim.domain.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;

import static cmc.feelim.config.exception.BaseResponseStatus.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public User getUserFromAuth() throws BaseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) authentication.getAuthorities();
        String name = authentication.getName();
        Optional<User> user = userRepository.findByEmail(name);

        if(!user.isPresent()) {
            throw new BaseException(BaseResponseStatus.INVALID_USER_JWT);
        }

        return user.get();
    }

    @Transactional
    public TokenDto reissueToken(String accessToken, String refreshToken) throws BaseException {
        // Refresh Token 검증
        if (tokenProvider.validateToken(refreshToken) == JwtTokenProvider.JwtCode.DENIED) {
            throw new BaseException(INVALID_REFRESH_TOKEN);
        }

        //Access Token에서 Member Id 가져오기
        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new BaseException(INVALID_EMAIL));

        //저장소에서 memberID 기반으로 refresh token 값 가져오기
        RefreshToken token = refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new BaseException(USER_LOGOUT));

        if (!token.getToken().equals(refreshToken)) {
            throw new BaseException(CHECK_REFRESH_TOKEN);
        }

        // 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.createSocialJwt(authentication.getName());

        // 저장소 정보 업데이트
        token.updateToken(tokenDto.getRefreshToken());

        return tokenDto;
    }

    @Transactional
    public LoginRes appleLogin(String appleUniqueNo) throws JsonProcessingException {
        Random random = new Random();
        String numStr = "";

        for (int j = 0; j < 3; j++) {
            String ran = Integer.toString(random.nextInt(10));
            numStr += ran;
        }

        String email = RandomStringUtils.randomAlphanumeric(5).toLowerCase(Locale.ROOT) + numStr + "@apple.com";
        System.out.println(email + "!!!!!!!!!!!!!!");
        String name = "apple" + numStr;
        String nickname = RandomStringUtils.randomAlphanumeric(8);
        String pwd = RandomStringUtils.randomAlphanumeric(45);

        // 로그인
        User user = userRepository.findByAppleUniqueNo(appleUniqueNo)
                .orElse(User.createApple(email, name, nickname, pwd, appleUniqueNo));

        userRepository.save(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(user.getId(), "", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        System.out.println("auth.getName: " + auth.getName() + "!!!!!!!!!!!!!!!!!!!!!!!!!");
        TokenDto token = tokenProvider.createSocialJwt(user.getEmail());

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(token.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return LoginRes.builder()
                .grantType(token.getGrantType())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .accessTokenExpiresIn(token.getAccessTokenExpiresIn())
                .role(user.getRole().name())
                .build();
    }

    @Transactional
    public LoginRes appleLogin1(AppleLoginReq appleLoginReq) throws JsonProcessingException {
        //토큰 복호화
        int i = appleLoginReq.getToken().lastIndexOf('.');
        String withoutSignature = appleLoginReq.getToken().substring(0, i+1);
        Jwt<Header, Claims> untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);

        Random random = new Random();
        String numStr = "";

        for (int j = 0; j < 3; j++) {
            String ran = Integer.toString(random.nextInt(10));
            numStr += ran;
        }

        String email = (String) untrusted.getBody().get("email");
//        String email = appleLoginReq.getEmail();
        String name = "apple" + numStr;
        String nickname = RandomStringUtils.randomAlphanumeric(8);
        String pwd = RandomStringUtils.randomAlphanumeric(45);

        // 로그인
        User user = userRepository.findByEmail(email)
                .orElse(User.create(email, name, nickname, pwd));

        userRepository.save(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(user.getId(), "", Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));

        TokenDto token = tokenProvider.createSocialJwt(auth.getName());

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(token.getRefreshToken())
                .build();

        if(refreshTokenRepository.findByUser(user).isPresent()){
            Optional<RefreshToken> refreshTokenByUser = refreshTokenRepository.findByUser(user);
            refreshTokenByUser.get().updateToken(refreshToken.getToken());
        } else {
            refreshTokenRepository.save(refreshToken);
        }

        return LoginRes.builder()
                .grantType(token.getGrantType())
                .accessToken(token.getAccessToken())
                .refreshToken(refreshToken.getToken())
                .accessTokenExpiresIn(token.getAccessTokenExpiresIn())
                .role(user.getRole().name())
                .build();
    }

    @Transactional
    public Long deleteUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        userRepository.delete(user.get());
        return user.get().getId();
    }
}
