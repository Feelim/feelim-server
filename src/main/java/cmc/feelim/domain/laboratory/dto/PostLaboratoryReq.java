package cmc.feelim.domain.laboratory.dto;

import cmc.feelim.domain.Address;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Embedded;
import java.util.List;

@Getter
@Setter
public class PostLaboratoryReq {
    private String name;

    private String introduction;

    private String phone;

    private String homepage;

    private String province;

    private String city;

    private String street;

    private double x;

    private double y;

    private List<MultipartFile> bills;
}
