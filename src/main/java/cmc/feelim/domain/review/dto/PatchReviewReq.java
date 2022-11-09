package cmc.feelim.domain.review.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PatchReviewReq {
    @NotNull
    private double star;
    @NotNull
    private String content;
    private List<MultipartFile> images = new ArrayList<>();
}
