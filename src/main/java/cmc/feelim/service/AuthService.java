package cmc.feelim.service;

import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponseStatus;
import cmc.feelim.domain.user.User;
import cmc.feelim.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

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
}
