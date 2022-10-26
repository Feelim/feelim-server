package cmc.feelim.domain.review;

import cmc.feelim.domain.BaseEntity;
import cmc.feelim.domain.image.Image;
import cmc.feelim.domain.laboratory.ProcessingLaboratory;
import cmc.feelim.domain.review.dto.PatchReviewReq;
import cmc.feelim.domain.review.dto.PostReviewReq;
import cmc.feelim.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NotNull
@NoArgsConstructor
@Where(clause = "status='ACTIVE'")
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

    public Review(ProcessingLaboratory laboratory, User user, PostReviewReq postReviewReq) {
        this.user = user;
        this.laboratory = laboratory;
        this.star = postReviewReq.getStar();
        this.content = postReviewReq.getContent();
    }

    public Long update(PatchReviewReq patchReviewReq) {
        this.star = patchReviewReq.getStar();
        this.content = patchReviewReq.getContent();
        return this.id;
    }
}
