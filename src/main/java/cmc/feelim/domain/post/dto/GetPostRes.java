package cmc.feelim.domain.post.dto;

import cmc.feelim.domain.comment.dto.GetCommentRes;
import cmc.feelim.domain.image.ImageDto;
import cmc.feelim.domain.post.Category;
import cmc.feelim.domain.post.Post;
import cmc.feelim.domain.user.dto.WriterDto;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    private WriterDto writer;
    private String title;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    //이미지 url, 파일명, 파일 타입으로 구성
    private List<ImageDto> images = new ArrayList<>();
    private List<GetCommentRes> comment;

    public GetPostRes(Post post) {
        this.id = post.getId();
        this.category = post.getCategory();
        this.writer = new WriterDto(post.getUser());
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.comment = post.getComments().stream()
                        .map(GetCommentRes::new)
                                .collect(Collectors.toList());
        //image 처리
        post.getImages().stream()
                .forEach(image -> {images.add(image.toDto());});


    }
}
