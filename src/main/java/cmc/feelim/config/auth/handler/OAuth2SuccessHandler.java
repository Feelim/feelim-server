package cmc.feelim.config.auth.handler;

import cmc.feelim.config.auth.dto.SessionUser;
import cmc.feelim.config.auth.dto.TokenDto;
import cmc.feelim.config.security.JwtTokenProvider;
import cmc.feelim.domain.user.User;
import cmc.feelim.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        HttpSession session = request.getSession();
        SessionUser sessionMember = (SessionUser) session.getAttribute("user");
        Optional<User> user = userRepository.findByEmail(sessionMember.getEmail());

        Authentication auth = new UsernamePasswordAuthenticationToken(sessionMember.getId(), "", authentication.getAuthorities());

//        String jwt = jwtTokenProvider.createSocialJwt(user.get().getEmail());
        TokenDto token = jwtTokenProvider.createSocialJwt(user.get().getEmail());

        String targetUrl = UriComponentsBuilder.fromUriString("/auth/success")
                .queryParam("userId", user.get().getId())
                .queryParam("grantType", token.getGrantType())
                .queryParam("accessToken", token.getAccessToken())
                .queryParam("refreshToken", token.getRefreshToken())
                .queryParam("accessTokenExpiresIn", token.getAccessTokenExpiresIn())
                .queryParam("role", sessionMember.getRole())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request,response,targetUrl);

    }

}
