package cmc.feelim.domain.post.dto;

import cmc.feelim.domain.post.Category;
import cmc.feelim.domain.post.Post;
import cmc.feelim.domain.user.dto.WriterDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GetRecommendationsRes {
    private Long id;
    private Category category;
    private WriterDto writer;
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime time;
    private int commentNum;
    private String image;

    public GetRecommendationsRes(Post post) {
        this.id = post.getId();
        this.category = post.getCategory();
        this.writer = new WriterDto(post.getUser());
        this.title = post.getTitle();
        this.time = post.getCreatedAt();
        this.commentNum = post.getComments().size();
        if(!post.getImages().isEmpty()) {
            this.image = post.getImages().get(0).getUrl();
        }
    }
}
