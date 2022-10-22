package cmc.feelim.domain.image;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageDto {
    private String url;
    private String fileName;
    private String fileType;

    public ImageDto(Image image) {
        this.url = image.getUrl();
        this.fileName = image.getOriginFileName();
        this.fileType = image.getFileType();
    }
}
