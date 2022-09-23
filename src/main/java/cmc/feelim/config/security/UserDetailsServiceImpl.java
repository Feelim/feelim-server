package cmc.feelim.config.security;

import cmc.feelim.domain.user.User;
import cmc.feelim.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetailsImpl loadUserByUsername(String email) throws UsernameNotFoundException {
                User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("등록되지 않은 사용자입니다."));
        return new UserDetailsImpl(user);
    }
}
