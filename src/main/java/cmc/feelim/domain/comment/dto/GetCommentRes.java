package cmc.feelim.domain.comment.dto;

import cmc.feelim.domain.comment.Comment;
import cmc.feelim.domain.post.dto.GetPostRes;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class GetCommentRes {
    private Long id;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;

    public GetCommentRes(Comment comment) {
        this.id = comment.getId();
        this.nickname = comment.getUser().getNickname();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }
}
