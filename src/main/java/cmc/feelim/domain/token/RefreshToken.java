package cmc.feelim.domain.token;

import cmc.feelim.domain.BaseEntity;
import cmc.feelim.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Where(clause = "status='ACTIVE'")
public class RefreshToken extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;

    @ManyToOne
    private User user;
    private String token;

    public RefreshToken(User user, String token) {
        this.user = user;
        this.token = token;
    }

//    @Builder
//    public RefreshToken(String key, String value) {
//        this.id = key;
//        this.token = value;
//    }

    public RefreshToken updateToken(String refreshToken) {
        this.token = refreshToken;
        return this;
    }

    public static RefreshToken createToken(User user, String token){
        return new RefreshToken(user, token);
    }
}
