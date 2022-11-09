package cmc.feelim.domain.image;

import cmc.feelim.domain.BaseEntity;
import cmc.feelim.domain.archive.Archive;
import cmc.feelim.domain.film.Film;
import cmc.feelim.domain.laboratory.ProcessingLaboratory;
import cmc.feelim.domain.post.Post;
import cmc.feelim.domain.review.Review;
import cmc.feelim.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    private String originFileName;

    private Long fileSize;

    private String fileType;

    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "film_id")
    private Film film;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processing_laboratory_id")
    private ProcessingLaboratory laboratory;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "processing_laboratory_id")
    private ProcessingLaboratory labProfile;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "processing_laboratory_id")
    private ProcessingLaboratory labBackground;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archive_id")
    private Archive archive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public Image(String url) {
        this.url = url;
    }

    public Image(String fileName, String fileUrl, long size, String fileType) {
        this.originFileName = fileName;
        this.url = fileUrl;
        this.fileSize = size;
        this.fileType = fileType;
    }

    public void updatePost(Post post) {
        this.post = post;
    }
    public void updateLaboratory(ProcessingLaboratory laboratory) {
        this.laboratory = laboratory;
    }

    public void updateLabProfile(ProcessingLaboratory laboratory) {
        this.labProfile = laboratory;
    }

    public void updateLabBackground(ProcessingLaboratory laboratory) {
        this.labBackground = laboratory;
    }

    public void updateUser(User user) {
        this.user = user;
    }

    public ImageDto toDto() {
        return new ImageDto(this);
    }

    public void updateReview(Review review) {
        this.review = review;
    }
}
