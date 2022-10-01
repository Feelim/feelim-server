package cmc.feelim.domain.comment;

import cmc.feelim.domain.BaseEntity;
import cmc.feelim.domain.comment.dto.PatchCommentReq;
import cmc.feelim.domain.comment.dto.PostCommentReq;
import cmc.feelim.domain.post.Post;
import cmc.feelim.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String content;

    public Comment(User user, Post post, PostCommentReq postCommentReq) {
        this.user = user;
        this.post = post;
        this.content = post.getContent();
    }

    public void update(PatchCommentReq patchCommentReq) {
        this.content = patchCommentReq.getContent();
    }
}
