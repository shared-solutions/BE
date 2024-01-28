package friend.spring.repository;

import friend.spring.domain.mapping.Comment_like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<Comment_like, Long> {
}
