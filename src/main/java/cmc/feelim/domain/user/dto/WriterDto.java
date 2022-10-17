package cmc.feelim.domain.user.dto;

import cmc.feelim.domain.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WriterDto {
    private Long id;
    private String nickname;

    public WriterDto(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
    }
}
