package cmc.feelim.service;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.domain.user.User;
import cmc.feelim.domain.user.UserRepository;
import cmc.feelim.domain.user.dto.GetProfileRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;

    /** 유저 프로필 반환 **/
    public GetProfileRes getProfile() throws BaseException {
        User user = authService.getUserFromAuth();
        GetProfileRes profile = new GetProfileRes(user);
        return profile;
    }
}
