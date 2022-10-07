package cmc.feelim.domain.laboratory.dto;

import cmc.feelim.domain.Address;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Embedded;
import java.awt.*;
import java.util.List;

@Getter
@Setter
public class PostLaboratoryReq {
    private String name;

    private String introduction;

    private String phone;

    private String homepage;

    private Address address;

    private Point point;

    private List<MultipartFile> bills;
}
