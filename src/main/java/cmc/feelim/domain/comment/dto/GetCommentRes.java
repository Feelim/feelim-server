package cmc.feelim.domain.comment.dto;

import cmc.feelim.domain.comment.Comment;
import cmc.feelim.domain.post.dto.GetPostRes;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class GetCommentRes {
    private Long id;
    private Long userId;
    private String picture;
    private String nickname;
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")

    private LocalDateTime createdAt;

    public GetCommentRes(Comment comment) {
        this.id = comment.getId();
        this.userId = comment.getUser().getId();
        this.nickname = comment.getUser().getNickname();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();

        int index = comment.getUser().getImages().size() - 1;

        if(comment.getUser().getImages() != null) {
            this.picture = comment.getUser().getImages().get(index).getUrl();
        }
    }
}
