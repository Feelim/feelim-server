package cmc.feelim.domain.user;

import cmc.feelim.domain.BaseEntity;
import cmc.feelim.domain.archive.Archive;
import cmc.feelim.domain.comment.Comment;
import cmc.feelim.domain.film.Film;
import cmc.feelim.domain.follow.Follow;
import cmc.feelim.domain.likes.Likes;
import cmc.feelim.domain.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @Column(length = 45)
    private String email;

    @NotNull
    @Column(length = 45)
    private String pwd;

    @NotNull
    @Column(length = 15)
    private String nickname;

    @NotNull
    @Column(length = 15)
    private String name;

    @Column(length = 100)
    private String introduction;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Film> films = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Archive> archives = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Likes> likes = new ArrayList<>();

    //내가 팔로우하는 유저
    @OneToMany(mappedBy = "toUser", cascade = CascadeType.ALL)
    private List<Follow> follows = new ArrayList<>();
}
