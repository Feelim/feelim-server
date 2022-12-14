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
public class GetPostsRes {
    private Long id;
    private Category category;
    private WriterDto writer;
    private String title;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime time;
    private int commentNum;

    public GetPostsRes(Post post) {
        this.id = post.getId();
        this.category = post.getCategory();
        this.writer = new WriterDto(post.getUser());
        this.title = post.getTitle();
        this.content = post.getContent();
        this.time = post.getCreatedAt();
        this.commentNum = post.getComments().size();
    }
}
