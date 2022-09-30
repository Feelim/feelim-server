package cmc.feelim.domain.post.dto;

import cmc.feelim.domain.comment.dto.GetCommentRes;
import cmc.feelim.domain.post.Category;
import cmc.feelim.domain.post.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class GetPostRes {
    private Long id;
    private Category category;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private List<String> images = new ArrayList<>();
    private List<GetCommentRes> comment;

    public GetPostRes(Post post) {
        this.id = post.getId();
        this.category = post.getCategory();
        this.nickname = post.getUser().getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.comment = post.getComments().stream()
                        .map(GetCommentRes::new)
                                .collect(Collectors.toList());
        //image 처리
        post.getImages().stream()
                .forEach(image -> {images.add(image.getUrl());});
    }
}
