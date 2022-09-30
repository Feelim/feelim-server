package cmc.feelim.domain.image;

import cmc.feelim.domain.BaseEntity;
import cmc.feelim.domain.archive.Archive;
import cmc.feelim.domain.film.Film;
import cmc.feelim.domain.post.Post;
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

    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "film_id")
    private Film film;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archive_id")
    private Archive archive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public Image(String url) {
        this.url = url;
    }

    public Image(String fileName, String fileUrl, long size) {
        this.originFileName = fileName;
        this.url = fileUrl;
        this.fileSize = size;
    }

    public void updatePost(Post post) {
        this.post = post;
    }
}
