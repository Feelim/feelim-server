package cmc.feelim.service;

import cmc.feelim.config.auth.dto.TokenDto;
import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponseStatus;
import cmc.feelim.config.security.JwtTokenProvider;
import cmc.feelim.domain.token.RefreshToken;
import cmc.feelim.domain.token.RefreshTokenRepository;
import cmc.feelim.domain.user.User;
import cmc.feelim.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
                .orElseThrow(() -> {
                    try {
                        throw new BaseException(INVALID_EMAIL);
                    } catch (BaseException e) {
                        throw new RuntimeException(e);
                    }
                });

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
}
