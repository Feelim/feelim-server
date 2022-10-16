package cmc.feelim.domain.user.dto;

import cmc.feelim.domain.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetProfileRes {
    private Long id;
    private String image;
    private String nickname;
    private String introduction;

    public GetProfileRes(User user) {

        if(!user.getImages().isEmpty()) {
            int index = user.getImages().size() - 1;
            this.image = user.getImages().get(index).getUrl();
        }

        this.id = user.getId();

        this.nickname = user.getNickname();
        this.introduction = user.getIntroduction();
    }
}
