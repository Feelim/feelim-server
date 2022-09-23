package cmc.feelim.config.security;

import cmc.feelim.domain.user.Role;

import cmc.feelim.domain.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private static final String ROLE_PREFIX = "ROLE_";

    private final User user;

    //사용자 권한을 콜렉션 형태로 반환
    //클래스 자료형은 GrantedAuthority를 구현해야 함
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = user.getRole();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(ROLE_PREFIX + role.toString());
        Collection<GrantedAuthority> authorities = new ArrayList<>(); //여러개의 권한을 가질 수 있어서 List
        authorities.add(authority);

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPwd();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    //계정이 만료되지 않았는지 리턴(true: 만료 안 됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 잠겨있는지 리턴(true: 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호가 만료되지 않았는지 리턴(true: 만료 안됨)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 활성화(사용가능)인지 리턴(true: 활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getName(){
        return user.getName();
    }
}
