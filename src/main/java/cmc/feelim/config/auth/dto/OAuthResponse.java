package cmc.feelim.config.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuthResponse {
    private Long userId;
    private String jwt;
}
