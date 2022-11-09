package cmc.feelim.config.auth.dto;

import lombok.Data;

@Data
public class AppleLoginReq {
    private String token;
    private String email;

    public AppleLoginReq(String token, String email) {
        this.token = token;
        this.email = email;
    }
}
