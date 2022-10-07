package cmc.feelim.domain.laboratory;

import cmc.feelim.domain.Address;
import cmc.feelim.domain.BaseEntity;
import cmc.feelim.domain.image.Image;
import cmc.feelim.domain.laboratory.dto.PostLaboratoryReq;
import cmc.feelim.domain.review.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Where(clause = "status='ACTIVE'")
public class ProcessingLaboratory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "laboratory_id")
    private Long id;

    @NotNull
    @Column(length = 20)
    private String name;

    private String introduction;

    private String phone;

    private String homepage;

    @Embedded
    private Address address;

    //위도, 경도
    @NotNull
    private Point point;

    @OneToMany(mappedBy = "laboratory", cascade = CascadeType.ALL)
    private List<Image> bills = new ArrayList<>();

    @OneToMany(mappedBy = "laboratory", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    public ProcessingLaboratory(PostLaboratoryReq postLaboratoryReq) {
        this.name = postLaboratoryReq.getName();
        this.introduction = postLaboratoryReq.getIntroduction();
        this.phone = postLaboratoryReq.getPhone();
        this.homepage = postLaboratoryReq.getHomepage();
        this.address = postLaboratoryReq.getAddress();
        this.point = postLaboratoryReq.getPoint();
    }

    public void updateImage(List<Image> images) {
        this.bills = images;
    }
}
