package cmc.feelim.domain.post.dto;

import cmc.feelim.domain.post.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class PostPostingReq {
    @NotNull
    private Category category;
    @NotNull
    private String title;
    private String content;
    private List<MultipartFile> images;
}
