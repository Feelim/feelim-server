package cmc.feelim.domain.review.dto;

import cmc.feelim.domain.review.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetReviewRes {
    private Long id;
    private String nickname;
    private String content;
    private double star;

    public GetReviewRes(Review review) {
        this.id = review.getId();
        this.nickname = review.getUser().getNickname();
        this.content = review.getContent();
        this.star = review.getStar();
    }
}
