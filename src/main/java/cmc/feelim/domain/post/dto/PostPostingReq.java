package cmc.feelim.domain.post.dto;

import cmc.feelim.domain.post.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class PostPostingReq {
    private Category category;
    private String title;
    private String content;
    private List<MultipartFile> images;
}
