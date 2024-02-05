package friend.spring.repository;

import friend.spring.domain.mapping.Post_scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostScrapRepository extends JpaRepository<Post_scrap, Long> {

    Optional<Post_scrap> findByPostIdAndUserId(Long postId, Long userId);
}
