package cmc.feelim.domain.laboratory;

import cmc.feelim.domain.Address;
import cmc.feelim.domain.BaseEntity;
import cmc.feelim.domain.image.Image;
import cmc.feelim.domain.laboratory.dto.PostLaboratoryReq;
import cmc.feelim.domain.order.Order;
import cmc.feelim.domain.review.Review;
import cmc.feelim.utils.GeomUtil;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @Column(columnDefinition = "blob")
    @JsonManagedReference
    private Point point;

    @OneToMany(mappedBy = "laboratory", cascade = CascadeType.ALL)
    private List<Image> bills = new ArrayList<>();

    @OneToMany(mappedBy = "laboratory", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "laboratory", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "labProfile", cascade = CascadeType.ALL)
    private List<Image> profiles = new ArrayList<>();

    @OneToMany(mappedBy = "labBackground", cascade = CascadeType.ALL)
    private List<Image> backgrounds = new ArrayList<>();

    public ProcessingLaboratory(PostLaboratoryReq postLaboratoryReq) {
        this.name = postLaboratoryReq.getName();
        this.introduction = postLaboratoryReq.getIntroduction();
        this.phone = postLaboratoryReq.getPhone();
        this.homepage = postLaboratoryReq.getHomepage();
        this.address = new Address(postLaboratoryReq.getProvince(), postLaboratoryReq.getCity(), postLaboratoryReq.getStreet());
        this.point = GeomUtil.createPoint(postLaboratoryReq.getX(), postLaboratoryReq.getY());
    }

    public void updateImage(List<Image> images) {
        this.bills = images;
    }

    public void updateProfile(Image profile) {
        this.profiles.add(profile);
    }

    public void updateBackground(Image background) {
        this.backgrounds.add(background);
    }
}
