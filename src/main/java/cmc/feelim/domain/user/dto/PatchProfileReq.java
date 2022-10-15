package cmc.feelim.domain.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;

@Getter
@Setter
public class PatchProfileReq {
    @Size(min = 1, max = 8)
    private String nickname;
    @Size(max = 100)
    private String introduction;
    private MultipartFile image;
}
