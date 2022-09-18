package cmc.feelim.domain.laboratory;

import cmc.feelim.domain.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.awt.*;

@Entity
@Getter
@NoArgsConstructor
public class ProcessingLaboratory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "laboratory_id")
    private Long id;

    @NotNull
    @Column(length = 20)
    private String name;

    @NotNull
    @Column(length = 100)
    private String address;

    //위도, 경도
    @NotNull
    private Point point;
}
