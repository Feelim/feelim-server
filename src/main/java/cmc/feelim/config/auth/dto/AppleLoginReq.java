package cmc.feelim.config.auth.dto;

import lombok.Data;

@Data
public class AppleLoginReq {
    private String token;
    public AppleLoginReq(String token) {
        this.token = token;
    }
}
