package friend.spring.repository;

import friend.spring.domain.mapping.Post_like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<Post_like, Long> {

    Optional<Post_like> findByPostIdAndUserId(Long postId, Long userId);

    Integer countByPostId(Long postId);
}
