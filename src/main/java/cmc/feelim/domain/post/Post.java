package cmc.feelim.domain.post;

import cmc.feelim.domain.BaseEntity;
import cmc.feelim.domain.comment.Comment;
import cmc.feelim.domain.image.Image;
import cmc.feelim.domain.post.dto.PatchPostReq;
import cmc.feelim.domain.post.dto.PostPostingReq;
import cmc.feelim.domain.report.Report;
import cmc.feelim.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Where(clause = "status='ACTIVE'")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Category category;

    @NotNull
    @Column(length = 30)
    private String title;

    @Column(length = 5000)
    private String content;

    private boolean recommendation = false;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Report> reports = new ArrayList<>();

    public Post(PostPostingReq postPostingReq, User user) {
        this.user = user;
        this.category = postPostingReq.getCategory();
        this.title = postPostingReq.getTitle();
        this.content = postPostingReq.getContent();
    }

    public void updateImage(List<Image> images) {
        this.images = images;
    }

    public void updatePost(PatchPostReq patchPostReq) {
        this.category = patchPostReq.getCategory();
        this.title = patchPostReq.getTitle();
        this.content = patchPostReq.getContent();
    }

    public Long updateRecommendation(boolean recommendation) {
        this.recommendation = recommendation;
        return this.id;
    }
}
