package cmc.feelim.config.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class LoginRes {
    private String grantType;
    private String accessToken;
    private Long accessTokenExpiresIn;
    private String refreshToken;
    private String role;

    @Builder
    public LoginRes(String grantType, String accessToken, Long accessTokenExpiresIn, String refreshToken, String registerStatus, String role) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
        this.refreshToken = refreshToken;
        this.role = role;
    }
}
