package cmc.feelim.domain.user;

import cmc.feelim.domain.BaseEntity;
import cmc.feelim.domain.archive.Archive;
import cmc.feelim.domain.comment.Comment;
import cmc.feelim.domain.film.Film;
import cmc.feelim.domain.follow.Follow;
import cmc.feelim.domain.image.Image;
import cmc.feelim.domain.likes.Likes;
import cmc.feelim.domain.order.Order;
import cmc.feelim.domain.post.Post;
import cmc.feelim.domain.report.Report;
import cmc.feelim.domain.review.Review;
import cmc.feelim.domain.token.RefreshToken;
import cmc.feelim.domain.user.dto.PatchProfileReq;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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

    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    @Email(message = "이메일 형식에 맞지 않습니다.")
    @Column(length = 45)
    private String email;

    @Column(length = 20)
    private String phone;

    // 숫자, 특수문자, 영문 중 2가지 이상 사용, 8글자 이상
    @NotNull
//    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,45}", message = "비밀번호는 숫자, 특수문자, 영문 중 2가지 이상 사용, 8글자 이상 45자 이하로 구성해주세요.")
    @Column(length = 45)
    private String pwd;

    @NotNull
    @Column(length = 8)
    private String nickname;

    @NotNull
    @Column(length = 8)
    private String name;

    @Column(length = 100)
    private String introduction;

    @Column(nullable = true, unique = true)
    private String fcmToken;

    @Column(length = 70, unique = true)
    private String appleUniqueNo;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

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

    //내가 신고한 게시물
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Report> reports = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews= new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    @Builder
    public User (String email, String phone, String pwd, String nickname, String name, String appleUniqueNo) {
        this.email = email;
        this.phone = phone;
        this.pwd = pwd;
        this.nickname = nickname;
        this.name = name;
        this.role = Role.USER;
        this.appleUniqueNo = appleUniqueNo;
    }

    public static User create(String email, String name, String nickname, String pwd) {
        return User.builder()
                .email(email)
                .name(name)
                .nickname(nickname)
                .pwd(pwd)
                .build();
    }

    public static User createApple(String email, String name, String nickname, String pwd, String appleUniqueNo) {
        return User.builder()
                .email(email)
                .name(name)
                .nickname(nickname)
                .pwd(pwd)
                .appleUniqueNo(appleUniqueNo)
                .build();
    }

    public void updateProfile(PatchProfileReq patchProfileReq) {
        this.nickname = patchProfileReq.getNickname();
        this.introduction = patchProfileReq.getIntroduction();
    }

    public void updateImage(Image image) {
        images.add(image);
    }

    public void addFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
