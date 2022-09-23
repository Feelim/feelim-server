package cmc.feelim.config;

import cmc.feelim.config.auth.handler.OAuth2SuccessHandler;
import cmc.feelim.config.auth.service.CustomOAuth2UserService;
import cmc.feelim.config.security.JwtAuthenticationFilter;
import cmc.feelim.config.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler successHandler;

    //비밀번호 암호화를 위한 Bean
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    // 정적인 파일에 대한 요청들
    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/v2/api-docs",
            "/v3/api-docs/**",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/file/**",
            "/image/**",
            "/swagger/**",
            "/swagger-ui/**",
            // other public endpoints of your API may be appended to this array
            "/h2/**"
    };

    /*
    web FilterChainProxy 생성 필터
     */
    @Override
    public void configure(WebSecurity web) throws Exception{
        //Spring Security 가 인증을 무시할 경로 설정
//        web.ignoring().antMatchers("/css/**", "/img/**", "/js/**", "/lib/**", "/vendor/**");
        web.ignoring().antMatchers(AUTH_WHITELIST);
    }


    /*
    Security 설정
    @param http HTTP 요청에 대한 보안 구성
    @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception{

        http.cors().disable()
                .cors().disable()
                .csrf().disable().authorizeRequests()
                /** login 없이 접근 허용 하는 url **/
                .antMatchers("/**").permitAll()
                .antMatchers("/user/**").permitAll()
                .antMatchers("/auth/**").permitAll()
                /** admin의 경우 ADMIN 권한이 있는 사용자만 접근 가능 **/
                .antMatchers("/admin").hasRole("ADMIN")
                /** 그 외 모든 요청은 인증과정 필요 **/
                .anyRequest().authenticated()
                .and()
                /** 로그인 페이지 생성 **/
                    .oauth2Login()
                        .successHandler(successHandler)
                    /** 네이버 USER INFO의 응답을 처리하기 위한 설정 **/
                    .userInfoEndpoint()
                        .userService(customOAuth2UserService)
                    .and()
                .and()
                /** 토큰 기반 인증이라 session 사용 x **/
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                /** JwtAuthenticationFilter는 UsernamePasswordAuthenticationFilter 전에 넣음**/
                    .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
    }
}
