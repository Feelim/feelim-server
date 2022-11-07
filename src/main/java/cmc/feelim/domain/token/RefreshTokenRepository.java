package cmc.feelim.domain.token;

import cmc.feelim.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
//    Optional<RefreshToken> findByKey(String key);

    Optional<Object> findByUserId(String name);

    Optional<RefreshToken> findByUser(User user);
}
