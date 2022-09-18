package cmc.feelim.domain.film;

import cmc.feelim.domain.BaseEntity;
import cmc.feelim.domain.image.Image;
import cmc.feelim.domain.user.User;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NotNull
public class Film extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Column(length = 30)
    private String title;

    private String content;

    private boolean privacy;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private Camera camera;

    @Enumerated(EnumType.STRING)
    private Type filmType;

    @Column(length = 100)
    private String record;

    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();
}