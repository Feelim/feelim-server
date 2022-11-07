package cmc.feelim.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "Refresh";


    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //헤더에서 JWT 받아옴
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

        logger.error("resolveToken: " + token);

        //유효한 토큰인지 확인
        if( token != null && token.startsWith("Bearer ")){
            val jwtToken = token.substring(7);

            if(jwtTokenProvider.validateToken(jwtToken) == JwtTokenProvider.JwtCode.ACCESS){
                //토큰이 유효하면 토큰으로부터 유저 정보를 받아옴
                Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken);
                //SecurityContext에 Authentication 객체 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.error("mmmmmmmm: " + token);

            } else if (jwtTokenProvider.validateToken(jwtToken) == JwtTokenProvider.JwtCode.EXPIRED) {
                String refresh = resolveToken((HttpServletRequest) request, REFRESH_HEADER);
                // refresh token을 확인해서 재발급해준다
                if(refresh != null && jwtTokenProvider.validateToken(refresh) == JwtTokenProvider.JwtCode.ACCESS){
                    String newRefresh = jwtTokenProvider.reissueRefreshToken(refresh);
                    if(newRefresh != null){
                        response.setHeader(REFRESH_HEADER, "Bearer "+newRefresh);

                        // access token 생성
                        Authentication authentication = jwtTokenProvider.getAuthentication(refresh);
                        response.setHeader(AUTHORIZATION_HEADER, "Bearer "+jwtTokenProvider.createAccessToken(authentication));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.info("reissue refresh Token & access Token");
                    }
                }
            } else {
                log.debug("JWT Token does not begin with Bearer String");
                logger.error("validate: " + token);
            }

            logger.error("checkToken: " + token);

        }
            chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request, String header) {
        String bearerToken = request.getHeader(header);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String resolveToken(HttpServletRequest request){
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

}
