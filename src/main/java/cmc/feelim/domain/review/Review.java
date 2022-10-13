package cmc.feelim.domain.review;

import cmc.feelim.domain.BaseEntity;
import cmc.feelim.domain.image.Image;
import cmc.feelim.domain.laboratory.ProcessingLaboratory;
import cmc.feelim.domain.user.User;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NotNull
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processing_laboratory_id")
    private ProcessingLaboratory laboratory;

    private double star;

    private String content;

}
