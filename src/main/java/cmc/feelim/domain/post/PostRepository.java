package cmc.feelim.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategory(Category category);

    List<Post> findByTitleContaining(@Param("keyword") String keyword);

    List<Post> findByContentContaining(@Param("keyword") String keyword);
}
