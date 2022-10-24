package cmc.feelim.domain.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostReviewReq {
    private double star;
    private String content;
}
