package cmc.feelim.domain.review.dto;

import cmc.feelim.domain.image.Image;
import cmc.feelim.domain.image.ImageDto;
import cmc.feelim.domain.review.Review;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GetReviewRes {
    private Long reviewId;
    private Long userId;
    private String nickname;
    private String content;
    private double star;
    private List<ImageDto> images = new ArrayList<>();

    public GetReviewRes(Review review) {
        this.reviewId = review.getId();
        this.userId = review.getUser().getId();
        this.nickname = review.getUser().getNickname();
        this.content = review.getContent();
        this.star = review.getStar();
        review.getImages().stream()
                .forEach(image -> {images.add(image.toDto());});
    }
}
