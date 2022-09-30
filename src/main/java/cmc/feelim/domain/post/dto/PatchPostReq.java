package cmc.feelim.domain.post.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class PatchPostReq {
    private String title;
    private String content;
    private List<MultipartFile> images;
}
