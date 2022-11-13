package cmc.feelim.config.security;

import cmc.feelim.config.auth.dto.TokenDto;
import cmc.feelim.config.exception.BaseException;
import cmc.feelim.config.exception.BaseResponseStatus;
import cmc.feelim.domain.token.RefreshToken;
import cmc.feelim.domain.token.RefreshTokenRepository;
import cmc.feelim.domain.user.Role;
import cmc.feelim.domain.user.User;
import cmc.feelim.domain.user.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    //토큰 유효 시간 30분
    @Value("${jwt.access-expired}")
    private long tokenValidTime;
    private static final String BEARER_TYPE = "Bearer ";
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

    private final UserDetailsServiceImpl userDetailsService;

    //객체 초기화, secretKey를 Base64로 인코딩
    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String generateToken(UserDetailsImpl userDetails){

        Map<String, Object> claims = new HashMap<>();

        //역할 가져옴
        val isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_" + Role.ADMIN));
        if(isAdmin){
            claims.put("role", "admin");
        } else{
            claims.put("role", "user");
        }

        /** username == user.getIdentity **/
        val myName = userDetails.getUsername();

        System.out.println("userName from userDetails " + userDetails.getUsername() + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        claims.put("myName", myName);

        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidTime * 1000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    //JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //토큰에서 회원 정보 추출
    public String getUserPk(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    //Request의 Header에서 token 값을 가져옴.
    public String resolveToken(HttpServletRequest request){
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    //토큰의 유효성 + 만료 일자 확인
    public JwtCode validateToken(String jwtToken){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
//            return !claims.getBody().getExpiration().before(new Date());
            return JwtCode.ACCESS;
        } catch (ExpiredJwtException e){
            return JwtCode.EXPIRED;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("JwtException: {}", e);
        }
        return JwtCode.DENIED;
    }

    @Transactional
    public TokenDto createSocialJwt(String username) {

        long now = (new Date()).getTime();

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "user");
        val myName = username;
        claims.put("myName", myName);

        String accessToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidTime * 1000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        Optional<User> user = userRepository.findByEmail(username);

        RefreshToken refresh = new RefreshToken(user.get(), refreshToken);

        if(refreshTokenRepository.findByUser(user.get()).isPresent()){
            Optional<RefreshToken> refreshTokenByUser = refreshTokenRepository.findByUser(user.get());
            refreshTokenByUser.get().updateToken(refresh.getToken());
        } else {
            refreshTokenRepository.save(refresh);
        }

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(new Date(System.currentTimeMillis() + tokenValidTime * 1000).getTime())
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public String reissueRefreshToken(String refreshToken) throws RuntimeException{
        // refresh token을 디비의 그것과 비교해보기
        Authentication authentication = getAuthentication(refreshToken);
        RefreshToken findRefreshToken = (RefreshToken) refreshTokenRepository.findByUserId(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("userId : " + authentication.getName() + " was not found"));

        if(findRefreshToken.getToken().equals(refreshToken)){
            // 새로운거 생성
            String newRefreshToken = createRefreshToken(authentication);
            findRefreshToken.updateToken(newRefreshToken);
            return newRefreshToken;
        }
        else {
            log.info("refresh 토큰이 일치하지 않습니다. ");
            return null;
        }
    }

    private String createRefreshToken(Authentication authentication){

        long now = (new Date()).getTime();

        return Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createAccessToken(Authentication authentication) {

        long now = (new Date()).getTime();

        String username = authentication.getName();

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "user");
        val myName = username;
        claims.put("myName", myName);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidTime * 1000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public static enum JwtCode{
        DENIED,
        ACCESS,
        EXPIRED;
    }
}
