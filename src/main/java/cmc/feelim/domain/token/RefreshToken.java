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

    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    private String token;

    @Builder
    public RefreshToken(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public RefreshToken updateToken(String refreshToken) {
        this.token = refreshToken;
        return this;
    }

    public static RefreshToken createToken(User user, String token){
        return new RefreshToken(user, token);
    }
}
